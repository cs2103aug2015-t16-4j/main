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
		EditModifier editor = new EditModifier("test1.txt"); 
		int lineToBeEdit = Integer.parseInt(command.substring(5, 6));
		
		if(command.contains("by date")) {
			String newDate = command.substring(command.indexOf("by date") + 8);
			
			if(AddLogic.checkValidDate(newDate)) {
				editor.editDate(lineToBeEdit, newDate);
				FeedbackPane.displaySuccessfulEdit();
			}
			
			else {
				FeedbackPane.displayInvalidDate();
			}
		}
		
		else if(command.contains("by title")) {
			String newTitle = command.substring(command.indexOf("by title") + 9);
			
			editor.editTitle(lineToBeEdit, newTitle);
			FeedbackPane.displaySuccessfulEdit();
		}
		
		else if(command.contains("by all")) {
			String newTitle = command.substring(command.indexOf("by all") + 7, command.lastIndexOf("by") - 1);
			String newDate = command.substring(command.lastIndexOf("by") + 3);
			
			if(AddLogic.checkValidDate(newDate)) {
				editor.editAll(lineToBeEdit, newTitle, newDate);
				FeedbackPane.displaySuccessfulEdit();
			}
			
			else {
				FeedbackPane.displayInvalidDate();
			}
		}
	}
}
