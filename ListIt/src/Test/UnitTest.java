package Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import fileModifier.FileModifier;
import listItLogic.AddLogic;
import listItLogic.DeleteLogic;
import listItLogic.InvalidCommandException;
import taskGenerator.Task;

public class UnitTest {
	
	FileModifier modifier = FileModifier.getInstance();

	@Test
	public void test() throws InvalidCommandException {
		ArrayList<Task> expected = new ArrayList<Task>();
		DeleteLogic.clearFile();
		testDeleteLogic(expected, "test clear");
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		expected.add(task1);
		AddLogic.addEventWithDeadline("add Complete EE2020 oscilloscope project by 03112015");
		testAddWithDeadlineLogic(modifier.getContentList(), expected, 
				                 "test add with deadline logic");
		Task task2 = new Task("OP2 presentation", "06112015");
		expected.add(task2);
		AddLogic.addEventWithDeadline("add Oral Presentation 2 of Software Demo by 06112015");
	}
    
	private void testDeleteLogic(ArrayList<Task> expected, String description) {
		ArrayList<Task> actual = modifier.getContentList();
		assertEquals(description, expected, actual);
	}
	
	private void testAddWithDeadlineLogic(ArrayList<Task> actual, ArrayList<Task> expected, 
			                              String description) {
	    assertEquals(description, expected, actual);	
	}
}
