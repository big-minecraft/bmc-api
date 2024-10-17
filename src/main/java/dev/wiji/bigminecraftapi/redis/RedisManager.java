package dev.wiji.bigminecraftapi.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisManager {
	private final JedisPool pool;
	private final Jedis jedis;

	public RedisManager() {
		pool = new JedisPool("redis-service", 6379);
		jedis = pool.getResource();
	}

	protected void addListener(RedisListener listener) {
		JedisPubSub pubSub = new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				listener.onMessage(message);
			}
		};

		jedis.subscribe(pubSub, listener.getChannel());
	}

	public void publish(String channel, String message) {
		jedis.publish(channel, message);
	}
}
