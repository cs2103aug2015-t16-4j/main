# Shrestha A0130280X
###### src\listItLogic\AddLogic.java
``` java
package listItLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

/**
 * This class adds a task to the data file, by breaking down the string command
 * and separates the different variables, in order to create the task object to 
 * be added. 
 * @version 0.5
 */
public class AddLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static String addDefaultMessage = "null";
	private static String addDeadlineMessage = "null";
	private static String addRankMessage = "null";
	private static String addBlockMessage = "null";
	private static String addRecurMessage = "null";
	private static final int VALUE_NO_REPEAT_CYCLE = 0;
	private static final String STRING_NULL = "null";
	private static final String EMPTY_STRING = "";
	private static final String MESSAGE_INVALID_INPUT = "invalid input";
	private static final String MESSAGE_ADD_TITLE = "Please enter an event title";
	private static final String MESSAGE_ADD_VALID_DATE = "enter a valid date";
	private static final String MESSAGE_INVALID_RANK = "invalid rank input";
	private static final String MESSAGE_RECUR_START = "please enter a start date";
	private static final String MESSAGE_RECUR_END = "please enter an end date";
	private static final String MESSAGE_RECUR_CYCLE = "please enter a recur cycle";
	private static final String MESSAGE_INVALID_RANGE = "start date should be " + "earlier than end date";
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
	private static final String DAY_MONDAY = "monday";
	private static final String DAY_TUESDAY = "tuesday";
	private static final String DAY_WEDNESDAY = "wednesday";
	private static final String DAY_THURSDAY = "thursday";
	private static final String DAY_FRIDAY = "friday";
	private static final String DAY_SATURDAY = "saturday";
	private static final String DAY_SUNDAY = "sunday";
	private static final String DAY_TOMORROW = "tomorrow";
	private static final String DAY_WEEK = "week";
	
	/**
	 * This method adds a task object that has only a title and 
	 * the deadline variable.
	 * @param command string command input by the user with an "add" at the start
	 */
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
				newTask = createTaskWithTimeAndDeadline(eventTitle, deadline);
			} else {
				newTask = createTaskWithDeadline(eventTitle, deadline);
			}
			modifier.addTask(newTask);
			FeedbackPane.displayValidAdd();
		} else if (isDayOfWeek(deadline)) {
			displayInvalidInput();
		} else if (isWord(deadline)) {
			addEventDefault(command);
		} else {
			displayInvalidInput();
			LoggingLogic.logging(addDeadlineMessage);
		}
	}
	
	
	private static Task createTaskWithDeadline(String eventTitle, String deadline) {
		return new Task(eventTitle, deadline);
	}

	private static Task createTaskWithTimeAndDeadline(String eventTitle, String deadline) {
		return new Task(eventTitle, deadline, true);
	}

	private static void displayInvalidInput() {
		addDeadlineMessage = MESSAGE_ADD_VALID_DATE;
		FeedbackPane.displayInvalidInput();
	}

	/** 
	 * Checks if the string variable is a word or not, in order to check for
	 * the date input by the user, and therefore parse it into the date object.
	 * @param word string word in the command
	 * @return true if it is a word, false if it is not a word
	 */
	public static boolean isWord(String word) {
		try {
			int date = Integer.parseInt(word);
			return false;
		} catch (NumberFormatException e) {
			return true;
		}
	}

	/**
	 * @param deadline deadline variable
	 * @return true if deadline is a day of the week, else returns false.
	 */
	public static boolean isDayOfWeek(String deadline) {
		deadline = deadline.toLowerCase();
		if (deadline.contains(DAY_MONDAY) || deadline.contains(DAY_TUESDAY) || deadline.contains(DAY_WEDNESDAY)
				|| deadline.contains(DAY_THURSDAY) || deadline.contains(DAY_FRIDAY) || deadline.contains(DAY_SATURDAY)
				|| deadline.contains(DAY_SUNDAY) || deadline.contains(DAY_TOMORROW) || deadline.contains(DAY_WEEK)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getDeadlineMessage() {
		return addDeadlineMessage;
	}

	/**
	 * Gets the event deadline from the command input by finding the keyword,
	 * "on" or "by" and then ensures that the deadline entered is valid.
	 * @param command string command input by the user with an "add" at the start
	 * @return the deadline variable
	 */
	private static String getEventDeadline(String command) {
		String deadline = WHITESPACE;
		if (command.contains(COMMAND_BY)) {
			deadline = getEventDeadlineAfterBy(command);
			if (isValidDate(deadline)) {
				return deadline;
			}
		}
		if (command.contains(COMMAND_ON)) {
			deadline = getEventDeadlineAfterOn(command);
			if (isValidDate(deadline)) {
				return deadline;
			}
		}
		return deadline;
	}

	
	private static String getEventDeadlineAfterOn(String command) {
		return command.substring(command.lastIndexOf(COMMAND_ON) + 3);
	}

	private static String getEventDeadlineAfterBy(String command) {
		return command.substring(command.lastIndexOf(COMMAND_BY) + 3);
	}

	/**
	 * Gets the title task object for a single day event 
	 * @param command string command input by the user with an "add" at the start
	 * @return the title of the task object
	 */
	private static String getEventTitleDeadline(String command) {
		if (hasBothOnAndBy(command)) {
			if (isByAfterOn(command)) {
				return getEventTitleBeforeBy(command);
			} else {
				return getEventTitleBeforeOn(command);
			}
		} else if (command.contains(COMMAND_BY)) {
			return getEventTitleBeforeBy(command);
		} else {
			return getEventTitleBeforeOn(command);
		}
	}

	
	private static String getEventTitleBeforeBy(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_BY) - 1);
	}

	private static boolean isByAfterOn(String command) {
		return command.lastIndexOf(COMMAND_BY) > command.lastIndexOf(COMMAND_ON);
	}

	/**
	 * @param command  string command input by the user with an "add" at the start
	 * @return true if the above holds, else returns false.
	 */
	private static boolean hasBothOnAndBy(String command) {
		return command.contains(COMMAND_BY) && command.contains(COMMAND_ON);
	}

	/**
	 * Checks if the date entered is a valid date or not. This date variable 
	 * must be input in the set format, ddMMyyyy, or ddMMyyyy HHmm.
	 * After which, it will parse the date into the date object if valid.
	 * @param newDate date as a string input
	 * @return true if date input is valid, else returns false.
	 */
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

	/**
	 * Adds an task consisting of just the event title. 
	 * If no title is found, returns an error message.
	 * @param command string command input by the user with an "add" at the start
	 */
	public static void addEventDefault(String command) {
		String eventTitle = null;
		eventTitle = getEventTitleDefault(command);
		if (eventTitle.equals(STRING_NULL)) {
			FeedbackPane.displayInvalidTitle();
		} else {
			Task newTask = new Task(eventTitle);
			modifier.addTask(newTask);
			FeedbackPane.displayValidAdd();
		}
	}

	/**
	 * @param command string command input by the user with an "add" at the start
	 * @return event title
	 */
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

	public static String getDefaultMessage() {
		return addDefaultMessage;
	}

	/**
	 * Adds a task that has the title and importance variable.
	 * Task may or may not have a date input.
	 * @param command string command input by the user with an "add" at the start
	 */
	public static void addEventWithImportance(String command) {
		String eventTitle = null;
		if (isEventWithDeadline(command)) {
			String deadline = null;
			try {
				eventTitle = getEventTitleDeadline(command);
				deadline = getEventDeadlineImportance(command);
			} catch (Exception e) {
				addEventWithDeadline(command);
				return;
			}

			try {
				if (isValidRank(command)) {
					int rank = getRankValue(command);
					if (isValidDate(deadline)) {
						Task newTask;
						if (containsTime(deadline)) {
							newTask = createTaskWithDeadlineRankAndTime(eventTitle, deadline, rank);
						} else {
							newTask = createTaskWithDeadlineAndRank(eventTitle, deadline, rank);
						}
						modifier.addTask(newTask);
						FeedbackPane.displayValidAdd();
					} else {
						eventTitle = getEventTitleImportance(command);
						Task newTask = new Task(eventTitle, rank);
						modifier.addTask(newTask);
						FeedbackPane.displayValidAdd();
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
				if (isValidRank(command)) {
					int rank = getRankValue(command);
					Task newTask = new Task(eventTitle, rank);
					modifier.addTask(newTask);
					FeedbackPane.displayValidAdd();
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

	private static Task createTaskWithDeadlineAndRank(String eventTitle, String deadline, int rank) {
		return new Task(eventTitle, deadline, rank);
	}

	private static Task createTaskWithDeadlineRankAndTime(String eventTitle, String deadline, int rank) {
		return new Task(eventTitle, deadline, rank, true);
	}

	
	public static String getRankMessage() {
		return addRankMessage;
	}

	private static int getRankValue(String command) {
		return Integer.parseInt(command.substring(command.lastIndexOf(COMMAND_RANK) + 5));
	}

	private static String getEventTitleImportance(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_RANK) - 1);
	}

	private static String getEventDeadlineImportance(String command) {
		if(command.contains(COMMAND_BY)) {
			return command.substring(command.lastIndexOf(COMMAND_BY) + 3, command.lastIndexOf(COMMAND_RANK) - 1);
		} else {
			return command.substring(command.lastIndexOf(COMMAND_ON) + 3, command.lastIndexOf(COMMAND_RANK) - 1);
		}
	}

	/**
	 * @param command string command input by the user with an "add" at the start
	 * @return true if event has a deadline, else returns false
	 */
	private static boolean isEventWithDeadline(String command) {
		return command.contains(COMMAND_BY) || command.contains(COMMAND_ON);
	}

	/**
	 * Adds an event with a timeline. Event can either have a timeline consisting of
	 * a single day timeline or a timeline that spans through multiple days
	 * @param command string command input by the user with an "add" at the start
	 */
	public static void addEventWithTimeline(String command) {
		String eventTitle = new String();
		String startDate = new String();
		String endDate = new String();
		boolean isValidOnCommand = false;

		if (command.contains(COMMAND_ON)) {
			try {
				eventTitle = getEventTitleBeforeOn(command);
				startDate = getSingleDateStartTime(command);
				if(isValidDate(startDate)) {
					isValidOnCommand = true;
				}
			} catch (Exception e) {
				isValidOnCommand = false;
			}
			if (isValidOnCommand == true) {
				if (isEventWithImportance(command)) {
					if (isValidRank(command)) {
						endDate = getSingleDateEndTimeWithImportance(command);
						if (isCorrectRange(startDate, endDate)) {
							addTaskWithTimelineAndRank(command, eventTitle, startDate, endDate);
							return;
						} else {
							FeedbackPane.displayInvalidDate();
							return;
						}
					} else if (isRankNonCommand(command)) {
						endDate = getSingleDateEndTime(command);
						if (isCorrectRange(startDate, endDate)) {
							addTaskWithTimelineAndNoRank(command, eventTitle, startDate, endDate);
							return;
						} else {
							FeedbackPane.displayInvalidDate();
							return;
						}
					} else {
						addRankMessage = MESSAGE_INVALID_RANK;
						FeedbackPane.displayInvalidInput();
						return;
					}
				} else {
					endDate = getSingleDateEndTime(command);
					if (isCorrectRange(startDate, endDate)) {
						addTaskWithTimelineAndNoRank(command, eventTitle, startDate, endDate);
						return;
					} else {
						FeedbackPane.displayInvalidDate();
						return;
					}
				}
			}
		}

		if (!command.contains(COMMAND_ON) || !isValidOnCommand) {
			eventTitle = getEventTitleTimeline(command);
			startDate = getStartDate(command);
			if (isValidDate(startDate)) {
				if (isEventWithImportance(command)) {
					if (isValidRank(command)) {
						endDate = getEndDateImportance(command);
						if (isCorrectRange(startDate, endDate)) {
							addTaskWithTimelineAndRank(command, eventTitle, startDate, endDate);
						} else {
							FeedbackPane.displayInvalidDate();
						}
					} else if (isRankNonCommand(command)) {
						endDate = getEndDateTimeline(command);
						if (isCorrectRange(startDate, endDate)) {
							addTaskWithTimelineAndNoRank(command, eventTitle, startDate, endDate);
						} else {
							FeedbackPane.displayInvalidDate();
						}
					} else {
						addRankMessage = MESSAGE_INVALID_RANK;
						FeedbackPane.displayInvalidInput();
					}
				} else {
					endDate = getEndDateTimeline(command);
					if (isCorrectRange(startDate, endDate)) {
						addTaskWithTimelineAndNoRank(command, eventTitle, startDate, endDate);
						return;
					} else {
						FeedbackPane.displayInvalidDate();
						return;
					}
				}
			}
		} else {
			AddLogic.addEventDefault(command);
		}
	}

	private static String getSingleDateEndTimeWithImportance(String command) {
		return command.substring(command.lastIndexOf("to") + 3, command.lastIndexOf("rank") - 1);
	}

	private static String getEventTitleBeforeOn(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_ON) - 1);
	}

	/**
	 * @param command string command input by the user with an "add" at the start
	 * @param eventTitle event title
	 * @param startDate start date of the event
	 * @param endDate end date of the event
	 */
	private static void addTaskWithTimelineAndNoRank(String command, String eventTitle, String startDate,
			String endDate) {
		Task newTask;
		if (containsTime(endDate)) {
			newTask = new Task(eventTitle, startDate, endDate, true);
		} else {
			newTask = new Task(eventTitle, startDate, endDate);
		}
		modifier.addTask(newTask);
		FeedbackPane.displayValidAdd();
	}
	
	/**
	 * Adds a task with title, timeline and rank
	 * @param command string command input by the user with an "add" at the start
	 * @param eventTitle event title
	 * @param startDate start date of the event
	 * @param endDate end date of the event
	 */
	private static void addTaskWithTimelineAndRank(String command, String eventTitle, String startDate,
			String endDate) {
		int rank = getRankValue(command);
		Task newTask;
		if (containsTime(endDate)) {
			newTask = new Task(eventTitle, startDate, endDate, rank, true);
		} else {
			newTask = new Task(eventTitle, startDate, endDate, rank);
		}
		modifier.addTask(newTask);
		FeedbackPane.displayValidAdd();
	}

	private static String getSingleDateEndTime(String command) {
		return command.substring(command.lastIndexOf(COMMAND_ON) + 3, command.lastIndexOf(COMMAND_START_TIME) - 1) + " "
				+ command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3);
	}

	private static String getSingleDateStartTime(String command) {
		return command.substring(command.lastIndexOf(COMMAND_ON) + 3, command.lastIndexOf(COMMAND_START_TIME) - 1) + " "
				+ command.substring(command.lastIndexOf(COMMAND_START_TIME) + 5,
						command.lastIndexOf(COMMAND_END_TIME) - 1);
	}

	/**
	 * Checks if the word rank is a word for the event title, or the importance 
	 * variable key word. 
	 * @param command string command input by the user with an "add" at the start
	 * @return false if the word is a keyword, else returns true.
	 */
	private static boolean isRankNonCommand(String command) {
		try {
			int rank = Integer.parseInt(command.substring(command.lastIndexOf(COMMAND_RANK) + 5));
			return false;
		} catch (NumberFormatException e) {
			return true;
		}
	}

	/**
	 * Converts the rank variable from string to integer, then checks if
	 * the rank is a valid integer number (1,2 or 3)
	 * @param command string command input by the user with an "add" at the start
	 * @return true if rank is valid integer, else returns false
	 */
	private static boolean isValidRank(String command) {
		boolean isValid = false;
		try {
			int rank = getRankValue(command);
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

	/**
	 * @param command string command input by the user with an "add" at the start
	 * @return true if the command contains "rank". Else, returns false
	 */
	private static boolean isEventWithImportance(String command) {
		return command.contains(COMMAND_RANK);
	}

	/**
	 * Creates a recursive task event, by checking if the event has a title, 
	 * recursive keyword "repeat", repeat type and the repeat cycle. Checks if all 
	 * the type and cycles are valid inputs as well.
	 * @param command string command input by the user with an "add" at the start
	 */
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
				deadline = getEventDeadlineAfterOn(command);
			} catch (Exception e) {
				deadline = EMPTY_STRING;
			}
			if (isValidDate(deadline) && repeatCycle != 0) {
				if (containsTime(deadline)) {
					Task newTask = createRecurringTaskWithDeadlineAndTime(deadline, repeatType, repeatCycle,
							eventTitle);
					modifier.addTask(newTask);
					FeedbackPane.displayValidAdd();
				} else {
					Task newTask = createRecurringTaskWithDeadline(deadline, repeatType, repeatCycle, eventTitle);
					modifier.addTask(newTask);
					FeedbackPane.displayValidAdd();
				}
			} else if (!hasRepeatCycle(repeatCycle)) {
				addRecurMessage = MESSAGE_RECUR_CYCLE;
				FeedbackPane.displayInvalidInput();
				LoggingLogic.logging(addRecurMessage);

			} else if (isDeadlineEmpty(deadline)) {
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

	private static Task createRecurringTaskWithDeadline(String deadline, String repeatType, int repeatCycle,
			String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, deadline, true);
	}

	private static Task createRecurringTaskWithDeadlineAndTime(String deadline, String repeatType, int repeatCycle,
			String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, deadline, true, true);
	}

	/**
	 * @param deadline
	 * @return true if it is empty, else returns false
	 */
	private static boolean isDeadlineEmpty(String deadline) {
		return deadline.equals(EMPTY_STRING);
	}

	/**
	 * Checks if the repeat cycle value is 0 or not.
	 * @param repeatCycle 
	 * @return true if repeat cycle = 0, else returns false
	 */
	private static boolean hasRepeatCycle(int repeatCycle) {
		return repeatCycle != VALUE_NO_REPEAT_CYCLE;
	}

	public static String getRecurMessage() {
		return addRecurMessage;
	}

	/**
	 * Parses the repeat cycle from a string into an integer
	 * @param repeatCycle how often will the event repeat per type
	 * @return the repeat cycle in integer form
	 */
	private static int parseRepeatAmount(String repeatCycle) {
		try {
			int cycle = Integer.parseInt(repeatCycle.substring(0, repeatCycle.indexOf(WHITESPACE)));
			return cycle;
		} catch (StringIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/**
	 * @param repeatCycle daily, monthly, yearly
	 * @return repeat type
	 */
	private static String parseRepeatType(String repeatCycle) {
		return repeatCycle.substring(repeatCycle.indexOf(WHITESPACE) + 1);
	}

	/**
	 * @param repeatCycle daily, monthly, yearly
	 * @return true if it contains the keyword, else returns false
	 */
	public static boolean isCorrectRepeatCycle(String repeatCycle) {
		boolean isCorrect = false;

		assert repeatCycle != null;

		if (repeatCycle.contains(REPEAT_DAILY) || repeatCycle.contains(REPEAT_MONTHLY)
				|| repeatCycle.contains(REPEAT_YEARLY) || repeatCycle.contains(REPEAT_WEEKLY)) {
			isCorrect = true;

		}
		return isCorrect;
	}


	/**
	 * Checks if the string command has the correct keywords, and then adds a 
	 * recursive task to the task list. Else, displays invalid input if keyword is wrong.
	 * @param command string command input by the user with an "add" at the start
	 */
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
				startDate = EMPTY_STRING;
				endDate = EMPTY_STRING;
			}
			if (isValidDate(startDate) && hasRepeatCycle(repeatCycle)) {
				if (containsTime(startDate)) {
					Task newTask = createRecurringTaskWithTimeline(startDate, endDate, repeatType, repeatCycle,
							eventTitle);
					modifier.addTask(newTask);
					FeedbackPane.displayValidAdd();
				} else {
					Task newTask = createRecurringTaskWithTimelineAndNoTime(startDate, endDate, repeatType, repeatCycle,
							eventTitle);
					modifier.addTask(newTask);
					FeedbackPane.displayValidAdd();
				}
			} else if (isDeadlineEmpty(startDate)) {
				addRecurMessage = MESSAGE_RECUR_START;
				FeedbackPane.displayInvalidInput();
			} else if (isDeadlineEmpty(endDate)) {
				addRecurMessage = MESSAGE_RECUR_END;
				FeedbackPane.displayInvalidInput();
			} else if (!hasRepeatCycle(repeatCycle)) {
				addRecurMessage = MESSAGE_RECUR_CYCLE;
				FeedbackPane.displayInvalidInput();
			} else {
				addEventDefault(command);
			}
		} else {
			addEventWithTimeline(command);
		}
	}

	private static Task createRecurringTaskWithTimelineAndNoTime(String startDate, String endDate, String repeatType,
			int repeatCycle, String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, startDate, endDate, true);
	}

	private static Task createRecurringTaskWithTimeline(String startDate, String endDate, String repeatType,
			int repeatCycle, String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, startDate, endDate, true, true);
	}

	/**
	 * Adds a block event, which lasts over more than 1 day, to the task list. 
	 * Checks if the keywords entered are correct as well.
	 * @param command string command input by the user with an "add" at the start
	 */
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
				FeedbackPane.displayValidAdd();
			} else {
				addBlockMessage = MESSAGE_INVALID_RANGE;
				LoggingLogic.logging(addBlockMessage);
				FeedbackPane.displayInvalidInput();
			}
		} else {
			addBlockMessage = MESSAGE_INVALID_INPUT;
			LoggingLogic.logging(addBlockMessage);
			FeedbackPane.displayInvalidAdd();
		}
	}

	/**
	 * Checks if the range of the date entered (From and to) are in the correct form,
	 * such as start date must be earlier than end date.
	 * @param start starting date
	 * @param end ending date
	 * @return true if format is correct, else returns false.
	 */
	private static boolean isCorrectRange(String start, String end) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(FORMAT_DATE);
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(FORMAT_DATETIME);
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

		if (isStartDateBeforeEndDate(startDate, endDate)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param startDate
	 * @param endDate
	 * @return -1 if the starting date is before end date
	 */
	private static boolean isStartDateBeforeEndDate(Date startDate, Date endDate) {
		return startDate.compareTo(endDate) == -1;
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

	/**
	 * @param date date variable, with or without time.
	 * @return true if it has a time variable, else returns false
	 */
	public static boolean containsTime(String date) {
		if (date.contains(WHITESPACE)) {
			return true;
		} else {
			return false;
		}
	}
}
```
###### src\listItLogic\CommandParser.java
``` java
package listItLogic;

/**
 * This class contains methods which parses the user commands by checking if the 
 * command has white space (more than 1 word), or has no white space (1 word only). 
 * Then, it can execute the command. 
 * @version 0.5
 */
public class CommandParser {
	private static final String WHITESPACE = " ";
	
	public CommandParser() {
		
	}
	
	/**
	 * This method parses the user commands by checking if the  
     * command has white space (more than 1 word), or has no white space (1 word only).
	 * @param command string command input by the user
	 * @throws InvalidCommandException 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void processCommand(String command) {
		
		assert command != null;
		
		if (hasWhitespace(command)) {
			ExecuteCommand.processCommandWithSpace(command);
		} else {
			ExecuteCommand.processCommandWithoutSpace(command);
		}
	}

	/**
	 * Checks if the command has a white space or not.
	 * @param command string command input by the user
	 * @return true if the command string has a white space, else returns false.
	 */
	private static boolean hasWhitespace(String command) {
		return command.contains(WHITESPACE);
	}
}
	
	
```
###### src\listItLogic\DeleteLogic.java
``` java
package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

/**
 * This class contains methods which deletes a particular task from the task list.
 * @version 0.5
 */
public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static final String DELETE_VALID = "The task has been successfully"
                                                + " deleted.\n";

	/**
	 * This method finds a task from the list by getting the line index of the task,
	 * after which, it removes the task from the liat.
	 * @param command String command entered by the user with a "delete" keyword. 
	 */
	public static void deleteEvent(String command) {
		int taskIndexToBeDelete = getDeleteIndex(command);
		int sizeOfFile = 0;
		if (modifier.isViewModeComplete()) {
			sizeOfFile  = modifier.getCompleteContentList().size();
		} else {
			sizeOfFile = modifier.getContentList().size();
		}
		
		if (isValidIndex(taskIndexToBeDelete, sizeOfFile)) {
			modifier.removeTask(taskIndexToBeDelete - 1);  
			FeedbackPane.displayMessage(DELETE_VALID);
		} else {
			FeedbackPane.displayInvalidIndexToDelete();
		}
	}

	/**
	 * Checks if the index entered is valid (in the range of the task list)
	 * @param taskIndexToBeDelete line index of the task to be deleted
	 * @param sizeOfFile size of the task list to get the index range
	 * @return true if index is valid, else returns false
	 */
	private static boolean isValidIndex(int taskIndexToBeDelete, int sizeOfFile) {
		return (taskIndexToBeDelete-1) < sizeOfFile && taskIndexToBeDelete-1 >= 0;
	}

	private static int getDeleteIndex(String command) {
		return Integer.parseInt(command.substring(7));
	}
	
	public static String getMessage() {
		return message;
	}

	/**
	 * This method clears the entire data file when "clear" is entered. It does not
	 * need the line index.
	 */
	public static void clearFile() {
		modifier.clearAll(); 
	}

}
```
###### src\listItLogic\EditLogic.java
``` java
package listItLogic;

import java.util.ArrayList;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

/**
 * This class contains methods to edit the selected task. Edition can be done by
 * either editing the title, importance level, date, time, repeat type (for recursive
 * tasks), the entire block time of the task, or everything.
 * @version 0.5
 */
public class EditLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static final String WHITESPACE = " ";
	private static final String COMMAND_TITLE = "by title";
	private static final String COMMAND_IMPORTANCE = "by impt";
	private static final String COMMAND_DEADLINE = "by date";
	private static final String COMMAND_TIMELINE = "by time";
	private static final String COMMAND_TO = "to";
	private static final String COMMAND_FROM = "from";
	private static final String COMMAND_REPEAT = "by repeat";
	private static final String COMMAND_BLOCK = "cancel block";
	private static final int IMPORTANCE_LEVEL_ONE = 1; 
	private static final int IMPORTANCE_LEVEL_TWO = 2; 
	private static final int IMPORTANCE_LEVEL_THREE = 3; 
	private static final String  EDIT_IMPORTANCE_INVALID = "Invalid Importance "
			                                                + "level,there are only"
			                                                + " 3 types: 1 , 2 or 3.\n"; 
    private static final String  EDIT_DATE_INVALID= "Invalid date is input\n"; 
    private static final String  EDIT_INPUT = "Invalid input!\n"; 
    private static final String  EDIT_IMPORTANCE_VALID = "Correct type of "
    		                                              + "importance level, "
    		                                              + "sucessfully editted!"; 
   	
	private static String  message = null; 
	
	/**
	 * Edits the task event selected by the user, according to the line index the 
	 * user inputs. also determines the variable type the user wants to input so as to
	 * edit the correct variable. 
	 * Repeat type = daily, monthly or yearly.
	 * Repeat cycle = period of how long the type should occur for. After the cycle is
	 *                complete, the cycle is reset and run again.
	 * @param command String command input by the user with an "edit" keyword.
	 */
    public static void editEvent(String command) {
		int indexToBeEdit = getEditIndex(command)-1;
		
		ArrayList<Task> taskList = modifier.getContentList();

		assert indexToBeEdit >= 0;
		
		if (!isValidRange(indexToBeEdit, taskList)) {
			FeedbackPane.displayInvalidInput();
			message = EDIT_INPUT; 
			LoggingLogic.logging(message);
		} else {
			if (isEditByDate(command)) {
				String newDate = getNewDate(command);
				if (AddLogic.isValidDate(newDate)) {
					modifier.editEndDate(indexToBeEdit, newDate);
					FeedbackPane.displayValidEdit();
				} else {
					FeedbackPane.displayInvalidDate();
					message = EDIT_DATE_INVALID;
					LoggingLogic.logging(message);
				}
			} else if (isEditByTitle(command)) {
				String newTitle = getNewTitle(command);
				modifier.editTitle(indexToBeEdit, newTitle);
				FeedbackPane.displayValidEdit();
			} else if (isEditByImportance(command)) {
				int newImportance = getNewImportanceLevel(command);
				
				if (isVeryImportant(newImportance) || 
					isImportant(newImportance) || 
					isNotImportant(newImportance)){
						modifier.editImportance(indexToBeEdit, newImportance);
						message = EDIT_IMPORTANCE_VALID; 
						LoggingLogic.logging(message);
						FeedbackPane.displayValidEdit();
					} else {
						FeedbackPane.displayInvalidIndexImptLevel();
						message = EDIT_IMPORTANCE_INVALID;
						LoggingLogic.logging(message);
					}
				} else if (isEditByTimeline(command)) {
					String newStartDate = getNewStartDate(command);
					String newEndDate = getNewEndDate(command);
					modifier.editTimeline(indexToBeEdit, newStartDate, newEndDate);
					FeedbackPane.displayValidEdit();
				} else if (isEditByRepeat(command)) {
					String repeatCommand = getRepeatCommand(command);
					if (AddLogic.isCorrectRepeatCycle(repeatCommand)) {
						int newPeriod = 0;
						String repeatType = null;
						newPeriod = getNewPeriod(repeatCommand);
						repeatType = getRepeatType(repeatCommand);
						modifier.editRepeat(indexToBeEdit, newPeriod, repeatType);
						FeedbackPane.displayValidEdit();
					} else {
						FeedbackPane.displayInvalidEdit();
					}

				} else if (isEditByBlock(command)) {
					modifier.editBlock(indexToBeEdit);
					FeedbackPane.displayValidEdit();
				}
			}
		}

    
	private static String getRepeatType(String repeatCommand) {
		return repeatCommand.substring(repeatCommand.indexOf(WHITESPACE) + 1);
	}

	private static int getNewPeriod(String repeatCommand) {
		return Integer.parseInt(repeatCommand.substring(0, repeatCommand.indexOf(WHITESPACE)));
	}

	private static String getRepeatCommand(String command) {
		return command.substring(command.indexOf(COMMAND_REPEAT)
				                                 + 10);
	}

	private static boolean isNotImportant(int newImportance) {
		return newImportance == IMPORTANCE_LEVEL_THREE;
	}

	private static boolean isImportant(int newImportance) {
		return newImportance == IMPORTANCE_LEVEL_TWO;
	}

	private static boolean isVeryImportant(int newImportance) {
		return newImportance == IMPORTANCE_LEVEL_ONE;
	}

	private static boolean isValidRange(int indexToBeEdit, 
			                            ArrayList<Task> taskList) {
		return indexToBeEdit < taskList.size();
	}

	private static String getNewEndDate(String command) {
		return command.substring(command.indexOf(COMMAND_TO) + 3);
	}

	private static String getNewStartDate(String command) {
		return command.substring(command.indexOf(COMMAND_FROM) + 5,
				                 command.indexOf(COMMAND_TO) - 1);
	}

	private static int getNewImportanceLevel(String command) {
		return Integer.parseInt(command.substring(command.indexOf(COMMAND_IMPORTANCE)
				                                  + 8));

	}

	private static String getNewTitle(String command) {
		return command.substring(command.indexOf(COMMAND_TITLE) + 9);
	}

	private static String getNewDate(String command) {
		return command.substring(command.indexOf(COMMAND_DEADLINE) + 8);
	}
	
	/**
	 * Checks if the command contains a timeline input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByTimeline(String command) {
		return command.contains(COMMAND_TIMELINE);
	}

	/**
	 * Checks if the command contains a importance input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByImportance(String command) {
		return command.contains(COMMAND_IMPORTANCE);
	}

	/**
	 * Checks if the command contains a title input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByTitle(String command) {
		return command.contains(COMMAND_TITLE);
	}
	
	/**
	 * Checks if the command contains a date input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByDate(String command) {
		return command.contains(COMMAND_DEADLINE);
	}

	/**
	 * Checks if the command contains a recursive input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByRepeat(String command) {
		return command.contains(COMMAND_REPEAT);
	}

	/**
	 * Checks if the command contains a block input.
	 * @param command String command input by the user with an "edit" keyword.
	 * @return true if the command contains it, else returns false.
	 */
	private static boolean isEditByBlock(String command) {
		return command.contains(COMMAND_BLOCK);
	}

	private static int getEditIndex(String command) {
		return Integer.parseInt(command.substring(5, 6));
	}

	public static String getMessage() {
		return message;
	}
}
```
###### src\listItLogic\ExecuteCommand.java
``` java
package listItLogic;

import java.util.ArrayList;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

/**
 * This class contains methods to perform the selection logic action to take,
 * after the command is parsed (by CommandParser). After the action is confirmed,
 * the modified task list is stored to both the temporary undo and redo stacks and
 * then the logic actions are carried out.  
 * @version 0.5
 *
 */
public class ExecuteCommand {

	private static final String WHITESPACE = " ";
	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final Object EDIT_COMMAND = "edit";
	private static final String WITH_DEADLINE = "by";
	private static final String WITH_IMPT = "rank";
	private static final String WITH_TIMELINE_CONDITION1 = "from";
	private static final String WITH_TIMELINE_CONDITION2 = "to";
	private static final String UNDO_COMMAND = "undo";
	private static final String REDO_COMMAND = "redo";
	private static final String SEARCH_COMMAND = "search";
	private static final String TYPE_RECURSIVE = "repeat";
	private static final String TYPE_BLOCK = "block";
	private static final String CHANGE_DIRECTORY_COMMAND = "cd";
	private static final String WITH_DEADLINE_TYPE2 = "on";
	private static final Object COMPLETE_COMMAND = "complete";
	private static final String EXIT_COMMAND = "exit";
	private static final String HELP_COMMAND = "help";
	private static final String INVALID_REDO ="No action can be redo!\n";
	private static final String INVALID_UNDO ="Undo not available\n";
	private static final String VALID_REDO ="Redo successful!\n";
	private static final String VALID_UNDO ="Undo sucessful!\n";


	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	private static FileModifier modifier = FileModifier.getInstance();

	/**
	 * Process the commands that has white space(more than 1 word). Possible commands are
	 * add, edit, delete, complete, search, display, change directory. Also stores the
	 * task list and the completed task list to the undo and redo stacks.
	 * @param command String command entered by the user with more than 1 word
	 */
	public static void processCommandWithSpace(String command) {
		String commandType = getCommandType(command);

		if (commandType.equals(ADD_COMMAND)) {
			if (modifier.isViewModeComplete()) {
				FeedbackPane.displayInvalidAdd();
				return;
			}
			
			if (!undoRedo.isRedoEmpty()) {
				clearRedoList();
			}
			
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			assert taskList != null;
			assert taskCompleteList != null;
			saveCurrentFileToUndoList(taskList, taskCompleteList);
			
			if (command.contains(TYPE_RECURSIVE) 
				&& command.contains(WITH_TIMELINE_CONDITION1) 
				&& command.contains(WITH_TIMELINE_CONDITION2)) {
				AddLogic.addRecursiveEventTimeline(command);
			} else if (command.contains(TYPE_RECURSIVE) 
					   && (command.contains(WITH_DEADLINE_TYPE2))) {
				AddLogic.addRecursiveEventDeadline(command);
			} else if (command.contains(WITH_TIMELINE_CONDITION1)
					   && command.contains(WITH_TIMELINE_CONDITION2)) {
				AddLogic.addEventWithTimeline(command);
			} else if (command.contains(WITH_IMPT)) {
				AddLogic.addEventWithImportance(command);
			} else if (command.contains(WITH_DEADLINE)
					   || command.contains(WITH_DEADLINE_TYPE2)) {
				AddLogic.addEventWithDeadline(command);
			} else if(command.contains(TYPE_BLOCK)){
				AddLogic.addBlockEvent(command);
			} else {
				AddLogic.addEventDefault(command);
			}
		}else if (commandType.equals(DELETE_COMMAND)) {
			if (!undoRedo.isRedoEmpty()) {
				clearRedoList();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			assert taskList != null;
			assert taskCompleteList != null;
			saveCurrentFileToUndoList(taskList, taskCompleteList);
			DeleteLogic.deleteEvent(command);
		} else if (commandType.equals(EDIT_COMMAND)) {
			if (modifier.isViewModeComplete()) {
				FeedbackPane.displayInvalidEdit();
				return;
			}
			
			if (!undoRedo.isRedoEmpty()) {
				clearRedoList();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			assert taskList != null;
			assert taskCompleteList != null;
			saveCurrentFileToUndoList(taskList, taskCompleteList);

			EditLogic.editEvent(command);
		} else if (commandType.equals(SEARCH_COMMAND)) {
			SearchLogic.searchKeyWord(command);
		}else if (commandType.equals(DISPLAY_COMMAND)) {
			DisplayLogic.determineDisplayMode(command);
		} else if (commandType.equals(CHANGE_DIRECTORY_COMMAND)) {
			ChangeDirectoryLogic.changeDirectory(command);
		} else if (commandType.equals(COMPLETE_COMMAND)){
			if (modifier.isViewModeComplete()) {
				FeedbackPane.displayInvalidComplete();
				return;
			}
			if (!undoRedo.isRedoEmpty()) {
				clearRedoList();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			assert taskList != null;
			assert taskCompleteList != null;
			saveCurrentFileToUndoList(taskList, taskCompleteList);
			
			CompleteLogic.completeEvent(command); 
		} else {
			FeedbackPane.displayInvalidInput();
		}
	}

	/**
	 * Clears the redo stack
	 */
	private static void clearRedoList() {
		undoRedo.clearRedo();
		undoRedo.clearRedoComplete();
	}

	/**
	 * Saves the current list to the undo stack for both the completed task list
	 * as well as the normal task list
	 * @param taskList normal task list
	 * @param taskCompleteList completed task list
	 */
	private static void saveCurrentFileToUndoList(ArrayList<Task> taskList,
			                            ArrayList<Task> taskCompleteList) {
		undoRedo.storeListToUndo(taskList);
		undoRedo.storeListToUndoComplete(taskCompleteList);
	}

	private static String getCommandType(String command) {
		return command.substring(0, command.indexOf(WHITESPACE));
	}
	
	/**
     * Process the commands that has no white space(1 word commands). Possible commands are
	 * display, exit, help and clear. Also stores the
	 * task list and the completed task list to the undo and redo stacks.
	 * @param command String command entered by the user with 1 word only.
	 */
	public static void processCommandWithoutSpace(String command) {
		if (command.equals(DISPLAY_COMMAND)) {
			DisplayLogic.defaultDisplay();
		} else if (command.equals(EXIT_COMMAND)){
			System.exit(0);
		} else if (command.equals(HELP_COMMAND)) {
			HelpLogic.displayHelp();
		} else if (command.equals(CLEAR_COMMAND)) {
			if (!undoRedo.isRedoEmpty()) {
				clearRedoList();
			}
			
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			assert taskList != null;
			assert taskCompleteList != null;
			saveCurrentFileToUndoList(taskList, taskCompleteList);
			DeleteLogic.clearFile();
		} else if (command.equals(UNDO_COMMAND)) {
			if (undoRedo.isUndoEmpty()) {
				FeedbackPane.displayInvalidUndo();
				LoggingLogic.logging(INVALID_UNDO);
			} else {
				ArrayList<Task> previousTaskList = undoRedo.getListFromUndo();
				ArrayList<Task> previousCompleteTaskList = undoRedo.getListFromUndoComplete();
 				saveCurrentFileToRedoList(modifier.getCompleteContentList());
				updateAndSaveFile(previousTaskList, previousCompleteTaskList);
				LoggingLogic.logging(VALID_UNDO);
				FeedbackPane.displayMessage(VALID_UNDO);
			}
		} else if (command.equals(REDO_COMMAND)) { 
			if (undoRedo.isRedoEmpty()) {
				FeedbackPane.displayInvalidRedo();
				LoggingLogic.logging(INVALID_REDO);
			} else {
				ArrayList<Task> lastTaskList = undoRedo.getListFromRedo();
				ArrayList<Task> lastCompleteTaskList = undoRedo.getListFromRedoComplete();
				saveCurrentFileToUndoList(modifier.getContentList(), lastCompleteTaskList);
				updateAndSaveFile(lastTaskList, lastCompleteTaskList);
				LoggingLogic.logging(VALID_REDO);
				FeedbackPane.displayMessage(VALID_REDO);
			}
		}else if (command.equals(EXIT_COMMAND)){
			System.exit(0);
		}
		else {
			FeedbackPane.displayInvalidInput();
		}
	}

	/**
	 * Updates both task list and completed task list by saving it, then displays it.
	 * @param previousTaskList
	 * @param previousCompleteTaskList
	 */
	private static void updateAndSaveFile(ArrayList<Task> previousTaskList,
			                              ArrayList<Task> previousCompleteTaskList) {
		modifier.saveFile(previousTaskList);
		modifier.saveCompleteFile(previousCompleteTaskList);
		modifier.display();
	}

	/**
	 * Saves the current list to the redo stack for the completed task list
	 * as well as the normal task list
	 * @param taskCompleteList completed task list
	 */
	private static void saveCurrentFileToRedoList(ArrayList<Task> completedList) {
		undoRedo.storeListToRedo(modifier.getContentList());
		undoRedo.storeListToRedoComplete(completedList);
	}
}
```
###### src\listItLogic\LoggingLogic.java
``` java
package listItLogic;

/**
 * This class just performs the simple task of creating 
 * a log that prints out a message on the console. 
 * This is done for analysis purposes. 
 * 
 * @version 0.5
 */

public class LoggingLogic {
	
	public static void logging(String message){
		System.out.println(message); 
	}
}
```
###### src\listItLogic\SearchLogic.java
``` java
package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

/**
 * This class contains methods to search for the user input keyword, to find that keyword
 * in our task list. Keyword can be in the form of importance variable, date variable or 
 * event title. 
 * @version 0.5
 */
public class SearchLogic {
	private static final String SEARCH_DATE = "date";
	private static final String SEARCH_DEFAULT = "default";
	private static final String SEARCH_IMPT = "impt";
	private static final String SEARCH_ALPHA = "alpha";
	private static final int IMPORTANCE_LEVEL_ONE = 1;
	private static final int IMPORTANCE_LEVEL_TWO = 2;
	private static final int IMPORTANCE_LEVEL_THREE = 3;
	private static final String INVALID_IMPT = "Invalid Importance level, " + "there are only 3 types: "
			+ "1 , 2 or 3.\n";
	private static final String NO_SEARCH = "No content to display.\n";
	private static final String SEARCH_IMPORTANCE_VALID = "Search by importance level" + " works.\n";
	private static final String SEARCH_DEFAULT_VALID = "Default search level works.\n";
	private static final String SEARCH_ALPHA_VALID = "Alpha search level works.\n";
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static String message = "null";

	/**
	 * This method determines the type of search keyword the user has input, and
	 * checks if the user has input it in the correct format. After which, it sorts
	 * and displays the searched task list to the user. 
	 * @param command String command input by the user with a "search" keyword.
	 */
	public static void searchKeyWord(String command) {
		FileModifier modifier = FileModifier.getInstance();

		String keyword = command.substring(7);

		if (isSearchByDate(keyword)) {
			keyword = getKeyword(keyword);
			taskList = modifier.searchByDate(keyword);
			if (isTaskListEmpty(taskList)) {
				message = NO_SEARCH;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			} else {
				modifier.setViewMode(SEARCH_DEFAULT);
				sortAndDisplaySearchList(modifier, taskList);
				message = SEARCH_DEFAULT_VALID;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			}

		} else if (isSearchByImportance(keyword)) {
			keyword = getKeyword(keyword);
			int imptLevel = getImportanceLevel(keyword);

			if (isVeryImportant(imptLevel) || isImportant(imptLevel) || isNotImportant(imptLevel)) {
				taskList = modifier.searchByImportance(imptLevel);
				modifier.setViewMode(SEARCH_IMPT);
				sortAndDisplaySearchList(modifier, taskList);
				message = SEARCH_IMPORTANCE_VALID;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			} else {
				FeedbackPane.displayInvalidIndexImptLevel();
				message = INVALID_IMPT;
				taskList.clear();
				LoggingLogic.logging(message);
			}
		} else {
			taskList = modifier.searchKeyword(keyword);
			if (isTaskListEmpty(taskList)) {
				message = NO_SEARCH;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			} else {
				modifier.setViewMode(SEARCH_ALPHA);
				sortAndDisplaySearchList(modifier, taskList);
				message = SEARCH_ALPHA_VALID;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			}
		}
	}

	/**
	 * Checks if the importance level is rank 3
	 * @param imptLevel importance level
	 * @return true if importance level is rank 3
	 */
	private static boolean isNotImportant(int imptLevel) {
		return imptLevel == IMPORTANCE_LEVEL_THREE;
	}

	/**
	 * Checks if the importance level is rank 2
	 * @param imptLevel importance level
	 * @return true if importance level is rank 2
	 */
	private static boolean isImportant(int imptLevel) {
		return imptLevel == IMPORTANCE_LEVEL_TWO;
	}

	/**
	 * Checks if the importance level is rank 1
	 * @param imptLevel importance level
	 * @return true if importance level is rank 1
	 */
	private static boolean isVeryImportant(int imptLevel) {
		return imptLevel == IMPORTANCE_LEVEL_ONE;
	}

	/**
	 * Checks if the keyword in the command is "impt"
	 * @param keyword
	 * @return true if the keyword is "impt"
	 */
	private static boolean isSearchByImportance(String keyword) {
		return keyword.indexOf(SEARCH_IMPT) == 0;
	}

	/**
	 * Checks if the keyword in the command is "date"
	 * @param keyword
	 * @return true if the keyword is "date"
	 */
	private static boolean isSearchByDate(String keyword) {
		return keyword.indexOf(SEARCH_DATE) == 0;
	}

	/**
	 * Checks if the search task list is an empty task list or not.
	 * @param taskList2 the task list that has the search keyword.
	 * @return true if task list is empty, else returns false.
	 */
	private static boolean isTaskListEmpty(ArrayList<Task> taskList2) {
		if (taskList.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Sorts and displays the search task list.
	 * @param modifier the modifier variable in our FileModifier class
	 * @param taskList task list that has the search keyword
	 */
	private static void sortAndDisplaySearchList(FileModifier modifier, ArrayList<Task> taskList) {
		modifier.sort(taskList);
		modifier.display(taskList);
	}

	private static String getKeyword(String keyword) {
		return keyword.substring(5);
	}

	private static int getImportanceLevel(String keyword) {
		return Integer.parseInt(keyword);
	}

	public static ArrayList<Task> getTaskList() {
		return taskList;
	}

	public static String getMessage() {
		return message;
	}
}
```
###### src\Test\UnitTest.java
``` java
	@Test
	public void testAdd1() {
		clearExpectedActual();
		actual = modifier.getContentList();
		AddLogic.addEventDefault("add ");
		actual = modifier.getContentList();
		compareResults("test default add", expected, actual);
	}

	private void clearExpectedActual() {
		DeleteLogic.clearFile();
		expected.clear();
	}

	@Test
	public void testAdd2() {
		clearExpectedActual();
		AddLogic.addEventWithDeadline("add complete EE2020 lab report by next " + "Friday");
		actual = modifier.getContentList();
		compareResults("test adding with deadline in wrong format", expected, actual);
	}

	@Test
	public void testAdd3() {
		clearExpectedActual();
		Task task3 = new Task("go for light and sound show at the Gardens by the " + "Bay");
		expected.add(task3);
		AddLogic.addEventWithDeadline("add go for light and sound show at the " + "Gardens by the Bay");
		actual = modifier.getContentList();
		compareResults("testing adding floating task with the word by", expected, actual);
	}

	@Test
	public void testAdd4() {
		clearExpectedActual();
		AddLogic.addEventWithDeadline("add attend meeting by 152015");
		actual = modifier.getContentList();
		compareResults("testing adding with deadline with wrong date format", expected, actual);
	}

	@Test
	public void testAdd5() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add watch movie rank 0");
		actual = modifier.getContentList();
		compareResults("test adding with wrong rank range", expected, actual);
	}

	@Test
	public void testAdd6() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add go for manicure rank 4");
		actual = modifier.getContentList();
		compareResults("test adding with wrong rank range", expected, actual);
	}

	@Test
	public void testAdd7() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add do groceries rank 3");
		actual = modifier.getContentList();
		Task task4 = new Task("do groceries", 3);
		expected.add(task4);
		compareResults("test adding with right rank range", expected, actual);
	}

	@Test
	public void testAdd8() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add rank all staff based on capabilities");
		Task task5 = new Task("rank all staff based on capabilities");
		expected.add(task5);
		actual = modifier.getContentList();
		compareResults("testing input with the rank not as a command word", expected, actual);
	}

	@Test
	public void testAdd9() {
		clearExpectedActual();
		AddLogic.addBlockEvent("add attend regional youth conference block" + " 14112015 to 17112015");
		Task task6 = new Task("attend regional youth conference", "14112015", "17112015");
		task6.setBlocking(true);
		expected.add(task6);
		actual = modifier.getContentList();
		compareResults("test normal blocking", expected, actual);
	}

	@Test
	public void testAdd10() {
		clearExpectedActual();
		AddLogic.addBlockEvent("add attend Navratri festival block 19112015" + " to 10112015");
		actual = modifier.getContentList();
		compareResults("test blocking with reversed dates", expected, actual);
	}

	@Test
	public void testAdd11() {
		clearExpectedActual();
		AddLogic.addBlockEvent("add attend sports event block 112015 to 122015");
		actual = modifier.getContentList();
		compareResults("test blocking with wrong date format", expected, actual);
	}

```
