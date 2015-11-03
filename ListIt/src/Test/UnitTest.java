package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import fileModifier.FileModifier;
import javafx.application.Application;
import javafx.stage.Stage;
import listItLogic.AddLogic;
import listItLogic.DeleteLogic;
import listItLogic.EditLogic;
import listItLogic.SearchLogic;
import listItUI.*;
import taskGenerator.Task;

public class UnitTest {
	FileModifier modifier = FileModifier.getInstance();

	@BeforeClass
	public static void setUpApplication() throws InterruptedException {
		// Initialize Java FX

		System.out.printf("About to launch FX App ListIt\n");
		Thread t = new Thread("JavaFX Init Thread") {
			public void run() {
				Application.launch(UIMain.class, new String[0]);
			}
		};
		t.setDaemon(true);
		t.start();
		System.out.printf("FX App ListIt thread started\n");
		Thread.sleep(600);
	}



	@Test
	public void test() {

		ArrayList<Task> expected = new ArrayList<Task>();
		ArrayList<Task> expectedSearchList = new ArrayList<Task>();
		ArrayList<Task> actualSearchList = new ArrayList<Task>();
		String addMessage = null;
		String deleteMessage = null;
		String addDeadlineMessage = null;
		String addRankMessage = null;
		String searchMessage = null;

		DeleteLogic.clearFile();
		testDeleteLogicClear(expected, "test clear"); 

		Task task1 = new Task("EE2020 Oscilloscope project", "03112015", 1);
		AddLogic.addEventWithDeadline("add EE2020 Oscilloscope project by 03112015 rank 1");
		expected.add(task1); 
		Task task2 = new Task("OP2 presentation", "06112015", 3);
		AddLogic.addEventWithDeadline("add 0P2 presentation by 06112015 rank 3");
		expected.add(task2); 

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

		AddLogic.addEventWithImportance("add watch movie rank 0");
		addRankMessage = AddLogic.getRankMessage();
		testAddRankLogic("test adding with wrong rank range", expected, addRankMessage, "invalid rank input");

		AddLogic.addEventWithImportance("add go for manicure rank 4");
		addRankMessage = AddLogic.getRankMessage();
		testAddRankLogic("test adding with wrong rank range", expected, addRankMessage,
				"invalid rank input");

		AddLogic.addEventWithImportance("add do groceries rank 3");
		addRankMessage = AddLogic.getRankMessage();
		Task task4 = new Task("do groceries", 3);
		expected.add(task4);
		testAddRankLogic("test adding with right rank range", expected, addRankMessage,
				"invalid rank input");

		//test key word present and not present . 
		SearchLogic.searchKeyWord("search Oral Presentation 2");
		actualSearchList = SearchLogic.getTaskList();
		testSearchLogicValid("test search by title" , expectedSearchList, actualSearchList); 

		SearchLogic.searchKeyWord("search tutorial work"); 
		searchMessage=SearchLogic.getMessage(); 
		String expectedMessage = "No content to display"; 
		testSearchLogicInvalid("test search by title , invalid input" , expectedMessage , searchMessage);

		//test for importance ( 1 & 4)  
		SearchLogic.searchKeyWord("search impt 3");
		actualSearchList = SearchLogic.getTaskList();	
		expectedSearchList.add(task2); 
		testSearchLogicValid("test search by impt" , expectedSearchList, actualSearchList); 

		SearchLogic.searchKeyWord("search impt 4");
		searchMessage=SearchLogic.getMessage();; 
		expectedMessage = "Invalid Importance level , there are only 3 types : 1 ,2 3.\n"; 
		testSearchLogicInvalid("test search by impt, ranking 4 which is not present" , expectedMessage, searchMessage);


		SearchLogic.searchKeyWord("search date 03112015");
		actualSearchList = SearchLogic.getTaskList();
		Task task = new Task ("EE2020 Oscilloscope project", "03112015","1"); 
		expectedSearchList.add(task); 
		testSearchLogicValid("test search by date" , expectedSearchList, actualSearchList); 
		// the expected search list should be cleared to test another search type . 
		expectedSearchList = clearExpectedSearchList(expectedSearchList); 

		//testing a date not present 
		SearchLogic.searchKeyWord("search data 05112015"); 
		searchMessage=SearchLogic.getMessage(); 
		expectedMessage = "No content to display"; 
		testSearchLogicInvalid("test search by impt" , expectedMessage, searchMessage); 


		testSort(expected,modifier.getContentList() ); 

		//edit by date
		expected = getExpectedforEditDate(expected);
		EditLogic.editEvent("edit 2 by date 08112015"); 
		ArrayList<Task> actual = new ArrayList<Task>();
		actual = modifier.getContentList(); 
		testEditLogic("test if edit by date works",expected , actual);

		// edit impt 
		expected = getExpectedforEditImpt(expected);
		EditLogic.editEvent("edit 2 by impt 2"); 
		actual = modifier.getContentList(); 
		testEditLogic("test if edit by date works",expected , actual); 
		expected = getExpectedforEditTitle(expected);

		EditLogic.editEvent("edit 2 by title Oral presentation 2 "); 
		actual = modifier.getContentList(); 
		testEditLogic("test if edit by title works",expected , actual);
	}

	private ArrayList<Task> getExpectedforEditTitle(ArrayList<Task> expected) {
		//remove the first second title and edit the title. 
		expected.remove(1); 
		Task task3 = new Task("Oral presentation 2", "06112015","3"); 
		expected.add(task3); 	
		return expected; 
	}
	private ArrayList<Task> getExpectedforEditImpt(ArrayList<Task> expected) {
		expected.remove(1); 
		Task task3 = new Task("OP2 presentation", "06112015","2"); 
		expected.add(task3); 	
		return expected; 
	}

	private ArrayList<Task> getExpectedforEditDate(ArrayList<Task> expected) {
		expected.remove(1); 
		Task task3 = new Task("OP2 presentation", "08112015","3"); 
		expected.add(task3); 	
		return expected; 
	}
	private void testEditLogic(String description , ArrayList<Task >expected ,ArrayList<Task > actual){
		assertEquals (description , expected , actual);
	}
	private ArrayList<Task> clearExpectedSearchList(ArrayList<Task> expectedSearchList) {
		expectedSearchList.clear(); 
		return expectedSearchList; 
	}
	private void testSearchLogicInvalid(String description, String expectedMessage, String searchMessage) {
		assertEquals(description , expectedMessage , searchMessage); 
	}

	private void testSearchLogicValid(String description, ArrayList<Task> expectedSearch, ArrayList<Task> actualSearch) {
		assertEquals(description, expectedSearch, actualSearch);
	}

	private void testAddRankLogic(String description, ArrayList<Task> expected, String actualMessage,
			String expectedMessage) {
		if(actualMessage != null) {
			assertEquals(description, expectedMessage, actualMessage);
		} else {
			ArrayList<Task> actual = modifier.getContentList();
			assertEquals(description, expected, actual);
		}
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

