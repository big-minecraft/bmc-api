package dev.wiji.bigminecraftapi.controllers;

import com.google.gson.Gson;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.enums.InstanceState;
import dev.wiji.bigminecraftapi.enums.RedisChannel;
import dev.wiji.bigminecraftapi.objects.MinecraftInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

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
			Map<String, String> instanceStrings = jedis.hgetAll("instances");
			for (String instance : instanceStrings.values()) {
				MinecraftInstance minecraftInstance = gson.fromJson(instance, MinecraftInstance.class);
				instances.add(minecraftInstance);
			}
		}

		return instances;
	}

	public List<MinecraftInstance> getProxies() {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();

		List<MinecraftInstance> proxies = new ArrayList<>();

		try (Jedis jedis = redisManager.getCommandPool().getResource()) { // Use command pool
			Map<String, String> proxyStrings = jedis.hgetAll("proxies");
			for (String instance : proxyStrings.values()) {
				MinecraftInstance proxy = gson.fromJson(instance, MinecraftInstance.class);
				proxies.add(proxy);
			}
		}

		return proxies;
	}

	public Map<UUID, String> getPlayers() {
		List<MinecraftInstance> proxies = getProxies();
		Map<UUID, String> players = new HashMap<>();

		for (MinecraftInstance proxy : proxies) players.putAll(proxy.getPlayers());

		return players;
	}

	public Map<UUID, String> getPlayers(String deployment) {
		List<MinecraftInstance> deployments = getInstances();
		Map<UUID, String> players = new HashMap<>();

		for (MinecraftInstance instance : deployments) {
			if (instance.getDeployment().equals(deployment)) {
				players.putAll(instance.getPlayers());
			}
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
