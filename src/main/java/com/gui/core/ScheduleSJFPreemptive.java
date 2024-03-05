package com.gui.core;

import java.util.*;

public class ScheduleSJFPreemptive implements Scheduler {
	
	private final PriorityQueue<Task> tasks;
	
	private final Comparator<Task> sortByBurstByName = (lhs, rhs) -> {
		if (lhs.getBurstLeft() != rhs.getBurstLeft())
			return Integer.compare(lhs.getBurstLeft(), rhs.getBurstLeft());
		return lhs.getName().compareTo(rhs.getName());
	};
	
	public ScheduleSJFPreemptive() {
		tasks = new PriorityQueue<>(sortByBurstByName);
	}
	
	@Override
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	@Override
	public Task getTask(Task lastTask) {
		if (tasks.isEmpty()) return null;
		return tasks.poll();
	}
	@Override
	public boolean canPreempt(Task task, int timeRan) {
		if (tasks.isEmpty()) return false;
		return (tasks.peek().getBurstLeft() <  task.getBurstLeft());
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
