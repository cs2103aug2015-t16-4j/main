package listItLogic;

import java.util.ArrayList;

public class HelpLogic {
	
	private static ArrayList<String> commandList = new ArrayList<String>();
	private static ArrayList<String> inputCommand = new ArrayList<String>();
	
	public static void createHelpList() {
		
		commandList.add("add event with deadline");
		commandList.add("add event with timeline");
		commandList.add("add event with no deadline");
		commandList.add("add event with rank");
		commandList.add("add recurring tasks");
		commandList.add("add events with range of dates");
		commandList.add("edit date only");
		commandList.add("edit title only");
		commandList.add("edit timeline only");
		commandList.add("edit rank only");
		commandList.add("deleting event");
		commandList.add("undo");
		commandList.add("redo");
		commandList.add("display default");
		commandList.add("search by title");
		commandList.add("search by date");
		commandList.add("search by importance");
		commandList.add("change directory");
		commandList.add("marking task as complete");
		commandList.add("clear file");
		
		inputCommand.add("add <description> by <deadline>");
		inputCommand.add("add <description> from <start time> to <end time>");
		inputCommand.add("add <description>");
		inputCommand.add("add <description>");
		inputCommand.add("add <description> by <deadline> rank <number>");
		inputCommand.add("add <description> repeat <monthly/yearly/daily>");
		inputCommand.add("add <description> block <start date> to <end date>");
		inputCommand.add("edit <task index> by date <new date>");
		inputCommand.add("edit <task index> by title <updated title>");
		inputCommand.add("edit <task index> by time from <new start date> to <new end date>");
		inputCommand.add("edit <task index> <new rank>");
		inputCommand.add("delete <task index>");
		inputCommand.add("undo");
		inputCommand.add("redo");
		inputCommand.add("display");
		inputCommand.add("search <title>");
		inputCommand.add("search date <date>");
		inputCommand.add("search impt <rank>");
		inputCommand.add("cd <new directory>");
		inputCommand.add("complete <task index>");
		inputCommand.add("clear");
		
		
	}

	public static void displayHelp() {
		createHelpList();
	}

}
