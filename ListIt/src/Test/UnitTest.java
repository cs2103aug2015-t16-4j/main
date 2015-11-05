package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import fileModifier.FileModifier;
import javafx.application.Application;
import listItLogic.AddLogic;
import listItLogic.DeleteLogic;
import listItLogic.EditLogic;
import listItLogic.SearchLogic;
import listItLogic.UndoAndRedoLogic;
import listItUI.*;
import taskGenerator.Task;
import static org.hamcrest.CoreMatchers.*;

public class UnitTest {
	
	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	FileModifier modifier = FileModifier.getInstance();
	ArrayList<Task> expected = new ArrayList<Task>();
	ArrayList<Task> expectedSearchList = new ArrayList<Task>();
	ArrayList<Task> actualSearchList = new ArrayList<Task>();
	String addMessage = "null";
	String deleteMessage = "null";
	String addDeadlineMessage = "null";
	String addRankMessage = "null";
	String searchMessage = "null";
	String addTimelineMessage = "null";
	String actualEditMessage = "null"; 
	String expectedEditMessage = "null"; 
	String addBlockMessage = "null";
	String addRecurMessage = "null";

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
		Thread.sleep(500);
	}
	
	@Test
	public void testDelete() {
		DeleteLogic.clearFile();
		testDeleteLogicClear(expected, "test clear"); 

		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project by 03112015"); 
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add 0P2 presentation by 06112015"); 

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

	private void addEvent(Task task1, String command) {
		AddLogic.addEventWithDeadline(command);
		expected.add(task1);
	}
	
	@Test
	public void testAdd() {
		AddLogic.addEventDefault("add "); 
		addMessage = AddLogic.getMessage();
		testAddLogic("test default add", expected, "Please enter an event title", addMessage);

		AddLogic.addEventWithDeadline("add complete EE2020 lab report by next Friday");
		addDeadlineMessage = AddLogic.getDeadlineMessage();
		testAddLogic("test adding with deadline in wrong format", expected, 
				addDeadlineMessage, "enter a valid date");

		Task task3 = new Task("go for light and sound show at the Gardens by the Bay");
		addEvent(task3, "add go for light and sound show at the Gardens by the Bay");
		addDeadlineMessage = AddLogic.getDeadlineMessage();
		testAddLogic("testing adding floating task with the word by", expected, 
				addDeadlineMessage, "null");

		AddLogic.addEventWithDeadline("add attend meeting by 152015");
		addDeadlineMessage = AddLogic.getDeadlineMessage();
		testAddLogic("testing adding with deadline with wrong date format", expected,
				addDeadlineMessage, "enter a valid date");

		AddLogic.addEventWithImportance("add watch movie rank 0");
		addRankMessage = AddLogic.getRankMessage();
		testAddLogic("test adding with wrong rank range", expected, addRankMessage, "invalid rank input");

		AddLogic.addEventWithImportance("add go for manicure rank 4");
		addRankMessage = AddLogic.getRankMessage();
		testAddLogic("test adding with wrong rank range", expected, addRankMessage,
				"invalid rank input");

		AddLogic.addEventWithImportance("add do groceries rank 3");
		addRankMessage = AddLogic.getRankMessage();
		Task task4 = new Task("do groceries", 3);
		expected.add(task4);
		testAddLogic("test adding with right rank range", expected, addRankMessage,
				"null");
		
		AddLogic.addEventWithImportance("add rank all staff based on capabilities");
		Task task5 = new Task("rank all staff based on capabilities");
		expected.add(task5);
		addRankMessage = AddLogic.getRankMessage();
		testAddLogic("testing input with the rank not as a command word", expected, addRankMessage, "null");
		
		AddLogic.addBlockEvent("add attend regional youth conference block from 14112015 to 17112015");
		Task task6 = new Task("attend regional youth conference", "14112015", "17112015");
		expected.add(task6);
		addBlockMessage = AddLogic.getBlockMessage();
		testBlockLogic("test normal blocking", expected, addBlockMessage, "null");
		
		AddLogic.addBlockEvent("add attend Navratri festival block from 19112015 to 10112015");
		addBlockMessage = AddLogic.getBlockMessage();
		testBlockLogic("test blocking with reversed dates", expected, addBlockMessage, "start date should be earlier than end date");
		
		AddLogic.addBlockEvent("add attend sports event block from 112015 to 122015");
		addBlockMessage = AddLogic.getBlockMessage();
		testBlockLogic("test blocking with wrong date format", expected, addBlockMessage, "invalid input");
		
		AddLogic.addRecursiveEventDeadline("add Jade's Birthday repeat yearly on 03102015");
		addRecurMessage = AddLogic.getRecurMessage();
		testAddRecur("test add recurring event with no repeat cycle", expected, addRecurMessage, "please enter a recur cycle");
		
		AddLogic.addRecursiveEventDeadline("add go to India repeat 1 yearly");
		addRecurMessage = AddLogic.getRecurMessage();
		testAddRecur("test add recurring event with no deadline", expected, addRecurMessage, "please enter a start date");
		
		AddLogic.addRecursiveEventTimeline("add go to India repeat 1 yearly");
		addRecurMessage = AddLogic.getRecurMessage();
		testAddRecur("test add recurring event with no timeline", expected, addRecurMessage, "please enter a start date");
		
		/*AddLogic.addEventWithTimeline("add attend project meeting on 05112015 from 1400 to 1200 rank 1");
		addTimelineMessage = AddLogic.getTimelineMessage();
		testAddLogic("testing adding with wrong timeline input", expected, addTimelineMessage, 
				     "invalid timeline range");*/
	}
	
	@Test
	public void testSearch() {
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
		Task task2 = new Task("OP2 presentation", "06112015", 3);
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
		searchMessage = SearchLogic.getMessage(); 
		expectedMessage = "No content to display"; 
		testSearchLogicInvalid("test search by impt", expectedMessage, searchMessage); 
	}
	
	public void testEdit() {
		//edit by date
		expected = getExpectedforEditDate(expected);
		EditLogic.editEvent("edit 2 by date 08112015"); 
		ArrayList<Task> actual = new ArrayList<Task>();
		actual = modifier.getContentList(); 
		testEditLogic("test if edit by date works",expected , actual);
		
		EditLogic.editEvent("edit 2 by date 11142015");
		actualEditMessage = EditLogic.getMessage(); 
		expectedEditMessage = "Invalid date is inputed\n"; 
		testEditLogicInvalid("test if invalid date for edit works",expectedEditMessage , actualEditMessage); 		

		// edit impt 
		expected = getExpectedforEditImpt(expected);
		EditLogic.editEvent("edit 2 by impt 2"); 
		actual = modifier.getContentList(); 
		testEditLogic("test if edit by date works",expected , actual); 
		expected = getExpectedforEditTitle(expected);
		
		EditLogic.editEvent("edit 2 by impt 5"); 
		actualEditMessage = EditLogic.getMessage(); 
		expectedEditMessage = "Invalid Importance level,there are only 3 types: 1 , 2 or 3.\n"; 
		testEditLogicInvalid("test if invalid importance level for edit works",expectedEditMessage , actualEditMessage); 

        //edit title
		EditLogic.editEvent("edit 2 by title Oral presentation 2 "); 
		actual = modifier.getContentList(); 
		testEditLogic("test if edit by title works",expected , actual);
	}
	
	/*@Test
	public void testUndoRedo() {
		//Test empty undo and redo method
        ArrayList<Task> actual = modifier.getContentList();
		AddLogic.addEventDefault("test empty undo string");
		expected = undoRedo.getListFromUndo();
		testEmptyUndo("test if empty undo works", expected, actual);
		
		actual = modifier.getContentList();
		AddLogic.addEventDefault("test empty redo string");
		expected = undoRedo.getListFromRedo();
		testEmptyRedo("test if empty redo works", expected, actual);
		
		//Test undo and redo method with strings
		actual = modifier.getContentList();
		undoRedo.storeListToUndo(modifier.getContentList());
		AddLogic.addEventDefault("test undo String");
		expected = undoRedo.getListFromUndo();
		testUndo("test if undo works", expected, actual);
		
		actual = modifier.getContentList();
		undoRedo.storeListToRedo(modifier.getContentList());
		AddLogic.addEventDefault("test undo String");
		expected = undoRedo.getListFromRedo();
		testRedo("test if redo works", expected, actual);
	}*/
	
	private void testRedo(String description, ArrayList<Task> expected, ArrayList<Task> actual) {
		assertThat(description, actual, not(expected));
	}

	private void testUndo(String description, ArrayList<Task> expected, ArrayList<Task> actual) {
		assertThat(description, actual, not(expected));
	}

	private void testEmptyRedo(String description, ArrayList<Task> expected, ArrayList<Task> actual) {
		assertEquals(description, actual, expected);
	}
	
	private void testEmptyUndo(String description, ArrayList<Task> expected, ArrayList<Task> actual) {
		assertEquals(description, actual, expected);
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
	
	private void testEditLogicInvalid(String description, String expectedEditMessage, String actualEditMessage) {
		assertEquals(description , expectedEditMessage , actualEditMessage); 
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

	private void testAddLogic(String description, ArrayList<Task> expected, String actualMessage,
			String expectedMessage) {
		if(!actualMessage.equals("null")) {
			assertEquals(description, expectedMessage, actualMessage);
		} else {
			ArrayList<Task> actual = modifier.getContentList();
			assertEquals(description, expected, actual);
		}
	}
	
	private void testAddRecur(String description, ArrayList<Task> expected, String actualMessage, String expectedMessage) {
		if(actualMessage.equals("null")) {
			ArrayList<Task> actual = modifier.getContentList();
			assertEquals(description, expected, actual);
		} else {
			assertEquals(description, expectedMessage, actualMessage);
		}
	}
	
	private void testBlockLogic(String description, ArrayList<Task> expected, String actualMessage, String expectedMessage) {
		if(actualMessage.equals("null")) {
			assertEquals(description, expectedMessage, actualMessage);
		} else {
			ArrayList<Task> actual = modifier.getContentList();
			assertEquals(description, expected, actual);
		}
	}

	private void testSort() {
		ArrayList<Task> actual = modifier.getContentList();
		String description = "test if sort works"; 		
		assertEquals(description , expected , actual); 		
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
}

