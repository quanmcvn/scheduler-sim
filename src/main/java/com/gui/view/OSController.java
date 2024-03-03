package com.gui.view;

import com.gui.core.OS;
import com.gui.core.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	@FXML
	private TextField tickInput;
	@FXML
	private Label globalTime;
	private OS os;
	private SchedulerController schedulerController;
	private List<TaskController> taskControllers;
	private boolean showLevel = false;
	public void setOS(OS os) {
		this.os = os;
		List<Task> taskList = this.os.getTaskList();
		taskControllers = new ArrayList<>();
		if (os.getSchedulerType().equals("multilevel")) {
			showLevel = true;
		}
		for (Task task : taskList) {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Task.fxml")));
			Node taskNode = null;
			try {
				taskNode = loader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			TaskController taskController = loader.getController();
			taskController.setTask(task, showLevel);
			taskControllers.add(taskController);
			this.hbox.getChildren().add(taskNode);
		}
        String path = "NormalScheduler.fxml";
        if (os.getSchedulerType().equals("multilevel")) {
            path = "MultilevelScheduler.fxml";
			showLevel = true;
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
		globalTime.setText("Global Time: " + os.getGlobalTime());
		
		if (os.getCurrentTask() != null) {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Task.fxml")));
			Node taskNode = null;
			try {
				taskNode = loader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			TaskController taskController = loader.getController();
			taskController.setTask(os.getCurrentTask(), showLevel);
			
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
	
	@FXML
	private void onTickfor() {
		int input = 0;
		try {
			input = Integer.parseInt(tickInput.getText());
		} catch (NumberFormatException e) {
			System.out.println("Number please");
			return;
		}
		if (input <= 0) {
			return;
		}
		System.out.println("Skipping (at most) " + input + " tick(s) ...");
		for (int i = 0; i < input; ++ i) {
			if (os.done()) break;
			os.tick();
		}
		update();
	}
	
	@FXML
	private void onStat() {
		if (!os.done()) return;
		try {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Stat.fxml")));
			AnchorPane component = loader.load();
			StatController statController = loader.getController();
			statController.setText(os.stat());
			schedulerContainer.getChildren().clear();
			schedulerContainer.getChildren().add(component);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
