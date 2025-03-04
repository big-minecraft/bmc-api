package dev.kyriji.bigminecraftapi;

import dev.kyriji.bigminecraftapi.controllers.NetworkManager;
import dev.kyriji.bigminecraftapi.controllers.RedisManager;
import dev.kyriji.bigminecraftapi.objects.ApiSettings;

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