package dev.kyriji.bigminecraftapi.controllers;

import com.google.gson.Gson;
import dev.kyriji.bigminecraftapi.BigMinecraftAPI;
import dev.kyriji.bigminecraftapi.enums.InstanceState;
import dev.kyriji.bigminecraftapi.enums.RedisChannel;
import dev.kyriji.bigminecraftapi.objects.MinecraftInstance;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class NetworkManager {
	private final Gson gson;

	public NetworkManager() {
		gson = new Gson();
	}

	public List<MinecraftInstance> getInstances() {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		List<MinecraftInstance> instances = new ArrayList<>();

		try (Jedis jedis = redisManager.commandPool.getResource()) {
			Set<String> keys = jedis.keys("*");

			for(String key : keys) {
				if(key.equals("proxy")) continue;
				instances.addAll(getInstances(key));
			}
		}

		return instances;
	}


	public List<MinecraftInstance> getInstances(String deployment) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();

		List<MinecraftInstance> instances = new ArrayList<>();

		try (Jedis jedis = redisManager.getCommandPool().getResource()) {
			Map<String, String> instanceStrings = jedis.hgetAll(deployment);
			for(String instance : instanceStrings.values()) {
				MinecraftInstance minecraftInstance = gson.fromJson(instance, MinecraftInstance.class);
				instances.add(minecraftInstance);
			}
		}

		return instances;
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
