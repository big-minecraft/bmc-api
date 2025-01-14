package dev.wiji.bigminecraftapi.controllers;

import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import dev.wiji.bigminecraftapi.enums.RedisChannel;

public abstract class RedisListener {

	private final String channel;

	public RedisListener(RedisChannel channel) {
		this(channel.getRef());
	}

	public RedisListener(String channel) {
		this.channel = channel;
	}

	public abstract void onMessage(String message);


	public String getChannel() {
		return channel;
	}
}
