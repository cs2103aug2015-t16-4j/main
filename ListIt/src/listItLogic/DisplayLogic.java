package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import taskGenerator.Task;
import taskGenerator.TaskComparatorAlpha;
import taskGenerator.TaskComparatorDefault;
import taskGenerator.TaskComparatorImpt;

import java.util.Collections;

public class DisplayLogic {
	
	private static final String DISPLAY_ALPHA = "display alphabetic";
	private static final String DISPLAY_IMPT = "display impt";
	private static ArrayList<Task> list = new ArrayList<Task>();
	static FileModifier modifier = FileModifier.getInstance();
	
	
	public static void determineDisplayMode(String command) {
		if (command.equals(DISPLAY_ALPHA)) {
			displayByAlpha();
		} else if (command.equals(DISPLAY_IMPT)) {
			displayByImportance();
		} else {
			defaultDisplay();
		}
	}
	private static void displayByAlpha() {
		list = modifier.getContentList();
		if(list.isEmpty() == false) {
			Collections.sort(list, new TaskComparatorAlpha());
			modifier.displayAlpha(list);
		}
		else {
			modifier.displayEmpty();
		}
	}
	
	private static void displayByImportance() {
		list = modifier.getContentList();
		if(list.isEmpty() == false) {
			Collections.sort(list, new TaskComparatorImpt());
			modifier.displayImpt(list);
		}
		else {
			modifier.displayEmpty();
		}
	}
	
	public static void defaultDisplay() {
		list = modifier.getContentList();
		if(list.isEmpty() == false) {
			Collections.sort(list, new TaskComparatorDefault());
			modifier.display(list);
		}
		else {
			modifier.displayEmpty();
		}
	}
}
