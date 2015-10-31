package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import taskGenerator.Task;
import taskGenerator.TaskComparatorAlpha;
import taskGenerator.TaskComparatorDefault;
import taskGenerator.TaskComparatorImpt;

import java.util.Collections;

public class DisplayLogic {

	private static final String DISPLAY_ALPHA = "display alpha";
	private static final String DISPLAY_IMPT = "display impt";
	private static final Object DISPLAY_COMPLETE = "display complete";
	private static final String COMMAND_ALPHA = "alpha";
	private static final String COMMAND_IMPT = "impt";
	private static final String COMMAND_DEFAULT = "default";
	private static final String COMMAND_COMPLETE = "complete";
	
	private static ArrayList<Task> list = new ArrayList<Task>();
	static FileModifier modifier = FileModifier.getInstance();

	public static void determineDisplayMode(String command) {
		if (isDisplayAlphabetically(command)) {
			displayByAlpha();
		} else if (isDisplayByImportance(command)) {
			displayByImportance();
		} else if (isDisplayByComplete(command)) {
			displayByComplete();
		} else {
			defaultDisplay();
		}
	}

	private static boolean isDisplayByImportance(String command) {
		return command.equals(DISPLAY_IMPT);
	}

	private static boolean isDisplayAlphabetically(String command) {
		return command.equals(DISPLAY_ALPHA);
	}
	
	private static boolean isDisplayByComplete(String command) {
		return command.equals(DISPLAY_COMPLETE);
	}

	private static void displayByAlpha() {
		list = modifier.getContentList();
		modifier.setViewMode(COMMAND_ALPHA);
		updateFile();
	}

	private static void displayByImportance() {
		list = modifier.getContentList();
		modifier.setViewMode(COMMAND_IMPT);
		updateFile();
	}
	
	private static void displayByComplete() {
		list = modifier.getCompleteContentList();
		modifier.setViewMode(COMMAND_COMPLETE);
		updateFileComplete();
	}

	public static void defaultDisplay() {
		list = modifier.getContentList();
		modifier.setViewMode(COMMAND_DEFAULT);
		updateFile();
	}
	
	private static void updateFile() {
		modifier.sort(list);
		modifier.updateIndex(list);
		TaskCheckLogic.overDateCheck(list);
		modifier.saveFile(list);
		modifier.display(list);
	}
	
	private static void updateFileComplete() {
		modifier.updateIndex(list);
		modifier.saveCompleteFile(list);
		modifier.display(list);
	}
}
