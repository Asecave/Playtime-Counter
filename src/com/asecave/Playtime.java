package com.asecave;

public class Playtime {

	private String uuid;
	private long timePlayed;
	private long joinedAt;
	private String displayName;
	
	public Playtime(String uuid, long timePlayed, String displayName) {
		this.uuid = uuid;
		this.timePlayed = timePlayed;
		joinedAt = System.currentTimeMillis();
		this.displayName = displayName;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public long getTimePlayed() {
		return timePlayed;
	}

	public void setTimePlayed(long timePlayed) {
		this.timePlayed = timePlayed;
	}

	public long getTotalTime() {
		return System.currentTimeMillis() - joinedAt + timePlayed;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
