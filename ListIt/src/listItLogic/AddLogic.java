package listItLogic;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class AddLogic {
	
	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	private static FileModifier modifier = FileModifier.getInstance();

	public static void addEventWithDeadline(String command) {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		String eventTitle = null;
		boolean titleValid = false;
		
		try {
			eventTitle = command.substring(4, command.lastIndexOf("by")-1);
			titleValid = true;
		} catch (StringIndexOutOfBoundsException e) {
			FeedbackPane.displayNoTitle();
		}
		
		if(titleValid) {
			String deadline = command.substring(command.lastIndexOf("by") + 3);
			
			if(checkValidDate(deadline)) {
			
				Task newTask = new Task(eventTitle, deadline);
				
				modifier.addTask(newTask);
			}
		}
		else {
			FeedbackPane.displayInvalidDate();
		}
	}

	static boolean checkValidDate(String deadline) {
		boolean isValid = false;
		
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyy");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("ddMMyyyy");
		
		dateFormat1.setLenient(false);
		dateFormat2.setLenient(false);
		
		try {
			Date date = dateFormat1.parse(deadline);
			isValid = true;
		} catch (ParseException e) {
			isValid = false;
		}
		
		if(!isValid) {
			try {
				Date date = dateFormat2.parse(deadline);
				isValid = true;
			} catch (ParseException e) {
				isValid = false;
			}
		}
		
		return isValid;
	}	

	public static void addEventDefault(String command) {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		String eventTitle = command.substring(4);
		
		Task newTask = new Task(eventTitle);
		
		modifier.addTask(newTask);
	}
	
	//public static void addEventWithImportance (String command) {
		//String eventTitle = ; 
		//Task event = new Task(eventTitle);
		
	//	FileModifier fileInput = new FileModifier("test1.txt");
		
	//	fileInput.addToFile(event.toStringImportance);
	//}
	
}
