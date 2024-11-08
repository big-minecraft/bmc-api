package dev.wiji.bigminecraftapi;

import dev.wiji.bigminecraftapi.controllers.NetworkManager;
import dev.wiji.bigminecraftapi.enums.InstanceState;
import dev.wiji.bigminecraftapi.controllers.RedisManager;

public class BigMinecraftAPI {
	private static final RedisManager redisManager = new RedisManager();
	private static final NetworkManager networkManager = new NetworkManager();

	public static void init() {
		System.out.println("BigMinecraftAPI initialized!");
	}

	public static RedisManager getRedisManager() {
		return redisManager;
	}

	public static NetworkManager getNetworkManager() {
		return networkManager;
	}
}