// @@author Shawn A0124181R
package taskGenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class generates the data input by the user and turns it into a task object.
 * This task object contains various information such as the event title,
 * event date and the importance level. 
 * @version 0.5
 */
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2848287967383183184L;
	private String eventTitle;
	private Date startDate;
	private Date endDate;
	private boolean hasTime = false;
	private boolean isRepeat = false;
	private boolean isOverDate = false;
	private boolean isComplete = false;
	private boolean blocking = false;
	private Integer importance;
	private Integer index;
	private SimpleDateFormat dateTimeInputFormatter = new SimpleDateFormat("ddMMyyyy HHmm", Locale.US);
	private SimpleDateFormat dateInputFormatter = new SimpleDateFormat("ddMMyyyy", Locale.US);
	private SimpleDateFormat dateTimeOutputFormatter = new SimpleDateFormat("dd-MMMMM-yyyy HH:mm", Locale.US);
	private SimpleDateFormat dateOutputFormatter = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);
	private String repeatType;
	private int repeatCycle;

	// CONSTRUCTORS
	public Task() {
		this.eventTitle = null;
		this.importance = null;
		this.hasTime = false;
		this.isRepeat = false;
		this.repeatType = null;
		this.repeatCycle = 0;
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

	public Task(String eventTitle, String date, int importance, boolean hasTime) {
		this.eventTitle = eventTitle;
		try {
			this.endDate = dateTimeInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.hasTime = hasTime;
		this.importance = importance;
	}

	public Task(String eventTitle, int importance) {
		this.eventTitle = eventTitle;
		this.importance = importance;
	}

	public Task(String eventTitle, String start, String end, int importance) {
		this.eventTitle = eventTitle;
		try {
			this.startDate = dateInputFormatter.parse(start);
			this.endDate = dateInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.importance = importance;
	}

	public Task(String eventTitle, String start, String end, int importance, boolean hasTime) {
		this.eventTitle = eventTitle;
		try {
			this.startDate = dateTimeInputFormatter.parse(start);
			this.endDate = dateTimeInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.importance = importance;
		this.hasTime = hasTime;
	}

	public Task(String eventTitle, String start, String end) {
		this.eventTitle = eventTitle;
		this.importance = 3;
		try {
			this.startDate = dateInputFormatter.parse(start);
			this.endDate = dateInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Task(String eventTitle, String start, String end, boolean hasTime) {
		this.eventTitle = eventTitle;
		this.importance = 3;
		try {
			this.startDate = dateTimeInputFormatter.parse(start);
			this.endDate = dateTimeInputFormatter.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.hasTime = hasTime;
	}

	public Task(String eventTitle, String date) {
		this.eventTitle = eventTitle;
		this.importance = 3;
		try {
			this.endDate = dateInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Task(String eventTitle, String date, boolean hasTime) {
		this.eventTitle = eventTitle;
		this.importance = 3;
		try {
			this.endDate = dateTimeInputFormatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.hasTime = hasTime;
	}

	public Task(String eventTitle) {
		this.eventTitle = eventTitle;
		this.importance = 3;
	}

	public Task(String eventTitle, String repeatType, int repeatCycle, String deadline, boolean isRepeat) {
		this.eventTitle = eventTitle;
		try {
			this.endDate = dateInputFormatter.parse(deadline);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.isRepeat = isRepeat;
		this.repeatType = repeatType;
		this.repeatCycle = repeatCycle;
		this.importance = 3;
	}

	public Task(String eventTitle, String repeatType, int repeatCycle, String deadline, boolean isRepeat,
			boolean hasTime) {
		this.eventTitle = eventTitle;
		try {
			this.endDate = dateTimeInputFormatter.parse(deadline);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.isRepeat = isRepeat;
		this.hasTime = hasTime;
		this.repeatType = repeatType;
		this.repeatCycle = repeatCycle;
		this.importance = 3;
	}

	public Task(String eventTitle, String repeatType, int repeatCycle, String startDate, String endDate,
			boolean isRepeat, boolean hasTime) {
		this.eventTitle = eventTitle;
		try {
			this.startDate = dateTimeInputFormatter.parse(startDate);
			this.endDate = dateTimeInputFormatter.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.isRepeat = isRepeat;
		this.hasTime = hasTime;
		this.repeatType = repeatType;
		this.repeatCycle = repeatCycle;
		this.importance = 3;
	}

	public Task(String eventTitle, String repeatType, int repeatCycle, String startDate, String endDate,
			boolean isRepeat) {
		this.eventTitle = eventTitle;
		try {
			this.startDate = dateInputFormatter.parse(startDate);
			this.endDate = dateInputFormatter.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.isRepeat = isRepeat;
		this.repeatType = repeatType;
		this.repeatCycle = repeatCycle;
		this.importance = 3;
	}

	// GETTERS
	public boolean getRepeat() {
		return this.isRepeat;
	}

	public boolean getHasTime() {
		return this.hasTime;
	}

	public int getRepeatCycle() {
		return this.repeatCycle;
	}

	public String getRepeatType() {
		return this.repeatType;
	}

	public String getTitle() {
		return eventTitle;
	}

	public String getStartDate() {
		if (!isStartDateNull() && hasTime) {
			return dateTimeOutputFormatter.format(startDate);
		} else if (!isStartDateNull()) {
			return dateOutputFormatter.format(startDate);
		} else {
			return null;
		}
	}

	private boolean isStartDateNull() {
		return this.startDate == null;
	}

	public String getEndDate() {
		if (!isEndDateNull() && hasTime) {
			return dateTimeOutputFormatter.format(endDate);
		} else if (!isEndDateNull()) {
			return dateOutputFormatter.format(endDate);
		} else {
			return null;
		}
	}

	private boolean isEndDateNull() {
		return this.endDate == null;
	}

	public String getEndDateWithoutTime() {
		return dateOutputFormatter.format(endDate);
	}

	public String getDateInputForm() {
		return dateInputFormatter.format(endDate);
	}

	public Date getDateInDate() {
		return endDate;
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

	public Date getEndDateInDateType() {
		return this.endDate;
	}

	public Date getStartDateInDateType() {
		return this.startDate;
	}

	public boolean isOverDate() {
		return this.isOverDate;
	}

	public boolean isComplete() {
		return this.isComplete;
	}

	public boolean isBlocking() {
		return blocking;
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
			this.startDate = dateInputFormatter.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setStartDateWithTime(String start) {
		try {
			this.startDate = dateTimeInputFormatter.parse(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setEndDate(String end) {
		try {
			this.endDate = dateInputFormatter.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setEndDateWithTime(String endDate) {
		try {
			this.endDate = dateTimeInputFormatter.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setHasTime(boolean set) {
		this.hasTime = set;
	}

	public void setRepeatCycle(int cycle) {
		this.repeatCycle = cycle;
	}

	public void setRepeatType(String type) {
		this.repeatType = type;
	}

	public void setEndDateInDate(Date nextDeadline) {
		this.endDate = nextDeadline;
	}

	public void setStartDateInDate(Date nextStartDate) {
		this.startDate = nextStartDate;
	}

	public void setOverDate() {
		this.isOverDate = true;
	}

	public void setNotOverDate() {
		this.isOverDate = false;
	}

	public void setComplete() {
		this.isComplete = true;
	}

	public void setBlocking(boolean set) {
		this.blocking = set;
	}

	/**
	 * Checks if the 1 variable in the task object is equal to the same
	 * variable in another task object. Variables can be in the form of
	 * the date, time, title, importance.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		if (obj instanceof Task) {
			Task task = (Task) obj;

			if (this.startDate == null && task.startDate != null) {
				return false;
			}

			if (this.startDate != null && task.startDate == null) {
				return false;
			}

			if (this.startDate != null && task.startDate != null) {
				if (!this.startDate.equals(task.startDate)) {
					return false;
				}
			}

			if (this.endDate == null && task.endDate != null) {
				return false;
			}

			if (this.endDate != null && task.endDate == null) {
				return false;
			}

			if (this.endDate != null && task.endDate != null) {
				if (!this.endDate.equals(task.endDate)) {
					return false;
				}
			}

			return this.eventTitle.equals(task.eventTitle) && this.hasTime == task.hasTime
					&& this.isComplete == task.isComplete && this.isRepeat == task.isRepeat
					&& this.blocking == task.blocking && this.importance.equals(task.importance);
		} else {
			return false;
		}
	}
}