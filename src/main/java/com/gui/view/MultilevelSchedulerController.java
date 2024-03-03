package com.gui.view;

import com.gui.core.ScheduleMultilevelQueue;
import com.gui.core.Scheduler;
import com.gui.core.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultilevelSchedulerController implements SchedulerController {
	@FXML
	private HBox hbox1;
	@FXML
	private HBox hbox2;
	@FXML
	private HBox hbox3;
	private List<HBox> hboxList;
	private ScheduleMultilevelQueue scheduleMultilevelQueue;
	@FXML
	private void initialize() {
		hboxList = new ArrayList<>();
		hboxList.add(hbox1);
		hboxList.add(hbox2);
		hboxList.add(hbox3);
	}
	
	@Override
	public void setScheduler(Scheduler scheduler) {
		this.scheduleMultilevelQueue = (ScheduleMultilevelQueue) scheduler;
	}
	
	@Override
	public void update() {
		for (HBox hbox : hboxList) {
			hbox.getChildren().clear();
		}
		for (Task task : scheduleMultilevelQueue.getAllTask()) {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Task.fxml")));
			Node taskNode = null;
			try {
				taskNode = loader.load();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			TaskController taskController = loader.getController();
			taskController.setTask(task, true);
			hboxList.get(task.getQueueLevel()).getChildren().add(taskNode);
		}
	}
}
