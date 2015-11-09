# Urvashi A0127781Y
###### src\fileModifier\FileModifier.java
``` java
package fileModifier;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import listItLogic.LoggingLogic;
import listItLogic.TaskCheckLogic;
import listItUI.FeedbackPane;
import listItUI.OutputScreenPane;
import taskGenerator.Task;
import taskGenerator.TaskComparatorAlpha;
import taskGenerator.TaskComparatorDefault;
import taskGenerator.TaskComparatorImpt;

/**
 * This class runs the command given by the user, stores the created task
 * objects, and writes the data into the data file.
 * 
 * @version 0.5
 */
public class FileModifier {
	private static FileModifier modifier;
	private static String viewMode = "default";
	private File dataFile;
	private File completeDataFile;
	private File pathStorage;
	private String taskFileName = "Task.txt";
	private String completeTaskFileName = "CompleteTask.txt";
	private String pathStorageFileName = "path.txt";
	private static final String TYPE_DAILY = "daily";
	private static final String TYPE_WEEKLY = "weekly";
	private static final String TYPE_MONTHLY = "monthly";
	private static final String TYPE_YEARLY = "yearly";
	private static final String MODE_DEFAULT = "default";
	private static final String MODE_IMPT = "impt";
	private static final String MODE_ALPHA = "alpha";
	private static final String MODE_COMPLETE = "complete";
	private static final String ADDING_SUCCESSFUL = "Adding of the task was "
			                                         + "successful.\n";
	private static final String ADDING_UNSUCCESSFUL = "The task was not added"
			                                          + " could be because this"
			                                          + " task has been blocked for"
			                                          + " another event.\n";
	private static final String DELETE_VALID = "The task has been successfully"
            + " deleted.\n";
	private static final String CLEAR_VALID = "The file has been successfully been"
           + " cleared.\n";

	private FileModifier() {
		pathStorage = new File(pathStorageFileName);
		String path = null;
		try {
			pathStorage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader textFileReader = new BufferedReader(new FileReader(pathStorageFileName));
			path = textFileReader.readLine();
			textFileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (path != null) {
			try {
				dataFile = new File(path + taskFileName);
				completeDataFile = new File(path + completeTaskFileName);
				dataFile.createNewFile();
				completeDataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				dataFile = new File(taskFileName);
				completeDataFile = new File(completeTaskFileName);
				dataFile.createNewFile();
				completeDataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static FileModifier getInstance() {
		if (modifier == null) {
			modifier = new FileModifier();
		}
		return modifier;
	}

	/**
	 * This method saves the current task into the data file
	 * @param dataStorage data file that stores all tasks
	 */
	public void saveFile(ArrayList<Task> dataStorage) {
		try {
			FileOutputStream fos = new FileOutputStream(dataFile, false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dataStorage);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method saves the completed task list into the data file.
	 * @param completeTaskStorage the completed task list to be saved
	 */
	public void saveCompleteFile(ArrayList<Task> completeTaskStorage) {
		try {
			FileOutputStream fos = new FileOutputStream(completeDataFile, false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(completeTaskStorage);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method gets the current contents available in the file at that
	 * current instance
	 * @return the list of task objects
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Task> getContentList() {
		ArrayList<Task> list = new ArrayList<Task>();

		// Retrieve the list if the list exist, if not, create a empty list then
		// retrieve
		if (!isListEmpty(dataFile)) {
			try {
				FileInputStream fis = new FileInputStream(dataFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				list = (ArrayList<Task>) ois.readObject();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				FileOutputStream fos = new FileOutputStream(dataFile, false);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(new ArrayList<Task>());
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * This method gets the completed task list
	 * @return completed task list
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Task> getCompleteContentList() {
		ArrayList<Task> list = new ArrayList<Task>();

		// Retrieve the list if the list exists, if not, creates an empty list
		// then retrieves the empty list
		if (!isListEmpty(completeDataFile)) {
			try {
				FileInputStream fis = new FileInputStream(completeDataFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				list = (ArrayList<Task>) ois.readObject();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				FileOutputStream fos = new FileOutputStream(completeDataFile, 
						                                    false);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(new ArrayList<Task>());
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * Checks if the task list is empty or not
	 * @return true if list is empty false if not empty
	 */
	private boolean isListEmpty(File file) {
		boolean isEmpty = true;

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<Task> list = (ArrayList<Task>) ois.readObject();
			ois.close();
			isEmpty = false;
		} catch (Exception e) {
			isEmpty = true;
		}
		return isEmpty;
	}

	/**
	 * This returns the current data file for the normal task list
	 * @return data file that stores all tasks
	 */
	public File getDataFile() {
		return this.dataFile;
	}

	/**
	 * This returns the current data file for the completed task list.
	 * @return dataFile for the completed task list
	 */
	public File getCompleteDataFile() {
		return this.completeDataFile;
	}

	/**
	 * This returns the file that contains the paths of data files
	 * @return  path folder
	 */
	public File getPathFile() {
		return this.pathStorage;
	}

	/**
	 * This method adds the task to the current list of tasks
	 * @param newtask the task to be added into the list
	 */
	public boolean addTask(Task newtask) {
		assert newtask != null;
		
		if (TaskCheckLogic.blockedDateCheck(newtask)) {
			ArrayList<Task> newList = modifier.getContentList();
			newList.add(newtask);
			updateFile(newList);
			LoggingLogic.logging(ADDING_SUCCESSFUL);
			return true;
		} else {
			FeedbackPane.displayInvalidAddBlocked();
			LoggingLogic.logging(ADDING_UNSUCCESSFUL);
			return false;
		}
	}

	/**
	 * This method updates the list after a change is done to the task by
	 * sorting the list and then updating the line index after that
	 * @param newList the task list
	 */
	private void updateFile(ArrayList<Task> newList) {
		modifier.sort(newList);
		modifier.updateIndex(newList);
		TaskCheckLogic.overDateCheck(newList);
		modifier.saveFile(newList);
		modifier.display(newList);
	}

	/**
	 * This method updates the completed task list after a change is done
	 * to the task by updating the line index.
	 * @param newList the completed task list
	 */
	private void updateCompleteListFile(ArrayList<Task> newList) {
		modifier.updateIndex(newList);
		modifier.saveCompleteFile(newList);
		if (isViewModeComplete()) {
			modifier.display(newList);
		}
	}

	/**
	 * This method removes a task from the current list
	 * @param index the line number of the task to be removed
	 */
	public void removeTask(int index) {
		assert index >= 0;
		
		ArrayList<Task> taskList;
		if (isViewModeComplete()) {
			taskList = modifier.getCompleteContentList();
			taskList.remove(index);
			updateCompleteListFile(taskList);
			LoggingLogic.logging(DELETE_VALID);
		} else {
			taskList = modifier.getContentList();
			taskList.remove(index);
			updateFile(taskList);
			LoggingLogic.logging(DELETE_VALID);
		}

	}

	/**
	 * This method will check whether the list is complete or not then display
	 * the appropriate UI by calling the appropriate methods
	 */
	public void display() {
		if (isViewModeComplete()) {
			display(getCompleteContentList());
		} else {
			display(getContentList());
		}
	}

	/**
	 * This method will display the UI after all the tasks are updated
	 * @param taskList the entire list of tasks to be displayed
	 */
	public void display(ArrayList<Task> taskList) {
		if (taskList.isEmpty()) {
			OutputScreenPane.displayEmpty();
			LoggingLogic.logging("Nothing to display");
		} else if (isViewModeDefault()) {
			OutputScreenPane.displayList(taskList);
			LoggingLogic.logging("Displaying in default mode success");
		} else if (isViewModeImpt()) {
			OutputScreenPane.displayListImpt(taskList);
			LoggingLogic.logging("Display by importance level mode success");
		} else if (isViewModeAlpha()) {
			OutputScreenPane.displayListAlpha(taskList);
			LoggingLogic.logging("Display in alphabetical mode success");
		} else if (isViewModeComplete()) {
			OutputScreenPane.displayListComplete(taskList);
			LoggingLogic.logging("Complete list is displayed");
		}
	}

	public boolean isViewModeComplete() {
		return viewMode.equals(MODE_COMPLETE);
	}

	
	private boolean isViewModeAlpha() {
		return viewMode.equals(MODE_ALPHA);
	}

	private boolean isViewModeImpt() {
		return viewMode.equals(MODE_IMPT);
	}

	private boolean isViewModeDefault() {
		return viewMode.equals(MODE_DEFAULT);
	}

	/**
	 * This method clears the entire list of tasks
	 */
	public void clearAll() {
		ArrayList<Task> taskList;
		if (isViewModeComplete()) {
			taskList = modifier.getCompleteContentList();
			taskList.clear();
			updateCompleteListFile(taskList);
			LoggingLogic.logging(CLEAR_VALID);
		} else {
			taskList = modifier.getContentList();
			taskList.clear();
			updateFile(taskList);
			LoggingLogic.logging(CLEAR_VALID);
		}
	}

	/**
	 * This method searches the current list by title/keyword, not caring about
	 * case sensitivity
	 * @param keyword the keyword to be searched for
	 * @return the list of tasks which contains the keyword
	 */
	public ArrayList<Task> searchKeyword(String keyword) {
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();
		
		assert keyword != null;

		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (task.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
				searchList.add(task);
			}
		}
		return searchList;
	}

	/**
	 * This method searches the current list by the importance variable
	 * @param searchKey The level of importance, 1, 2 or 3
	 * @return the list of tasks which has that importance level
	 */
	public ArrayList<Task> searchByImportance(int searchKey) {
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();
		
		assert searchKey >= 1;
		assert searchKey <= 3;

		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (task.getImportance() == searchKey) {
				searchList.add(task);
			}
		}
		return searchList;
	}

	/**
	 * This method searches the current list by the date input
	 * @param date the date variable
	 * @return list of tasks which is on the date searched
	 */
	public ArrayList<Task> searchByDate(String date) {
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();
		
		assert date != null;

		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (task.getEndDate() != null) {
				if (task.getDateInputForm().contains(date)) {
					searchList.add(task);
				}
			}
		}
		return searchList;
	}

	/**
	 * This method edits the end date variable, by checking whether it is a date
	 * object with time or date without time then changes the date appropriately
	 * @param lineToBeEdit the line index of the task to be edited
	 * @param endDate the date object, with or without time
	 */
	public void editEndDate(int lineToBeEdit, String endDate) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task task = taskList.get(lineToBeEdit);
		if (endDate.contains(" ")) {
			task.setEndDateWithTime(endDate);
			task.setHasTime(true);
		} else {
			task.setEndDate(endDate);
			task.setHasTime(false);
		}
		taskList.set(lineToBeEdit, task);
		updateFile(taskList);
	}

	/**
	 * This method edits the entire timeline of the task, which must include
	 * both the start date as well as the end date
	 * @param lineToBeEdit the line index of the task to be edited
	 * @param startDate the start date object with or without time
	 * @param endDate the end date object with or without time
	 */
	public boolean editTimeline(int lineToBeEdit, String startDate, String endDate) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task task = taskList.get(lineToBeEdit);
		if (startDate.contains(" ")) {
			task.setStartDateWithTime(startDate);
			task.setEndDateWithTime(endDate);
			task.setHasTime(true);
		} else {
			task.setStartDate(startDate);
			task.setEndDate(endDate);
			task.setHasTime(false);
		}
		if(TaskCheckLogic.blockedDateCheck(task)) {
			taskList.set(lineToBeEdit, task);
			updateFile(taskList);
			return true;
		} else {
			FeedbackPane.displayInvalidEditBlocked();
			return false;
		}
	}

	/**
	 * This method edits the title of the task
	 * @param lineToBeEdit the line index of the task to be edited
	 * @param newTitle the edited version of the title
	 */
	public void editTitle(int lineToBeEdit, String newTitle) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task task = taskList.get(lineToBeEdit);
		task.setEventTitle(newTitle);
		taskList.set(lineToBeEdit, task);
		updateFile(taskList);
	}

	/**
	 * This method edits the importance variable of the task
	 * @param IndexToBeEdit the line index of the task to be edited
	 * @param newImportance the edited version of the importance variable
	 */
	public void editImportance(int IndexToBeEdit, int newImportance) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task task = taskList.get(IndexToBeEdit);
		task.setImportance(newImportance);
		taskList.set(IndexToBeEdit, task);
		updateFile(taskList);
	}

	/**
	 * This method sorts the list of tasks according to our own sorting
	 * algorithm in the task class
	 * @param taskList the entire list to be sorted
	 */
	public void sort(ArrayList<Task> taskList) {
		if (isViewModeDefault()) {
			Collections.sort((taskList), new TaskComparatorDefault());
		} else if (isViewModeImpt()) {
			Collections.sort((taskList), new TaskComparatorImpt());
		} else if (isViewModeAlpha()) {
			Collections.sort(taskList, new TaskComparatorAlpha());
		}
	}

	/**
	 * This method sets the view mode
	 * @param newMode view mode
	 */
	public void setViewMode(String newMode) {
		viewMode = newMode;
	}

	/**
	 * This method updates the line index of the entire task list
	 * @param taskList the entire list of tasks
	 */
	public void updateIndex(ArrayList<Task> taskList) {
		if (taskList.isEmpty()) {
			return;
		} else {
			for (int i = 1; i <= taskList.size(); i++) {
				taskList.get(i - 1).setIndex(i);
			}
		}
	}

	/**
	 * This method sends the task selected into the completed task list and
	 * removes it from the current task list
	 * @param index the line index of the task to be moved
	 */
	public void completeTask(int index) {
		ArrayList<Task> completedList = modifier.getCompleteContentList();
		ArrayList<Task> taskList = modifier.getContentList();
		Calendar calendar = Calendar.getInstance();

		Task completedTask = taskList.get(index);
		if (completedTask.getRepeat() == false) {
			completedTask.setComplete();
			completedList.add(0, completedTask);
			taskList.remove(index);
			updateFile(taskList);
			updateCompleteListFile(completedList);
		} else {
			if (completedTask.getStartDate() == null) {
				Date currentDeadline = completedTask.getEndDateInDateType();
				int repeatCycle = completedTask.getRepeatCycle();
				String repeatType = completedTask.getRepeatType();
				calendar.setTime(currentDeadline);
				Date nextDeadline = getNextDeadline(calendar, repeatCycle, 
						                            repeatType);
				completedTask.setEndDateInDate(nextDeadline);
				taskList.set(index, completedTask);
				updateFile(taskList);
			} else {
				Date currentStartDate = completedTask.getStartDateInDateType();
				Date currentEndDate = completedTask.getEndDateInDateType();
				int repeatCycle = completedTask.getRepeatCycle();
				String repeatType = completedTask.getRepeatType();
				calendar.setTime(currentStartDate);
				Date nextStartDate = getNextDeadline(calendar, repeatCycle,
						                             repeatType);
				calendar.setTime(currentEndDate);
				Date nextEndDate = getNextDeadline(calendar, repeatCycle,
						                           repeatType);
				completedTask.setStartDateInDate(nextStartDate);
				completedTask.setEndDateInDate(nextEndDate);
				taskList.set(index, completedTask);
				updateFile(taskList);
			}
		}
	}

	/**
	 * This method edits the recursive tasks, by changing these variables; the
	 * period of repetition and the type of repetition.
	 * @param index line index
	 * @param newPeriod The time frame of the recursive task
	 * @param repeatType the type of recursive task
	 */
	public void editRepeat(int index, int newPeriod, String repeatType) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task tempTask = taskList.get(index);
		tempTask.setRepeatCycle(newPeriod);
		tempTask.setRepeatType(repeatType);
		taskList.set(index, tempTask);
		updateFile(taskList);
	}

	/**
	 * This method gets the next date for the recursive function by calling the
	 * calendar and calculating the date according to the type of recursive
	 * function the user input
	 * @param calendar the actual calendar to get the actual time
	 * @param repeatCycle the length of time needed to repeat
	 * @param repeatType the type of recursive task
	 * @return the deadline object with a startdate and enddate
	 */
	private Date getNextDeadline(Calendar calendar, int repeatCycle,
			                     String repeatType) {
		if (repeatType.equals(TYPE_DAILY)) {
			calendar.add(Calendar.DATE, repeatCycle);
		} else if (repeatType.equals(TYPE_WEEKLY)) {
			calendar.add(Calendar.WEEK_OF_YEAR, repeatCycle);
		} else if (repeatType.equals(TYPE_MONTHLY)) {
			calendar.add(Calendar.MONTH, repeatCycle);
		} else if (repeatType.equals(TYPE_YEARLY)) {
			calendar.add(Calendar.YEAR, repeatCycle);
		}

		Date nextDeadline = calendar.getTime();
		return nextDeadline;
	}

	/**
	 * Edits the block timeline, which has a start and end time
	 * @param index the line index of the task to be edited
	 */
	public void editBlock(int index) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task tempTask = taskList.get(index);
		tempTask.setBlocking(false);
		taskList.set(index, tempTask);
		updateFile(taskList);
	}

	/**
	 * Moves the current file location to the destination selected by the user
	 * @param destination1 the data file for the normal task list
	 * @param destination2 the data file for the completed task list
	 * @return true if successfully moved, else returns false
	 */
	public boolean moveFiles(Path destination1, Path destination2) {

		try {
			dataFile = new File(Files.move(dataFile.toPath(), destination1,
					            REPLACE_EXISTING).toString());
			completeDataFile = new File(Files.move(completeDataFile.toPath(),
					                    destination2, REPLACE_EXISTING).toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
```
###### src\listItLogic\EditLogic.java
``` java
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
```
###### src\listItLogic\TaskCheckLogic.java
``` java
package listItLogic;

import java.util.ArrayList;
import java.util.Date;

import fileModifier.FileModifier;
import taskGenerator.Task;

/**
 * This class contains all the methods that check if the command date input
 * entered by the user is of a valid date input, and also compares the task date
 * to the actual calendar date.
 * @version 0.5
 */
public class TaskCheckLogic {
	static FileModifier modifier = FileModifier.getInstance();

	public TaskCheckLogic() {

	}

	/**
	 * Checks if the date variable of the task is over the actual calendar date.
	 * @param taskList selected task list
	 */
	public static void overDateCheck(ArrayList<Task> taskList) {
		Task tempTask = new Task();
		Date systemTime = new Date();
		for (int i = 0; i < taskList.size(); i++) {
			tempTask = taskList.get(i);
			if (!isEndDateNull(tempTask)) {
				if (isOverDate(tempTask, systemTime)) {
					tempTask.setOverDate();
					taskList.set(i, tempTask);
				} else {
					tempTask.setNotOverDate();
					taskList.set(i, tempTask);
				}
			} else {
				break;
			}
		}

		modifier.saveFile(taskList);
	}

	private static boolean isOverDate(Task tempTask, Date systemTime) {
		return systemTime.compareTo(tempTask.getEndDateInDateType()) > 0;
	}

	private static boolean isEndDateNull(Task tempTask) {
		return tempTask.getEndDate() == null;
	}

	/**
	 * Checks the block tasks in the list, to see if there is a time line overlap
	 * between the newTask and blockingTask
	 * 
	 * @param taskForCheck
	 *            task in a block input
	 * @return true if above holds, else returns false.
	 */
	public static boolean blockedDateCheck(Task taskForCheck) {
		boolean result = true;
		if (!isEndDateNull(taskForCheck) && !isStartDateNull(taskForCheck)) {
			ArrayList<Task> taskList = modifier.getContentList();
			Task tempTask = new Task();
			for (int i = 0; i < taskList.size(); i++) {
				tempTask = taskList.get(i);
				if (tempTask.isBlocking()) {
					if (isBlockDatesValid(taskForCheck, tempTask)) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}

	private static boolean isBlockDatesValid(Task taskForCheck, Task tempTask) {
		if (taskForCheck.getStartDateInDateType().compareTo(tempTask.getStartDateInDateType()) == -1
				&& taskForCheck.getEndDateInDateType().compareTo(tempTask.getEndDateInDateType()) == 1) {
			return true;
		} else if (taskForCheck.getEndDateInDateType().compareTo(tempTask.getStartDateInDateType()) == 1
				&& taskForCheck.getEndDateInDateType().compareTo(tempTask.getEndDateInDateType()) == -1) {
			return true;
		} else if (taskForCheck.getStartDateInDateType().compareTo(tempTask.getStartDateInDateType()) == 1 
				&& taskForCheck.getStartDateInDateType().compareTo(tempTask.getEndDateInDateType()) == -1) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isStartDateNull(Task taskForCheck) {
		return taskForCheck.getStartDate() == null;
	}
}
```
###### src\Test\UnitTest.java
``` java
	@Test
	// this tests invalid date input
	public void testAdd12() {
		clearExpectedActual();
		AddLogic.addEventWithTimeline("add attend JP Morgon conference from" + " 15112015 to 12112015");
		actual = modifier.getContentList();
		compareResults("testing adding invalid date for timeline task", expected, actual);
	}

	@Test
	public void testAdd13() {
		clearExpectedActual();
		Task task3 = new Task("attend JP Morgon conference", "12122015", "15122015", 1);
		expected.add(task3);
		AddLogic.addEventWithTimeline("add attend JP Morgon conference from " + "12122015 to 15122015 rank 1");

		actual = modifier.getContentList();

		compareResults("testing adding valid date for timeline task", expected, actual);
	}

	@Test
	public void testSearch1() {
		clearExpectedActual();
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add OP2 presentation by 06112015");
		SearchLogic.searchKeyWord("search presentation");
		actual = SearchLogic.getTaskList();
		compareResults("test search by title", expected, actual);
	}

	@Test
	public void testSearch2() {
		clearExpectedActual();
		SearchLogic.searchKeyWord("search presentation");
		actual = SearchLogic.getTaskList();
		compareResults("test seearching for a title tht does not exist", expected, actual);
	}

	@Test
	public void testSearch3() {
		clearExpectedActual();
		Task task = new Task("OP2 presentation", "06112015", 3);
		expected.add(task);
		AddLogic.addEventWithImportance("add OP2 presentation by 06112015 rank 3");
		SearchLogic.searchKeyWord("search impt 3");
		actual = SearchLogic.getTaskList();
		compareResults("test search by impt", expected, actual);
	}

	@Test
	public void testSearch4() {
		clearExpectedActual();
		
		AddLogic.addEventWithImportance("add test event rank 2");
		
		SearchLogic.searchKeyWord("search impt 4");
		actual = SearchLogic.getTaskList();
		compareResults("test search by impt, ranking 4 which is not present", expected, actual);
	}

	@Test
	public void testSearch5() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add EE2020 Oscilloscope project by 03112015" + " rank 1");
		SearchLogic.searchKeyWord("search date 03112015");
		actual = SearchLogic.getTaskList();
		Task task = new Task("EE2020 Oscilloscope project", "03112015", 1);
		expected.add(task);
		compareResults("test search by date", expected, actual);
	}

	@Test
	public void testSearch6() {
		clearExpectedActual();
		SearchLogic.searchKeyWord("search data 05112015");
		actual = SearchLogic.getTaskList();
		compareResults("test search by impt", expected, actual);
	}

	@Test
	public void testEdit1() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add OP2 presentation by 08112015 rank 3");
		EditLogic.editEvent("edit 1 by date 08112015");
		actual = modifier.getContentList();
		expected = getExpectedforEditDate(expected);
		compareResults("test if edit by date works", expected, actual);
	}

```
