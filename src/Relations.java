import java.util.HashMap;
import java.util.HashSet;

public class Relations {
	static String relatives = "father mother son daughter sister brother husband wife";
	static String[] rels = relatives.split(" ");
	static HashMap<String, Integer> relationTypes = new HashMap<String, Integer>();
	static HashMap<Integer, String> revRelationTypes = new HashMap<Integer, String>();

//	method that starts the chain of relation creation depending on the relation type written in the sentence
	static void relationStart(Person mainPerson, HashSet<Person> relatives, String relType) {
		if (relType.startsWith("mother")) {
			setMomRelations(mainPerson, relatives);
			return;
		}
		if (relType.startsWith("father")) {
			setDadRelations(mainPerson, relatives);
			return;
		}
		if (relType.startsWith("wife")) {
			setWifeRelations(mainPerson, relatives);
			return;
		}

		if (relType.startsWith("husband")) {
			setHusbandRelations(mainPerson, relatives);
			return;
		}
		if ("brother sister".contains(relType)) {
			setSiblingRelations(mainPerson, relatives);
			return;
		}
		if ("son daughter".contains(relType)) {
			setKidsRelations(mainPerson, relatives);
		}
	}

//	sets sibling relations of kids 
	static void setKidSiblingRelations(Person mainPerson, HashSet<Person> relatives) {
		relatives.forEach(k -> {
			if (k.mother == mainPerson || k.father == mainPerson) {
				k.setSiblings(relatives);
				k.siblings.remove(k);
			}
		});
	}

//	sets all relations connected to the input "mother"
	static void setMomRelations(Person mainPerson, HashSet<Person> relatives) {
		if (mainPerson.mother != null) {
			System.out.println(mainPerson.name + " already has a mom! No new relations created!");
			return;
		}
		relatives.forEach(mom -> {
			mainPerson.setMom(mom);
			HashSet<Person> kids = new HashSet<Person>();
			kids.add(mainPerson);
			if (!mainPerson.siblings.isEmpty()) {
				mom.setKids(mainPerson.siblings);
			}
			setKidSiblingRelations(mom, kids);
			if (mainPerson.mother != null && mainPerson.mother.husband != null) {
				mainPerson.mother.husband.setKids(kids);
				mainPerson.mother.husband.setKids(mainPerson.siblings);
			}
		});

	}

//	sets all relations connected to the input "father"
	static void setDadRelations(Person mainPerson, HashSet<Person> relatives) {
		if (mainPerson.father != null) {
			System.out.println(mainPerson.name + " already has a father! No new relations created!");
			return;
		}
		relatives.forEach(father -> {
			mainPerson.setDad(father);
			HashSet<Person> kids = new HashSet<Person>();
			kids.add(mainPerson);
			if (!mainPerson.siblings.isEmpty()) {
				father.setKids(mainPerson.siblings);
			}
			setKidSiblingRelations(father, kids);
			if (mainPerson.father != null && mainPerson.father.wife != null) {
				mainPerson.father.wife.setKids(kids);
				mainPerson.father.wife.setKids(mainPerson.siblings);
			}

		});

	}

//	sets all relations regarding to the input "wife"
	static void setWifeRelations(Person mainPerson, HashSet<Person> relatives) {
		if (mainPerson.wife != null) {
			System.out.println(mainPerson.name + " already has a wife! No new relations created!");
			return;
		}
		relatives.forEach(wife -> {
			mainPerson.setWife(wife);
			mainPerson.setKids(wife.kids);
			wife.setKids(mainPerson.kids);

		});
	}

//	sets all relations regarding to the input "husband"
	static void setHusbandRelations(Person mainPerson, HashSet<Person> relatives) {
		if (mainPerson.husband != null) {
			System.out.println(mainPerson.name + " already has a husband! No new relations created!");
			return;
		}
		relatives.forEach(husband -> {
			mainPerson.setHusband(husband);
			mainPerson.setKids(husband.kids);
			husband.setKids(mainPerson.kids);

		});
	}

//	sets all relations regarding to the input "son" or "daughter"
	static void setKidsRelations(Person mainPerson, HashSet<Person> relatives) {
		mainPerson.setKids(relatives);
		setKidSiblingRelations(mainPerson, relatives);
		relatives.forEach(kid -> {
			if (!kid.siblings.isEmpty() && kid.father != null) {
				kid.siblings.forEach(s -> {
					s.setDad(mainPerson);
				});
				setKidSiblingRelations(kid.father, relatives);
			}
			if (!kid.siblings.isEmpty() && kid.mother != null) {
				kid.siblings.forEach(s -> {
					s.setMom(mainPerson);
				});
				setKidSiblingRelations(kid.father, relatives);
			}
		});
		if (mainPerson.wife != null) {
			mainPerson.setKids(mainPerson.wife.kids);
			mainPerson.wife.setKids(relatives);

		}
		if (mainPerson.husband != null) {
			mainPerson.setKids(mainPerson.husband.kids);
			mainPerson.husband.setKids(relatives);
		}
	}

//	sets all relations regarding to the input "sister" or "brother"
	static void setSiblingRelations(Person mainPerson, HashSet<Person> relatives) {
		mainPerson.setSiblings(relatives);
		HashSet<Person> mSib = new HashSet<Person>();
		mSib.add(mainPerson);
		relatives.forEach(sib -> {
			sib.setSiblings(mSib);

			if (mainPerson.father != null) {
				sib.setDad(mainPerson.father);
			}
			if (mainPerson.mother != null) {
				sib.setMom(mainPerson.mother);

			}
			if (sib.father != null) {
				mainPerson.setDad(sib.father);
				mainPerson.father.setKids(mainPerson.siblings);
				if (sib.father.wife != null) {
					sib.father.setWife(sib.mother);
					sib.father.wife.setKids(sib.siblings);
				}
			}
			if (sib.mother != null) {
				mainPerson.setMom(sib.mother);
				mainPerson.mother.setKids(mainPerson.siblings);
				if (sib.mother.husband != null) {
					sib.mother.setHusband(sib.father);
					sib.mother.husband.setKids(sib.siblings);
				}

			}
		});

	}
}
