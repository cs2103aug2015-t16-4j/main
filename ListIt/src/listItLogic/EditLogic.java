package listItLogic;

import java.io.File;

import fileModifier.EditModifier;
import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import listItUI.TextScreenPenal;

public class EditLogic {
	
	private static UndoAndRedoLogic undoRedo;
	private static FileModifier modifier;
	
	public EditLogic( ) {
		UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
		FileModifier modifier = FileModifier.getInstance();
	}

	public static void editEvent(String command) {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		int lineToBeEdit = Integer.parseInt(command.substring(5, 6));
		
		if(command.contains("by date")) {
			String newDate = command.substring(command.indexOf("by date") + 8);
			
			if(AddLogic.checkValidDate(newDate)) {
				FileModifier.editDate(lineToBeEdit, newDate);
				FeedbackPane.displaySuccessfulEdit();
			}
			
			else {
				FeedbackPane.displayInvalidDate();
			}
		}
		
		else if(command.contains("by title")) {
			String newTitle = command.substring(command.indexOf("by title") + 9);
			
			FileModifier.editTitle(lineToBeEdit, newTitle);
			FeedbackPane.displaySuccessfulEdit();
		}
		
		else if(command.contains("by importance")) {
			String newImportance = command.substring(command.indexOf("rank") + 5);
			
			FileModifier.editTitle(lineToBeEdit, newImportance);
			FeedbackPane.displaySuccessfulEdit();
		}
		
		else if(command.contains("by all")) {
			String newTitle = command.substring(command.indexOf("by all") + 7, command.lastIndexOf("rank") - 12);
			String newDate = command.substring(command.lastIndexOf("by") + 3);
			String newImportance = command.substring(command.lastIndexOf("rank") + 5);
			
			if(AddLogic.checkValidDate(newDate)) {
				FileModifier.editAll(lineToBeEdit, newTitle, newDate, newImportance);
				FeedbackPane.displaySuccessfulEdit();
			}
			
			else {
				FeedbackPane.displayInvalidDate();
			}
		}
	}
}
