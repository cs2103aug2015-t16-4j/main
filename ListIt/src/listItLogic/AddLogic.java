package listItLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileModifier.FileModifier;
import listItUI.TextScreenPenal;
import taskGenerator.Task;

public class AddLogic {

	public static void addEventWithDeadline(String command) {
		String eventTitle = null;
		boolean titleValid = false;
		
		try {
			eventTitle = command.substring(4, command.lastIndexOf("by")-1);
			titleValid = true;
		} catch (StringIndexOutOfBoundsException e) {
			TextScreenPenal.displayNoTitle();
		}
		
		if(titleValid) {
			String deadline = command.substring(command.lastIndexOf("by") + 3);
			
			if(checkValidDate(deadline)) {
			
				Task event = new Task(eventTitle, deadline);
			
				FileModifier fileInput = new FileModifier("test1.txt");
			
				fileInput.addToFile(event.toStringWithDeadline());
			}
		}
		else {
			TextScreenPenal.displayInvalidDate();
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
		String eventTitle = command.substring(4);
		
		Task event = new Task(eventTitle);
		
		FileModifier fileInput = new FileModifier("test1.txt");
		
		fileInput.addToFile(event.toStringWithoutDate());
	}
	
}
