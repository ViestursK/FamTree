import java.util.HashSet;
import java.util.stream.Collectors;

public class Person {

	HashSet<Person> brothers = new HashSet<Person>();
	HashSet<Person> sisters = new HashSet<Person>();
	HashSet<Person> daughters = new HashSet<Person>();
	HashSet<Person> sons = new HashSet<Person>();
	HashSet<Person> siblings = new HashSet<Person>();
	HashSet<Person> kids = new HashSet<Person>();

	static HashSet<Person> allPers = new HashSet<Person>();
	Person mother;
	Person father;
	Person husband;
	Person wife;
	boolean isAlive;
	String name;
	String gender;
	int id;
	String pronoun;
	String pronoun2;

	Person(int id, String name, String gender, boolean isAlive) {
		this.id = id;
		this.name = setFirstLetterToUpper(name);
		this.gender = gender;
		this.isAlive = isAlive;
		allPers.add(this);
		this.setPrononuns();

	}

	void setPrononuns() {
		if (this.gender.equals("M")) {
			this.pronoun = "he";
			this.pronoun2 = "his";
		}
		if (this.gender.equals("F")) {
			this.pronoun = "she";
			this.pronoun2 = "her";

		}
	}

	void setMom(Person mom) {
		this.mother = mom;
		mom.kids.add(this);

	}

	void setDad(Person dad) {
		this.father = dad;
		dad.kids.add(this);

	}

	void setKids(HashSet<Person> kids) {
		this.kids.addAll(kids); // only when mainPerson has a son/daughter
		for (Person k : kids) { // sets father and mother to the given kids
			if (this.gender.equals("F")) {
				k.mother = this;
			} else {
				k.father = this;
			}
		}
	}

	void setSiblings(HashSet<Person> siblings) {
		for (Person s : siblings) {
			s.siblings.add(this);

		}
		this.siblings.addAll(siblings);

		HashSet<Person> temp = new HashSet<Person>();
		temp.addAll(this.siblings);
		for (Person s : temp) {
			s.siblings.addAll(siblings);
			s.siblings.addAll(this.siblings);
			s.siblings.remove(s);
		}
	}

	void setWife(Person wife) {
		this.wife = wife;
		wife.husband = this;

	}

	void setHusband(Person husband) {
		this.husband = husband;
		husband.wife = this;

	}

	static int getCount(String sentence) {
		int count = 0;
		String[] arr = sentence.split(" ");
		for (String string : arr) {
			for (int i : FileReader.wordsToNums.values()) {
				if (string.equals(Integer.toString(i))) {
					count = i;
					return count;
				}
			}
			for (String s : FileReader.wordsToNums.keySet()) {
				if (string.equals(s)) {
					count = FileReader.wordsToNums.get(s);
					return count;
				}
			}
		}
		return count;
	}

	static String getGender(String[] arr) {
		String gender = arr[0].substring(arr[0].indexOf("(") + 1, arr[0].indexOf(")"));
		if (FoolProof.checkGender(gender)) {
			return gender;
		}
		return null;
	}

	static String getName(String[] arr) {
		String name = arr[0].substring(0, arr[0].indexOf("(")).strip();
		return name;
	}

	static String allGenders(String relation) {
		String male = "husband father son brother";
		String female = "wife mother daughter sister";
		String[] males = male.split(" ");
		String[] females = female.split(" ");

		for (String s : males) {
			if (relation.startsWith(s)) {
				return "M";
			}
		}
		for (String a : females) {
			if (relation.startsWith(a)) {
				return "F";
			}
		}
		return null;
	}

