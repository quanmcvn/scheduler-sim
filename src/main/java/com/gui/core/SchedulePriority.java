package com.gui.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SchedulePriority implements Scheduler {
	private final PriorityQueue<Task> tasks;
	
	public SchedulePriority() {
		this.tasks = new PriorityQueue<>((lhs, rhs) -> {
			if (lhs.getPriority() != rhs.getPriority()) return Integer.compare(lhs.getPriority(), rhs.getPriority());
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
