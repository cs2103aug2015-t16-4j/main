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

public class UnitTest {
	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	FileModifier modifier = FileModifier.getInstance();
	ArrayList<Task> actual = new ArrayList<Task>();
	ArrayList<Task> expected = new ArrayList<Task>();

	// @@author Shi Hao A0129916W
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

	// @@author Shi Hao A0129916W
	@Test
	public void testClear() {
		clearExpectedActual();
		DeleteLogic.clearFile();
		compareResults("test clear", expected, actual);
	}

	// @@author Shawn A0124181R
	@Test
	public void testDelete1() {
		clearExpectedActual();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project by 03112015");
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add OP2 presentation by 06112015");
		
		DeleteLogic.deleteEvent("delete 3");
		actual = modifier.getContentList();
		compareResults("test delete 3 from a list of 2 items", expected, actual);
	}

	@Test
	public void testDelete2() {
		clearExpectedActual();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project by 03112015");
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add OP2 presentation by 06112015");
		
		DeleteLogic.deleteEvent("delete 0");
		actual = modifier.getContentList();
		compareResults("test delete 0", expected, actual);
	}

	@Test
	public void testDelete3() {
		clearExpectedActual();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project by 03112015");
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add OP2 presentation by 06112015");
		
		DeleteLogic.deleteEvent("delete 2");
		actual = modifier.getContentList();
		expected.remove(1);
		compareResults("test delete 2", expected, actual);
	}

	private void addEvent(Task task1, String command) {
		AddLogic.addEventWithDeadline(command);
		expected.add(task1);
	}

	// @@author Shrestha A0130280X
	@Test
	public void testAdd1() {
		clearExpectedActual();
		actual = modifier.getContentList();
		AddLogic.addEventDefault("add ");
		actual = modifier.getContentList();
		compareResults("test default add", expected, actual);
	}

	private void clearExpectedActual() {
		DeleteLogic.clearFile();
		expected.clear();
	}

	@Test
	public void testAdd2() {
		clearExpectedActual();
		AddLogic.addEventWithDeadline("add complete EE2020 lab report by next " + "Friday");
		actual = modifier.getContentList();
		compareResults("test adding with deadline in wrong format", expected, actual);
	}

	@Test
	public void testAdd3() {
		clearExpectedActual();
		Task task3 = new Task("go for light and sound show at the Gardens by the " + "Bay");
		expected.add(task3);
		AddLogic.addEventWithDeadline("add go for light and sound show at the " + "Gardens by the Bay");
		actual = modifier.getContentList();
		compareResults("testing adding floating task with the word by", expected, actual);
	}

	@Test
	public void testAdd4() {
		clearExpectedActual();
		AddLogic.addEventWithDeadline("add attend meeting by 152015");
		actual = modifier.getContentList();
		compareResults("testing adding with deadline with wrong date format", expected, actual);
	}

