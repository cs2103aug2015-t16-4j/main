package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import fileModifier.FileModifier;
import javafx.application.Application;
import listItLogic.AddLogic;
import listItLogic.DeleteLogic;
import listItLogic.EditLogic;
import listItLogic.ExecuteCommand;
import listItLogic.SearchLogic;
import listItLogic.UndoAndRedoLogic;
import listItUI.*;
import taskGenerator.Task;
import static org.hamcrest.CoreMatchers.*;

public class UnitTest {
	
	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	FileModifier modifier = FileModifier.getInstance();
	ArrayList<Task> actual = new ArrayList<Task>();
	ArrayList<Task> expected = new ArrayList<Task>();
	ArrayList<Task> expectedSearchList = new ArrayList<Task>();
	ArrayList<Task> actualSearchList = new ArrayList<Task>();

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
	
	@Rule
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

	@Test
	public void testClear() {
		DeleteLogic.clearFile();
		compareResults("test clear", expected, actual);
	}
	
	@Test
	public void testDelete1() {
		DeleteLogic.clearFile();
		expected.clear();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project by 03112015"); 
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add 0P2 presentation by 06112015"); 

		DeleteLogic.deleteEvent("delete 3");
		actual = modifier.getContentList();
		compareResults("test delete 3 from a list of 2 items", expected,
	                                actual);
	}
	
	@Test
	public void testDelete2() {
		DeleteLogic.deleteEvent("delete 0");
		actual = modifier.getContentList();
		compareResults("test delete 0", expected, actual);
	}
    
	@Test
	public void testDelete3() {
		DeleteLogic.deleteEvent("delete 2");
		actual = modifier.getContentList();
		expected.remove(2);
		compareResults("test delete 2", expected, actual);
	}

	private void addEvent(Task task1, String command) {
		AddLogic.addEventWithDeadline(command);
		expected.add(task1);
	}
	
	@Test
	public void testAdd1() {
		DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addEventDefault("add "); 
		actual = modifier.getContentList();
		compareResults("test default add", expected, actual);
	}
	
	@Test
    public void testAdd2() {
		DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addEventWithDeadline("add complete EE2020 lab report by next "
				                       + "Friday");
		actual = modifier.getContentList();
		compareResults("test adding with deadline in wrong format", expected, 
				     actual);
    }

	@Test
	public void testAdd3() {
		DeleteLogic.clearFile();
		expected.clear();
		Task task3 = new Task("go for light and sound show at the Gardens by the Bay");
		expected.add(task3);
		AddLogic.addEventWithDeadline("add go for light and sound show at the Gardens by the Bay");
		actual  = modifier.getContentList();
		compareResults("testing adding floating task with the word by", expected, 
				      actual);
	}

	@Test
	public void testAdd4() {
		DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addEventWithDeadline("add attend meeting by 152015");
		actual = modifier.getContentList();
		compareResults("testing adding with deadline with wrong date format", expected,
				      actual);
	}
    
	@Test
	public void testAdd5() {
		DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addEventWithImportance("add watch movie rank 0");
		actual = modifier.getContentList();
		compareResults("test adding with wrong rank range", expected, 
				      actual);
	}
	
    @Test
    public void testAdd6() {
    	DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addEventWithImportance("add go for manicure rank 4");
		actual = modifier.getContentList();
		compareResults("test adding with wrong rank range", expected, actual);
    }

    @Test
    public void testAdd7() {
    	DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addEventWithImportance("add do groceries rank 3");
		actual = modifier.getContentList();
		Task task4 = new Task("do groceries", 3);
		expected.add(task4);
		compareResults("test adding with right rank range", expected, actual);
    }
	
    @Test
    public void testAdd8() {
    	DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addEventWithImportance("add rank all staff based on capabilities");
		Task task5 = new Task("rank all staff based on capabilities");
		expected.add(task5);
		actual = modifier.getContentList();
		compareResults("testing input with the rank not as a command word", expected, actual);
    }
    
    @Test
    public void testAdd9() {
    	DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addBlockEvent("add attend regional youth conference block from 14112015 to 17112015");
		Task task6 = new Task("attend regional youth conference", "14112015", "17112015");
		expected.add(task6);
		actual = modifier.getContentList();
		compareResults("test normal blocking", expected, actual);
    }
    
	@Test
	public void testAdd10() {
		DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addBlockEvent("add attend Navratri festival block from 19112015 to 10112015");
		actual = modifier.getContentList();
		compareResults("test blocking with reversed dates", expected, actual);
	}
	
	@Test
	public void testAdd11() {
		DeleteLogic.clearFile();
		expected.clear();
		AddLogic.addBlockEvent("add attend sports event block from 112015 to 122015");
		actual = modifier.getContentList();
		compareResults("test blocking with wrong date format", expected, actual);
	}
		/*AddLogic.addEventWithTimeline("add attend project meeting on 05112015 from 1400 to 1200 rank 1");
		addTimelineMessage = AddLogic.getTimelineMessage();
		testAddLogic("testing adding with wrong timeline input", expected, addTimelineMessage, 
				     "invalid timeline range");*/

	@Test
	public void testSearch1() {
		//test key word present and not present . 
		SearchLogic.searchKeyWord("search Oral Presentation 2");
		actualSearchList = SearchLogic.getTaskList();
		compareResults("test search by title" , expectedSearchList, actualSearchList); 
	}

