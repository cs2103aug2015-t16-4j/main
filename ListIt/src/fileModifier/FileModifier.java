package fileModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	public void display(ArrayList<Task> list) {
		OutputScreenPane.displayList(list);
	}

	public File getFile() {
		return dataFile;
	}

	public void setfile(File file){
		dataFile = file;
	}

	public ArrayList<Task> searchKeyword(String keyword) throws IOException, ClassNotFoundException{
		ArrayList<Task> searchList = new ArrayList<Task>();
		FileInputStream fis = new FileInputStream(dataFile);
		ObjectInputStream ois = new ObjectInputStream(fis);

		ArrayList<Task> contents = (ArrayList<Task>) ois.readObject();
		for ( int i =0;i<contents.size();i++ ){
			Task task = contents.get(i);
			if(task.getTitle().contains(keyword)){
				searchList.add(task);
			}	
		}	
		ois.close(); 	
		return searchList; 
	}

	public ArrayList<Task> searchByImportance(int searchKey ) throws IOException, ClassNotFoundException{
		ArrayList<Task> searchList = new ArrayList<Task>();
		FileInputStream fis = new FileInputStream(dataFile);
		ObjectInputStream ois = new ObjectInputStream(fis);
		ArrayList<Task> contents = (ArrayList<Task>) ois.readObject();

		for ( int i =0;i<contents.size();i++ ){
			Task task = contents.get(i);
			if(task.getImportance()==searchKey){
				searchList.add(task);
			}	
		}
		ois.close();
		return searchList;
	}
}