	@Test
	public void testAdd5() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add watch movie rank 0");
		actual = modifier.getContentList();
		compareResults("test adding with wrong rank range", expected, actual);
	}

	@Test
	public void testAdd6() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add go for manicure rank 4");
		actual = modifier.getContentList();
		compareResults("test adding with wrong rank range", expected, actual);
	}

	@Test
	public void testAdd7() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add do groceries rank 3");
		actual = modifier.getContentList();
		Task task4 = new Task("do groceries", 3);
		expected.add(task4);
		compareResults("test adding with right rank range", expected, actual);
	}

	@Test
	public void testAdd8() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add rank all staff based on capabilities");
		Task task5 = new Task("rank all staff based on capabilities");
		expected.add(task5);
		actual = modifier.getContentList();
		compareResults("testing input with the rank not as a command word", expected, actual);
	}

	@Test
	public void testAdd9() {
		clearExpectedActual();
		AddLogic.addBlockEvent("add attend regional youth conference block" + " 14112015 to 17112015");
		Task task6 = new Task("attend regional youth conference", "14112015", "17112015");
		task6.setBlocking(true);
		expected.add(task6);
		actual = modifier.getContentList();
		compareResults("test normal blocking", expected, actual);
	}

	@Test
	public void testAdd10() {
		clearExpectedActual();
		AddLogic.addBlockEvent("add attend Navratri festival block 19112015" + " to 10112015");
		actual = modifier.getContentList();
		compareResults("test blocking with reversed dates", expected, actual);
	}

	@Test
	public void testAdd11() {
		clearExpectedActual();
		AddLogic.addBlockEvent("add attend sports event block 112015 to 122015");
		actual = modifier.getContentList();
		compareResults("test blocking with wrong date format", expected, actual);
	}

	// @@author Urvashi A0127781Y
	@Test
	// this tests invalid date input
	public void testAdd12() {
		clearExpectedActual();
		AddLogic.addEventWithTimeline("add attend JP Morgon conference from" + " 15112015 to 12112015");
		actual = modifier.getContentList();
		compareResults("testing adding invalid date for timeline task", expected, actual);
	}

	@Test
	public void testAdd13() {
		clearExpectedActual();
		Task task3 = new Task("attend JP Morgon conference", "12122015", "15122015", 1);
		expected.add(task3);
		AddLogic.addEventWithTimeline("add attend JP Morgon conference from " + "12122015 to 15122015 rank 1");

		actual = modifier.getContentList();

		compareResults("testing adding valid date for timeline task", expected, actual);
	}

	@Test
	public void testSearch1() {
		clearExpectedActual();
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add OP2 presentation by 06112015");
		SearchLogic.searchKeyWord("search presentation");
		actual = SearchLogic.getTaskList();
		compareResults("test search by title", expected, actual);
	}

	@Test
	public void testSearch2() {
		clearExpectedActual();
		SearchLogic.searchKeyWord("search presentation");
		actual = SearchLogic.getTaskList();
		compareResults("test seearching for a title tht does not exist", expected, actual);
	}

	@Test
	public void testSearch3() {
		clearExpectedActual();
		Task task = new Task("OP2 presentation", "06112015", 3);
		expected.add(task);
		AddLogic.addEventWithImportance("add OP2 presentation by 06112015 rank 3");
		SearchLogic.searchKeyWord("search impt 3");
		actual = SearchLogic.getTaskList();
		compareResults("test search by impt", expected, actual);
	}

	@Test
	public void testSearch4() {
		clearExpectedActual();
		
		AddLogic.addEventWithImportance("add test event rank 2");
		
		SearchLogic.searchKeyWord("search impt 4");
		actual = SearchLogic.getTaskList();
		compareResults("test search by impt, ranking 4 which is not present", expected, actual);
	}

	@Test
	public void testSearch5() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add EE2020 Oscilloscope project by 03112015" + " rank 1");
		SearchLogic.searchKeyWord("search date 03112015");
		actual = SearchLogic.getTaskList();
		Task task = new Task("EE2020 Oscilloscope project", "03112015", 1);
		expected.add(task);
		compareResults("test search by date", expected, actual);
	}

	@Test
	public void testSearch6() {
		clearExpectedActual();
		SearchLogic.searchKeyWord("search data 05112015");
		actual = SearchLogic.getTaskList();
		compareResults("test search by impt", expected, actual);
	}

	@Test
	public void testEdit1() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add OP2 presentation by 08112015 rank 3");
		EditLogic.editEvent("edit 1 by date 08112015");
		actual = modifier.getContentList();
		expected = getExpectedforEditDate(expected);
		compareResults("test if edit by date works", expected, actual);
	}

	// @@author Shi Hao A0129916W
	@Test
	public void testEdit2() {
		clearExpectedActual();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project by 03112015");
		EditLogic.editEvent("edit 1 by date 11142015");
		actual = modifier.getContentList();
		compareResults("test if invalid date for edit works", expected, actual);
	}

	@Test
	// edit importance testing with correct input
	public void testEdit3() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add go for manicure and pedicure rank 3");
		EditLogic.editEvent("edit 1 by impt 2");
		actual = modifier.getContentList();
		getExpectedforEditImpt();
		compareResults("test if edit by impt works", expected, actual);
	}

	@Test
	// edit importance with wrong input
	public void testEdit4() {
		clearExpectedActual();
		EditLogic.editEvent("edit 2 by impt 5");
		actual = modifier.getContentList();
		compareResults("test if invalid importance level for edit works", expected, actual);
	}

	@Test
	// edit title correct input
	public void testEdit5() {
		clearExpectedActual();
		getExpectedforEditTitle();
		AddLogic.addEventWithDeadline("add 0P2 presentation by 12112015");
		EditLogic.editEvent("edit 1 by title Oral presentation 2");
		actual = modifier.getContentList();
		compareResults("test if edit by title works", expected, actual);
	}

	// edit title with wrong input
	public void testEdit6() {
		clearExpectedActual();
		AddLogic.addEventWithDeadline("add 0P2 presentation by 12112015");
		EditLogic.editEvent("edit 2 by title Oral presentation 2 ");
		actual = modifier.getContentList();
		compareResults("test if edit invalid number works", expected, actual);
	}
 
	// @@author Shawn A0124181R
	@Test
	public void testUndoRedo1() {
		clearExpectedActual();
		ExecuteCommand.processCommandWithSpace("add abc");
		ExecuteCommand.processCommandWithoutSpace("undo");
		actual = modifier.getContentList();
		testUndo("test if undo works", expected, actual);
	}

	@Test
	public void testUndoRedo2() {
		clearExpectedActual();
		Task task = new Task("test unavailable redo");
		expected.add(task);
		ExecuteCommand.processCommandWithSpace("add test unavailable redo");
		ExecuteCommand.processCommandWithoutSpace("redo");
		actual = modifier.getContentList();
		compareResults("test if empty redo works", expected, actual);
	}

	@Test
	public void testUndoRedo3() {
		clearExpectedActual();
		Task task1 = new Task("test undo1");
		Task task2 = new Task("test undo2");
		expected.add(task1);
		expected.add(task2);

		ExecuteCommand.processCommandWithSpace("add test undo1");
		ExecuteCommand.processCommandWithSpace("add test undo2");
		ExecuteCommand.processCommandWithSpace("add test undo3");
		ExecuteCommand.processCommandWithSpace("add test undo4");
		ExecuteCommand.processCommandWithoutSpace("undo");
		ExecuteCommand.processCommandWithoutSpace("undo");

		actual = modifier.getContentList();

		testUndo("test if undo works for multiple undo", expected, actual);
	}

	@Test
	public void testUndoRedo4() {
		clearExpectedActual();
		Task task1 = new Task("test undo1");
		Task task2 = new Task("test undo2");
		Task task3 = new Task("test undo3");
		Task task4 = new Task("test undo4");
		expected.add(task1);
		expected.add(task2);
		expected.add(task3);
		expected.add(task4);

		ExecuteCommand.processCommandWithSpace("add test undo1");
		ExecuteCommand.processCommandWithSpace("add test undo2");
		ExecuteCommand.processCommandWithSpace("add test undo3");
		ExecuteCommand.processCommandWithSpace("add test undo4");
		ExecuteCommand.processCommandWithoutSpace("undo");
		ExecuteCommand.processCommandWithoutSpace("undo");
		ExecuteCommand.processCommandWithoutSpace("redo");
		ExecuteCommand.processCommandWithoutSpace("redo");

		actual = modifier.getContentList();

		testUndo("test if both undo redo works", expected, actual);
	}

	private void testUndo(String description, ArrayList<Task> expected, ArrayList<Task> actual) {
		assertEquals(description, actual, expected);
	}

	@Test
	public void testUndoRedo5() {
		clearExpectedActual();
		Task task1 = new Task("test undo1");
		expected.add(task1);

		ExecuteCommand.processCommandWithSpace("add test undo1");
		ExecuteCommand.processCommandWithoutSpace("undo");
		ExecuteCommand.processCommandWithoutSpace("redo");

		actual = modifier.getContentList();

		testUndo("test if single undo and redo redo works", expected, actual);
	}

	@Test
	public void testUndoRedo6() {
		clearExpectedActual();
		undoRedo.clearUndo();
		
		ExecuteCommand.processCommandWithoutSpace("undo");
		
		testUndo("test unavailble undo", expected, actual);
	}

	@Test
	public void testUndoRedo7() {
		clearExpectedActual();
		
		expected.add(new Task("test undo1"));
		
		ExecuteCommand.processCommandWithSpace("add test undo1");
		ExecuteCommand.processCommandWithSpace("complete 1");
		ExecuteCommand.processCommandWithoutSpace("undo");
		
		actual = modifier.getContentList();
		
		testUndo("test unavailble undo", expected, actual);
	}

	private void getExpectedforEditTitle() {
		Task task3 = new Task("Oral presentation 2", "12112015", 3);
		expected.add(task3);
	}

	private void getExpectedforEditImpt() {
		Task task1 = new Task("go for manicure and pedicure", 2);
		expected.add(task1);
	}

	private ArrayList<Task> getExpectedforEditDate(ArrayList<Task> expected) {
		Task task3 = new Task("OP2 presentation", "08112015", 3);
		expected.add(task3);
		return expected;
	}

	private void compareResults(String description, ArrayList<Task> expected, ArrayList<Task> actual) {
		assertEquals(description, expected, actual);
	}

	// @@author Shi Hao A0129916W
	@Test
	public void testSort1() {
		clearExpectedActual();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project on 03112015");
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add OP2 presentation by 06112015");
		actual = modifier.getContentList();
		compareResults("test if sort works", expected, actual);
	}
	
	@Test
	public void testSort2() {
		clearExpectedActual();
		Task task1 = new Task("go to school", "08112015");
		Task task2 = new Task("go home", "10112015");
		Task task3 = new Task("have lunch", "14112015");
		expected.add(task1);
		expected.add(task2);
		expected.add(task3);
		
		AddLogic.addEventWithDeadline("add have lunch by 14112015");
		AddLogic.addEventWithDeadline("add go home by 10112015");
		AddLogic.addEventWithDeadline("add go to school by 08112015");
		
		actual = modifier.getContentList();
		compareResults("test if sort works", expected, actual);
	}
	
	@Test
	public void testSort3() {
		clearExpectedActual();
		Task task1 = new Task("go to school", "08112015");
		Task task2 = new Task("play game", "10112015 0800", "10112015 1000", true);
		Task task3 = new Task("have lunch");
		expected.add(task1);
		expected.add(task2);
		expected.add(task3);
		
		AddLogic.addEventDefault("add have lunch");
		AddLogic.addEventWithTimeline("add play game on 10112015 from 0800 to 1000");
		AddLogic.addEventWithDeadline("add go to school by 08112015");
		
		actual = modifier.getContentList();
		compareResults("test if sort works", expected, actual);
	}
}
