package taskGenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.Node;

public class Task implements Serializable {

	private String eventTitle;
	private Date startDate;
	private Date endDate;
	private boolean isRepeat;
	private Integer importance;
	private Integer index;
	private SimpleDateFormat dateTimeInputFormatter = new SimpleDateFormat("ddMMyyyy HHmm");
	private SimpleDateFormat dateInputFormatter = new SimpleDateFormat("ddMMyyyy");
	private SimpleDateFormat dateOutputFormatter = new SimpleDateFormat("dd-MMMMM-yyyy");
	private SimpleDateFormat timeOutputFormatter = new SimpleDateFormat("HH:mm");
	private String repeatCycle;
	private String repeatday;
	private String exception;

	// CONSTRUCTORS
	public Task() {
		this.eventTitle = null;
		this.importance = null;
		this.isRepeat = false;
		this.repeatday = null;
		this.exception = null;
		this.startDate = null;
		this.endDate = null;
	}

	public Task(String eventTitle, String date, int importance) {
		this.eventTitle = eventTitle;
		try {
			this.endDate = dateInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.importance = importance;
	}

	public Task(String eventTitle, int importance) {
		this.eventTitle = eventTitle;
		this.importance = importance;
	}

	public Task(String eventTitle, String start, String end, int importance) {
		this.eventTitle = eventTitle;
		try {
			this.startDate = dateTimeInputFormatter.parse(start);
			this.endDate = dateTimeInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.importance = importance;
	}

	public Task(String eventTitle, String start, String end) {
		this.eventTitle = eventTitle;
		this.importance = 3;
		try {
			this.startDate = dateTimeInputFormatter.parse(start);
			this.endDate = dateTimeInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Task(String eventTitle, String date) {
		this.eventTitle = eventTitle;
		this.importance = 3;
		try {
			this.endDate = dateInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.importance = 3;
	}
	
	public Task(String eventTitle, String repeatCycle, String repeatday, String exception, boolean isRepeat) {
		this.eventTitle = eventTitle;
		this.repeatCycle = repeatCycle;
		this.isRepeat = isRepeat;
		this.repeatday = repeatday;
		this.exception = exception;
	}
	
	public Task(String eventTitle) {
		this.eventTitle = eventTitle;
		this.importance = 3;
	}

	// GETTERS
	
	public boolean getRepeat() {
		return this.isRepeat;
	}
	
	public String getRepeatDay() {
		return this.repeatday;
	}
	
	public String getException() {
		return this.exception;
	}
	
	public String getRepeatCycle() {
		return this.repeatCycle;
	}
	
	public String getTitle() {
		return eventTitle;
	}

	public String getStartDate() {
		if (this.startDate != null) {
			return dateOutputFormatter.format(startDate);
		} else {
			return null;
		}
	}
	
	public String getEndDate() {
		if (this.endDate != null) {
			return dateOutputFormatter.format(endDate);
		} else {
			return null;
		}
	}
	
	public String getDateInputForm() {
		return dateInputFormatter.format(endDate);
	}
	
	public Date getDateInDate() {
		return endDate;
	}

	public String getStartTime() {
		if (this.startDate != null) {
			return timeOutputFormatter.format(startDate);
		} else {
			return null;
		}
	}
	
	public String getEndTime() {
		if (this.endDate != null) {
			return timeOutputFormatter.format(endDate);
		} else {
			return null;
		}
	}

	public Integer getImportance() {
		return importance;
	}

	public String getEventTitle() {
		return eventTitle;
	}
	
	public Integer getIndex() {
		return index;
	}

	// SETTERS
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}
	
	public void setStartDate(String start) {
		try {
			this.startDate = dateTimeInputFormatter.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setEndDate(String end) {
		try {
			this.endDate = dateTimeInputFormatter.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
}