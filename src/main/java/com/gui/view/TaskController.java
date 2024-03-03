package com.gui.view;

import com.gui.core.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TaskController {
	@FXML
	private Label name;
	@FXML
	private Label queueLevel;
	@FXML
	private Label burst;
	@FXML
	private Label priority;
	@FXML
	private Label arrivalTime;
	@FXML
	private Label burstLeft;
	@FXML
	private Label turnAroundTime;
	@FXML
	private Label waitingTime;
	@FXML
	private Label respondTime;
	private Task task;
	private boolean showLevel = false;
	public void setTask(Task task, boolean showLevel) {
		this.task = task;
		this.showLevel = showLevel;
		update();
	}
	public void update() {
		name.setText("Name: " + task.getName());
		queueLevel.setText("Level: " + task.getQueueLevel());
		burst.setText("Burst: " + task.getBurst());
		priority.setText("Priority: " + task.getPriority());
		arrivalTime.setText("Arrival Time: " + task.getArrivalTime());
		burstLeft.setText("Burst Left: " + task.getBurstLeft());
		turnAroundTime.setText("Turn Around Time: " + (task.getTurnAroundTime() < 0 ? "?" : task.getTurnAroundTime()));
		waitingTime.setText("Waiting Time: " + (task.getWaitingTime() < 0 ? "?" : task.getWaitingTime()));
		respondTime.setText("Respond Time: " + (task.getRespondTime() < 0 ? "?" : task.getRespondTime()));
		queueLevel.setVisible(showLevel);
	}
}
