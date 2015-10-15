package taskGenerator;

public class Task {
	private String eventTitle;
	private String deadline;
	private int importance;
	
	public Task(String eventTitle, String deadline, int importance) {
		this.eventTitle = eventTitle;
		this.deadline = generateDate(deadline);
		this.importance = importance;
	}
	
	public Task(String eventTitle, int importance) {
		this.eventTitle = eventTitle;
		this.importance = importance;
	}
	
	public Task(String title, String date, boolean correctDateFormat, int importance) {
		if(correctDateFormat) {
			this.eventTitle = title;
			this.deadline = date;
			this.importance = importance;
		}
		else {
			this.eventTitle = title;
			this.deadline = generateDate(date);
			this.importance = importance;
		}
	}
	
	public Task(String eventTitle, String deadline) {
		this.eventTitle = eventTitle;
		this.deadline = generateDate(deadline);
	}

	public Task(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public Task(String title, String date, boolean correctDateFormat) {
		if(correctDateFormat) {
			this.eventTitle = title;
			this.deadline = date;
		}
		else {
			this.eventTitle = title;
			this.deadline = generateDate(date);
		}
	}

	private String generateDate(String deadline) {
		String month = deadline.substring(2, 4);
		month = ChangeToMonthInWord(month);
		
		return deadline.substring(0, 2) + "-" + month + "-" + deadline.substring(4);
	}

	private String ChangeToMonthInWord(String month) {
		int monthInt = Integer.parseInt(month);
		String monthInWord;
		
		switch(monthInt) {
			case 1:  monthInWord = "January";
			break;
			
			case 2:  monthInWord = "February";
			break;
			
			case 3:  monthInWord = "March";
			break;
        
			case 4:  monthInWord = "April";
			break;
        
			case 5:  monthInWord = "May";
			break;
        
			case 6:  monthInWord = "June";
			break;
        
			case 7:  monthInWord = "July";
			break;
        
			case 8:  monthInWord = "August";
			break;
        
			case 9:  monthInWord = "September";
			break;
        
			case 10: monthInWord = "October";
			break;
        
			case 11: monthInWord = "November";
			break;
        
			default: monthInWord = "December";
			break;
		}
		return monthInWord;
	}
	
	public String toStringWithDeadline() {
		return "-T " + this.eventTitle + " -D " + this.deadline;
	}
	
	public String toStringWithoutDate() {
		return "-T " + this.eventTitle;
	}
	
	public String toStringImportanceWithoutDate() {
		return "-T " + this.eventTitle + "-I" + this.importance;
	}
	
	public String toStringImportanceWithDate() {
		return "-T " + this.eventTitle + "-D" + this.deadline + "-I" + this.importance;
	}
	
	public String getTitle() {
		return eventTitle;
	}
	
	public String getDate() {
		return deadline;
	}
	
	public int getImportance() {
		return importance;
	}

}