	public String getPrintingInfo() {
		String result = "";
		this.name = setFirstLetterToUpper(this.name);
		result += "Chosen person: " + this.name + ". ";
		this.pronoun = setFirstLetterToUpper(this.pronoun);
		if (this.wife != null) {
			if (this.wife.isAlive) {
				result += this.pronoun + " has a wife, her name is " + this.wife.name + ". ";
			} else {
				result += this.pronoun + " had a wife, named " + this.wife.name + ". Sadly, she passed away.";
			}

		}
		if (this.husband != null) {
			if (this.husband.isAlive) {
				result += this.pronoun + " has a husband - " + this.husband.name + ". ";
			} else {
				result += this.pronoun + " had a husband, named " + this.husband.name + ". He ded.";
			}
		}

		if (this.father != null) {
			if (this.father.isAlive) {
				result += this.pronoun + " has a father " + this.father.name + ". ";
			} else {
				result += this.pronoun + " had a father " + this.father.name + ". R.I.P. ";
			}

		}
		if (this.mother != null) {
			if (this.mother.isAlive) {
				result += this.pronoun + " has a dear mother,  her name - " + this.mother.name + ". ";
			} else {
				result += this.pronoun + " had a mother,  her name was " + this.mother.name + ". ";
			}
		}
		if (!this.siblings.isEmpty()) {
			if (this.siblings.size() == 1) {
				Person p = allPers.stream().map(a -> a).collect(Collectors.toList()).get(0);
				if (p.isAlive) {
					if (p.gender.equals("M")) {
						result += this.pronoun2 + " brother is " + setFirstLetterToUpper(p.name) + ". ";
					} else {
						result += this.pronoun2 + " sister is " + setFirstLetterToUpper(p.name) + ". ";
					}
				} else {
					if (p.gender.equals("M")) {
						result += this.pronoun + " had a brother " + setFirstLetterToUpper(p.name)
								+ ". He passed away recently.";
					} else {
						result += this.pronoun + " had a sister - " + setFirstLetterToUpper(p.name) + ". ";
					}
				}
			} else {
				String brothers = "";
				String sisters = "";
				String passedSiblings = "";
				for (Person p : this.siblings) {
					if (!p.isAlive) {
						passedSiblings += " " + setFirstLetterToUpper(p.name) + ",";

					} else {
						if (p.gender.equals("M")) {
							brothers += " " + setFirstLetterToUpper(p.name) + ",";
						}
						if (p.gender.equals("F")) {
							sisters += " " + setFirstLetterToUpper(p.name) + ",";
						}

					}
				}
				if (brothers.equals(" ")) {
					result += this.pronoun + " has no brothers. ";
				} else if (brothers.endsWith(",")) {
					brothers = setFirstLetterToUpper(this.pronoun2) + " brothers are:"
							+ brothers.substring(0, brothers.lastIndexOf(","));
					brothers += ".";
					result += brothers;
				}
				if (sisters.equals(" ")) {
					result += this.pronoun + " has no sisters. ";
				} else if (sisters.endsWith(",")) {
					sisters = setFirstLetterToUpper(this.pronoun2) + " sisters are:"
							+ sisters.substring(0, sisters.lastIndexOf(" "));
					sisters += ".";
					result += sisters;
				}
				if (!passedSiblings.equals("") && passedSiblings.endsWith(",")) {
					passedSiblings = this.pronoun + " had siblings - "
							+ passedSiblings.substring(0, passedSiblings.lastIndexOf(" "));
					passedSiblings += ".";
					result += passedSiblings;

				}
			}

		}

		if (this.kids.isEmpty()) {
			result += " " + this.pronoun + " has no kids. ";
		} else if (this.kids.size() == 1) {
			result += " " + setFirstLetterToUpper(this.pronoun2) + " kid is -";
			Person p = this.kids.stream().map(a -> a).collect(Collectors.toList()).get(0);
			result += " " + p.name + ".";
		} else {
			result += " " + setFirstLetterToUpper(this.pronoun2) + " kids are:";
			for (Person p : this.kids) {
				result += " " + p.name + ",";
			}
		}
		if (result.endsWith(",")) {
			result = result.substring(0, result.lastIndexOf(","));
			result += ".";
		}

		return result;

	}

	static String setFirstLetterToUpper(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String getExtraRelInfo() {// I tried but did not make it until the dead line
		String result = "";
		if (this.father != null) {
			if (this.father.father != null && this.father.father.isAlive == true) {
				result += this.name + " also has a grandfather " + this.father.father.name + ". ";
				if (this.father.father.father != null && this.father.father.father.isAlive == true) {
					result += this.name + " also has a grand-grandfather " + this.father.father.name + ". ";
				}
			}

		}
		if (this.mother != null) {

			if (this.mother.mother != null && this.mother.mother.isAlive == true) {
				result += this.name + " also has a grandmother " + this.mother.mother.name + ". ";
			}
		}
		return result;
	}

	public String toString() {
		return this.id + " " + this.name + " " + this.gender + " isAlive: " + this.isAlive;
	}
}
