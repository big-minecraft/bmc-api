package dev.wiji.bigminecraftapi.controllers;

import dev.wiji.bigminecraftapi.enums.RedisChannel;
import dev.wiji.bigminecraftapi.objects.ApiSettings;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {
	protected final JedisPool pool;

	public RedisManager(ApiSettings settings) {
		pool = new JedisPool(settings.getRedisHost(), settings.getRedisPort());
	}

	public void addListener(RedisListener listener) {
		JedisPubSub pubSub = new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				listener.onMessage(message);
			}
		};

		new Thread(() -> {
			try(Jedis jedisSubscriber = pool.getResource()) {
				jedisSubscriber.subscribe(pubSub, listener.getChannel());
			}
		}).start();
	}

	public void publish(RedisChannel channel, String message) {
		try (Jedis jedisPublisher = pool.getResource()) {
			jedisPublisher.publish(channel.getRef(), message);
		}
	}

	public void publish(String channel, String message) {
		try (Jedis jedisPublisher = pool.getResource()) {
			jedisPublisher.publish(channel, message);
		}
	}
}