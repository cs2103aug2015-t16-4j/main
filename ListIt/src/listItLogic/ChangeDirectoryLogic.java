package listItLogic;

import java.io.File;
import java.util.ArrayList;

import fileModifier.FileModifier;
import taskGenerator.Task;

public class ChangeDirectoryLogic {
	private static File dataFile;
	private static ArrayList<Task> list;
	private static final String SUCCESS_MESSAGE = "Directory successfully made";
	private static final String ERROR_MESSAGE = "Directory already exists";
	
	public static void changeDirectory(String command) {
		
		String directoryLocation = command.substring(3);
		FileModifier modifier = FileModifier.getInstance();
		dataFile = new File("directoryLocation");
		list = new ArrayList<Task>();
		
		if(!dataFile.exists()) {
			dataFile.mkdir();
			list = modifier.getContentList();
			modifier.saveFile(list);
			System.out.println(SUCCESS_MESSAGE);
		} else {
			System.err.println(ERROR_MESSAGE);
		}
		
	}
}
