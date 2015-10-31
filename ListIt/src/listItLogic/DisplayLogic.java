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
	private static final String COMMAND_ALPHA = "alpha";
	private static final String COMMAND_IMPT = "impt";
	private static final String COMMAND_DEFAULT = "default";
	private static ArrayList<Task> list = new ArrayList<Task>();
	static FileModifier modifier = FileModifier.getInstance();

	public static void determineDisplayMode(String command) {
		if (isDisplayAlphabetically(command)) {
			displayByAlpha();
		} else if (isDisplayByImportance(command)) {
			displayByImportance();
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

	private static void displayByAlpha() {
		list = modifier.getContentList();
		modifier.setViewMode(COMMAND_ALPHA);
		updateFile();
	}

	private static void updateFile() {
		modifier.sort(list);
		modifier.updateIndex(list);
		modifier.saveFile(list);
		modifier.display(list);
	}

	private static void displayByImportance() {
		list = modifier.getContentList();
		modifier.setViewMode(COMMAND_IMPT);
		updateFile();
	}

	public static void defaultDisplay() {
		list = modifier.getContentList();
		modifier.setViewMode(COMMAND_DEFAULT);
		updateFile();
	}
}
