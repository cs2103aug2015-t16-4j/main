package listItLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileModifier.FileModifier;
import taskGenerator.Task;

public class AddLogic {

	private static FileModifier modifier = FileModifier.getInstance();

	public static void addEventWithDeadline(String command) {
		String eventTitle = null;
		String deadline = null;

		try {
			eventTitle = getEventTitleDeadline(command);
			deadline = getEventDeadline(command);
		} catch (Exception e) {
			addEventDefault(command);
		}

		if (isValidDate(deadline)) {
			Task newTask = new Task(eventTitle, deadline);
			modifier.addTask(newTask);
		} else {
			addEventDefault(command);
		}
	}
	
	public static void addRecursiveEvent(String command) {
		String eventTitle = command.substring(0, command.lastIndexOf("repeat") - 2);
		String repeatCycle = command.substring(command.lastIndexOf("repeat") + 7);
		
		Task newTask = new Task(eventTitle, repeatCycle, "repeat");
		modifier.addTask(newTask);
	}

	private static String getEventDeadline(String command) {
		return command.substring(command.lastIndexOf("by") + 3);
	}

	private static String getEventTitleDeadline(String command) {
		return command.substring(4, command.lastIndexOf("by") - 1);
	}

	static boolean isValidDate(String deadline) {
		boolean isValid = false;

		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		dateFormat.setLenient(false);

		try {
			Date date = dateFormat.parse(deadline);
			isValid = true;
		} catch (ParseException e) {
			isValid = false;
		}

		return isValid;
	}

	public static void addEventDefault(String command) {
		String eventTitle = getEventTitleDefault(command);
		Task newTask = new Task(eventTitle);
		modifier.addTask(newTask);
	}

	private static String getEventTitleDefault(String command) {
		return command.substring(4);
	}

	public static void addEventWithImportance(String command) {
		String eventTitle = new String();
		if (isEventWithDeadline(command)) {
			String deadline = new String();
			try {
				eventTitle = getEventTitleDeadline(command);
				deadline = getEventDeadlineImportance(command);
			} catch (Exception e) {
				addEventWithDeadline(command);
				return;
			}

			try {
				int rank = convertStringToInt(command);
				if (isValidDate(deadline)) {
					Task newTask = new Task(eventTitle, deadline, rank);
					modifier.addTask(newTask);
				} else {
					eventTitle = getEventTitleImportance(command);
					Task newTask = new Task(eventTitle, rank);
					modifier.addTask(newTask);
				}
			} catch (Exception e) {
				addEventWithDeadline(command);
				return;
			}
		} else {
			try {
				eventTitle = getEventTitleImportance(command);
				int rank = convertStringToInt(command);
				Task newTask = new Task(eventTitle, rank);
				modifier.addTask(newTask);
			} catch (Exception e) {
				addEventDefault(command);
				return;
			}
		}
	}

	private static int convertStringToInt(String command) {
		return Integer.parseInt(command.substring(command.lastIndexOf("rank") + 5));
	}

	private static String getEventTitleImportance(String command) {
		return command.substring(4, command.lastIndexOf("rank") - 1);
	}

	private static String getEventDeadlineImportance(String command) {
		return command.substring(command.lastIndexOf("by") + 3, command.lastIndexOf("rank") - 1);
	}

	private static boolean isEventWithDeadline(String command) {
		return command.contains("by");
	}

	public static void addEventWithTimeline(String command) {
		String eventTitle = new String();
		String deadline = new String();
		String start = new String();

		try {
			eventTitle = getEventTitleDeadline(command);
			deadline = getEventDeadlineTimeline(command);
			start = getStartTime(command);
		} catch (Exception e) {
			addEventWithImportance(command);
			return;
		}

		if (isValidDate(deadline) && isValidTime(start)) {
			if (isEventWithImportance(command)) {
				try {
					int rank = convertStringToInt(command);
					String end = getEndTimeImportance(command);
					Task newTask = new Task(eventTitle, deadline, start, end, rank);
					modifier.addTask(newTask);
				} catch (Exception e) {
					String end = getEndTime(command);
					Task newTask = new Task(eventTitle, deadline, start, end);
					modifier.addTask(newTask);
				}

			} else {
				String end = getEndTime(command);
				Task newTask = new Task(eventTitle, deadline, start, end);
				modifier.addTask(newTask);
			}
		} else {
			addEventWithImportance(command);
			return;
		}
	}

	private static String getEndTime(String command) {
		return command.substring(command.lastIndexOf("to") + 3);
	}

	private static String getEndTimeImportance(String command) {
		return command.substring(command.lastIndexOf("to") + 3, command.lastIndexOf("rank") - 1);
	}

	private static boolean isEventWithImportance(String command) {
		return command.contains("rank");
	}

	private static String getStartTime(String command) {
		return command.substring(command.lastIndexOf("from") + 5, command.lastIndexOf("to") - 1);
	}

	private static String getEventDeadlineTimeline(String command) {
		return command.substring(command.lastIndexOf("by") + 3, command.lastIndexOf("from") - 1);
	}

	private static boolean isValidTime(String start) {
		boolean isValid = false;
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm");

		try {
			formatter.parse(start);
			isValid = true;
		} catch (ParseException e) {
			isValid = false;
		}

		return isValid;
	}

}
