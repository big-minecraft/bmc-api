package dev.wiji.bigminecraftapi.objects;

import java.util.*;

public class MinecraftInstance {

	private final String uid;
	private final String name;
	private final String podName;
	private final String ip;
	private final String gamemode;
	private final boolean initialServer;
	private final Map<UUID, String> players;

	public MinecraftInstance(String uid, String name, String podName, String ip, String gamemode, boolean initialServer) {
		this.uid = uid;
		this.name = name;
		this.ip = ip;
		this.podName = podName;
		this.gamemode = gamemode;
		this.initialServer = initialServer;

		this.players = new HashMap<>();
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

	public String getGamemode() {
		return gamemode;
	}

	public boolean isInitialServer() {
		return initialServer;
	}

	public Map<UUID, String> getPlayers() {
		return players;
	}

	public void addPlayer(UUID playerId, String playerName) {
		players.put(playerId, playerName);
	}

	public void removePlayer(UUID playerId) {
		players.remove(playerId);
	}

	public boolean hasPlayer(UUID playerId) {
		return players.containsKey(playerId);
	}

	public void setPlayers(Map<UUID, String> players) {
		this.players.clear();
		this.players.putAll(players);
	}

}
