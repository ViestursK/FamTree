import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserInput {
	static Scanner input = new Scanner(System.in);
	String sentence;

	static String sentence() {
		System.out.println("Enter text:");
		String text = input.nextLine();
		return text;
	}

	static int startInput() {
		String choice;
		do {
			choice = input.nextLine();
			if (!choice.matches("[1-3]")) {
				System.err.println("Please input a valid number.");
			}
		} while (!choice.matches("[1-3]"));
		int num = Integer.parseInt(choice);
		return num;
	}

	static int printPersonInfo() {
		String temp;
		int choice = 0;
		List<Integer> ids = null;
		ids = Person.allPers.stream().map(p -> p.id).collect(Collectors.toList());
		do {
			try {
				temp = input.nextLine();
				choice = Integer.parseInt(temp);
				if (!ids.contains(choice)) {
					System.out.println(
							"Please input a number that is on the list - I cannot serve you more than you have earned!");
				}
			} catch (Exception e) {
				System.out.println("Please input a valid number!");
			}
		} while (!ids.contains(choice));
		return choice;
	}

}
