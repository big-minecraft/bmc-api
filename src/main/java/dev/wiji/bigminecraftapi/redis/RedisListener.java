package dev.wiji.bigminecraftapi.redis;

import dev.wiji.bigminecraftapi.BigMinecraftAPI;

public abstract class RedisListener {

	private final String channel;

	public RedisListener(String channel) {
		this.channel = channel;

		BigMinecraftAPI.getRedisManager().addListener(this);
	}

	public abstract void onMessage(String message);


	public String getChannel() {
		return channel;
	}
}
