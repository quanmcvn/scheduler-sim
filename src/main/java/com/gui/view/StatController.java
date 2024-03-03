package com.gui.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class StatController {
	@FXML
	private TextArea textArea;
	public void setText(String text) {
		textArea.setText(text);
	}
}
