package com.gui.core;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ScheduleSJF implements Scheduler {
	private final PriorityQueue<Task> tasks;
	
	public ScheduleSJF() {
		this.tasks = new PriorityQueue<>((lhs, rhs) -> {
			if (lhs.getBurstLeft() != rhs.getBurstLeft()) return Integer.compare(lhs.getBurstLeft(), rhs.getBurstLeft());
			return lhs.getName().compareTo(rhs.getName());
		});
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
	
	@Override
	public List<Task> getAllTask() {
		return new ArrayList<>(tasks);
	}
}
