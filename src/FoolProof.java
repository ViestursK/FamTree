import java.util.Collections;

public class FoolProof {

	static boolean checkCount(int count) {
		int min = Collections.min(FileReader.wordsToNums.values());
		int max = Collections.max(FileReader.wordsToNums.values());
		if ((count >= min && count <= max)) {
			return true;
		}
		return false;
	}

	static String[] getOtherCount(String offsprings) {
		String[] arr = offsprings.strip().replaceAll("\\p{Punct}", "").split(" ");
		return arr;
	}

	static boolean compareCount(int one, int two) {
		if (one == two)
			return true;
		return false;
	}

	static boolean checkGender(String letter) {
		if (letter.equalsIgnoreCase("M") || letter.equalsIgnoreCase("F")) {
			return true;
		}
		return false;
	}

	static boolean affiliation(String[] words) {
		for (String s : words) {
			if (s.equals("has") || s.equals("had")) {
				return true;
			}
		}
		return false;
	}

	static boolean isAlive(String[] words) {
		for (String i : words) {
			if (i.equals("has"))
				return true;
		}
		return false;
	}


	static String checkRelationType(String text) {
		for (String s : Relations.rels) {
			for (String a : text.split(" ")) {
				if (a.startsWith(s))
					return s;
			}
		}

		return null;
	}

}
