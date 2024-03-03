package com.gui.core;

import java.util.*;

public class SchedulePriorityPreemptive implements Scheduler {
	
	private final PriorityQueue<Task> tasks;
	
	private final Comparator<Task> sortByPriorityByName = (lhs, rhs) -> {
		if (lhs.getPriority() != rhs.getPriority())
			return Integer.compare(lhs.getPriority(), rhs.getPriority());
		return lhs.getName().compareTo(rhs.getName());
	};
	
	public SchedulePriorityPreemptive() {
		tasks = new PriorityQueue<>(sortByPriorityByName);
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
		return sortByPriorityByName.compare(tasks.peek(), task) < 0;
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
