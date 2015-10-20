package taskGenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable, Comparable<Task>{
	public enum Field{
		eventTitle, deadline, importance, inputFormatter, outputFormatter;
	}
	
	private Field field;
	private String eventTitle;
	private Date deadline;
	private String start;
	private String end;
	private Integer importance;
	private SimpleDateFormat inputFormatter = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MMMMM-yyyy");

	//CONSTRUCTORS	
	public Task() {
		this.eventTitle = null;
		this.deadline = null;
		this.importance = null;
		this.start = null;
		this.end = null;
	}
	
	public Task(String eventTitle, String deadline, int importance) {
		this.eventTitle = eventTitle;
		try {
			this.deadline = inputFormatter.parse(deadline);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.importance = importance;
	}
	
	public Task(String eventTitle, int importance) {
		this.eventTitle = eventTitle;
		this.importance = importance;
	}
	
	public Task(String eventTitle, String deadline, String start, String end, int importance) {
		this.eventTitle = eventTitle;
		try {
			this.deadline = inputFormatter.parse(deadline);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.start = start;
		this.end = end;
		this.importance = importance;	
	}
	
	public Task(String eventTitle, String deadline, String start, String end) {
		try {
			this.deadline = inputFormatter.parse(deadline);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.eventTitle = eventTitle;
		this.start = start;
		this.end = end;
	}
	
	public Task(String eventTitle, String deadline) {
		this.eventTitle = eventTitle;
		try {
			this.deadline = inputFormatter.parse(deadline);
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
	
	public String getTitle() {
		return eventTitle;
	}
	
	public String getDeadline() {
		if(this.deadline != null) {
			return outputFormatter.format(deadline);
		}
		else {
			return null;
		}
	}
	
	public Integer getImportance() {
		return importance;
	}

	
	@Override
	public int compareTo(Task task) {
		int comparision = 0;
		
		switch(field){
		
		case eventTitle:
			comparision = this.eventTitle.compareTo(task.getTitle());
			return comparision;
		
		case deadline:
			comparision = this.getDeadline().compareTo(task.getDeadline());
			return comparision;
		
		case importance:
			comparision = this.importance.compareTo(task.getImportance());
			return importance;
		}
		
		return importance;
		
	}
}
