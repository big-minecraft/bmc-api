package dev.wiji.bigminecraftapi;

import dev.wiji.bigminecraftapi.controllers.NetworkManager;
import dev.wiji.bigminecraftapi.enums.InstanceState;
import dev.wiji.bigminecraftapi.controllers.RedisManager;
import dev.wiji.bigminecraftapi.objects.ApiSettings;

public class BigMinecraftAPI {
	private static RedisManager redisManager;
	private static NetworkManager networkManager;

	public static void init() {
		ApiSettings settings = new ApiSettings();
		settings.setRedisHost("redis-service");
		settings.setRedisPort(6379);

		init(settings);
	}

	public static void init(ApiSettings settings) {

		redisManager = new RedisManager(settings);
		networkManager = new NetworkManager();

		System.out.println("BigMinecraftAPI initialized!");
	}

	public static RedisManager getRedisManager() {
		return redisManager;
	}

	public static NetworkManager getNetworkManager() {
		return networkManager;
	}
}