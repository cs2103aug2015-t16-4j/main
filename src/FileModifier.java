import java.io.*;

public class FileModifier {
	public FileModifier() {
		
	}
	
	public static void createFile() {
		File textFile = new File("test.txt");
		
		try {
			textFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void addEvent(String command) {
		try {
			BufferedWriter textWriter = new BufferedWriter(new FileWriter("test.txt", true));
			
			textWriter.write(command.substring(4));
			textWriter.newLine();
			
			textWriter.close();
			
			TextScreenPenal.sucessfulAdd(command.substring(4));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void displayEvent() {
		String content = null;
		Integer lineCount = 1;
		boolean isDisplayed = false;
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader("test.txt"));
			
			while((content = textReader.readLine()) != null) {
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

	public static void clearFile() {
		BufferedWriter textWriter;
		try {
			textWriter = new BufferedWriter(new FileWriter("test.txt", false));
			textWriter.write("");
			textWriter.close();
			TextScreenPenal.sucessfulClear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteEvent(String command) {
		File tempFile = new File("temp.txt");
		File inputFile = new File("test.txt");
		Integer lineToBeDelete = Integer.parseInt(command.substring(7));
		Integer lineCount = 1;
		String aLineOfContent;
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(tempFile, true));
			
			while((aLineOfContent = textReader.readLine()) != null) {
				if(lineCount != lineToBeDelete) {
					textWriter.write(aLineOfContent);
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

	public static void editEvent(String command) {
		File tempFile = new File("temp.txt");
		File inputFile = new File("test.txt");
		Integer lineToBeEdit = Integer.parseInt(command.substring(5, 6));
		Integer lineCount = 1;
		String aLineOfContent;
		String replacement = command.substring(7);
		
		try {
			BufferedReader textReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter textWriter = new BufferedWriter(new FileWriter(tempFile, true));
			
			while((aLineOfContent = textReader.readLine()) != null) {
				if(lineCount != lineToBeEdit) {
					textWriter.write(aLineOfContent);
				}
				else {
					textWriter.write(replacement);
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