	@Test
	public void testSearch2() {
		SearchLogic.searchKeyWord("search tutorial work"); 
		actualSearchList = SearchLogic.getTaskList();
		compareResults("test seearching for a title tht does not exist", expectedSearchList, actualSearchList);
	}

	@Test
	public void testSearch3() {
		//test for importance ( 1 & 4)  
		SearchLogic.searchKeyWord("search impt 3");
		actualSearchList = SearchLogic.getTaskList();
		Task task2 = new Task("OP2 presentation", "06112015", 3);
		expectedSearchList.add(task2); 
		compareResults("test search by impt" , expectedSearchList, actualSearchList); 
	}

	@Test
	public void testSearch4() {
		SearchLogic.searchKeyWord("search impt 4");
		actualSearchList = SearchLogic.getTaskList();
		compareResults("test search by impt, ranking 4 which is not present" , expectedSearchList, actualSearchList);
	}

    @Test
    public void testSearch5() {
		SearchLogic.searchKeyWord("search date 03112015");
		actualSearchList = SearchLogic.getTaskList();
		Task task = new Task ("EE2020 Oscilloscope project", "03112015","1"); 
		expectedSearchList.add(task); 
		compareResults("test search by date" , expectedSearchList, actualSearchList); 
		// the expected search list should be cleared to test another search type . 
		expectedSearchList = clearExpectedSearchList(expectedSearchList); 
    }

    @Test
    public void testSearch6() {
		//testing a date not present 
		SearchLogic.searchKeyWord("search data 05112015"); 
		actualSearchList = SearchLogic.getTaskList(); 
		compareResults("test search by impt", expectedSearchList, actualSearchList); 
	}
	
	public void testEdit1() {
		//edit by date
		expected = getExpectedforEditDate(expected);
		EditLogic.editEvent("edit 2 by date 08112015"); 
		actual = modifier.getContentList(); 
		compareResults("test if edit by date works", expected, actual);
	}
	
	@Test
	public void testEdit2() {
		EditLogic.editEvent("edit 2 by date 11142015");
		compareResults("test if invalid date for edit works", expected, actual);
	}
	
	@Test
	public void testEdit3() {
		// edit impt 
		EditLogic.editEvent("edit 2 by impt 2"); 
		actual = modifier.getContentList(); 
		expected = getExpectedforEditImpt(expected);
		compareResults("test if edit by date works", expected, actual);
	}
	
	@Test
	public void testEdit4() {
		EditLogic.editEvent("edit 2 by impt 5"); 
		actual = modifier.getContentList();
		compareResults("test if invalid importance level for edit works", expected, actual);
	}

	@Test
	public void testEdit5() {
        //edit title
		expected = getExpectedforEditTitle(expected);
		EditLogic.editEvent("edit 2 by title Oral presentation 2 "); 
		actual = modifier.getContentList(); 
		compareResults("test if edit by title works",expected , actual);
	}
	
	@Test
<<<<<<< HEAD
	public void testUndoRedo() {
=======
	public void testUndoRedo1() {
>>>>>>> origin/master
		//Test empty undo and redo method
		ArrayList<Task> actual = new ArrayList<Task>();
        ArrayList<Task> expected = modifier.getContentList();
		AddLogic.addEventDefault("test empty undo string");
<<<<<<< HEAD
		actual = undoRedo.getListFromUndo();
		testEmptyUndo("test if empty undo works", expected, actual);
		
		expected = modifier.getContentList();
		AddLogic.addEventDefault("test empty redo string");
		actual = undoRedo.getListFromRedo();
		testEmptyRedo("test if empty redo works", expected, actual);
		
		//Test undo and redo method with strings
		expected = modifier.getContentList();
		ExecuteCommand.processCommandWithSpace("add test undo String");
		actual = undoRedo.getListFromUndo();
		testUndo("test if undo works", expected, actual);
		
		expected = modifier.getContentList();
		ExecuteCommand.processCommandWithSpace("add test undo String");
		actual = undoRedo.getListFromRedo();
		testRedo("test if redo works", expected, actual);
	}
	
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
=======
		expected = undoRedo.getListFromUndo();
		compareResults("test if empty undo works", expected, actual);
	}
	
	@Test
	public void testUndoRedo2() {
		actual = modifier.getContentList();
		AddLogic.addEventDefault("test empty redo string");
		expected = undoRedo.getListFromRedo();
		compareResults("test if empty redo works", expected, actual);
	}
	
	@Test
	public void testUndoRedo3() {
		//Test undo and redo method with strings
		actual = modifier.getContentList();
		undoRedo.storeListToUndo(modifier.getContentList());
		AddLogic.addEventDefault("test undo String");
		expected = undoRedo.getListFromUndo();
		compareResults("test if undo works", expected, actual);
	}
	
	@Test
	public void testUndoRedo4() {
		actual = modifier.getContentList();
		undoRedo.storeListToRedo(modifier.getContentList());
		AddLogic.addEventDefault("test undo String");
		expected = undoRedo.getListFromRedo();
		compareResults("test if redo works", expected, actual);
>>>>>>> origin/master
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
	
	private ArrayList<Task> clearExpectedSearchList(ArrayList<Task> expectedSearchList) {
		expectedSearchList.clear(); 
		return expectedSearchList; 
	}
	
	private void compareResults(String description, ArrayList<Task> expected, 
			                  ArrayList<Task> actual) {
		assertEquals(description, expected, actual);
	}
	
	private void testSort() {
		ArrayList<Task> actual = modifier.getContentList();
		String description = "test if sort works"; 		
		assertEquals(description , expected , actual); 		
	}

}
