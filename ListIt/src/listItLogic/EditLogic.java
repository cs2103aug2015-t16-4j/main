// @@author Urvashi A0127781Y
package listItLogic;

import java.util.ArrayList;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

/**
 * This class contains methods to edit the selected task. Edition can be done by
 * either editing the title, importance level, date, time, repeat type (for recursive
 * tasks), the entire block time of the task, or everything.
 * @version 0.5
 */
public class EditLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static final String WHITESPACE = " ";
	private static final String COMMAND_TITLE = "by title";
	private static final String COMMAND_IMPORTANCE = "by impt";
	private static final String COMMAND_DEADLINE = "by date";
	private static final String COMMAND_TIMELINE = "by time";
	private static final String COMMAND_TO = "to";
	private static final String COMMAND_FROM = "from";
	private static final String COMMAND_REPEAT = "by repeat";
	private static final String COMMAND_BLOCK = "cancel block";
	private static final int IMPORTANCE_LEVEL_ONE = 1; 
	private static final int IMPORTANCE_LEVEL_TWO = 2; 
	private static final int IMPORTANCE_LEVEL_THREE = 3; 
	private static final String  EDIT_IMPORTANCE_INVALID = "Invalid Importance "
			                                                + "level,there are only"
			                                                + " 3 types: 1 , 2 or 3.\n"; 
    private static final String  EDIT_DATE_INVALID= "Invalid date is input\n"; 
    private static final String  EDIT_INPUT = "Invalid input!\n"; 
    private static final String  EDIT_IMPORTANCE_VALID = "Correct type of "
    		                                              + "importance level, "
    		                                              + "sucessfully editted!"; 
   	
	private static String  message = null; 
	
	/**
	 * Edits the task event selected by the user, according to the line index the 
	 * user inputs. also determines the variable type the user wants to input so as to
	 * edit the correct variable. 
	 * Repeat type = daily, monthly or yearly.
	 * Repeat cycle = period of how long the type should occur for. After the cycle is
	 *                complete, the cycle is reset and run again.
	 * @param command String command input by the user with an "edit" keyword.
	 */
    public static void editEvent(String command) {
		int indexToBeEdit = getEditIndex(command)-1;
		
		ArrayList<Task> taskList = modifier.getContentList();

		assert indexToBeEdit >= 0;
		
		if (!isValidRange(indexToBeEdit, taskList)) {
			FeedbackPane.displayInvalidInput();
			message = EDIT_INPUT; 
			LoggingLogic.logging(message);
		} else {
			if (isEditByDate(command)) {
				String newDate = getNewDate(command);
				if (AddLogic.isValidDate(newDate)) {
					modifier.editEndDate(indexToBeEdit, newDate);
					FeedbackPane.displayValidEdit();
				} else {
					FeedbackPane.displayInvalidDate();
					message = EDIT_DATE_INVALID;
					LoggingLogic.logging(message);
				}
			} else if (isEditByTitle(command)) {
				String newTitle = getNewTitle(command);
				modifier.editTitle(indexToBeEdit, newTitle);
				FeedbackPane.displayValidEdit();
			} else if (isEditByImportance(command)) {
				int newImportance = getNewImportanceLevel(command);
				
				if (isVeryImportant(newImportance) || 
					isImportant(newImportance) || 
					isNotImportant(newImportance)){
						modifier.editImportance(indexToBeEdit, newImportance);
						message = EDIT_IMPORTANCE_VALID; 
						LoggingLogic.logging(message);
						FeedbackPane.displayValidEdit();
					} else {
						FeedbackPane.displayInvalidIndexImptLevel();
						message = EDIT_IMPORTANCE_INVALID;
						LoggingLogic.logging(message);
					}
				} else if (isEditByTimeline(command)) {
					String newStartDate = getNewStartDate(command);
					String newEndDate = getNewEndDate(command);
					if(AddLogic.isCorrectRange(newStartDate, newEndDate)) {
						boolean isSuccess =	modifier.editTimeline(indexToBeEdit, newStartDate, newEndDate);
						if(isSuccess) {
							FeedbackPane.displayValidEdit();
						}
					} else {
						FeedbackPane.displayInvalidDate();
					}
				} else if (isEditByRepeat(command)) {
					String repeatCommand = getRepeatCommand(command);
					if (AddLogic.isCorrectRepeatCycle(repeatCommand)) {
						int newPeriod = 0;
						String repeatType = null;
						newPeriod = getNewPeriod(repeatCommand);
						repeatType = getRepeatType(repeatCommand);
						modifier.editRepeat(indexToBeEdit, newPeriod, repeatType);
						FeedbackPane.displayValidEdit();
					} else {
						FeedbackPane.displayInvalidEdit();
					}

				} else if (isEditByBlock(command)) {
					modifier.editBlock(indexToBeEdit);
					FeedbackPane.displayValidEdit();
				}
			}
		}

    
	private static String getRepeatType(String repeatCommand) {
		return repeatCommand.substring(repeatCommand.indexOf(WHITESPACE) + 1);
	}

	private static int getNewPeriod(String repeatCommand) {
		return Integer.parseInt(repeatCommand.substring(0, repeatCommand.indexOf(WHITESPACE)));
	}

	private static String getRepeatCommand(String command) {
		return command.substring(command.indexOf(COMMAND_REPEAT)
				                                 + 10);
	}

	private static boolean isNotImportant(int newImportance) {
		return newImportance == IMPORTANCE_LEVEL_THREE;
	}

	private static boolean isImportant(int newImportance) {
		return newImportance == IMPORTANCE_LEVEL_TWO;
	}

	private static boolean isVeryImportant(int newImportance) {
		return newImportance == IMPORTANCE_LEVEL_ONE;
	}

	private static boolean isValidRange(int indexToBeEdit, 
			                            ArrayList<Task> taskList) {
		return indexToBeEdit < taskList.size();
	}

	private static String getNewEndDate(String command) {
		return command.substring(command.indexOf(COMMAND_TO) + 3);
	}

	private static String getNewStartDate(String command) {
		return command.substring(command.indexOf(COMMAND_FROM) + 5,
				                 command.indexOf(COMMAND_TO) - 1);
	}

	private static int getNewImportanceLevel(String command) {
		return Integer.parseInt(command.substring(command.indexOf(COMMAND_IMPORTANCE)
				                                  + 8));

	}

	private static String getNewTitle(String command) {
		return command.substring(command.indexOf(COMMAND_TITLE) + 9);
	}

	private static String getNewDate(String command) {
		return command.substring(command.indexOf(COMMAND_DEADLINE) + 8);
	}
	
	/**
	 * Checks if the command contains a timeline input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByTimeline(String command) {
		return command.contains(COMMAND_TIMELINE);
	}

	/**
	 * Checks if the command contains a importance input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByImportance(String command) {
		return command.contains(COMMAND_IMPORTANCE);
	}

	/**
	 * Checks if the command contains a title input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByTitle(String command) {
		return command.contains(COMMAND_TITLE);
	}
	
	/**
	 * Checks if the command contains a date input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByDate(String command) {
		return command.contains(COMMAND_DEADLINE);
	}

	/**
	 * Checks if the command contains a recursive input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByRepeat(String command) {
		return command.contains(COMMAND_REPEAT);
	}

	/**
	 * Checks if the command contains a block input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByBlock(String command) {
		return command.contains(COMMAND_BLOCK);
	}

	private static int getEditIndex(String command) {
		return Integer.parseInt(command.substring(5, 6));
	}

	public static String getMessage() {
		return message;
	}
}
