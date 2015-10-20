package fileModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import listItUI.OutputScreenPane;
import taskGenerator.Task;

public class FileModifier {
	private static FileModifier modifier;
	private File dataFile;
	private File storeDataFile; 

	private FileModifier() {
		dataFile = new File("test1.ser");
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
		
		modifier.saveFile(newList);
		
		OutputScreenPane.displayList(newList);
	}
	
	public void removeTask(int index) {
		ArrayList<Task> taskList = modifier.getContentList();
		
		taskList.remove(index);
		
		modifier.saveFile(taskList);
		
		OutputScreenPane.displayList(taskList);
	}
	
	public void display(ArrayList<Task> taskList) {
		OutputScreenPane.displayList(taskList);
	}
	
	public void clearAll() {
		ArrayList<Task> taskList = modifier.getContentList();
		
		taskList.clear();
		
		modifier.saveFile(taskList);
		
		OutputScreenPane.displayList(taskList);
	}

	public File getFile() {
		return dataFile;
	}

	public void setfile(File file){
		dataFile = file;
	}
	
	public void createTempFile() throws IOException{
		
		File temp = File.createTempFile("tempfile", ".tmp");
		   
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

	public ArrayList<Task> searchByImportance(int searchKey ) throws IOException, ClassNotFoundException{
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
			if(task.getDate().contains(date)){
				searchList.add(task);
			}	
		}
		return searchList;
	}
}
