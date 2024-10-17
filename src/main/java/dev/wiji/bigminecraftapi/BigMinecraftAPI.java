package dev.wiji.bigminecraftapi;

import dev.wiji.bigminecraftapi.redis.RedisManager;

public class BigMinecraftAPI {
	public static RedisManager redisManager = new RedisManager();

	public static void init() {
		System.out.println("BigMinecraftAPI initialized!");
	}

	public static RedisManager getRedisManager() {
		return redisManager;
	}
}