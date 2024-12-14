package dev.wiji.bigminecraftapi.enums;

public enum RedisChannel {

	QUEUE_PLAYER("queue-player"),
	QUEUE_RESPONSE("queue-response"),
	INSTANCE_MODIFIED("instance-modified"),
	PROXY_CONNECT("proxy-connect"),
	PROXY_DISCONNECT("proxy-disconnect"),
	INSTANCE_SWITCH("instance-switch"),
	REQUEST_INITIAL_INSTANCE("request-initial-instance"),
	INITIAL_INSTANCE_RESPONSE("initial-instance-response"),
	INSTANCE_STATE_CHANGE("instance-state-change"),
	PROXY_REGISTER("proxy-register"),
	DEPLOYMENT_MODIFIED("deployment-modified"),
	;

	private final String ref;

	RedisChannel(String ref) {
		this.ref = ref;
	}

	public String getRef() {
		return ref;
	}
}
