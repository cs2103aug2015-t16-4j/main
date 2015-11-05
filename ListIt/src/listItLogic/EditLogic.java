package listItLogic;

import java.util.ArrayList;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class EditLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static final String COMMAND_TITLE = "by title";
	private static final String COMMAND_IMPORTANCE = "by impt";
	private static final String COMMAND_DEADLINE = "by date";
	private static final String COMMAND_TIMELINE = "by time";
	private static final String COMMAND_TO = "to";
	private static final String COMMAND_FROM = "from";
	private static final String COMMAND_REPEAT = "by repeat";
	private static final String COMMAND_BLOCK = "cancel block";
	private static String  message =null; 
	private static final String  editImptanceInvalid = "Invalid Importance level,there are only 3 types: 1 , 2 or 3.\n"; 
    private static final String  editDateInvalid = "Invalid date is inputed\n"; 
    private static final int importanceLevel1 = 1; 
    private static final int importanceLevel2 = 2; 
    private static final int importanceLevel3 = 3; 
	
    public static void editEvent(String command) {
		int indexToBeEdit = convertStringIndexToInt(command)-1;
		ArrayList<Task> taskList = modifier.getContentList();
		
		if(indexToBeEdit >= taskList.size()) {
			FeedbackPane.displayInvalidInput();
		} else {
		if (isEditByDate(command)) {
			String newDate = getNewDate(command);
			if (AddLogic.isValidDate(newDate)) {
				modifier.editEndDate(indexToBeEdit, newDate);
			} else {
				FeedbackPane.displayInvalidDate();
				message = editDateInvalid; 
			}
		} else if (isEditByTitle(command)) {
			String newTitle = getNewTitle(command);
			modifier.editTitle(indexToBeEdit, newTitle);
		} else if (isEditByImportance(command)) {
			int newImportance = getNewImportanceLevel(command);
			
<<<<<<< HEAD
			if(newImportance.equals("1") || newImportance.equals("2") || newImportance.equals("3")){
				modifier.editImportance(IndexToBeEdit - 1, newImportance); 
=======
			if(newImportance == importanceLevel1|| newImportance == importanceLevel2 || newImportance== importanceLevel3){
				modifier.editImportance(indexToBeEdit, newImportance);			
>>>>>>> origin/master
			}else{
				FeedbackPane.displayInvalidIndexImptLevel(); 
				message = editImptanceInvalid; 
			}
		} else if (isEditByTimeline(command)) {
			String newStartDate = getNewStartDate(command);
			String newEndDate = getNewEndDate(command);
			modifier.editTimeline(indexToBeEdit, newStartDate, newEndDate);
		} else if (isEditByRepeat(command)) {
			String repeatCommand = command.substring(command.indexOf(COMMAND_REPEAT) + 10);
			if (AddLogic.isCorrectRepeatCycle(repeatCommand)) {
				int newPeriod = 0;
				String repeatType = null;
				newPeriod = Integer.parseInt(repeatCommand.substring(0, repeatCommand.indexOf(" ")));
				repeatType = repeatCommand.substring(repeatCommand.indexOf(" ") + 1);
				modifier.editRepeat(indexToBeEdit , newPeriod, repeatType);
			} else {
				FeedbackPane.displayInvalidEdit();
			}

		} else if (isEditByBlock(command)) {
			modifier.editBlock(indexToBeEdit);
		}
		}
	}

	private static String getNewEndDate(String command) {
		return command.substring(command.indexOf(COMMAND_TO) + 3);
	}

	private static String getNewStartDate(String command) {
		return command.substring(command.indexOf(COMMAND_FROM) + 5, command.indexOf(COMMAND_TO) - 1);
	}

	private static int getNewImportanceLevel(String command) {
		return Integer.parseInt(command.substring(command.indexOf(COMMAND_IMPORTANCE) + 8));
		
	}

	private static String getNewTitle(String command) {
		return command.substring(command.indexOf(COMMAND_TITLE) + 9);
	}

	private static String getNewDate(String command) {
		return command.substring(command.indexOf(COMMAND_DEADLINE) + 8);
	}

	private static boolean isEditByTimeline(String command) {
		return command.contains(COMMAND_TIMELINE);
	}

	private static boolean isEditByImportance(String command) {
		return command.contains(COMMAND_IMPORTANCE);
	}

	private static boolean isEditByTitle(String command) {
		return command.contains(COMMAND_TITLE);
	}

	private static boolean isEditByDate(String command) {
		return command.contains(COMMAND_DEADLINE);
	}

	private static boolean isEditByRepeat(String command) {
		return command.contains(COMMAND_REPEAT);
	}
	
	private static boolean isEditByBlock(String command) {
		return command.contains(COMMAND_BLOCK);
	}

	private static int convertStringIndexToInt(String command) {
		return Integer.parseInt(command.substring(5, 6));
	}
	public static String getMessage(){
		return message; 
	}
}
