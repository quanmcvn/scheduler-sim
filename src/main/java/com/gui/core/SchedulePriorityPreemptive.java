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
		Comparator<Task> sortByPrioByName = (lhs, rhs) -> {
			if (lhs.getPriority() != rhs.getPriority())
				return Integer.compare(lhs.getPriority(), rhs.getPriority());
			return lhs.getName().compareTo(rhs.getName());
		};
		tasks.sort(sortByPrioByName);
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
		// actually don't need to do computation here (for a worse performance)
		// if task is better, it will be added in and be taken out next getTask() rightaway
		// if it is not, then we are right
		return true;
//		Comparator<Task> sortByPrioByName = (lhs, rhs) -> {
//			if (lhs.getPriority() != rhs.getPriority())
//				return Integer.compare(lhs.getPriority(), rhs.getPriority());
//			if (lhs.getName().equals(task.getName())) return -1;
//			return lhs.getName().compareTo(rhs.getName());
//		};
//		Task ret = Collections.min(tasks, sortByPrioByName);
//		return sortByPrioByName.compare(ret, task) < 0;
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
