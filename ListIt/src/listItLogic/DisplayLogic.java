package listItLogic;

import fileModifier.FileModifier;

public class DisplayLogic {

	public static void displayEvent() {
		FileModifier textFile = new FileModifier("test1.txt");
		
		textFile.displayDefault();
	}
}
