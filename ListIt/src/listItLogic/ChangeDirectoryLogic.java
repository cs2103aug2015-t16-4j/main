package listItLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class ChangeDirectoryLogic {
	static FileModifier modifier = FileModifier.getInstance();
	private static String message = "null"; 
	private static final String CHANGE_DIRECTORY_VALID = "Changing the directory of the file is successful."; 
	private static final String CHANGE_DIRECTORY_INVALID = "Changing the directory of the file was not sucessful please try again."; 

	

	
	public static void changeDirectory(String command) {
		String newPath = command.substring(command.indexOf("cd") + 3);
		BufferedWriter textFileWriter;
		
		try {
			textFileWriter = new BufferedWriter(new FileWriter(modifier.getPathFile(), false));
			textFileWriter.write(newPath);
			textFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(modifier.getDataFile().renameTo(new File(newPath + modifier.getDataFile().getName())) 
				&& modifier.getCompleteDataFile().renameTo(new File(newPath + modifier.getCompleteDataFile().getName()))) {
			FeedbackPane.displayValidFileMove();
			message = CHANGE_DIRECTORY_VALID;
			LoggingLogic.logging(message);
		} else {
			FeedbackPane.displayInvalidFileMove();
			message = CHANGE_DIRECTORY_VALID; 
			LoggingLogic.logging(message);
		}
	}
}