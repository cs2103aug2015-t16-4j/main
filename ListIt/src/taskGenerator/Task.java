package taskGenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable{
	private String eventTitle;
	private Date deadline;
	private Integer importance;
	private SimpleDateFormat inputFormatter = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MMMMM-yyyy");
	
	public Task() {
		this.eventTitle = null;
		this.deadline = null;
		this.importance = null;
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
	
	public String getTitle() {
		return eventTitle;
	}
	
	public String getDate() {
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
}
