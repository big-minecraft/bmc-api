package dev.wiji.bigminecraftapi.controllers;

import com.google.gson.Gson;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.enums.InstanceState;
import dev.wiji.bigminecraftapi.enums.RedisChannel;
import dev.wiji.bigminecraftapi.objects.MinecraftInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.*;

public class NetworkManager {
	private final Gson gson;

	public NetworkManager() {
		gson = new Gson();
	}

	public List<MinecraftInstance> getInstances() {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();

		List<MinecraftInstance> instances = new ArrayList<>();

		try (Jedis jedis = redisManager.pool.getResource()) {
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

		try (Jedis jedis = redisManager.pool.getResource()) {
			jedis.watch("proxies");
			Transaction transaction = jedis.multi();
			Response<Map<String, String>> response = transaction.hgetAll("proxies");
			List<Object> results = transaction.exec();

			if (results == null) {
				return getProxies();
			}

			Map<String, String> proxyStrings = response.get();
			for (Map.Entry<String, String> entry : proxyStrings.entrySet()) {
				try {
					MinecraftInstance proxy = gson.fromJson(entry.getValue(), MinecraftInstance.class);
					if (proxy != null) {
						proxies.add(proxy);
					}
				} catch (Exception e) {
					System.err.println("Error parsing proxy data for key: " + entry.getKey());
					e.printStackTrace();
				}
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

	public boolean isPlayerConnected(UUID player) {
		return getPlayers().containsKey(player);
	}

	public void queuePlayer(UUID player, String deployment) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		redisManager.publish(RedisChannel.QUEUE_PLAYER.getRef(), player.toString() + ":" + deployment);
	}

	public static void setInstanceState(InstanceState state) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		redisManager.publish(RedisChannel.INSTANCE_STATE_CHANGE.getRef(), state.name());
	}

}
