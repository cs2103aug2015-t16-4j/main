// @@author Shawn A0124181R
package listItLogic;

import java.util.ArrayList;

import listItUI.UIMain;

/**
 * this class contains the method to activate our help function, which causes another 
 * pop up window (that contains all our possible commands) to appear. 
 * @version 0.5
 */
public class HelpLogic {
	
	private static ArrayList<String> commandList = new ArrayList<String>();
	private static ArrayList<String> inputCommand = new ArrayList<String>();
	private static HelpLogic help;
	private static final String HELP_MESSAGE = "The help sheet is shown on the "
			                                    + "screen.\n"; 

	public HelpLogic () {
		createHelpList();
	}
	
	public static HelpLogic getInstance() {
		if(!isExists()) {
			help = new HelpLogic();
		}
		return help;
	}

	private static boolean isExists() {
		return help != null;
	}
	
	/**
	 * This method creates the help list by creating 2 arrays, commandList 
	 * and inputCommand
	 * 
	 */
	public void createHelpList() {	
		commandList.add("add event with deadline");
		commandList.add("add event with timeline");
		commandList.add("add event with timeline, single day");
		commandList.add("add event with no deadline");
		commandList.add("add event with rank");
		commandList.add("add recurring tasks with deadline");
		commandList.add("add recurring tasks with timeline");
		commandList.add("add timeline events with blocking ability");
		commandList.add("edit date only");
		commandList.add("edit title only");
		commandList.add("edit timeline only");
		commandList.add("edit rank only");
		commandList.add("deleting event");
		commandList.add("undo");
		commandList.add("redo");
		commandList.add("display default");
		commandList.add("display by date");
		commandList.add("display by importance");
		commandList.add("display alphabetically");
		commandList.add("search by title");
		commandList.add("search by date");
		commandList.add("search by importance");
		commandList.add("change directory");
		commandList.add("marking task as complete");
		commandList.add("clear file");
		commandList.add("help");
		commandList.add("exit");
		
		inputCommand.add("add <description> by <deadline>");
		inputCommand.add("add <description> from <start date time> to "
				          + "<end date time> ");
		inputCommand.add("add <description> on <date> from <start time> "
				          + "to <end time>");
		inputCommand.add("add <description>");
		inputCommand.add("add <description> by <deadline> rank <number>");
		inputCommand.add("add <description> repeat <repeat period>"
				         + " <monthly/yearly/daily> " + "on <date>");
		inputCommand.add("add <description> repeat <repeat period>" + " <monthly/yearly/daily> " + "from <startDate> to <endDate>");
		inputCommand.add("add <description> block <start date> to <end date>");
		inputCommand.add("edit <task index> by date <new date>");
		inputCommand.add("edit <task index> by title <updated title>");
		inputCommand.add("edit <task index> by time from <new start date> "
				         + "to <new end date>");
		inputCommand.add("edit <task index> by impt <new rank>");
		inputCommand.add("delete <task index>");
		inputCommand.add("undo");
		inputCommand.add("redo");
		inputCommand.add("display");
		inputCommand.add("display date");
		inputCommand.add("display impt");
		inputCommand.add("display alpha");
		inputCommand.add("search <title>");
		inputCommand.add("search date <date>");
		inputCommand.add("search impt <rank>");
		inputCommand.add("cd <new directory>");
		inputCommand.add("complete <task index>");
		inputCommand.add("clear");
		inputCommand.add("help");
		inputCommand.add("exit");
	}
	
	/**
	 * This method displays the help arrays created into a new UI window
	 */
	public static void displayHelp() {
		help = getInstance();
		UIMain.popUpHelp(commandList, inputCommand);	
		LoggingLogic.logging(HELP_MESSAGE);
	}
}
