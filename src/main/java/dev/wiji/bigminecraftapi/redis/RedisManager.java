package dev.wiji.bigminecraftapi.redis;

import com.google.gson.Gson;
import dev.wiji.bigminecraftapi.objects.MinecraftInstance;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RedisManager {
	private final JedisPool pool;
	private final Gson gson;

	public RedisManager() {
		pool = new JedisPool("redis-service", 6379);
		gson = new Gson();
	}

	protected void addListener(RedisListener listener) {
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

	public void publish(String channel, String message) {
		try (Jedis jedisPublisher = pool.getResource()) {
			jedisPublisher.publish(channel, message);
		}
	}

	public List<MinecraftInstance> getInstances() {
		List<MinecraftInstance> instances = new ArrayList<>();

		try (Jedis jedis = pool.getResource()) {
			Map<String, String> instanceStrings = jedis.hgetAll("instances");
			for (String instance : instanceStrings.values()) {
				MinecraftInstance minecraftInstance = gson.fromJson(instance, MinecraftInstance.class);
				instances.add(minecraftInstance);
			}
		}

		return instances;
	}
}