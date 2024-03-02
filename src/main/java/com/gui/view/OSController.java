package com.gui.view;

import com.gui.core.OS;
import com.gui.core.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OSController {
	@FXML
	private AnchorPane schedulerContainer;
	@FXML
	private HBox hbox;
	@FXML
	private AnchorPane currentTask;
	private OS os;
	private SchedulerController schedulerController;
	private List<TaskController> taskControllers;
	public void setOS(OS os) {
		this.os = os;
		List<Task> taskList = this.os.getTaskList();
		taskControllers = new ArrayList<>();
		for (Task task : taskList) {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Task.fxml")));
			Node taskNode = null;
			try {
				taskNode = loader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			TaskController taskController = loader.getController();
			taskController.setTask(task);
			taskControllers.add(taskController);
			this.hbox.getChildren().add(taskNode);
		}
        String path = "NormalScheduler.fxml";
        if (os.getSchedulerType().equals("multilevel")) {
            path = "MultilevelScheduler.fxml";
        }
		try {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
			AnchorPane component = loader.load();
            schedulerController = loader.getController();
			schedulerController.setScheduler(os.getScheduler());
			schedulerContainer.getChildren().clear();
            schedulerContainer.getChildren().add(component);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
	}
	public void update() {
		for (TaskController taskController : taskControllers) {
			taskController.update();
		}
		schedulerController.update();
		
		if (os.getCurrentTask() != null) {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Task.fxml")));
			Node taskNode = null;
			try {
				taskNode = loader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			TaskController taskController = loader.getController();
			taskController.setTask(os.getCurrentTask());
			
			currentTask.getChildren().clear();
			currentTask.getChildren().add(taskNode);
		} else {
			currentTask.getChildren().clear();
		}
		
	}
	@FXML
	private void onTickHalfUpdate() {
		os.tickHalfUpdate();
		update();
	}
	@FXML
	private void onTickUpdate() {
		os.tickUpdate();
		update();
	}
	@FXML
	private void onTickRun() {
		os.tickRun();
		update();
	}
	@FXML
	private void onTick() {
		os.tick();
		update();
	}
	@FXML
	private void onTickHalf() {
		os.tickHalf();
		update();
	}
	@FXML
	private void onTickThird() {
		os.tickThird();
		update();
	}
	
}
