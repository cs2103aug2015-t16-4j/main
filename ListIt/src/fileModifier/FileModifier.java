// @@author Urvashi A0127781Y
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
	public void addTask(Task newtask) {
		if (TaskCheckLogic.blockedDateCheck(newtask)) {
			ArrayList<Task> newList = modifier.getContentList();
			newList.add(newtask);
			updateFile(newList);
			LoggingLogic.logging(ADDING_SUCCESSFUL);
		} else {
			FeedbackPane.displayInvalidAddBlocked();
			LoggingLogic.logging(ADDING_UNSUCCESSFUL);
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
	public void editTimeline(int lineToBeEdit, String startDate, String endDate) {
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
		taskList.set(lineToBeEdit, task);
		updateFile(taskList);
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