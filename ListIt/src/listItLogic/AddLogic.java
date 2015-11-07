package listItLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class AddLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static String addDefaultMessage = "null";
	private static String addDeadlineMessage = "null";
	private static String addRankMessage = "null";
	private static String addBlockMessage = "null";
	private static String addRecurMessage = "null";
	private static final String MESSAGE_INVALID_INPUT = "invalid input";
	private static final String MESSAGE_ADD_TITLE = "Please enter an event title";
	private static final String MESSAGE_ADD_VALID_DATE = "enter a valid date";
	private static final String MESSAGE_INVALID_RANK = "invalid rank input";
	private static final String MESSAGE_RECUR_START = "please enter a start date";
	private static final String MESSAGE_RECUR_END = "please enter an end date";
	private static final String MESSAGE_RECUR_CYCLE = "please enter a recur cycle";
	private static final String MESSAGE_INVALID_RANGE = "start date should be earlier than end date";
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
		} else if (isDayOfWeek(deadline)) {
			addDeadlineMessage = MESSAGE_ADD_VALID_DATE;
			FeedbackPane.displayInvalidInput();
		} else if (isWord(deadline)) {
			addEventDefault(command);
		} else {
			addDeadlineMessage = MESSAGE_ADD_VALID_DATE;
			FeedbackPane.displayInvalidInput();
			 LoggingLogic.logging(addDeadlineMessage);
		}
	}

	public static boolean isWord(String word) {
		try {
			int date = Integer.parseInt(word);
			return false;
		} catch (NumberFormatException e) {
			return true;
		}
	}

	public static boolean isDayOfWeek(String deadline) {
		deadline = deadline.toLowerCase();
		if (deadline.contains("monday") || deadline.contains("tuesday") || deadline.contains("wednesday")
				|| deadline.contains("thursday") || deadline.contains("friday") || deadline.contains("saturday")
				|| deadline.contains("sunday") || deadline.contains("tomorrow") || deadline.contains("week")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getDeadlineMessage() {
		return addDeadlineMessage;
	}

	private static String getEventDeadline(String command) {
		String deadline = " ";
		if (command.contains(COMMAND_BY)) {
			deadline = command.substring(command.lastIndexOf(COMMAND_BY) + 3);
			if (isValidDate(deadline)) {
				return deadline;
			}
		}
		if (command.contains(COMMAND_ON)) {
			deadline = command.substring(command.lastIndexOf(COMMAND_ON) + 3);
			if (isValidDate(deadline)) {
				return deadline;
			}
		}
		return deadline;
	}

	private static String getEventTitleDeadline(String command) {
		if (command.contains(COMMAND_BY) && command.contains(COMMAND_ON)) {
			if (command.lastIndexOf(COMMAND_BY) > command.lastIndexOf(COMMAND_ON)) {
				return command.substring(4, command.lastIndexOf(COMMAND_BY) - 1);
			} else {
				return getSingleDateTitleForTimeline(command);
			}
		} else if (command.contains(COMMAND_BY)) {
			return command.substring(4, command.lastIndexOf(COMMAND_BY) - 1);
		} else {
			return getSingleDateTitleForTimeline(command);
		}
	}

	static boolean isValidDate(String newDate) {
		boolean isValid = false;
		
		assert newDate != null;

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
		if (eventTitle.equals("null")) {
			FeedbackPane.displayInvalidTitle();
		} else {
			Task newTask = new Task(eventTitle);
			modifier.addTask(newTask);
		}
	}

	private static String getEventTitleDefault(String command) {
		if (command.length() > 4) {
			return command.substring(4);
		} else {
			addDefaultMessage = MESSAGE_ADD_TITLE;
			FeedbackPane.displayInvalidInput();
			LoggingLogic.logging(addDefaultMessage);
			return "null";
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
				if (rank >= 1 && rank <= 3) {
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
				} else {
					addRankMessage = MESSAGE_INVALID_RANK;
					FeedbackPane.displayInvalidInput();
					LoggingLogic.logging(addRankMessage);

				}
			} catch (Exception e) {
				addEventWithDeadline(command);
				return;
			}
		} else {
			try {
				eventTitle = getEventTitleImportance(command);
				int rank = convertStringToInt(command);
				if (rank >= 1 && rank <= 3) {
					Task newTask = new Task(eventTitle, rank);
					modifier.addTask(newTask);
				} else {
					addRankMessage = MESSAGE_INVALID_RANK;
					FeedbackPane.displayInvalidInput();
				}
			} catch (Exception e) {
				addEventDefault(command);
				return;
			}
		}
	}

	public static String getRankMessage() {
		return addRankMessage;
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
		boolean invalidOnCommand = false;

		if (command.contains(COMMAND_ON)) {
			try {
				eventTitle = getSingleDateTitleForTimeline(command);
				startDate = getSingleDateStartTime(command);
				endDate = getSingleDateEndDate(command);
			} catch (Exception e) {
				invalidOnCommand = true;
			}
			if (isValidDate(startDate) && isValidDate(endDate) && invalidOnCommand == false) {
				if (isValidRank(command)) {
					addTaskWithTimelineAndRank(command, eventTitle, startDate, endDate);
				} else if (isRankNonCommand(command)) {
					addTaskWithTimelineAndNoRank(command, eventTitle, startDate, endDate);
				} else {
					addRankMessage = MESSAGE_INVALID_RANK;
					FeedbackPane.displayInvalidInput();
				}
			}
		}

		if (!command.contains(COMMAND_ON) || invalidOnCommand) {
			eventTitle = getEventTitleTimeline(command);
			startDate = getStartDate(command);
			if (isValidDate(startDate)) {
				if (isCorrectRange(startDate, endDate)) {
					if (isEventWithImportance(command)) {
						if (isValidRank(command)) {
							endDate = getEndDateImportance(command);
							addTaskWithTimelineAndRank(command, eventTitle, startDate, endDate);
						} else if (isRankNonCommand(command)) {
							endDate = getEndDateTimeline(command);
							addTaskWithTimelineAndNoRank(command, eventTitle, startDate, endDate);
						} else {
							addRankMessage = MESSAGE_INVALID_RANK;
							FeedbackPane.displayInvalidInput();
						}
					} else {
						endDate = getEndDateTimeline(command);
						addTaskWithTimelineAndNoRank(command, eventTitle, startDate, endDate);
					}
				} else {
					FeedbackPane.displayInvalidDate();
					return;
				}
			} else {
				AddLogic.addEventDefault(command);
			}
		}
	}

	private static String getSingleDateTitleForTimeline(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_ON) - 1);
	}

	private static void addTaskWithTimelineAndNoRank(String command, String eventTitle, String startDate,
			String endDate) {
		Task newTask;
		if (containsTime(endDate)) {
			newTask = new Task(eventTitle, startDate, endDate, true);
		} else {
			newTask = new Task(eventTitle, startDate, endDate);
		}
		modifier.addTask(newTask);
	}

	private static void addTaskWithTimelineAndRank(String command, String eventTitle, String startDate,
			String endDate) {
		int rank = convertStringToInt(command);
		Task newTask;
		if (containsTime(endDate)) {
			newTask = new Task(eventTitle, startDate, endDate, rank, true);
		} else {
			newTask = new Task(eventTitle, startDate, endDate, rank);
		}
		modifier.addTask(newTask);
	}

	private static String getSingleDateEndDate(String command) {
		return command.substring(command.lastIndexOf(COMMAND_ON) + 3, command.lastIndexOf(COMMAND_START_TIME) - 1) + " "
				+ command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3);
	}

	private static String getSingleDateStartTime(String command) {
		return command.substring(command.lastIndexOf(COMMAND_ON) + 3, command.lastIndexOf(COMMAND_START_TIME) - 1) + " "
				+ command.substring(command.lastIndexOf(COMMAND_START_TIME) + 5,
						command.lastIndexOf(COMMAND_END_TIME) - 1);
	}

	private static boolean isRankNonCommand(String command) {
		try {
			int rank = Integer.parseInt(command.substring(command.lastIndexOf(COMMAND_RANK) + 5));
			return false;
		} catch (NumberFormatException e) {
			return true;
		}
	}

	private static boolean isValidRank(String command) {
		boolean isValid = false;
		try {
			int rank = convertStringToInt(command);
			if (rank >= 1 && rank <= 3) {
				isValid = true;
			} else {
				isValid = false;
			}
		} catch (NumberFormatException e) {
			addRankMessage = "Not an integer value";
		}
		return isValid;
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
			try {
				deadline = command.substring(command.lastIndexOf(COMMAND_ON) + 3);
			} catch (Exception e) {
				deadline = "";
			}
			if (isValidDate(deadline) && repeatCycle != 0) {
				if (containsTime(deadline)) {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, deadline, true, true);
					modifier.addTask(newTask);
				} else {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, deadline, true);
					modifier.addTask(newTask);
				}
			} else if (repeatCycle == 0) {
				addRecurMessage = MESSAGE_RECUR_CYCLE;
				FeedbackPane.displayInvalidInput();
				LoggingLogic.logging(addRecurMessage);

			} else if (deadline.equals("")) {
				addRecurMessage = MESSAGE_RECUR_START;
				FeedbackPane.displayInvalidInput();
			} else {
				addEventDefault(command);
			}
		} else {
			addEventWithDeadline(command);
			return;
		}
	}

	public static String getRecurMessage() {
		return addRecurMessage;
	}

	private static int parseRepeatAmount(String repeatCycle) {
		try {
			int cycle = Integer.parseInt(repeatCycle.substring(0, repeatCycle.indexOf(" ")));
			return cycle;
		} catch (StringIndexOutOfBoundsException e) {
			return 0;
		}
	}

	private static String parseRepeatType(String repeatCycle) {
		return repeatCycle.substring(repeatCycle.indexOf(WHITESPACE) + 1);
	}

	public static boolean isCorrectRepeatCycle(String repeatCycle) {
		boolean isCorrect = false;
		
		assert repeatCycle != null;

		if (repeatCycle.contains(REPEAT_DAILY) || repeatCycle.contains(REPEAT_MONTHLY)
				|| repeatCycle.contains(REPEAT_YEARLY) || repeatCycle.contains(REPEAT_WEEKLY)) {
			isCorrect = true;

		}
		return isCorrect;
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
			try {
				startDate = command.substring(command.lastIndexOf(COMMAND_START_TIME) + 5,
						command.lastIndexOf(COMMAND_END_TIME) - 1);
				endDate = command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3);
			} catch (Exception e) {
				startDate = "";
				endDate = "";
			}
			if (isValidDate(startDate) && repeatCycle != 0) {
				if (containsTime(startDate)) {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, startDate, endDate, true, true);
					modifier.addTask(newTask);
				} else {
					Task newTask = new Task(eventTitle, repeatType, repeatCycle, startDate, endDate, true);
					modifier.addTask(newTask);
				}
			} else if (startDate.equals("")) {
				addRecurMessage = MESSAGE_RECUR_START;
				FeedbackPane.displayInvalidInput();
			} else if (endDate.equals("")) {
				addRecurMessage = MESSAGE_RECUR_END;
				FeedbackPane.displayInvalidInput();
			} else if (repeatCycle == 0) {
				addRecurMessage = MESSAGE_RECUR_CYCLE;
				FeedbackPane.displayInvalidInput();
			} else {
				addEventDefault(command);
			}
		} else {
			addEventWithTimeline(command);
		}
	}

	public static void addBlockEvent(String command) {
		Task newTask = new Task();
		String eventTitle = getEventTitleBlock(command);
		String start = getBlockEventStartDate(command);
		String end = getEndTime(command);
		if (isValidDate(end) && isValidDate(start)) {
			if (isCorrectRange(start, end)) {
				if (containsTime(end)) {
					newTask = new Task(eventTitle, start, end, true);
				} else {
					newTask = new Task(eventTitle, start, end);
				}
				newTask.setBlocking(true);
				modifier.addTask(newTask);
			} else {
				addBlockMessage = MESSAGE_INVALID_RANGE;
				LoggingLogic.logging(addBlockMessage);
				FeedbackPane.displayInvalidInput();
			}
		} else {
			addBlockMessage = MESSAGE_INVALID_INPUT;
			LoggingLogic.logging(addBlockMessage);
			//System.out.println(addBlockMessage);
			FeedbackPane.displayInvalidAdd();
		}
	}

	private static boolean isCorrectRange(String start, String end) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy");
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("ddMMyyyy HHmm");
		Date startDate;
		Date endDate;
		try {
			if (containsTime(start)) {
				startDate = dateTimeFormatter.parse(start);
				endDate = dateTimeFormatter.parse(end);
			} else {
				startDate = dateFormatter.parse(start);
				endDate = dateFormatter.parse(end);
			}
		} catch (ParseException e) {
			return false;
		}

		if (startDate.compareTo(endDate) == -1) {
			return true;
		} else {
			return false;
		}
	}

	public static String getBlockMessage() {
		return addBlockMessage;
	}

	private static String getBlockEventStartDate(String command) {
		return command.substring(command.lastIndexOf(COMMAND_BLOCK) + 6, command.lastIndexOf(COMMAND_END_TIME) - 1);
	}

	private static String getEventTitleBlock(String command) {
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
