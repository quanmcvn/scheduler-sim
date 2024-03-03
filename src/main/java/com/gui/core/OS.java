package com.gui.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.util.*;

public class OS {
	private int globalTime;
	private List<Task> taskList;
	private final List<Task> totalTaskList;
	private Scheduler scheduler;
	private final CPU cpu;
	private final GanttChart chart;
	private String fileInput;
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
		totalTaskList.addAll(this.taskList);
	}
	
	public List<Task> getTaskList() {
		return taskList;
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public OS() {
		this.taskList = new ArrayList<>();
		this.totalTaskList = new ArrayList<>();
		this.cpu = new CPU();
		this.chart = new GanttChart();
		totalTaskRan = 0;
		indexNext = 0;
		currentRunTime = 0;
	}
	
	public boolean readFromFile(String filename) {
		taskList.clear();
		this.fileInput = filename;
		File file = new File(filename);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.print("There is no file '" + filename + "'. Try again?");
			return false;
		}
		try {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] values = line.split(",\\s*");
				
				String name = values[0];
				int burstTime = Integer.parseInt(values[1]);
				int priority = Integer.parseInt(values[2]);
				int arrivalTime = Integer.parseInt(values[3]);
				int queueLevel = Integer.parseInt(values[4]);
				taskList.add(new Task(name, burstTime, priority, arrivalTime, queueLevel));
			}
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid file input format, check again?");
			return false;
		}
		taskList.sort(Comparator.comparingInt(Task::getArrivalTime));
		totalTaskList.addAll(taskList);
		return true;
	}
	public String getSchedulerType() {
		if (this.scheduler instanceof ScheduleFCFS) {
			return "fcfs";
		} else if (this.scheduler instanceof ScheduleSJF) {
			return "sjf";
		} else if (this.scheduler instanceof ScheduleSJFPreemptive) {
			return "sjf_preemptive";
		} else if (this.scheduler instanceof SchedulePriority) {
			return "priority";
		} else if (this.scheduler instanceof SchedulePriorityPreemptive) {
			return "priority_preemptive";
		} else if (this.scheduler instanceof ScheduleRoundRobin) {
			return "roundrobin";
		} else if (this.scheduler instanceof ScheduleMultilevelQueue) {
			return "multilevel";
		}
		return "???";
	}
	public String getOutputFile() {
		String schedType = getSchedulerType();
		String fileName = FileSystems.getDefault().getPath(fileInput).getFileName().toString();
		return String.format("result/%s_%s.txt", fileName, schedType);
	}
	
	private int totalTaskRan = 0;
	private int indexNext = 0;
	private int currentRunTime = 0;
	// dummy task
	private final Task sleep = new Task("sleep", 100000, 100000, 100000, 0);
	private Task currentTask = sleep;
	private boolean updated = false;
	private boolean halfUpdated = false;
	private boolean isNextTickRunForceContextSwitch = false;
	public Task getCurrentTask() {
		return currentTask;
	}
	public int getGlobalTime() {
		return globalTime;
	}
	/**
	 * <p> Get notified when there is (are) task(s) coming in </p>
	 * <p> Add that (those) task(s) to the scheduler </p>
	 * @Return true if successfully half update
	 */
	public boolean tickHalfUpdate() {
		if (halfUpdated) return false;
		
		while (indexNext < taskList.size()) {
			if (taskList.get(indexNext).getArrivalTime() <= globalTime) {
				scheduler.addTask(taskList.get(indexNext));
				++ indexNext;
			} else break;
		}
		
		halfUpdated = true;
		return true;
	}
	
	/**
	 * <p> Choose the next task to be run </p>
	 * <p> Will secretly call tickHalfUpdate() first (wait, is this really a secret now that i said it out loud?)</p>
	 * <p> Multiple tickUpdate() without tickRun() will do nothing </p>
	 *
	 * @Return true if successfully update
	 */
	public boolean tickUpdate() {
		if (updated) return false;
		
		if (done()) return false;
		
		tickHalfUpdate();
		
		if ((scheduler.isRoundRobin(currentTask) && scheduler.canPreempt(currentTask, currentRunTime)) && currentTask != sleep) {
			isNextTickRunForceContextSwitch = true;
		} else {
			isNextTickRunForceContextSwitch = false;
		}
		
		if (currentTask == sleep || scheduler.canPreempt(currentTask, currentRunTime) || currentTask.getBurstLeft() <= 0) {
			if (currentTask != sleep && currentTask.getBurstLeft() > 0) {
				scheduler.addTask(currentTask);
			}
			currentRunTime = 0;
			currentTask = scheduler.getTask(currentTask);
		}
		if (currentTask == null) {
			currentTask = sleep;
		}
		
		updated = true;
		return true;
	}
	
	/**
	 * <p> Run the chosen task for 1 tick (ms in this case) </p>
	 * <p> Requires a tickUpdate() called before it (will not do anything otherwise) </p>
	 *
	 * @Return true if successfully update
	 */
	public boolean tickRun() {
		if (!updated) return false;
		
		if (currentTask.getBurst() == currentTask.getBurstLeft()) {
			// first time run
			currentTask.setRespondTime(globalTime - currentTask.getArrivalTime());
		}
		
		cpu.run(currentTask, 1);
		globalTime += 1;
		currentRunTime += 1;
		
		if (isNextTickRunForceContextSwitch) {
			chart.addTask(currentTask, 1);
		} else {
			chart.addTaskNoContextSwitch(currentTask, 1);
		}
		
		if (currentTask != sleep && currentTask.getBurstLeft() <= 0) {
			++ totalTaskRan;
			// turn around time = global_time - arrival_time
			currentTask.setTurnAroundTime(globalTime - currentTask.getArrivalTime());
			// waiting time = turn_around_time - burst_time
			currentTask.setWaitingTime(currentTask.getTurnAroundTime() - currentTask.getBurst());
		}
		
		halfUpdated = false;
		updated = false;
		return true;
	}
	
	/**
	 * 1 tick, do the update and run
	 */
	public void tick() {
		tickUpdate();
		tickRun();
	}
	
	/**
	 * Try to tickHalfUpdate(), if failed then try to tickUpdate(), if failed then try to tickRun()
	 */
	public void tickThird() {
		if (tickHalfUpdate()) return;
		if (tickUpdate()) return;
		tickRun();
	}
	
	/**
	 * Try to tickUpdate(), if failed then try to tickRun()
	 */
	public void tickHalf() {
		if (tickUpdate()) return;
		tickRun();
	}
	
	public boolean done() {
		if (totalTaskRan >= totalTaskList.size()) {
			currentTask = null;
			return true;
		}
		return false;
	}
	
	/**
	 * Run from start to finish in one time
	 */
	public void run() {
		totalTaskRan = 0;
		indexNext = 0;
		currentRunTime = 0;
		while (tickUpdate()) {
			tickRun();
		}
	}
	
	/**
	 * @return Full statistic of the run (avg tat, waiting time, respond time, and gantt chart)
	 */
	public String stat() {
		StringBuilder ret = new StringBuilder();
		int totalTurnAroundTime = 0;
		int totalWaitingTime = 0;
		int totalRespondTime = 0;
		for (Task task: totalTaskList) {
			totalTurnAroundTime += task.getTurnAroundTime();
			totalWaitingTime += task.getWaitingTime();
			totalRespondTime += task.getRespondTime();
			ret.append(String.format("Task %s, turn_around_time: %d, waiting_time: %d, respond_time: %d\n", task.getName(), task.getTurnAroundTime(), task.getWaitingTime(), task.getRespondTime()));
		}
		ret.append(String.format("Total: turn_around_time: %d, waiting_time: %d, respond_time: %d\n",
				totalTurnAroundTime, totalWaitingTime, totalRespondTime));
		ret.append(String.format("AVG  : turn_around_time: %.4f, waiting_time: %.4f, respond_time: %.4f\n",
				(float) totalTurnAroundTime / totalTaskList.size(), (float) totalWaitingTime / totalTaskList.size(), (float) totalRespondTime / totalTaskList.size()));
		ret.append(String.format("Total context switch: %d\n", chart.getSize() - 1));
		
		ret.append(chart.draw());
		return ret.toString();
	}
	
	public static void main(String[] args) {
		OS os = new OS();
		os.readFromFile("test/bt1.txt");
		os.setScheduler(new ScheduleSJFPreemptive());
		os.run();
		System.out.println(os.stat());
	}
}
