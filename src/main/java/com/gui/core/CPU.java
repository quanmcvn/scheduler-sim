package com.gui.core;

public class CPU {
    public void run(Task task, int time) {
		task.setBurstLeft(task.getBurstLeft() - time);
    }
}
