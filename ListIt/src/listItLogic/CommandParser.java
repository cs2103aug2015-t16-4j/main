package listItLogic;


public class CommandParser {
	
	public CommandParser() {
		
	}
	
	public static void processCommand(String command) throws InvalidCommandException {
		if(command.contains(" ")) {
			ExecuteCommand.processCommandWithSpace(command);
		}
		
		else {
			ExecuteCommand.processCommandWithoutSpace(command);
		}
	}
}
	
	