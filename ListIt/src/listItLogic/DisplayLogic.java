package listItLogic;


import java.io.File;
import java.util.ArrayList;

import fileModifier.FileModifier;
import listItUI.OutputScreenPane;
import taskGenerator.Task;

public class DisplayLogic {
	
	private static final String DISPLAY_DATE = "display date";
	private static final String DISPLAY_IMPT = "display impt";
	private static ArrayList<Task> list = new ArrayList<Task>();
	static FileModifier modifier = FileModifier.getInstance();
	
	
	public static void determineDisplayMode(String command) {
		if (command.equals(DISPLAY_DATE)) {
			displayByDate();
		} else if (command.equals(DISPLAY_IMPT)) {
			displayByImportance();
		} else {
			defaultDisplay();
		}
	}
	private static void displayByDate() {
		list = modifier.getContentList();
		//Comparator.sortByDate(list);
		modifier.display(list);
	}
	
	private static void displayByImportance() {
		list = modifier.getContentList();
		//Comparator.sortByImpt(list);
		modifier.display(list);
	}
	
	private static void defaultDisplay() {
		list = modifier.getContentList();
		//Comparator.sortDefault(list);
		modifier.display(list);
	}
}
