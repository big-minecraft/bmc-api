package dev.wiji.bigminecraftapi.controllers;

import com.google.gson.Gson;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.enums.InstanceState;
import dev.wiji.bigminecraftapi.enums.RedisChannel;
import dev.wiji.bigminecraftapi.objects.MinecraftInstance;
import redis.clients.jedis.Jedis;

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

	public boolean isPlayerConnected(UUID player) {
		return getPlayers().containsKey(player);
	}

	public void queuePlayer(UUID player, String gamemode) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		redisManager.publish(RedisChannel.QUEUE_PLAYER.getRef(), player.toString() + ":" + gamemode);
	}

	public static void setInstanceState(InstanceState state) {
		RedisManager redisManager = BigMinecraftAPI.getRedisManager();
		redisManager.publish(RedisChannel.INSTANCE_STATE_CHANGE.getRef(), state.name());
	}

}
