package fileModifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import listItUI.TextScreenPenal;

public class FileModifier {
	private String fileName;
	
	public FileModifier(String fileName) {
		this.fileName = fileName;
		File textFile = new File(fileName);
		if(!textFile.exists()) {
			try {
				textFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addToFile(String content) {
		BufferedWriter textWriter;
		boolean isAdded = false;
		try {
			textWriter = new BufferedWriter(new FileWriter(fileName, true));
		
			textWriter.write(content);
			textWriter.newLine();
			
			textWriter.close();
			
			isAdded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(isAdded) {
			if(content.contains("-D")) {
				String title = content.substring(content.indexOf("-T") + 3, content.indexOf("-D") - 1);
				String date = content.substring(content.indexOf("-D") + 2);
				
				TextScreenPenal.sucessfulAdd(title + " by " + date);
			}
			else {
				String title = content.substring(content.indexOf("-T") + 3);
				
				TextScreenPenal.sucessfulAdd(title);
			}
		}
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

	public File getFile() {
		File textFile = new File(fileName);
		return textFile;
	}
}
