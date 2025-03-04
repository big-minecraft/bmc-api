package dev.kyriji.bigminecraftapi.objects;

public class ApiSettings {
	private String redisHost;
	private int redisPort;

	public String getRedisHost() {
		return redisHost;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}
}
