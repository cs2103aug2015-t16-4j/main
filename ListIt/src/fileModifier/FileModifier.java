package fileModifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import taskGenerator.Task;

public class FileModifier {
	private static FileModifier modifier;
	private File dataFile;
	
	private FileModifier() {
		dataFile = new File("test1.txt");
	}
	
	public static FileModifier getInstance() {
		if(modifier == null) {
			modifier = new FileModifier();
		}
		return modifier;
	}
	
	public void saveFile(ArrayList<Task> dataStorage){
		try{
			FileOutputStream fos = new FileOutputStream("text1.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dataStorage);
			oos.close();
		} catch(Exception ex){
			ex.printStackTrace(); 
		}
	}

	public void addToFile(Task newTask){
		
	
	}

	public void deleteLine(int lineToBeDelete) {
		
	}

	public void displayDefault() {

	}

	private String changeFormat(String content) {
		if(content.contains("-D")) {
			String title = content.substring(content.indexOf("-T") + 3, content.indexOf("-D") - 1);
			String date = content.substring(content.indexOf("-D") + 2);
			
			return title + " by " + date;
		}
		else {
			String title = content.substring(content.indexOf("-T") + 3);
			
			return title;
		}
	}

	public void clearAll() {

	}

	public File getFile() {
		return dataFile;
	}
	
	public void setfile(File file){
		 dataFile = file;
	}
	
	public static void searchKeyword(String keyword) throws IOException, ClassNotFoundException{
	ArrayList<Task> searchList = new ArrayList<Task>();
	FileInputStream fis = new FileInputStream("test1.txt");
	ObjectInputStream ois = new ObjectInputStream(fis);
	
	ArrayList<Task> contents = (ArrayList<Task>) ois.readObject();
		for ( int i =0;i<contents.size();i++ ){
			Task task = contents.get(i);
			if(task.getTitle().contains(keyword)){
				searchList.add(task);
			}	
		}	
	ois.close(); 	
	}
}
