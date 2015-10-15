package listItLogic;


public class CommandParser {
	
	public CommandParser() {
		
	}
	
	public static void processCommand(String command) {
		if(command.contains(" ")) {
			ExecuteCommand.processCommandWithSpace(command);
		}
		
		else {
			ExecuteCommand.processCommandWithoutSpace(command);
		}
	}
}
	
	