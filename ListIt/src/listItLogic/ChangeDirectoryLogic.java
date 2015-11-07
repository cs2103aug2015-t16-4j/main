package listItLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
		boolean isSucessful = true;

		try {
			textFileWriter = new BufferedWriter(new FileWriter(modifier.getPathFile(), false));
			textFileWriter.write(newPath);
			textFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Path destinationOfDataFile = Paths.get(newPath + "\\" + modifier.getDataFile().getName());
		Path destinationOfCompleteFile = Paths.get(newPath + "\\" + modifier.getCompleteDataFile().getName());
		
		isSucessful = modifier.moveFiles(destinationOfDataFile, destinationOfCompleteFile);

		if (isSucessful) {
			FeedbackPane.displayValidFileMove();
			message = CHANGE_DIRECTORY_VALID;
			LoggingLogic.logging(message);
		} else {
			FeedbackPane.displayInvalidFileMove();
			message = CHANGE_DIRECTORY_INVALID;
			LoggingLogic.logging(message);
		}
	}
}