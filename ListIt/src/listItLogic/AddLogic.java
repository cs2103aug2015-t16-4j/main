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
			eventTitle = command.substring(4, command.lastIndexOf("by") - 1);
			deadline = command.substring(command.lastIndexOf("by") + 3);
		} catch (Exception e) {
			addEventDefault(command);
			return;
		}

		if (checkValidDate(deadline)) {

			Task newTask = new Task(eventTitle, deadline);

			modifier.addTask(newTask);
		} else {
			addEventDefault(command);
			return;
		}
	}

	static boolean checkValidDate(String deadline) {
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
		String eventTitle = command.substring(4);

		Task newTask = new Task(eventTitle);

		modifier.addTask(newTask);
	}

	public static void addEventWithImportance(String command) {
		String eventTitle = new String();
		if (command.contains("by")) {

			String deadline = new String();
			try {
				eventTitle = command.substring(4, command.lastIndexOf("by") - 1);
				deadline = command.substring(command.lastIndexOf("by") + 3, command.lastIndexOf("rank") - 1);
			} catch (Exception e) {
				addEventWithDeadline(command);
				return;
			}

			try {
				int rank = Integer.parseInt(command.substring(command.lastIndexOf("rank") + 5));

				if (checkValidDate(deadline)) {
					Task newTask = new Task(eventTitle, deadline, rank);
					modifier.addTask(newTask);
				} else {
					eventTitle = command.substring(4, command.lastIndexOf("rank") - 1);
					Task newTask = new Task(eventTitle, rank);
					modifier.addTask(newTask);
				}
			} catch (Exception e) {
				addEventWithDeadline(command);
				return;
			}
		} else {
			try {
				eventTitle = command.substring(4, command.lastIndexOf("rank") - 1);
				int rank = Integer.parseInt(command.substring(command.lastIndexOf("rank") + 5));
				Task newTask = new Task(eventTitle, rank);
				modifier.addTask(newTask);
			} catch (Exception e) {
				addEventDefault(command);
				return;
			}
		}
	}

	public static void addEventWithTimeline(String command) {
		String eventTitle = new String();
		String deadline = new String();
		String start = new String();

		try {
			eventTitle = command.substring(4, command.lastIndexOf("by") - 1);
			deadline = command.substring(command.lastIndexOf("by") + 3, command.lastIndexOf("from") - 1);
			start = command.substring(command.lastIndexOf("from") + 5, command.lastIndexOf("to") - 1);
		} catch (Exception e) {
			addEventWithImportance(command);
			return;
		}

		if (checkValidDate(deadline) && checkValidTime(start)) {

			if (command.contains("rank")) {
				try {
					int rank = Integer.parseInt(command.substring(command.lastIndexOf("rank") + 5));
					String end = command.substring(command.lastIndexOf("to") + 3, command.lastIndexOf("rank") - 1);
					Task newTask = new Task(eventTitle, deadline, start, end, rank);
					modifier.addTask(newTask);
				} catch (Exception e) {
					String end = command.substring(command.lastIndexOf("to") + 3);
					Task newTask = new Task(eventTitle, deadline, start, end);
					modifier.addTask(newTask);
				}

			} else {
				String end = command.substring(command.lastIndexOf("to") + 3);
				Task newTask = new Task(eventTitle, deadline, start, end);
				modifier.addTask(newTask);
			}
		} else {
			addEventWithImportance(command);
			return;
		}
	}

	private static boolean checkValidTime(String start) {
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
