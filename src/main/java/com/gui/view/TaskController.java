package com.gui.view;

import com.gui.core.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TaskController {
	@FXML
	private Label name;
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
	
	public void setTask(Task task) {
		name.setText("Name: " + task.getName());
		burst.setText("Burst: " + task.getBurst());
		priority.setText("Priority: " + task.getPriority());
		arrivalTime.setText("Arrival Time: " + task.getArrivalTime());
		burstLeft.setText("Burst Left: " + task.getBurstLeft());
		turnAroundTime.setText("Turn Around Time: " + task.getTurnAroundTime());
		waitingTime.setText("Waiting Time: " + task.getWaitingTime());
		respondTime.setText("Respond Time: " + task.getRespondTime());
	}
}
