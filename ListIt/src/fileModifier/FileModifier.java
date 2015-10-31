package fileModifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import listItUI.OutputScreenPane;
import taskGenerator.Task;
import taskGenerator.TaskComparatorAlpha;
import taskGenerator.TaskComparatorDefault;
import taskGenerator.TaskComparatorImpt;

public class FileModifier {
	private static FileModifier modifier;
	private static String viewMode = "default";
	private File dataFile;
	private File completeDataFile;
	private File pathStorage;
	private String taskFileName = "Task.ser";
	private String completeTaskFileName = "CompleteTask.ser";
	private String pathStorageFileName = "path.txt";
	private static final String MODE_DEFAULT = "default";
	private static final String MODE_IMPT = "impt";
	private static final String MODE_ALPHA = "alpha";
	private static final String MODE_COMPLETE = "complete";

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
			dataFile = new File(path + taskFileName);
			completeDataFile = new File(path + completeTaskFileName);
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

	public ArrayList<Task> getContentList() {
		ArrayList<Task> list = new ArrayList<Task>();

		// Retrieve the list if the list exist, if not, create a empty list then
		// retrieve
		if (!isListEmpty()) {
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

	public ArrayList<Task> getCompleteContentList() {
		ArrayList<Task> list = new ArrayList<Task>();

		// Retrieve the list if the list exist, if not, create a empty list then
		// retrieve
		if (!isListEmpty()) {
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
				FileOutputStream fos = new FileOutputStream(completeDataFile, false);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(new ArrayList<Task>());
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	private boolean isListEmpty() {
		boolean isEmpty = true;

		try {
			FileInputStream fis = new FileInputStream(dataFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<Task> list = (ArrayList<Task>) ois.readObject();
			ois.close();
			isEmpty = false;
		} catch (Exception e) {
			isEmpty = true;
		}
		return isEmpty;
	}

	public void addTask(Task newtask) {
		ArrayList<Task> newList = modifier.getContentList();
		newList.add(newtask);
		updateFile(newList);
	}

	private void updateFile(ArrayList<Task> newList) {
		modifier.sort(newList);
		modifier.updateIndex(newList);
		if (isViewModeComplete()) {
			modifier.saveCompleteFile(newList);
		} else {
			modifier.saveFile(newList);
		}
		modifier.display(newList);
	}

	public void removeTask(int index) {
		ArrayList<Task> taskList;
		if (isViewModeComplete()) {
			taskList = modifier.getCompleteContentList();
		} else {
			taskList = modifier.getContentList();
		}
		taskList.remove(index);
		updateFile(taskList);
	}

	public void display(ArrayList<Task> taskList) {
		if (taskList.isEmpty()) {
			OutputScreenPane.displayEmpty();
		} else if (isViewModeDefault()) {
			OutputScreenPane.displayList(taskList);
		} else if (isViewModeImpt()) {
			OutputScreenPane.displayListImpt(taskList);
		} else if (isViewModeAlpha()) {
			OutputScreenPane.displayListAlpha(taskList);
		} else if (isViewModeComplete()) {
			OutputScreenPane.displayListComplete(taskList);
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

	public void clearAll() {
		ArrayList<Task> taskList;
		if (isViewModeComplete()) {
			taskList = modifier.getCompleteContentList();
		} else {
			taskList = modifier.getContentList();
		}
		taskList.clear();

		updateFile(taskList);
	}

	public ArrayList<Task> searchKeyword(String keyword) {
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();

		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (task.getTitle().contains(keyword)) {
				searchList.add(task);
			}
		}
		return searchList;
	}

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

	public ArrayList<Task> searchByDate(String date) {
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();

		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			if (task.getDateInputForm().contains(date)) {
				searchList.add(task);
			}
		}
		return searchList;
	}

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

	public void editTitle(int lineToBeEdit, String newTitle) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task task = taskList.get(lineToBeEdit);
		task.setEventTitle(newTitle);
		taskList.set(lineToBeEdit, task);
		updateFile(taskList);
	}

	public void editImportance(int lineToBeEdit, String newImportance) {
		ArrayList<Task> taskList = modifier.getContentList();
		Task task = taskList.get(lineToBeEdit);
		task.setImportance(Integer.parseInt(newImportance));
		taskList.set(lineToBeEdit, task);
		updateFile(taskList);
	}

	public void sort(ArrayList<Task> taskList) {
		if (isViewModeDefault()) {
			Collections.sort((taskList), new TaskComparatorDefault());
		} else if (isViewModeImpt()) {
			Collections.sort((taskList), new TaskComparatorImpt());
		} else if (isViewModeAlpha()) {
			Collections.sort(taskList, new TaskComparatorAlpha());
		}
	}

	public void setViewMode(String newMode) {
		viewMode = newMode;
	}

	public void updateIndex(ArrayList<Task> taskList) {
		if (taskList.isEmpty()) {
			return;
		} else {
			for (int i = 1; i <= taskList.size(); i++) {
				taskList.get(i - 1).setIndex(i);
			}
		}
	}

	public void completeTask(int index) {
		ArrayList<Task> completedList = modifier.getCompleteContentList();
		ArrayList<Task> taskList = modifier.getContentList();
		Calendar calendar = Calendar.getInstance();

		Task completedTask = taskList.get(index);
		if (completedTask.getRepeat() == false) {
			completedList.add(0, completedTask);
			taskList.remove(index);
			saveCompleteFile(completedList);
			updateFile(taskList);
		} else {
			if (completedTask.getStartDate() == null) {
				Date currentDeadline = completedTask.getEndDateInDateType();
				int repeatCycle = completedTask.getRepeatCycle();
				String repeatType = completedTask.getRepeatType();
				calendar.setTime(currentDeadline);	
				Date nextDeadline = getNextDeadline(calendar, repeatCycle, repeatType);
				completedTask.setEndDateInDate(nextDeadline);
				taskList.set(index, completedTask);
				updateFile(taskList);
			}
		}
	}

	private Date getNextDeadline(Calendar calendar, int repeatCycle, String repeatType) {
		if (repeatType.equals("daily")) {
			calendar.add(Calendar.DATE, repeatCycle);
		} else if (repeatType.equals("weekly")) {
			calendar.add(Calendar.WEEK_OF_YEAR, repeatCycle);
		} else if (repeatType.equals("monthly")) {
			calendar.add(Calendar.MONTH, repeatCycle);
		} else if (repeatType.equals("yearly")) {
			calendar.add(Calendar.YEAR, repeatCycle);
		}
		
		Date nextDeadline = calendar.getTime();
		return nextDeadline;
	}
}