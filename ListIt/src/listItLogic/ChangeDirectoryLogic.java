// @author Shawn A0124181R
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

/**
 *This class contains methods which changes the directory of the entire ListIt,
 *by allowing the directory (which includes the data file and the completed data file.  
 * @version 0.5
 */
public class ChangeDirectoryLogic {
	private static final String COMMAND_CHANGE_DIRECTORY = "cd";
	static FileModifier modifier = FileModifier.getInstance();
	private static String message = "null";
	private static final String CHANGE_DIRECTORY_VALID = "Changing the directory of"
			                                              + " the file is successful.";
	private static final String CHANGE_DIRECTORY_INVALID = "Changing the directory"
			                                                + " of the file was not"
			                                                + " sucessful please try"
			                                                + " again.";

	/**
	 * This method checks if the path name entered is valid, then gets the data files and
	 * moves the files to the user input location.
	 * @param command string command input by the user with a "cd" at the start
	 */
	public static void changeDirectory(String command) {
		String newPath = getNewPath(command);
		assert command !=null;
		BufferedWriter textFileWriter;
		boolean isSucessful = true;

		try {
			textFileWriter = new BufferedWriter(new FileWriter(modifier.getPathFile(),
					                                           false));
			textFileWriter.write(newPath);
			textFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assert modifier.getDataFile() != null;
		assert modifier.getCompleteDataFile() != null;
		
		Path destinationOfDataFile = Paths.get(newPath + "\\" 
		                                       + modifier.getDataFile().getName());
		Path destinationOfCompleteFile = Paths.get(newPath + "\\"
		                                           + modifier.getCompleteDataFile().getName());
		
		isSucessful = modifier.moveFiles(destinationOfDataFile,
				                         destinationOfCompleteFile);

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

	private static String getNewPath(String command) {
		return command.substring(command.indexOf(COMMAND_CHANGE_DIRECTORY) + 3);
	}
}