package com.gui.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SchedulePriorityPreemptive implements Scheduler {
	private final ArrayList<Task> tasks;
	
	public SchedulePriorityPreemptive() {
		tasks = new ArrayList<>();
	}
	
	@Override
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	@Override
	public Task getTask(Task lastTask) {
		if (tasks.isEmpty()) return null;
		Comparator<Task> sortByPrioByName = (lhs, rhs) -> {
			if (lhs.getPriority() != rhs.getPriority())
				return Integer.compare(lhs.getPriority(), rhs.getPriority());
			if (lhs.getName().equals(lastTask.getName())) return -1;
			return lhs.getName().compareTo(rhs.getName());
		};
		Task ret = Collections.min(tasks, sortByPrioByName);
		tasks.remove(ret);
		return ret;
	}
	@Override
	public boolean canPreempt(Task task, int timeRan) {
		if (tasks.isEmpty()) return false;
		Comparator<Task> sortByPrioByName = (lhs, rhs) -> {
			if (lhs.getPriority() != rhs.getPriority())
				return Integer.compare(lhs.getPriority(), rhs.getPriority());
			if (lhs.getName().equals(task.getName())) return -1;
			return lhs.getName().compareTo(rhs.getName());
		};
		Task ret = Collections.min(tasks, sortByPrioByName);
		// intended, since we don't duplicate task
		return ret != task;
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
