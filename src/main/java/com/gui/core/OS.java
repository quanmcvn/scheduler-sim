package com.gui.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class OS {
	private int globalTime;
	private List<Task> taskList;
	private final List<Task> totalTaskList;
	private Scheduler scheduler;
	private final CPU cpu;
	private final GanttChart chart;
	private String fileinput;
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
		totalTaskList.addAll(this.taskList);
	}
	public OS() {
		this.taskList = new ArrayList<>();
		this.totalTaskList = new ArrayList<>();
		this.cpu = new CPU();
		this.chart = new GanttChart();
	}
	
	public void readFromFile(String filename) {
		taskList.clear();
		this.fileinput = filename;
		File file = new File(filename);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] values = line.split(",\\s*");
			
			String name = values[0];
			int burstTime = Integer.parseInt(values[1]);
			int priority = Integer.parseInt(values[2]);
			int arrivalTime = Integer.parseInt(values[3]);
			int queueLevel = Integer.parseInt(values[4]) - 1;
			taskList.add(new Task(name, burstTime, priority, arrivalTime, queueLevel));
		}
		totalTaskList.addAll(taskList);
	}
	public void redirectOutput() {
		String schedType = "";
		if (this.scheduler instanceof ScheduleFCFS) {
			schedType = "fcfs";
		} else if (this.scheduler instanceof ScheduleSJF) {
			schedType = "sjf";
		} else if (this.scheduler instanceof ScheduleSJFPreemptive) {
			schedType = "sjf_preemptive";
		} else if (this.scheduler instanceof SchedulePriority) {
			schedType = "priority";
		} else if (this.scheduler instanceof SchedulePriorityPreemptive) {
			schedType = "priority_preemptive";
		} else if (this.scheduler instanceof ScheduleRoundRobin) {
			schedType = "roundrobin";
		} else if (this.scheduler instanceof ScheduleMultilevelQueue) {
			schedType = "multilevel";
		}
		String fileoutput = "result/" +
				fileinput.replace("test/", "").replace(".txt", "") +
				"_" + schedType +
				".txt";
		try {
			PrintStream fileStream = new PrintStream(new FileOutputStream(fileoutput));
			System.setOut(fileStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		taskList.sort(Comparator.comparingInt(Task::getArrivalTime));
		int totalTaskRan = 0;
		int indexNext = 0;
		int currentRunTime = 0;
		// dummy task
		Task sleep = new Task("sleep", 100000, 100000, 100000, 0);
		Task currentTask = sleep;
		while (totalTaskRan < taskList.size()) {
			while (indexNext < taskList.size()) {
				if (taskList.get(indexNext).getArrivalTime() <= globalTime) {
					scheduler.addTask(taskList.get(indexNext));
					++ indexNext;
				} else break;
			}
			if (currentTask == sleep || scheduler.canPreempt(currentTask, currentRunTime) || currentTask.getBurstLeft() <= 0) {
				if (scheduler.isRoundRobin(currentTask) && currentTask != sleep) {
					chart.addTask(currentTask, currentRunTime);
				} else {
					chart.addTaskNoContextSwitch(currentTask, currentRunTime);
				}
				if (currentTask.getBurstLeft() <= 0) {
					++ totalTaskRan;
					// turn around time = global_time - arrival_time
					currentTask.setTurnAroundTime(globalTime - currentTask.getArrivalTime());
					// waiting time = turn_around_time - burst_time
					currentTask.setWaitingTime(currentTask.getTurnAroundTime() - currentTask.getBurst());
				}
				if (currentTask != sleep && currentTask.getBurstLeft() > 0) {
					scheduler.addTask(currentTask);
				}
				currentRunTime = 0;
				currentTask = scheduler.getTask(currentTask);
			}
			if (currentTask == null) {
				currentTask = sleep;
			}
			if (currentTask.getBurst() == currentTask.getBurstLeft()) {
				// first time run
				currentTask.setRespondTime(globalTime - currentTask.getArrivalTime());
			}
			cpu.run(currentTask, 1);
			globalTime += 1;
			currentRunTime += 1;
		}
	}
	
	public void stat() {
		int totalTurnAroundTime = 0;
		int totalWaitingTime = 0;
		int totalRespondTime = 0;
		for (Task task: totalTaskList) {
			totalTurnAroundTime += task.getTurnAroundTime();
			totalWaitingTime += task.getWaitingTime();
			totalRespondTime += task.getRespondTime();
			System.out.printf("Task %s, turn_around_time: %d, waiting_time: %d, respond_time: %d\n", task.getName(), task.getTurnAroundTime(), task.getWaitingTime(), task.getRespondTime());
		}
		System.out.printf("Total: turn_around_time: %d, waiting_time: %d, respond_time: %d\n",
				totalTurnAroundTime, totalWaitingTime, totalRespondTime);
		System.out.printf("AVG  : turn_around_time: %.4f, waiting_time: %.4f, respond_time: %.4f\n",
				(float) totalTurnAroundTime / totalTaskList.size(), (float) totalWaitingTime / totalTaskList.size(), (float) totalRespondTime / totalTaskList.size());
		System.out.printf("Total context switch: %d\n", chart.getSize() - 1);
		
		chart.draw();
	}
	
	public static void main(String[] args) {
		OS os = new OS();
		os.readFromFile("test/bt1.txt");
		os.setScheduler(new ScheduleSJFPreemptive());
		os.redirectOutput();
		os.run();
		os.stat();
	}
}
