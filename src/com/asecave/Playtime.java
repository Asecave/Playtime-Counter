package com.asecave;

public class Playtime {

	private String uuid;
	private long timePlayed;
	private long joinedAt;
	
	public Playtime(String uuid, long timePlayed) {
		this.uuid = uuid;
		this.timePlayed = timePlayed;
		joinedAt = System.currentTimeMillis();
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
}
