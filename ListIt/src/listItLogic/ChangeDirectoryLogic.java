package listItLogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import fileModifier.FileModifier;
import taskGenerator.Task;

public class ChangeDirectoryLogic {
	private static File dataFile;
	private static File directoryLocationFile;
	private FileWriter fw;
	private BufferedWriter bw;
	private FileReader fr;
	private BufferedReader br;
	
	private static ArrayList<Task> list;
	private static final String SUCCESS_MESSAGE = "Directory successfully made";
	private static final String ERROR_MESSAGE = "Directory already exists";
	
	
	
	public static void changeDirectory(String command) throws FileNotFoundException, IOException {
		
		FileModifier modifier = FileModifier.getInstance();
		
		String directoryLocation = command.substring(3);
		String directoryLocationFileName = "????";
		
		openFile(directoryLocationFile, directoryLocationFileName);
		clearFile();
		writeFile(directoryLocation);
		directoryLocationFile.mkdir();
		System.out.print(SUCCESS_MESSAGE);
		//dataFile = new File("directoryLocation");
		/*list = new ArrayList<Task>();
		
		if(!dataFile.exists()) {
			dataFile.mkdir();
			list = modifier.getContentList();
			modifier.saveFile(list);
			System.out.println(SUCCESS_MESSAGE);
		} else {
			System.err.println(ERROR_MESSAGE);
		}*/
	
	}

	private static void clearFile() throws IOException, FileNotFoundException {
		
		FileWriter fw = new FileWriter(directoryLocationFile, false);
		BufferedWriter bw = new BufferedWriter(fw);
		FileReader fr = new FileReader(directoryLocationFile);
		BufferedReader br = new BufferedReader(fr);

		bw.flush();
	}

	private static void writeFile(String directoryLocation) throws IOException, FileNotFoundException {
	
		FileWriter fw = new FileWriter(directoryLocationFile, true);
		BufferedWriter bw = new BufferedWriter(fw);
		FileReader fr = new FileReader(directoryLocationFile);
		BufferedReader br = new BufferedReader(fr);
		
		bw.write(directoryLocation);
		bw.newLine();
		bw.flush();
	}

	private static void openFile(File directoryLocationFile, String directoryLocationFileName) throws IOException, FileNotFoundException {
		directoryLocationFile = new File(directoryLocationFileName);
		if (!directoryLocationFile.exists()) {
			directoryLocationFile.createNewFile();
		}
	}

}
