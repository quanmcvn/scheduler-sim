package com.gui.core;

import java.util.ArrayList;

public class ScheduleMultilevelQueue implements Scheduler {
	private final ArrayList<Scheduler> schedulers;
	
	public ScheduleMultilevelQueue() {
		this.schedulers = new ArrayList<>();
		schedulers.add(new ScheduleRoundRobin(20));
		schedulers.add(new ScheduleSJFPreemptive());
		schedulers.add(new ScheduleFCFS());
	}
	
	@Override
	public void addTask(Task task) {
		schedulers.get(task.getQueueLevel()).addTask(task);
	}
	
	@Override
	public Task getTask(Task lastTask) {
		for (Scheduler scheduler : schedulers) {
			Task ret = scheduler.getTask(lastTask);
			if (ret != null) return ret;
		}
		return null;
	}
	
	@Override
	public boolean canPreempt(Task task, int timeRan) {
		for (int i = 0; i < task.getQueueLevel(); ++ i) {
			if (schedulers.get(i).hasTask()) {
				return true;
			}
		}
		return schedulers.get(task.getQueueLevel()).canPreempt(task, timeRan);
	}
	
	@Override
	public boolean isRoundRobin(Task task) {
		return schedulers.get(task.getQueueLevel()).isRoundRobin(task);
	}
	@Override
	public boolean hasTask() {
		for (Scheduler scheduler : schedulers) {
			if (scheduler.hasTask()) return true;
		}
		return false;
	}
}
