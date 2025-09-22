import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
	static AtomicInteger ai = new AtomicInteger();
	static int id;
	static DB db = new DB();

	public static void main(String[] args) {
// 		creating DB if it does not exist
		createDB();
//		reading data from DB, if there is any
		readDB();
		System.out.println("----------------------");
		System.out.println("Welcome to FamilyTree!");
		Menu.menu();
	}

	static void createDB() {
		db.createDB("FamTree");
		db.createTables();
	}

	static void readDB() {
		db.readRelTypes();
		db.readPersTable();
		db.readRelationData();
//		Get correct/new ids for people - AtomicInteger is the reason it has to be done in a "stupid way", but unique ids are needed for correct data push into DB
		if (!Person.allPers.isEmpty()) {
			Person.allPers.forEach(n -> {
				id = ai.incrementAndGet();
			});

		}
	}

//	if chosen in the menu, data that is in the program and has not yet been save to the database is pushed to tables in DB
	static void pushDataToDB() {
		if (Person.allPers.isEmpty()) {
			System.out.println("No data to push to data base! Bye!");
			return;
		}
		db.inputRelationTypes();
		Person.allPers.forEach(p -> {
			db.inputPersons(p.id, p.name, p.isAlive, p.gender);
		});
		db.inputRelations();
	}

//	Checking all the input errors, in order to make object creation without mistakes
	static void errorCheck() {
		String[] arr = {};
		String[] relatives = {};
		String s = "";
		String gender = "";
		String relation = "";
		boolean countComparison = false;
		int count = 0;
		do {
			String input = UserInput.sentence();
			if (!input.contains("-")) {
				System.out.println("ERROR! please provide '-' between relation type and related people names!");
				continue;
			}
			arr = input.split(" - ");
			s = arr[0].substring(arr[0].indexOf(")") + 1, arr[0].length()).trim();

			count = Person.getCount(s);
			gender = Person.getGender(arr);
			if (gender == null) {
				System.out.println("ERROR! Provide gender 'M' or 'F' in round brackets.");
				continue;
			}
			if (!FoolProof.affiliation(arr[0].split(" "))) {
				System.out.println("ERROR! Please provide a proper affiliation - has or had.");
				continue;
			}
			relation = FoolProof.checkRelationType(arr[0]);

			if (relation == null) {
				System.out.println(
						"ERROR! No relation type was given. Add first-base relation, like 'brother' or 'mother' for example.");
				continue;
			}
			if ("husband wife father mother".contains(relation) && count > 1) {
				System.out.println("ERROR! " + relation + " can only be one!");
				continue;
			}
			if (!FoolProof.checkCount(count)) {
				System.out.println("ERROR! Provide a valid count count 'a', or 1-9");
				continue;
			}
			relatives = FoolProof.getOtherCount(arr[1]);
			countComparison = FoolProof.compareCount(Person.getCount(s), FoolProof.getOtherCount(arr[1]).length);
			if (!countComparison) {
				System.out.println(
						"ERROR! Count mismatch. The number of relations must be the same provided relatives names!");
				continue;
			}
			if ((gender.equals("M")) && relation.equals("husband") || gender.equals("F") && relation.equals("wife")) {
				System.out.println("ERROR! Gender: " + gender + " cannot be married to the same sex.");
				relation = null;
			}

		} while (gender == null || relation == null || !countComparison || relatives == null);
		String name = Person.getName(arr);
		boolean isAlive = FoolProof.isAlive(arr[0].split(" "));
		createPeople(isAlive, name, gender, relation, relatives);

	}

//	Method creates people mentioned in the sentence 
	static void createPeople(boolean isAlive, String mPName, String mpGender, String relation, String[] relatives) {
		HashSet<String> allPersNames = new HashSet<String>();
		Person.allPers.stream().map(p -> p.name).collect(Collectors.toCollection(() -> allPersNames));
		Person mainPerson = null;
		String relGender = Person.allGenders(relation);
		HashSet<Person> rels = new HashSet<Person>();
		List<Person> getMainPerson = Person.allPers.stream().filter(p -> p.name.equals(mPName))
				.collect(Collectors.toList());
//		Person that is mentioned at the beginning of the sentence
		if (!getMainPerson.isEmpty()) {
			mainPerson = getMainPerson.get(0);
		}
		if (mainPerson == null) {
			id = ai.incrementAndGet();
			mainPerson = new Person(id, mPName, mpGender, isAlive);
		}

//		Relatives
		for (String s : relatives) {
			if (allPersNames.contains(s)) {
				Person existingRel = Person.allPers.stream().filter(p -> p.name.equals(s)).collect(Collectors.toList())
						.get(0);
				if (existingRel.isAlive != isAlive) {
					existingRel.isAlive = isAlive;
				}
				rels.add(existingRel);
				continue;
			}
			id = ai.incrementAndGet();
			Person relative = new Person(id, s, relGender, isAlive);
			rels.add(relative);
		}
		Relations.relationStart(mainPerson, rels, relation);
	}

}
