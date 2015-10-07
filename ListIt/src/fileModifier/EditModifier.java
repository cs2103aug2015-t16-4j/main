package fileModifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import taskGenerator.Task;

public class EditModifier {
	private String fileName;

	public EditModifier(String fileName) {
		this.fileName = fileName;
	}

	public void editDate(int lineToBeEdit, String newDate) {
		File tempFile = new File("temp.txt");
		File inputFile = new File(fileName);
		Integer lineCount = 1;
		String aLineOfContent;
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(tempFile, true));
			
			while((aLineOfContent = textReader.readLine()) != null) {
				if(lineCount != lineToBeEdit) {
					textWriter.write(aLineOfContent);
					textWriter.newLine();
				}
				else {
					String title = aLineOfContent.substring(aLineOfContent.indexOf("-T") + 3, aLineOfContent.indexOf("-D") - 1);
					
					Task editedTask = new Task(title, newDate);
					
					textWriter.write(editedTask.toStringWithDeadline());
					textWriter.newLine();
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
	
	public void editTitle(int lineToBeEdit, String newTitle) {
		File tempFile = new File("temp.txt");
		File inputFile = new File(fileName);
		Integer lineCount = 1;
		String aLineOfContent;
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(tempFile, true));
			
			while((aLineOfContent = textReader.readLine()) != null) {
				if(lineCount != lineToBeEdit) {
					textWriter.write(aLineOfContent);
					textWriter.newLine();
				}
				else {
					if(aLineOfContent.contains("-D")) {
						
						String date = aLineOfContent.substring(aLineOfContent.indexOf("-D") + 3);
						Task editedTask = new Task(newTitle, date, true);
						
						textWriter.write(editedTask.toStringWithDeadline());
						textWriter.newLine();
					}
					
					else {
						Task editedTask = new Task(newTitle);
						
						textWriter.write(editedTask.toStringWithoutDate());
						textWriter.newLine();
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
	
	public void editAll(int lineToBeEdit, String newTitle, String newDate) {
		File tempFile = new File("temp.txt");
		File inputFile = new File(fileName);
		Integer lineCount = 1;
		String aLineOfContent;
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(tempFile, true));
			
			while((aLineOfContent = textReader.readLine()) != null) {
				if(lineCount != lineToBeEdit) {
					textWriter.write(aLineOfContent);
					textWriter.newLine();
				}
				else {
					Task editedTask = new Task(newTitle, newDate);
					textWriter.write(editedTask.toStringWithDeadline());
					textWriter.newLine();
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
		
}
