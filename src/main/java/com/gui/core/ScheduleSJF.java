package com.gui.core;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ScheduleSJF implements Scheduler {
	private final PriorityQueue<Task> tasks;
	
	public ScheduleSJF() {
		this.tasks = new PriorityQueue<>(Comparator.comparingInt(Task::getBurstLeft));
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
		return false;
	}
	
	@Override
	public boolean isRoundRobin(Task task) {
		return false;
	}
	@Override
	public boolean hasTask() {
		return !tasks.isEmpty();
	}
}
