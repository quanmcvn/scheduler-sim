package com.gui.view;

import com.gui.core.Scheduler;
import com.gui.core.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Objects;

public class NormalSchedulerController implements SchedulerController {
	@FXML
	private HBox hbox;
	private Scheduler scheduler;
	@Override
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		update();
	}
	@Override
	public void update() {
		this.hbox.getChildren().clear();
		for (Task task : scheduler.getAllTask()) {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Task.fxml")));
			Node taskNode = null;
			try {
				taskNode = loader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			TaskController taskController = loader.getController();
			taskController.setTask(task, false);
			this.hbox.getChildren().add(taskNode);
		}
	}
}
