package com.gui.core;

import java.util.*;

public class ScheduleSJFPreemptive implements Scheduler {
	
	private final ArrayList<Task> tasks;
	
	public ScheduleSJFPreemptive() {
		tasks = new ArrayList<>();
	}
	
	@Override
	public void addTask(Task task) {
		tasks.add(task);
		Comparator<Task> sortByBurstByName = (lhs, rhs) -> {
			if (lhs.getBurstLeft() != rhs.getBurstLeft())
				return Integer.compare(lhs.getBurstLeft(), rhs.getBurstLeft());
			return lhs.getName().compareTo(rhs.getName());
		};
		tasks.sort(sortByBurstByName);
	}
	
	@Override
	public Task getTask(Task lastTask) {
		if (tasks.isEmpty()) return null;
		Comparator<Task> sortByBurstByName = (lhs, rhs) -> {
			if (lhs.getBurstLeft() != rhs.getBurstLeft())
				return Integer.compare(lhs.getBurstLeft(), rhs.getBurstLeft());
			if (lhs.getName().equals(lastTask.getName())) return -1;
			return lhs.getName().compareTo(rhs.getName());
		};
		Task ret = Collections.min(tasks, sortByBurstByName);
		tasks.remove(ret);
		return ret;
	}
	@Override
	public boolean canPreempt(Task task, int timeRan) {
		if (tasks.isEmpty()) return false;
		Comparator<Task> sortByBurstByName = (lhs, rhs) -> {
			if (lhs.getBurstLeft() != rhs.getBurstLeft())
				return Integer.compare(lhs.getBurstLeft(), rhs.getBurstLeft());
			if (lhs.getName().equals(task.getName())) return -1;
			return lhs.getName().compareTo(rhs.getName());
		};
		Task ret = Collections.min(tasks, sortByBurstByName);
		return sortByBurstByName.compare(ret, task) < 0;
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
