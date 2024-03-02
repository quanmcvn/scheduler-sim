package com.gui.core;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class ScheduleRoundRobin implements Scheduler {
	private final Queue<Task> tasks;
	private final int quantum;
	
	
	public ScheduleRoundRobin(int quantum) {
		if (quantum <= 0) throw new IllegalArgumentException("Number must be > 0");
		tasks = new LinkedList<>();
		this.quantum = quantum;
	}
	
	@Override
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	@Override
	public Task getTask(Task lastTask) {
		return tasks.poll();
	}
	
	@Override
	public boolean canPreempt(Task task, int timeRan) {
		return timeRan >= quantum;
	}
	
	@Override
	public boolean isRoundRobin(Task task) {
		return true;
	}
	@Override
	public boolean hasTask() {
		return !tasks.isEmpty();
	}
}
