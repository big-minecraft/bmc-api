package dev.kyriji.bigminecraftapi.enums;

public enum InstanceState {
	STARTING,
	RUNNING,
	BLOCKED,
	STOPPING,
	STOPPED,
	;

	public static InstanceState getState(String state) {
		for (InstanceState instanceState : values()) {
			if (instanceState.name().equalsIgnoreCase(state)) {
				return instanceState;
			}
		}
		return null;
	}
}
