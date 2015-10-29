package listItLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class AddLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static String addMessage = null;

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
			Task newTask;
			if(containsTime(deadline)) {
				newTask = new Task(eventTitle, deadline, true);
			} else {
				newTask = new Task(eventTitle, deadline);
			}
			modifier.addTask(newTask);
		} else {
			addEventDefault(command);
		}
	}

	public static void addRecursiveEvent(String command) {
		String repeatDay = null;
		String exception = null;
		String eventTitle = command.substring(0, command.lastIndexOf("repeat") - 2);
		String repeatCycle = command.substring(command.lastIndexOf("repeat") + 7, command.lastIndexOf("on") - 2);
		if(repeatCycle.equals("weekly") || repeatCycle.equals("yearly") || repeatCycle.equals("monthly")) {
			if(command.contains("ex")) {
				repeatDay = command.substring(command.lastIndexOf("on") + 3, command.lastIndexOf("ex") - 2);
				exception = command.substring(command.lastIndexOf("ex") + 3);
			} else {
				repeatDay = command.substring(command.lastIndexOf("on") + 3);
			}
		} else {
			if(command.contains("ex")) {
				exception = command.substring(command.lastIndexOf("ex") + 3);
			}
		}

		Task newTask = new Task(eventTitle, repeatCycle, repeatDay, exception, true);
		modifier.addTask(newTask);
	}

	private static String getEventDeadline(String command) {
		return command.substring(command.lastIndexOf("by") + 3);
	}

	private static String getEventTitleDeadline(String command) {
		return command.substring(4, command.lastIndexOf("by") - 1);
	}

	static boolean isValidDate(String newDate) {
		boolean isValid = false;

		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("ddMMyyyy HHmm");
		dateFormat.setLenient(false);
		dateTimeFormat.setLenient(false);

		try {
			Date date = dateFormat.parse(newDate);
			isValid = true;
		} catch (ParseException e) {
			isValid = false;
		}
		
		if(isValid == false) {
			try {
			Date date = dateTimeFormat.parse(newDate);
			isValid = true;
			} catch (ParseException e) {
				isValid = false;
			}
		}

		return isValid;
	}

	public static void addEventDefault(String command) {
		String eventTitle = null;
		eventTitle = getEventTitleDefault(command);
		if(eventTitle == null) {
			FeedbackPane.displayInvalidTitle();
		} else {
			Task newTask = new Task(eventTitle);
			modifier.addTask(newTask);
		}
	}

	private static String getEventTitleDefault(String command) {
		if (command.length() > 3) {
			return command.substring(4);
		} else  {
			addMessage = "Please enter an event title";
			return null;
		}
	}
	
	public static String getMessage() {
		return addMessage;
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
					Task newTask;
					if(containsTime(deadline)) {
						newTask = new Task(eventTitle, deadline, rank, true);
					} else {
						newTask = new Task(eventTitle, deadline, rank);
					}
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
		String startDate = new String();
		String endDate = new String();

		try {
			eventTitle = getEventTitleTimeline(command);
			startDate = getStartDate(command);
		} catch (Exception e) {
			addEventWithImportance(command);
			return;
		}

		if (isValidDate(startDate)) {
			if (isEventWithImportance(command)) {
				try {
					int rank = convertStringToInt(command);
					endDate = getEndDateImportance(command);
					Task newTask;
					if(containsTime(endDate)) {
						newTask = new Task(eventTitle, startDate, endDate, rank, true);
					} else {
						newTask = new Task(eventTitle, startDate, endDate, rank);
					}
					modifier.addTask(newTask);
				} catch (Exception e) {
					endDate = getEndDateTimeline(command);
					Task newTask;
					if(containsTime(endDate)) {
						newTask = new Task(eventTitle, startDate, endDate, true);
					} else {
						newTask = new Task(eventTitle, startDate, endDate);
					}
					modifier.addTask(newTask);
				}

			} else {
				endDate = getEndDateTimeline(command);
				Task newTask;
				if(containsTime(endDate)) {
					newTask = new Task(eventTitle, startDate, endDate, true);
				} else {
					newTask = new Task(eventTitle, startDate, endDate);
				}
				modifier.addTask(newTask);
			}
		} else {
			addEventWithImportance(command);
			return;
		}
	}

	private static String getEndDateTimeline(String command) {
		return command.substring(command.lastIndexOf("to") + 3);
	}

	private static String getStartDate(String command) {
		return command.substring(command.lastIndexOf("from") + 5, command.lastIndexOf("to") - 1);
	}

	private static String getEventTitleTimeline(String command) {
		return command.substring(4, command.lastIndexOf("from") - 1);
	}

	private static String getEndTime(String command) {
		return command.substring(command.lastIndexOf("to") + 3);
	}

	private static String getEndDateImportance(String command) {
		return command.substring(command.lastIndexOf("to") + 3, command.lastIndexOf("rank") - 1);
	}

	private static boolean isEventWithImportance(String command) {
		return command.contains("rank");
	}

	public static void addBlockEvent(String command){
		String eventTitle = getEventTitleBlockDate(command);
		String end = getEndTime(command);
		String start = getBlockEventStartDate(command);
		Task newTask = new Task(eventTitle,start,end); 
		modifier.addTask(newTask); 
	}

	private static String getBlockEventStartDate(String command) {
		return command.substring(command.lastIndexOf("block") + 5, command.lastIndexOf("to") - 1);	

	}
	private static String getEventTitleBlockDate(String command) {
		return command.substring(4, command.lastIndexOf("block") - 1);

	}
	
	public static boolean containsTime(String date) {
		if(date.contains(" ")) {
			return true;
		} else {
			return false;
		}
	}


}
