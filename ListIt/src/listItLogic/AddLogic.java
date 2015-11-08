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
	private static final String MESSAGE_INVALID_RANGE = "start date should be "
			                                            + "earlier than end date";
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
		if (deadline.contains(DAY_MONDAY) || deadline.contains(DAY_TUESDAY) 
			|| deadline.contains(DAY_WEDNESDAY) || deadline.contains(DAY_THURSDAY) 
			|| deadline.contains(DAY_FRIDAY) || deadline.contains(DAY_SATURDAY)
			|| deadline.contains(DAY_SUNDAY) || deadline.contains(DAY_TOMORROW) 
			|| deadline.contains(DAY_WEEK)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getDeadlineMessage() {
		return addDeadlineMessage;
	}

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

	private static boolean hasBothOnAndBy(String command) {
		return command.contains(COMMAND_BY) && command.contains(COMMAND_ON);
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
		if (eventTitle.equals(STRING_NULL)) {
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

	public static String getDefaultMessage() {
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
				if (isValidRank(command)) {
					int rank = getRankValue(command);
					if (isValidDate(deadline)) {
						Task newTask;
						if (containsTime(deadline)) {
							newTask = createTaskWithDeadlineRankAndTime(eventTitle,
									                                    deadline, 
									                                    rank);
						} else {
							newTask = createTaskWithDeadlineAndRank(eventTitle, 
									                                deadline,
									                                rank);
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
				if (isValidRank(command)) {
					int rank = getRankValue(command);
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

	private static Task createTaskWithDeadlineAndRank(String eventTitle, 
			                                          String deadline,
			                                          int rank) {
		return new Task(eventTitle, deadline, rank);
	}

	private static Task createTaskWithDeadlineRankAndTime(String eventTitle,
			                                              String deadline,
			                                              int rank) {
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
		return command.substring(command.lastIndexOf(COMMAND_BY) + 3, 
				                 command.lastIndexOf(COMMAND_RANK) - 1);
	}

	private static boolean isEventWithDeadline(String command) {
		return command.contains(COMMAND_BY);
	}

	public static void addEventWithTimeline(String command) {
		String eventTitle = new String();
		String startDate = new String();
		String endDate = new String();
		boolean isValidOnCommand = true;

		if (command.contains(COMMAND_ON)) {
			try {
				eventTitle = getEventTitleBeforeOn(command);
				startDate = getSingleDateStartTime(command);
				endDate = getSingleDateEndTime(command);
			} catch (Exception e) {
				isValidOnCommand = false;
			}
			if (isValidDate(startDate) && isValidDate(endDate) 
				&& isValidOnCommand == true) {
				if (isValidRank(command)) {
					addTaskWithTimelineAndRank(command, eventTitle, startDate,
							                   endDate);
				} else if (isRankNonCommand(command)) {
					addTaskWithTimelineAndNoRank(command, eventTitle, startDate,
							                     endDate);
				} else {
					addRankMessage = MESSAGE_INVALID_RANK;
					FeedbackPane.displayInvalidInput();
				}
			}
		}

		if (!command.contains(COMMAND_ON) || !isValidOnCommand) {
			eventTitle = getEventTitleTimeline(command);
			startDate = getStartDate(command);
			if (isValidDate(startDate)) {
				if (isCorrectRange(startDate, endDate)) {
					if (isEventWithImportance(command)) {
						if (isValidRank(command)) {
							endDate = getEndDateImportance(command);
							addTaskWithTimelineAndRank(command, eventTitle,
									                   startDate, endDate);
						} else if (isRankNonCommand(command)) {
							endDate = getEndDateTimeline(command);
							addTaskWithTimelineAndNoRank(command, eventTitle,
									                     startDate, endDate);
						} else {
							addRankMessage = MESSAGE_INVALID_RANK;
							FeedbackPane.displayInvalidInput();
						}
					} else {
						endDate = getEndDateTimeline(command);
						addTaskWithTimelineAndNoRank(command, eventTitle,
								                     startDate, endDate);
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

	private static String getEventTitleBeforeOn(String command) {
		return command.substring(4, command.lastIndexOf(COMMAND_ON) - 1);
	}

	private static void addTaskWithTimelineAndNoRank(String command, 
			                                         String eventTitle,
			                                         String startDate,
			                                         String endDate) {
		Task newTask;
		if (containsTime(endDate)) {
			newTask = createBlockedTaskWithTime(eventTitle, startDate, endDate);
		} else {
			newTask = createBlockedTaskWithNoTime(eventTitle, startDate, endDate);
		}
		modifier.addTask(newTask);
	}

	private static void addTaskWithTimelineAndRank(String command, 
			                                       String eventTitle, 
			                                       String startDate,
			                                       String endDate) {
		int rank = getRankValue(command);
		Task newTask;
		if (containsTime(endDate)) {
			newTask = new Task(eventTitle, startDate, endDate, rank, true);
		} else {
			newTask = new Task(eventTitle, startDate, endDate, rank);
		}
		modifier.addTask(newTask);
	}

	private static String getSingleDateEndTime(String command) {
		return command.substring(command.lastIndexOf(COMMAND_ON) + 3, 
			   command.lastIndexOf(COMMAND_START_TIME) - 1) + " "
			   + command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3);
	}

	private static String getSingleDateStartTime(String command) {
		return command.substring(command.lastIndexOf(COMMAND_ON) + 3, 
			   command.lastIndexOf(COMMAND_START_TIME) - 1) + " "
				+ command.substring(command.lastIndexOf(COMMAND_START_TIME) + 5,
				command.lastIndexOf(COMMAND_END_TIME) - 1);
	}

	private static boolean isRankNonCommand(String command) {
		try {
			int rank = Integer.parseInt(command.substring(
					                    command.lastIndexOf(COMMAND_RANK) + 5));
			return false;
		} catch (NumberFormatException e) {
			return true;
		}
	}

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
		return command.substring(command.lastIndexOf(COMMAND_END_TIME) + 3,
				                 command.lastIndexOf(COMMAND_RANK) - 1);
	}

	private static boolean isEventWithImportance(String command) {
		return command.contains(COMMAND_RANK);
	}

	public static void addRecursiveEventDeadline(String command) {
		String deadline = null;
		String repeatType = null;
		int repeatCycle = 0;
		String eventTitle = command.substring(4, 
				                              command.lastIndexOf(COMMAND_REPEAT) 
				                              - 1);
		String repeatCommand = command.substring(command.lastIndexOf(COMMAND_REPEAT)
				                                 + 7, command.lastIndexOf(COMMAND_ON)
				                                 - 1);
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
					Task newTask = createRecurringTaskWithDeadlineAndTime(deadline,
							                                              repeatType,
							                                              repeatCycle,
							                                              eventTitle);
					modifier.addTask(newTask);
				} else {
					Task newTask = createRecurringTaskWithDeadline(deadline,
							                                       repeatType,
							                                       repeatCycle,
							                                       eventTitle);
					modifier.addTask(newTask);
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

	private static Task createRecurringTaskWithDeadline(String deadline,
			                                            String repeatType,
			                                            int repeatCycle,
			                                            String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, deadline, true);
	}

	private static Task createRecurringTaskWithDeadlineAndTime(String deadline,
			                                                   String repeatType,
			                                                   int repeatCycle, 
			                                                   String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, deadline, true, true);
	}

	private static boolean isDeadlineEmpty(String deadline) {
		return deadline.equals(EMPTY_STRING);
	}

	private static boolean hasRepeatCycle(int repeatCycle) {
		return repeatCycle != VALUE_NO_REPEAT_CYCLE;
	}

	public static String getRecurMessage() {
		return addRecurMessage;
	}

	private static int parseRepeatAmount(String repeatCycle) {
		try {
			int cycle = Integer.parseInt(repeatCycle.substring(0, 
					                     repeatCycle.indexOf(WHITESPACE)));
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

		if (repeatCycle.contains(REPEAT_DAILY) 
			|| repeatCycle.contains(REPEAT_MONTHLY)
			|| repeatCycle.contains(REPEAT_YEARLY) 
			|| repeatCycle.contains(REPEAT_WEEKLY)) {
			isCorrect = true;

		}
		return isCorrect;
	}

	public static void addRecursiveEventTimeline(String command) {
		String startDate = null;
		String endDate = null;
		String repeatType = null;
		int repeatCycle = 0;
		String eventTitle = command.substring(4, command.lastIndexOf(COMMAND_REPEAT)
				                                                     - 1);
		String repeatCommand = command.substring(command.lastIndexOf(COMMAND_REPEAT)
				                                                     + 7,
				                                 command.lastIndexOf(COMMAND_START_TIME)
				                                                     - 1);
		if (isCorrectRepeatCycle(repeatCommand)) {
			repeatType = parseRepeatType(repeatCommand);
			repeatCycle = parseRepeatAmount(repeatCommand);
			try {
				startDate = command.substring(command.lastIndexOf(COMMAND_START_TIME)
						                                          + 5,
						                                          command.lastIndexOf(COMMAND_END_TIME)
						                                          - 1);
				endDate = command.substring(command.lastIndexOf(COMMAND_END_TIME)
						                                        + 3);
			} catch (Exception e) {
				startDate = EMPTY_STRING;
				endDate = EMPTY_STRING;
			}
			if (isValidDate(startDate) && hasRepeatCycle(repeatCycle)) {
				if (containsTime(startDate)) {
					Task newTask = createRecurringTaskWithTimeline(startDate,
							                                       endDate,
							                                       repeatType,
							                                       repeatCycle,
							                                       eventTitle);
					modifier.addTask(newTask);
				} else {
					Task newTask = createRecurringTaskWithTimelineAndNoTime(startDate,
							                                                endDate,
							                                                repeatType,
							                                                repeatCycle,
							                                                eventTitle);
					modifier.addTask(newTask);
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

	private static Task createRecurringTaskWithTimelineAndNoTime(String startDate,
			                                                     String endDate,
			                                                     String repeatType,
			                                                     int repeatCycle,
			                                                     String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, startDate,
				        endDate, true);
	}

	private static Task createRecurringTaskWithTimeline(String startDate, 
			                                            String endDate,
			                                            String repeatType,
			                                            int repeatCycle,
			                                            String eventTitle) {
		return new Task(eventTitle, repeatType, repeatCycle, startDate, endDate,
				        true, true);
	}

	public static void addBlockEvent(String command) {
		Task newTask = new Task();
		String eventTitle = getEventTitleBlock(command);
		String start = getBlockEventStartDate(command);
		String end = getEndTime(command);
		if (isValidDate(end) && isValidDate(start)) {
			if (isCorrectRange(start, end)) {
				if (containsTime(end)) {
					newTask = createBlockedTaskWithTime(eventTitle, start, end);
				} else {
					newTask = createBlockedTaskWithNoTime(eventTitle, start, end);
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
			FeedbackPane.displayInvalidAdd();
		}
	}

	private static Task createBlockedTaskWithNoTime(String eventTitle,
			                                        String start, String end) {
		return new Task(eventTitle, start, end);
	}

	private static Task createBlockedTaskWithTime(String eventTitle,
			                                      String start, String end) {
		return new Task(eventTitle, start, end, true);
	}

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

	private static boolean isStartDateBeforeEndDate(Date startDate, Date endDate) {
		return startDate.compareTo(endDate) == -1;
	}

	public static String getBlockMessage() {
		return addBlockMessage;
	}

	private static String getBlockEventStartDate(String command) {
		return command.substring(command.lastIndexOf(COMMAND_BLOCK) + 6,
				                 command.lastIndexOf(COMMAND_END_TIME) - 1);
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
