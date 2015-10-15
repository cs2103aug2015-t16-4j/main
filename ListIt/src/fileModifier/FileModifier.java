package fileModifier;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import listItUI.TextScreenPenal;
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
		File tempFile = new File("temp.txt");
		File inputFile = new File(fileName);
		Integer lineCount = 1;
		String aLineOfContent;
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(tempFile, true));
			
			while((aLineOfContent = textReader.readLine()) != null) {
				if(lineCount != lineToBeDelete) {
					textWriter.write(aLineOfContent);
					textWriter.newLine();
				}
				else {
					if(aLineOfContent.contains("-D")) {
						String title = aLineOfContent.substring(aLineOfContent.indexOf("-T") + 3, aLineOfContent.indexOf("-D") - 1);
						String date = aLineOfContent.substring(aLineOfContent.indexOf("-D") + 3);
						TextScreenPenal.deleteSucessful(title + " by " + date);
					}
					else {
						String title = aLineOfContent.substring(aLineOfContent.indexOf("-T") + 3);
						TextScreenPenal.deleteSucessful(title);
					}
				}
				lineCount++;
			}
			
			textReader.close();
			textWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputFile.delete();
		tempFile.renameTo(inputFile);
		
	}

	public void displayDefault() {
		String content = null;
		Integer lineCount = 1;
		boolean isDisplayed = false;
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader(fileName));
			
			while((content = textReader.readLine()) != null) {
				
				content = changeFormat(content);
				
				TextScreenPenal.display(content, lineCount);
				lineCount++;
				isDisplayed = true;	
			}
			
			textReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(isDisplayed == false) {
			TextScreenPenal.displayEmpty();
		}
		
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
		BufferedWriter textWriter;
		try {
			textWriter = new BufferedWriter(new FileWriter(fileName, false));
			textWriter.write("");
			textWriter.close();
			TextScreenPenal.sucessfulClear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
<<<<<<< HEAD

	public File getFile() {
		File textFile = new File(fileName);
		return textFile;
=======
	
	public void searchKeyword(String keyword){
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
		
>>>>>>> origin/master
	}
}
