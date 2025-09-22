import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {
	static File file = new File("src//wordsToNums.txt");
	static String encoding = "UTF-8";
	static HashMap<String, Integer> wordsToNums = new HashMap<String, Integer>();

	public static void readNumbersFile() {
		try {
			Scanner scan = new Scanner(file, encoding);
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] arr = line.split(" ");
				wordsToNums.put(arr[0], Integer.parseInt(arr[1]));
			}
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		 
	}
	
	
}
