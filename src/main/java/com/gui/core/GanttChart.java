package com.gui.core;

import java.util.ArrayList;
import java.util.List;

public class GanttChart {
	
	private static class Cell {
		private final String name;
		
		public String getName() {
			return name;
		}
		
		public int getTime() {
			return time;
		}
		
		public void setTime(int time) {
			this.time = time;
		}
		
		private int time;
		
		public Cell(String name, int time) {
			this.name = name;
			this.time = time;
		}
	}
	private final List<Cell> cells;
	
	public GanttChart() {
		this.cells = new ArrayList<>();
	}
	
	public void addTask(Task task, int time) {
		if (time > 0) cells.add(new Cell(task.getName(), time));
	}
	public void addTaskNoContextSwitch(Task task, int time) {
		Cell lastCell = !cells.isEmpty() ? cells.get(cells.size() - 1) : null;
		if (lastCell != null && task.getName().equals(lastCell.getName())) {
			lastCell.setTime(lastCell.getTime() + time);
		} else {
			addTask(task, time);
		}
	}
	
	public int getSize() {
		return cells.size();
	}
	
	private void printchar(char c, int times) {
		for (int i = 0; i < times; ++ i) {
			System.out.print(c);
		}
	}
	
	public void draw() {
		int n = cells.size();
		int scale = 1;
		int total = 0;
		for (Cell cell : cells) {
			// preferable size = len + pad 1 space left and right so not cramped
			int preferable_len = cell.getName().length() + 2;
			int scale_min = (preferable_len + cell.getTime() - 1) / cell.getTime();
			if (scale < scale_min) scale = scale_min;
			total += cell.getTime();
		}

		printchar('-', (total * scale + n + 1));
		System.out.print('\n');

		System.out.print('|');
		for (Cell cell : cells) {
			int spaces = cell.getTime() * scale - cell.getName().length();
			printchar(' ', (spaces / 2));
			System.out.printf("%s", cell.getName());
			printchar(' ', ((spaces + 1) / 2));
			System.out.print('|');
		}
		System.out.print("\n");
		printchar('-', (total * scale + n + 1));

		System.out.print("\n");

		int time_now = 0;
		for (Cell cell : cells) {
			System.out.print(time_now);
			int len = String.valueOf(time_now).length();
			time_now += cell.getTime();
			printchar(' ', (cell.getTime() * scale - len + 1));
		}
		System.out.printf("%d", time_now);
		System.out.print("\n");
	}
}
