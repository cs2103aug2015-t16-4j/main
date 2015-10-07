package listItLogic;

import fileModifier.FileModifier;

public class DeleteLogic {

	public static void deleteEvent(String command) {
		int LineToBeDelete = Integer.parseInt(command.substring(7));
		
		FileModifier textFile = new FileModifier("test1.txt");
		
		textFile.deleteLine(LineToBeDelete);
	}

	public static void clearFile() {
		FileModifier textFile = new FileModifier("test1.txt");
		
		textFile.clearAll();
		
	}

}
