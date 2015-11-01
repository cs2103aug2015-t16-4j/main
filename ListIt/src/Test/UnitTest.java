package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import fileModifier.FileModifier;
import javafx.application.Application;
import javafx.stage.Stage;
import listItLogic.AddLogic;
import listItLogic.DeleteLogic;
import listItLogic.EditLogic;
import listItLogic.SearchLogic;
import listItUI.UIMain;
import taskGenerator.Task;

public class UnitTest {

	FileModifier modifier = FileModifier.getInstance();

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

		Task task1 = new Task("EE2020 Oscilloscope project", "03112015","1");
		Task task2 = new Task("OP2 presentation", "06112015","3");
		expected.add(task1); 
		expected.add(task2); 

		// this tests the different types of add 
		testAddDefault(addMessage , expected); 
		testAddWithDeadline(addDeadlineMessage , expected); 
		testAddWithImpt(expected , addRankMessage); 

		testDelete(deleteMessage ,expected); 
		testSearch(searchMessage ,expectedSearchList , actualSearchList); 

		// the different edit types 
		//testEdit(expected); 


		testSort(expected,modifier.getContentList()); 
		//testComplete(); 
	}

	private void testSearch(String searchMessage, ArrayList<Task> expectedSearchList,ArrayList<Task> actualSearchList) {
		testSearchDate(actualSearchList,expectedSearchList , searchMessage); 
		testSearchImpt(actualSearchList,expectedSearchList , searchMessage); 
		testSearchTitle(actualSearchList,expectedSearchList , searchMessage); 
	}

	private void testSearchTitle(ArrayList<Task> actualSearchList, ArrayList<Task> expectedSearchList,
			String searchMessage) {
		//test key word present and not present . 
		SearchLogic.searchKeyWord("search Oral Presentation 2");
		actualSearchList = SearchLogic.getTaskList();
		testSearchLogicValid("test search by title" , expectedSearchList, actualSearchList); 

		SearchLogic.searchKeyWord("search tutorial work"); 
		//searchMessage=""; 
		String expectedMessage = "No content to display"; 
		testSearchLogicInvalid("test search by title , invalid input" , expectedMessage , searchMessage);
	}

	private void testSearchImpt(ArrayList<Task> actualSearchList, ArrayList<Task> expectedSearchList,
			String searchMessage) {
		//test for importance ( 1 & 4) 
		SearchLogic.searchKeyWord("search impt 3");
		actualSearchList = SearchLogic.getTaskList();
		Task task2 = new Task("OP2 presentation", "06112015","3");		
		expectedSearchList.add(task2); 
		testSearchLogicValid("test search by impt" , expectedSearchList, actualSearchList); 

		SearchLogic.searchKeyWord("search impt 4");
		//searchMessage=""; 
		String  expectedMessage = "Index is out of bounds!\n"; 
		testSearchLogicInvalid("test search by impt, ranking 4 which is not present" , expectedMessage, searchMessage);

	}
	private void testSearchDate(ArrayList<Task> actualSearchList, ArrayList<Task> expectedSearchList , String searchMessage) {
		SearchLogic.searchKeyWord("search date 03112015");
		actualSearchList = SearchLogic.getTaskList();
		Task task = new Task ("EE2020 Oscilloscope project", "03112015","1"); 
		expectedSearchList.add(task); 
		testSearchLogicValid("test search by date" , expectedSearchList, actualSearchList); 
		// the expected search list should be cleared to test another search type . 
		expectedSearchList = clearExpectedSearchList(expectedSearchList); 

		//testing a date not present 
		SearchLogic.searchKeyWord("search data 05112015"); 
		//searchMessage=""; 
		String expectedMessage = "No content to display"; 
		testSearchLogicInvalid("test search by impt" , expectedMessage, searchMessage); 

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


	private void testSort(ArrayList<Task> expected ,ArrayList<Task> actual ) {
		String description = "test if sort works"; 		
		assertEquals(description , expected , actual); 		
	}
	private void testDelete(String deleteMessage, ArrayList<Task> expected) {

		AddLogic.addEventWithDeadline("add Complete EE2020 oscilloscope project by 03112015 rank 1");
		AddLogic.addEventWithDeadline(" add OP2 presentation by 06112015 rank 3");

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


	private void testAddWithImpt(ArrayList<Task> expected , String addRankMessage){

		//this test the invalid inputs (boundary cases) 
		AddLogic.addEventWithImportance("add watch movie rank 0");
		addRankMessage = AddLogic.getRankMessage();
		testAddRankLogic("test adding with wrong rank range", expected, addRankMessage, "invalid rank input");

		AddLogic.addEventWithImportance("add go for manicure rank 4");
		addRankMessage = AddLogic.getRankMessage();
		testAddRankLogic("test adding with wrong rank range", expected, addRankMessage,
				"invalid rank input");

		AddLogic.addEventWithImportance("OP2 presentation by 06112015 rank 3");
		addRankMessage = AddLogic.getRankMessage();
		testAddRankLogic("test adding with right rank range", expected, addRankMessage,
				"invalid rank input");		
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

	private void testAddWithDeadline(String addMessage , ArrayList<Task> expected){
		ArrayList<Task> actual = new ArrayList<Task>(); 
		AddLogic.addEventWithDeadline("add Complete EE2020 oscilloscope project by 03112015 rank 1");
		AddLogic.addEventWithDeadline("OP2 presentation by 06112015 rank 3");
		actual = modifier.getContentList(); 
		testAddWithDeadlineLogic(actual, expected, "test add with deadline logic");
	}

	private void testAddWithDeadlineLogic(ArrayList<Task> actual, ArrayList<Task> expected, 
			String description) {
		assertEquals(description, expected, actual);	
	}

	private void testAddDefault(String addMessage , ArrayList<Task> expected){
		AddLogic.addEventDefault("add "); 
		addMessage = AddLogic.getMessage();
		testAddDefaultLogic("test default add", expected, "Please enter an event title", addMessage);	
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
} 