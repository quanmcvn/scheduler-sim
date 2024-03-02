package com.gui.core;

import java.util.*;

public class ScheduleFCFS implements Scheduler {
//	private final Queue<Task> tasks;
	private final PriorityQueue<Task> tasks;
	
	public ScheduleFCFS() {
		this.tasks = new PriorityQueue<>((lhs, rhs) -> {
			if (lhs.getArrivalTime() != rhs.getArrivalTime()) return Integer.compare(lhs.getArrivalTime(), rhs.getArrivalTime());
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
