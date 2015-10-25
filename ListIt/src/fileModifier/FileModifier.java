package fileModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import listItUI.OutputScreenPane;
import taskGenerator.Task;
import taskGenerator.TaskComparatorDefault;
import taskGenerator.TaskComparatorImpt;

public class FileModifier {
	private static FileModifier modifier;
	private static String viewMode = "default";
	private File dataFile;
	private String fileName = "test1.ser";

	private FileModifier() {
		dataFile = new File(fileName);
		try {
			dataFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FileModifier getInstance() {
		if(modifier == null) {
			modifier = new FileModifier();
		}
		return modifier;
	}

	public void saveFile(ArrayList<Task> dataStorage){
		try{
			FileOutputStream fos = new FileOutputStream(dataFile, false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dataStorage);
			oos.close();
		} catch(Exception e){
			e.printStackTrace(); 
		}
	}

	public ArrayList<Task> getContentList() {
		ArrayList<Task> list = new ArrayList<Task>();
		
		//Retrieve the list if the list exist, if not, create a empty list then retrieve
		if(fileContainsData()) {
			try{
				FileInputStream fis = new FileInputStream(dataFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				list = (ArrayList<Task>) ois.readObject();
				ois.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		else {
			
			try{
				FileOutputStream fos = new FileOutputStream(dataFile, false);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(new ArrayList<Task>());
				oos.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}

		return list;
	}

	private boolean fileContainsData() {
		boolean isDataExist = false;
		
		try{
			FileInputStream fis = new FileInputStream(dataFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<Task> list = (ArrayList<Task>) ois.readObject();
			ois.close();
			isDataExist = true;
		} catch(Exception e) {
			isDataExist = false;
		}
		return isDataExist;
	}
	
	public void addTask(Task newtask) {
		ArrayList<Task> newList = modifier.getContentList();
		
		newList.add(newtask);
		
		modifier.sort(newList);
		modifier.updateIndex(newList);
		modifier.saveFile(newList);
		
		modifier.display(newList);
	}

	public void removeTask(int index) {
		ArrayList<Task> taskList = modifier.getContentList();
		
		taskList.remove(index);
		
		modifier.sort(taskList);
		modifier.updateIndex(taskList);
		modifier.saveFile(taskList);
		
		modifier.display(taskList);
	}
	
	public void display(ArrayList<Task> taskList) {
		
		if(taskList.isEmpty()) {
			OutputScreenPane.displayEmpty();
		}
		
		else if(viewMode.equals("default")) {
			OutputScreenPane.displayList(taskList);
		}
		else if(viewMode.equals("impt")) {
			OutputScreenPane.displayListImpt(taskList);
		}
		else if(viewMode.equals("alpha")) {
			OutputScreenPane.displayListAlpha(taskList);
		}
	}
	
	public void clearAll() {
		ArrayList<Task> taskList = modifier.getContentList();
		
		taskList.clear();
		
		modifier.saveFile(taskList);
		
		modifier.display(taskList);
	}

	public ArrayList<Task> searchKeyword(String keyword) {
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();
		
		for ( int i =0;i<taskList.size();i++ ){
			Task task = taskList.get(i);
			if(task.getTitle().contains(keyword)){
				searchList.add(task);
			}	
		}
		return searchList; 
	}

	public ArrayList<Task> searchByImportance(int searchKey ) {
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();
		
		for ( int i =0;i<taskList.size();i++ ){
			Task task = taskList.get(i);
			if(task.getImportance()==searchKey){
				searchList.add(task);
			}	
		}
		return searchList;
	}
	
	public ArrayList<Task> searchByDate(String date){
		ArrayList<Task> taskList = modifier.getContentList();
		ArrayList<Task> searchList = new ArrayList<Task>();
		
		for ( int i =0;i<taskList.size();i++ ){
			Task task = taskList.get(i);
			if(task.getDateInputForm().contains(date)){
				searchList.add(task);
			}	
		}
		return searchList;
	}
	
	public void editDate(int lineToBeEdit, String newDate) {
		ArrayList<Task> taskList = modifier.getContentList();
		
		Task task = taskList.get(lineToBeEdit);
		
		task.setDate(newDate);

		taskList.set(lineToBeEdit, task);
		
		modifier.sort(taskList);
		modifier.updateIndex(taskList);
		modifier.saveFile(taskList);
		
		modifier.display(taskList);
	}
	
	public void editTitle(int lineToBeEdit, String newTitle) {
		ArrayList<Task> taskList = modifier.getContentList();
		
		Task task = taskList.get(lineToBeEdit);
		
		task.setEventTitle(newTitle);
		
		taskList.set(lineToBeEdit, task);
		
		modifier.sort(taskList);
		modifier.updateIndex(taskList);
		modifier.saveFile(taskList);
		
		modifier.display(taskList);
	}
	
	public void editImportance(int lineToBeEdit, String newImportance) {
		ArrayList<Task> taskList = modifier.getContentList();
		
		Task task = taskList.get(lineToBeEdit);
		
		task.setImportance(Integer.parseInt(newImportance));
		
		taskList.set(lineToBeEdit, task);
		
		modifier.sort(taskList);
		modifier.updateIndex(taskList);
		modifier.saveFile(taskList);
		
		modifier.display(taskList);
	}

	public void editTime(int indexToBeEdit, String newStartTime, String newEndTime) {
		ArrayList<Task> taskList = modifier.getContentList();
		
		Task task = taskList.get(indexToBeEdit);
		
		task.setStart(newStartTime);
		task.setEnd(newEndTime);
		
		modifier.sort(taskList);
		modifier.updateIndex(taskList);
		modifier.saveFile(taskList);
		
		modifier.display(taskList);
	}
	
	public void sort(ArrayList<Task> taskList) {
		if(viewMode.equals("default")) {
			Collections.sort((taskList), new TaskComparatorDefault());
		}
		else if(viewMode.equals("impt")) {
			Collections.sort((taskList), new TaskComparatorImpt());
		}
		
		else if(viewMode.equals("alpha")) {
			Collections.sort(taskList, new TaskComparatorImpt());
		}
	}
	
	public void setViewMode(String newMode) {
		viewMode = newMode;
	}
	
	public void updateIndex(ArrayList<Task> taskList) {
		if(taskList.isEmpty()) {
			return;
		}
		else {
			for(int i=1; i<= taskList.size(); i++) {
				taskList.get(i-1).setIndex(i);
			}
		}
	}
}
