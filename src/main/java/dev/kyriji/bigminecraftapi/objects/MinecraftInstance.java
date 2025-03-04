package dev.kyriji.bigminecraftapi.objects;

import java.util.*;

public class MinecraftInstance extends Instance {
	private final Map<UUID, String> players;

	public MinecraftInstance(String uid, String name, String podName, String ip, String deployment) {
		super(uid, name, podName, ip, deployment);

		this.players = new HashMap<>();
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
