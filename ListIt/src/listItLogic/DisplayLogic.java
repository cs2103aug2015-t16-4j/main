package listItLogic;

import fileModifier.FileModifier;

public class DisplayLogic {

	public static void displayEvent() {
		FileModifier textFile = FileModifier.getInstance();
		
		textFile.displayDefault();
	}
}
