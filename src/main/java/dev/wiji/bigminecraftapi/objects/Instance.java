package dev.wiji.bigminecraftapi.objects;

import dev.wiji.bigminecraftapi.enums.InstanceState;

public class Instance {

	private final String uid;
	private final String name;
	private final String podName;
	private final String ip;
	private final String deployment;

	private InstanceState state;

	public Instance(String uid, String name, String podName, String ip, String deployment) {
		this.uid = uid;
		this.name = name;
		this.ip = ip;
		this.podName = podName;
		this.deployment = deployment;

		this.state = InstanceState.RUNNING;
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

	public String getDeployment() {
		return deployment;
	}

	public InstanceState getState() {
		return state;
	}

	public void setState(InstanceState state) {
		this.state = state;
	}
}
