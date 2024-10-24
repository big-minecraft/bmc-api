package dev.wiji.bigminecraftapi.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinecraftInstance {

	private final String uid;
	private final String name;
	private final String podName;
	private final String ip;
	private final String gamemode;
	private final boolean initialServer;
	private final List<UUID> players;

	public MinecraftInstance(String uid, String name, String podName, String ip, String gamemode, boolean initialServer) {
		this.uid = uid;
		this.name = name;
		this.ip = ip;
		this.podName = podName;
		this.gamemode = gamemode;
		this.initialServer = initialServer;

		this.players = new ArrayList<>();
	}

	public String getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public String getPodName() {
		return podName;
	}

	public String getIp() {
		return ip;
	}

	public List<UUID> getPlayers() {
		return players;
	}

	public String getGamemode() {
		return gamemode;
	}

	public boolean isInitialServer() {
		return initialServer;
	}

}
