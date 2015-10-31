package listItLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class ChangeDirectoryLogic {
	static FileModifier modifier = FileModifier.getInstance();
	
	public static void changeDirectory(String command) {
		String newPath = command.substring(command.indexOf("cd") + 3);
		BufferedWriter textFileWriter;
		
		try {
			textFileWriter = new BufferedWriter(new FileWriter(modifier.getPathFile(), false));
			textFileWriter.write(newPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(modifier.getDataFile().renameTo(new File(newPath + modifier.getDataFile().getName())) 
				&& modifier.getCompleteDataFile().renameTo(new File(newPath + modifier.getCompleteDataFile().getName()))) {
			FeedbackPane.displayValidFileMove();
		} else {
			FeedbackPane.displayInvalidFileMove();
		}
	}
}