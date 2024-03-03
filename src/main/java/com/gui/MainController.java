package com.gui;

import com.gui.core.*;
import com.gui.view.OSController;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private Label fileInput;
    private ObservableList<String> scheduleTypes = FXCollections.observableArrayList(
            "fcfs", "sjf", "sjf_preemptive", "priority", "priority_preemptive", "roundrobin", "multilevel"
    );
    @FXML
    private AnchorPane container;
    @FXML
    private ComboBox<String> options;
	@FXML
	private Label quantumLabel;
	@FXML
	private TextField quantumInput;
    @FXML
    private void initialize() {
        options.setItems(scheduleTypes);
    }
    @FXML
    private void onOptionChoose() {
        System.out.println("chosen: " + options.getValue());
	    quantumLabel.setVisible(options.getValue().equals("roundrobin"));
	    quantumInput.setVisible(options.getValue().equals("roundrobin"));
    }
    @FXML
    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        
        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            System.out.println("Selected File: " + path);
            fileInput.setText(path);
        }
    }
    private OS os;
    @FXML
    private void setup() {
        if (!scheduleTypes.contains(options.getValue())) {
			System.out.println("Didn't choose schedule type");
			return;
        }
	    String schedulerType = options.getValue();
		Scheduler scheduler = null;
	    switch (schedulerType) {
		    case "fcfs" -> scheduler = new ScheduleFCFS();
		    case "sjf" -> scheduler = new ScheduleSJF();
		    case "sjf_preemptive" -> scheduler = new ScheduleSJFPreemptive();
		    case "priority" -> scheduler = new SchedulePriority();
		    case "priority_preemptive" -> scheduler = new SchedulePriorityPreemptive();
		    case "roundrobin" -> {
			    int quantum = 1;
			    try {
				    quantum = Integer.parseInt(quantumInput.getText());
			    } catch (NumberFormatException e) {
				    System.out.println("Number please");
				    return;
			    }
			    try {
				    scheduler = new ScheduleRoundRobin(quantum);
			    } catch (IllegalArgumentException e) {
				    System.out.println(e.getMessage());
			    }
		    }
		    case "multilevel" -> scheduler = new ScheduleMultilevelQueue();
		    default -> {
			    System.out.println("??? sched " + schedulerType);
				return;
		    }
	    }
		os = new OS();
		os.setScheduler(scheduler);
	    if (!os.readFromFile(fileInput.getText())) return;
//	    os.readFromFile("/home/quanmcvn/Desktop/Code/project/osing/lec/bt3/gui/test/bt2.txt");
	    try {
			FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("view/OS.fxml")));
			AnchorPane component = loader.load();
			container.getChildren().clear();
			container.getChildren().add(component);
			OSController osController = loader.getController();
			osController.setOS(os);
	    } catch (IOException e) {
		    System.err.println(e.getMessage());
	    }
    }
}