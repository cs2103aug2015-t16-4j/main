package listItLogic;

import fileModifier.EditModifier;
import listItUI.TextScreenPenal;

public class EditLogic {

	public static void editEvent(String command) {
		EditModifier editor = new EditModifier("test1.txt"); 
		int lineToBeEdit = Integer.parseInt(command.substring(5, 6));
		
		if(command.contains("by date")) {
			String newDate = command.substring(command.indexOf("by date") + 8);
			
			if(AddLogic.checkValidDate(newDate)) {
				editor.editDate(lineToBeEdit, newDate);
				TextScreenPenal.displaySuccessfulEdit();
			}
			
			else {
				TextScreenPenal.displayInvalidDate();
			}
		}
		
		else if(command.contains("by title")) {
			String newTitle = command.substring(command.indexOf("by title") + 9);
			
			editor.editTitle(lineToBeEdit, newTitle);
			TextScreenPenal.displaySuccessfulEdit();
		}
		
		else if(command.contains("by all")) {
			String newTitle = command.substring(command.indexOf("by all") + 7, command.lastIndexOf("by") - 1);
			String newDate = command.substring(command.lastIndexOf("by") + 3);
			
			if(AddLogic.checkValidDate(newDate)) {
				editor.editAll(lineToBeEdit, newTitle, newDate);
				TextScreenPenal.displaySuccessfulEdit();
			}
			
			else {
				TextScreenPenal.displayInvalidDate();
			}
		}
	}
}
