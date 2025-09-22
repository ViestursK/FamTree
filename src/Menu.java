
public class Menu {
	public static void menu() {

		FileReader.readNumbersFile();
		int choice;
		do {
			System.out.println("----------------------------------");
			System.out.println("Choose an action: ");
			String addPeople = "1 - Add person";
			String printInfo = "2 - Print info";
			String exit = "3 - Exit program";

			System.out.println(addPeople);
			System.out.println(printInfo);
			System.out.println(exit);
			choice = UserInput.startInput();
			System.out.println(choice);
		} while (choice == 0);

		if (choice == 1) {
			Main.errorCheck();
			menu();

		}
		if (choice == 2) {
			choosePersonForPrintInfo();
			menu();

		}
		if (choice == 3) {
			save();
		}

	}

	static void save() {
		int answ = 0;
		do {
			System.out.println("----------------------------------");
			System.out.println("Do you wish to: ");
			System.out.println("1 - Start over & not save all new input data.");
			System.out.println("2 - Save everything in database & exit.");
			System.out.println("3 - Exit without saving.");
			System.out.println("----------------------------------");
			answ = UserInput.startInput();
		} while (answ == 0);

		if (answ == 1) {
			Person.allPers.removeAll(Person.allPers);
			Main.main(null);
			menu();
		}
		if (answ == 2) {
			Main.pushDataToDB();
			System.out.println("Succesfully saved. Goodbye!");
		}
		if (answ == 3) {
			System.out.println("Thanks for wasting my time! Bye!");
			System.exit(0);
		}

	}

	static void choosePersonForPrintInfo() {

		int answ;
		do {
			System.out.println("----------------------------------");
			System.out.println("Please choose a person you would like to print info about: ");
			for (Person p : Person.allPers) {
				System.out.println(p.id + " - " + p.name);
			}
			answ = UserInput.printPersonInfo();

		} while (answ == 0);

//		Cannot use stream because answ variable is defined previously
		for (Person p : Person.allPers) {
			if (p.id == answ) {
				System.out.println("Print info:");
				System.out.println(p.getPrintingInfo());
				System.out.println("Extra info:");
				System.out.println(p.getExtraRelInfo());
			}
		}
	}
}
