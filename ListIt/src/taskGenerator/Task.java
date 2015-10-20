package taskGenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable, Comparable<Task>{
	public enum Field{
		EVENTTITLE, DATE, IMPORTANCE;
	}
	
	private Field field;
	private String eventTitle;
	private Date date;
	private String start;
	private String end;
	private Integer importance;
	private SimpleDateFormat inputFormatter = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MMMMM-yyyy");

	//CONSTRUCTORS	
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
			this.date = inputFormatter.parse(date);
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
			this.date = inputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.start = start;
		this.end = end;
		this.importance = importance;	
	}
	
	public Task(String eventTitle, String date, String start, String end) {
		try {
			this.date = inputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.eventTitle = eventTitle;
		this.start = start;
		this.end = end;
	}
	
	public Task(String eventTitle, String date) {
		this.eventTitle = eventTitle;
		try {
			this.date = inputFormatter.parse(date);
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
	
	
	//GETTERS
	public String getTitle() {
		return eventTitle;
	}
	
	public String getDate() {
		if(this.date != null) {
			return outputFormatter.format(date);
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
	
	//SETTERS
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	@Override
	public int compareTo(Task task) {
		int comparision = 0;
		
		switch(field){
		
		case EVENTTITLE:
			comparision = this.eventTitle.compareTo(task.getTitle());
			return comparision;
		
		case DATE:
			comparision =  this.getDate().compareTo(task.getDate());
			return comparision;
		
		case IMPORTANCE:
			comparision = this.importance.compareTo(task.getImportance());
			return importance;
		}
		
		return importance;
		
	}
}
