package taskGenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable, Comparable<Task> {
	public enum Field {
		EVENTTITLE, DATE, IMPORTANCE;
	}

	private Field field;
	private String eventTitle;
	private Date date;
	private Date start;
	private Date end;
	private Integer importance;
	private SimpleDateFormat dateInputFormatter = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat dateOutputFormatter = new SimpleDateFormat("dd-MMMMM-yyyy");
	private SimpleDateFormat timeInputFormatter = new SimpleDateFormat("HHmm");
	private SimpleDateFormat timeOutputFormatter = new SimpleDateFormat("HH:mm");

	// CONSTRUCTORS
	public Task() {
		this.eventTitle = null;
		this.date = null;
		this.importance = null;
		this.start = null;
		this.end = null;
	}

	public Task(String eventTitle, String date, int importance) {
		this.eventTitle = eventTitle;
		try {
			this.date = dateInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.importance = importance;
	}

	public Task(String eventTitle, int importance) {
		this.eventTitle = eventTitle;
		this.importance = importance;
	}

	public Task(String eventTitle, String date, String start, String end, int importance) {
		this.eventTitle = eventTitle;
		try {
			this.date = dateInputFormatter.parse(date);
			this.start = timeInputFormatter.parse(start);
			this.end = timeInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.importance = importance;
	}

	public Task(String eventTitle, String date, String start, String end) {
		this.eventTitle = eventTitle;
		try {
			this.date = dateInputFormatter.parse(date);
			this.start = timeInputFormatter.parse(start);
			this.end = timeInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Task(String eventTitle, String date) {
		this.eventTitle = eventTitle;
		try {
			this.date = dateInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.importance = 3;
	}

	public Task(String eventTitle) {
		this.eventTitle = eventTitle;
		this.importance = 3;
	}

	public Task(Field field) {
		this.field = field;
	}

	// GETTERS
	public String getTitle() {
		return eventTitle;
	}

	public String getDate() {
		if (this.date != null) {
			return dateOutputFormatter.format(date);
		} 
		else {
			return null;
		}
	}

	public String getStartTime() {
		if(this.start != null) {
			return timeOutputFormatter.format(start);
		}
		else {
			return null;
		}
	}
	
	public String getEndTime() {
		if(this.end != null) {
			return timeOutputFormatter.format(end);
		}
		else {
			return null;
		}
	}

	public Integer getImportance() {
		return importance;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	// SETTERS
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public void setDate(String date) {
		try {
			this.date = dateInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}
	
	public void setStart(String start) {
		try {
			this.start = timeInputFormatter.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setEnd(String end) {
		try {
			this.end = timeInputFormatter.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(Task task) {
		int comparision = 0;

		switch (field) {

		case EVENTTITLE:
			comparision = this.eventTitle.compareTo(task.getTitle());
			return comparision;

		case DATE:
			comparision = this.getDate().compareTo(task.getDate());
			return comparision;

		case IMPORTANCE:
			comparision = this.importance.compareTo(task.getImportance());
			return importance;
		}

		return importance;

	}
}
