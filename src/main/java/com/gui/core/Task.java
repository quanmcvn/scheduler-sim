package com.gui.core;

public class Task {
	private final String name;
	private final int burst;
	private final int priority;
	private final int arrivalTime;
	private final int queueLevel;
	private int burstLeft;
	private int turnAroundTime;
	private int waitingTime;
	private int respondTime;

    public Task(String name, int burst, int priority, int arrivalTime, int queueLevel) {
		this.name = name;
		this.burst = burst;
		this.priority = priority;
		this.arrivalTime = arrivalTime;
	    this.queueLevel = queueLevel;
		this.burstLeft = burst;
	    this.turnAroundTime = -1;
		this.waitingTime = -1;
		this.respondTime = -1;
    }
	
	public String getName() {
		return name;
	}
	
	public int getBurst() {
		return burst;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public int getQueueLevel() {
		return queueLevel;
	}
	
	public int getBurstLeft() {
		return burstLeft;
	}
	
	public void setBurstLeft(int burstLeft) {
		this.burstLeft = burstLeft;
	}
	
	public int getTurnAroundTime() {
		return turnAroundTime;
	}
	
	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}
	
	public int getWaitingTime() {
		return waitingTime;
	}
	
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	public int getRespondTime() {
		return respondTime;
	}
	
	public void setRespondTime(int respondTime) {
		this.respondTime = respondTime;
	}
	@Override
	public String toString() {
		return String.format(
				"Task: %s, burst: %d, priority: %d, arrival_time: %d, queuelevel: %d, " +
				"burst_left: %d, turn_around_time: %d, waiting_time: %d, respond_time: %d"
				, name, burst, priority, arrivalTime, queueLevel, burstLeft, turnAroundTime, waitingTime, respondTime);
	}
}
