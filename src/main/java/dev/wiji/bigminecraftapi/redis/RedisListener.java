package dev.wiji.bigminecraftapi.redis;

public abstract class RedisListener {

	private final String channel;

	public RedisListener(String channel) {
		this.channel = channel;
	}

	public abstract void onMessage(String message);


	public String getChannel() {
		return channel;
	}
}
