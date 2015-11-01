package listItLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class AddLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static String addDefaultMessage = null;
	private static String addDeadlineMessage = null;
	private static final String MESSAGE_ADD_TITLE = "Please enter an event title";
	private static final String MESSAGE_ADD_VALID_DATE1 = "Please enter a valid date! Days of the week are not accepted";
	private static final String MESSAGE_ADD_VALID_DATE2 = "add valid date";
	private static final String MESSAGE_ADD_VALID_DATE3 = "enter a vaild date";
	private static final String COMMAND_BY = "by";
	private static final String FORMAT_DATE = "ddMMyyyy";
	private static final String FORMAT_DATETIME = "ddMMyyyy HHmm";
	private static final String COMMAND_RANK = "rank";
	private static final String COMMAND_START_TIME = "from";
	private static final String COMMAND_END_TIME = "to";
	private static final String COMMAND_REPEAT = "repeat";
	private static final String COMMAND_ON = "on";
	private static final String WHITESPACE = " ";
	private static final String COMMAND_BLOCK = "block";
	private static final String REPEAT_DAILY = "daily";
	private static final String REPEAT_WEEKLY = "weekly";
	private static final String REPEAT_MONTHLY = "monthly";
	private static final String REPEAT_YEARLY = "yearly";

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
			if (containsTime(deadline)) {
				newTask = new Task(eventTitle, deadline, true);
			} else {
				newTask = new Task(eventTitle, deadline);
			}
			modifier.addTask(newTask);
		} else if (!isValidDate(deadline) && isStringObject(deadline)) {
			if(isDayOfWeek(deadline)) {
				addDeadlineMessage = MESSAGE_ADD_VALID_DATE1;
			} else {
				addDeadlineMessage = MESSAGE_ADD_VALID_DATE2;
				addEventDefault(deadline);
			} 
		} else if (!isValidDate(deadline) && !isStringObject(deadline)) {
			addDeadlineMessage = MESSAGE_ADD_VALID_DATE3;
		}
	}
	
	public static boolean isDayOfWeek(String deadline) {
		if(deadline.contains("monday") || deadline.contains("tuesday") 
		   || deadline.contains("wednesday") || deadline.contains("thursday") 
		   || deadline.contains("friday") || deadline.contains("saturday")
		   || deadline.contains("sunday") || deadline.contains("tomorrow")
		   || deadline.contains("week")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isStringObject(String deadline) {
		boolean isString = false;
		int date;
		try {
			date = Integer.parseInt(deadline);
		} catch (NumberFormatException e) {
			isString = true;
		}
		return isString;
	}
	
	public static String getDeadlineMessage() {
		return addDeadlineMessage;
	}

	private static String getEventDeadline(String command) {
		if(command.contains(COMMAND_BY)) {
			return command.substring(command.lastIndexOf(COMMAND_BY) + 3);
		} else {
			return command.substring(command.lastIndexOf(COMMAND_ON) + 3);
		}
	}

	private static String getEventTitleDeadline(String command) {
		if(command.contains(COMMAND_BY)) {
			return command.substring(4, command.lastIndexOf(COMMAND_BY) - 1);
		} else {
			return command.substring(4, command.lastIndexOf(COMMAND_ON) - 1);
		}
	}

	static boolean isValidDate(String newDate) {
		boolean isValid = false;

		SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(FORMAT_DATETIME);
		dateFormat.setLenient(false);
		dateTimeFormat.setLenient(false);

		try {
			Date date = dateFormat.parse(newDate);
			isValid = true;
		} catch (ParseException e) {
			isValid = false;
		}

		if (isValid == false) {
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
		if (eventTitle == null) {
			FeedbackPane.displayInvalidTitle();
		} else {
			Task newTask = new Task(eventTitle);
			modifier.addTask(newTask);
		}
	}

	private static String getEventTitleDefault(String command) {
		if (command.length() > 3) {
			return command.substring(4);
		} else {
			addDefaultMessage = MESSAGE_ADD_TITLE;
			return null;
		}
	}

	public static String getMessage() {
		return addDefaultMessage;
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
					if (containsTime(deadline)) {
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
		return Integer.parseInt(command.substring(command.lastIndexOf(COMMAND_RANK) + 5));
	}

	private static String getEventTitleImportance(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_RANK) - 1);
	}

	private static String getEventDeadlineImportance(String command) {
		return command.substring(command.lastIndexOf(COMMAND_BY) + 3, command.lastIndexOf(COMMAND_RANK) - 1);
	}

	private static boolean isEventWithDeadline(String command) {
		return command.contains(COMMAND_BY);
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
					if (containsTime(endDate)) {
						newTask = new Task(eventTitle, startDate, endDate, rank, true);
					} else {
						newTask = new Task(eventTitle, startDate, endDate, rank);
					}
					modifier.addTask(newTask);
				} catch (Exception e) {
					endDate = getEndDateTimeline(command);
					Task newTask;
					if (containsTime(endDate)) {
						newTask = new Task(eventTitle, startDate, endDate, true);
					} else {
						newTask = new Task(eventTitle, startDate, endDate);
					}
					modifier.addTask(newTask);
				}

			} else {
				endDate = getEndDateTimeline(command);
				Task newTask;
				if (containsTime(endDate)) {
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
		return command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3);
	}

	private static String getStartDate(String command) {
		return command.substring(command.lastIndexOf(COMMAND_START_TIME) + 5,
				command.lastIndexOf(COMMAND_END_TIME) - 1);
	}

	private static String getEventTitleTimeline(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_START_TIME) - 1);
	}

	private static String getEndTime(String command) {
		return command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3);
	}

	private static String getEndDateImportance(String command) {
		return command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3, command.lastIndexOf(COMMAND_RANK) - 1);
	}

	private static boolean isEventWithImportance(String command) {
		return command.contains(COMMAND_RANK);
	}

	public static void addRecursiveEventDeadline(String command) {
		String deadline = null;
		String repeatType = null;
		int repeatCycle = 0;
		String eventTitle = command.substring(4, command.lastIndexOf(COMMAND_REPEAT) - 1);
		String repeatCommand = command.substring(command.lastIndexOf(COMMAND_REPEAT) + 7,
				command.lastIndexOf(COMMAND_ON) - 1);
		if (isCorrectRepeatCycle(repeatCommand)) {
			repeatType = parseRepeatType(repeatCommand);
			repeatCycle = parseRepeatAmount(repeatCommand);
			deadline = command.substring(command.lastIndexOf(COMMAND_ON) + 3);
			if (isValidDate(deadline)) {
				if (containsTime(deadline)) {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, deadline, true, true);
					modifier.addTask(newTask);
				} else {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, deadline, true);
					modifier.addTask(newTask);
				}
			} else {
				addEventDefault(command);
			}
		} else {
			addEventWithDeadline(command);
			return;
		}
	}

	private static int parseRepeatAmount(String repeatCycle) {
		return Integer.parseInt(repeatCycle.substring(0, repeatCycle.indexOf(" ")));
	}

	private static String parseRepeatType(String repeatCycle) {
		return repeatCycle.substring(repeatCycle.indexOf(WHITESPACE) + 1);
	}

	private static boolean isCorrectRepeatCycle(String repeatCycle) {
		boolean result = false;

		if (repeatCycle.contains(REPEAT_DAILY) || repeatCycle.contains(REPEAT_MONTHLY)
				|| repeatCycle.contains(REPEAT_YEARLY) || repeatCycle.contains(REPEAT_WEEKLY)) {
			result = true;

		}
		return result;
	}

	public static void addRecursiveEventTimeline(String command) {
		String startDate = null;
		String endDate = null;
		String repeatType = null;
		int repeatCycle = 0;
		String eventTitle = command.substring(4, command.lastIndexOf(COMMAND_REPEAT) - 1);
		String repeatCommand = command.substring(command.lastIndexOf(COMMAND_REPEAT) + 7,
				command.lastIndexOf(COMMAND_START_TIME) - 1);
		if (isCorrectRepeatCycle(repeatCommand)) {
			repeatType = parseRepeatType(repeatCommand);
			repeatCycle = parseRepeatAmount(repeatCommand);
			startDate = command.substring(command.lastIndexOf(COMMAND_START_TIME) + 5,
					command.lastIndexOf(COMMAND_END_TIME) - 1);
			endDate = command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3);
			if (isValidDate(startDate)) {
				if (containsTime(startDate)) {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, startDate, endDate, true, true);
					modifier.addTask(newTask);
				} else {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, startDate, endDate, true);
					modifier.addTask(newTask);
				}
			} else {
				addEventDefault(command);
			}
		} else {
			addEventWithTimeline(command);
			return;
		}
	}

	public static void addBlockEvent(String command) {
		String eventTitle = getEventTitleBlockDate(command);
		String end = getEndTime(command);
		String start = getBlockEventStartDate(command);
		Task newTask = new Task(eventTitle, start, end);
		modifier.addTask(newTask);
	}

	private static String getBlockEventStartDate(String command) {
		return command.substring(command.lastIndexOf(COMMAND_BLOCK) + 5, command.lastIndexOf(COMMAND_END_TIME) - 1);

	}

	private static String getEventTitleBlockDate(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_BLOCK) - 1);

	}

	public static boolean containsTime(String date) {
		if (date.contains(WHITESPACE)) {
			return true;
		} else {
			return false;
		}
	}
}
