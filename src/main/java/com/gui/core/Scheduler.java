package com.gui.core;

import java.util.List;

public interface Scheduler {
	void addTask(Task task);
	Task getTask(Task lastTask);
	boolean canPreempt(Task task, int timeRan);
	boolean isRoundRobin(Task task);
	boolean hasTask();
	List<Task> getAllTask();
}
