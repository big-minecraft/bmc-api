package dev.kyriji.bigminecraftapi.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.kyriji.bigminecraftapi.BigMinecraftAPI;
import dev.kyriji.bigminecraftapi.enums.InstanceState;
import dev.kyriji.bigminecraftapi.enums.RedisChannel;
import dev.kyriji.bigminecraftapi.objects.Instance;
import dev.kyriji.bigminecraftapi.objects.MinecraftInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class NetworkManager {
	private final Gson gson;

	public NetworkManager() {
		gson = new Gson();
	}

	public List<MinecraftInstance> getInstances() {
		return getInstances("*");
	}

	public List<MinecraftInstance> getInstances(String deploymentName) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		String pattern = "instance:*:" + deploymentName;

		List<MinecraftInstance> resultList = new ArrayList<>();
		Type playerMapType = new TypeToken<Map<UUID, String>>(){}.getType();

		try(Jedis jedis = redisManager.getCommandPool().getResource()) {
			String cursor = "0";
			do {
				ScanResult<String> scanResult = jedis.scan(cursor, new ScanParams().match(pattern));
				cursor = scanResult.getCursor();

				for(String key : scanResult.getResult()) {
					Map<String, String> hashData = jedis.hgetAll(key);

					String uid = hashData.get("uid");
					String name = hashData.get("name");
					String podName = hashData.get("podName");
					String ip = hashData.get("ip");
					String deployment = hashData.get("deployment");
					String stateStr = hashData.get("state");

					InstanceState state = stateStr != null ? InstanceState.valueOf(stateStr) : null;

					MinecraftInstance instance = new MinecraftInstance(uid, name, podName, ip, deployment);;
					String playersStr = hashData.get("players");
					Map<UUID, String> players = playersStr != null ?
							gson.fromJson(playersStr, playerMapType) : new HashMap<>();

					instance.setPlayers(players);
					instance.setState(state);

					resultList.add(instance);
				}
			} while (!cursor.equals("0"));
		}

		return resultList;
	}


	public List<MinecraftInstance> getProxies() {
		return getInstances("proxy");
	}

	public Map<UUID, String> getPlayers() {
		return getPlayers("proxy");
	}

	public Map<UUID, String> getPlayers(String deployment) {
		List<MinecraftInstance> deployments = getInstances(deployment);
		Map<UUID, String> players = new HashMap<>();

		for(MinecraftInstance instance : deployments) {
			players.putAll(instance.getPlayers());
		}

		return players;
	}

	public boolean isPlayerConnected(UUID player) {
		return getPlayers().containsKey(player);
	}

	public void queuePlayer(UUID player, String deployment) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		redisManager.publish(RedisChannel.QUEUE_PLAYER.getRef(), player.toString() + ":" + deployment);
	}

	public void transferPlayer(UUID player, MinecraftInstance instance) {
		transferPlayer(player, instance.getIp());
	}

	public void transferPlayer(UUID player, String ip) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		redisManager.publish(RedisChannel.TRANSFER_PLAYER.getRef(), player.toString() + ":" + ip);
	}

	public static void setInstanceState(InstanceState state) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();

		redisManager.publish(RedisChannel.INSTANCE_STATE_CHANGE.getRef(), getIPAddress() + ":" + state.name());
	}

	public static String getIPAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (!address.isLoopbackAddress() && address.isSiteLocalAddress()) {
						return address.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
