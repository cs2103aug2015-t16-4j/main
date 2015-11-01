package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import fileModifier.FileModifier;
import listItLogic.AddLogic;
import listItLogic.DeleteLogic;
import listItLogic.EditLogic;
import listItLogic.SearchLogic;
import taskGenerator.Task;

public class UnitTest {
	
	FileModifier modifier = FileModifier.getInstance();

	@Test
	public void test() {
		ArrayList<Task> expected = new ArrayList<Task>();
		String addMessage = null;
		String deleteMessage = null;
		String addDeadlineMessage = null;
		
		DeleteLogic.clearFile();
		testDeleteLogicClear(expected, "test clear");
		
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		expected.add(task1);
		AddLogic.addEventWithDeadline("add Complete EE2020 oscilloscope project by 03112015");
		testAddWithDeadlineLogic(modifier.getContentList(), expected, 
				                 "test add with deadline logic");
		
		Task task2 = new Task("OP2 presentation", "06112015");
		expected.add(task2);
		AddLogic.addEventWithDeadline("add Oral Presentation 2 of Software Demo by 06112015");
		
		DeleteLogic.deleteEvent("delete 3");
		deleteMessage = DeleteLogic.getMessage();
	    testDeleteLogicDeleteEvent("Index is out of bounds", deleteMessage, expected, 
				                   "test delete 3");
		DeleteLogic.deleteEvent("delete 0");
		deleteMessage = DeleteLogic.getMessage();
		testDeleteLogicDeleteEvent("Index is out of bounds", deleteMessage, expected, 
				                   "test delete 0");
		
		DeleteLogic.deleteEvent("delete 2");
		deleteMessage = DeleteLogic.getMessage();
		testDeleteLogicDeleteEvent("null", deleteMessage, expected, "test delete 2");
		
		
		AddLogic.addEventDefault("add "); 
		addMessage = AddLogic.getMessage();
		testAddDefaultLogic("test default add", expected, "Please enter an event title", addMessage);
		
		AddLogic.addEventWithDeadline("add complete EE2020 lab report by next Friday");
		addDeadlineMessage = AddLogic.getDeadlineMessage();
		testAddDeadlineLogic("test adding with deadline in wrong format", expected, 
				             addDeadlineMessage, "Please enter a valid date! Days of the week are not accepted");
		
		AddLogic.addEventWithDeadline("add go for light and sound show at the Gardens by the Bay");
		Task task3 = new Task("go for light and sound show a the Gardens by the Bay");
		expected.add(task3);
		addDeadlineMessage = AddLogic.getDeadlineMessage();
		testAddDeadlineLogic("testing adding floating task with the word by", expected, 
				             addDeadlineMessage, null);
		
		AddLogic.addEventWithDeadline("add attend meeting by 152015");
		addDeadlineMessage = AddLogic.getDeadlineMessage();
		testAddDeadlineLogic("testing adding with deadline with wrong date format", expected,
				             addDeadlineMessage, "enter a valid date");
		
		SearchLogic.searchKeyWord("search date 03112015");
		testSearchLogicDate("test search by date" , expected); 
		
		//testing a date not present 
		SearchLogic.searchKeyWord("search data 05112015"); 
		testSearchLogicDate("test search date which is not present" , expected); 
		
	   //test for importance ( 1 & 4) 
		SearchLogic.searchKeyWord("search impt 3");
		
		SearchLogic.searchKeyWord("search impt 4");

	   //test key word present and not present . 
		SearchLogic.searchKeyWord("search Oral Presentation 2"); 
		
		SearchLogic.searchKeyWord("search tutorial work"); 
		testSort(expected,modifier.getContentList() ); 
	}
	
	private void testAddDeadlineLogic(String description, ArrayList<Task> expected, String actualMessage,
			                          String expectedMessage) {
		if(actualMessage != null) {
			assertEquals(description, expectedMessage, actualMessage);
		} else {
			ArrayList<Task> actual = modifier.getContentList();
			assertEquals(description, expected, actual);
		}
	}

	private void testSort(ArrayList<Task> expected ,ArrayList<Task> actual ) {
		String description = "test if sort works"; 		
		assertEquals(description , expected , actual); 		
	}

	private void testSearchLogicDate(String description, ArrayList<Task> expected ) {
		 // i need to get the searchList . 	
	}

	private void testAddDefaultLogic(String description, ArrayList<Task> expected, String expectedMessage, String actualMessage) {
		if(actualMessage == null) {
			ArrayList<Task> actual = modifier.getContentList();
			assertEquals(description, expected, actual);
		} else {
			assertEquals(description, expectedMessage, actualMessage);
		}
	}
    
	private void testDeleteLogicClear(ArrayList<Task> expected, String description) {
		ArrayList<Task> actual = modifier.getContentList();
		assertEquals(description, expected.size(), actual.size());
	}
	
	private void testDeleteLogicDeleteEvent(String expectedMessage, String actualMessage,  
			                                ArrayList<Task> expected, String description) {
		if(actualMessage != null) {
			assertEquals(description, expectedMessage, actualMessage);
		} else {
			ArrayList<Task> actual = modifier.getContentList();
			int indexToDelete = Integer.parseInt(description.substring(11));
			expected.remove(indexToDelete-1);
			assertEquals(description, expected, actual);
		}
	}
	
	private void testAddWithDeadlineLogic(ArrayList<Task> actual, ArrayList<Task> expected, 
			                              String description) {
	    assertEquals(description, expected, actual);	
	}
}
