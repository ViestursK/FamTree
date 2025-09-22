import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DB {
	Connection con; // connection to the DB
	Connection con2;
	Statement stmt; // To call requests and get responses. If using parallel threads, each needs its
					// own Statement
	PreparedStatement pstmt; // To prevent injections
	ResultSet rs; // holds response from the DB
	private String params = "?useSSL=false&autoReconnect=true&allowMultiQueries=true";
	private String username = "root";
	private String pw = "";
	private String dbName;

//	DataBase class constructor - creates connection and 
//	all the necessary elements to work with DB using mysql.connector library
	public DB() {
		try {
			// Explicitly load the MySQL JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + this.params, this.username,
					this.pw);
			this.stmt = con.createStatement();
			System.out.println("Connection successful!");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found! Make sure mysql-connector-java-5.1.48.jar is in classpath");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Problems with DB setup!");
			e.printStackTrace();
		}
	}

//	SQL query execution method to input data into DB - taken from lecture code about DB
	void execute(String query) {
		try {
			if (stmt != null) {
				stmt.execute(query);
			} else {
				System.out.println("Database connection not established. Cannot execute query.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	SQL query execution method to get data from DB - taken from lecture code about DB
	ResultSet executeGet(String query) {
		try {
			if (stmt != null) {
				return stmt.executeQuery(query);
			} else {
				System.out.println("Database connection not established. Cannot execute query.");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	database creation method, checks the given name and creates DB if it does not exist
	void createDB(String dbName) {
		if (!dbName.matches("^[a-zA-Z]+$")) {
			System.out.println("ERROR! Please enter a valid dbName");
		}
		try {
			this.dbName = dbName.toLowerCase();
			this.execute("CREATE DATABASE IF NOT EXISTS " + this.dbName + ";");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	after DB is created 3 tables are created as well to store relation data of this project
//	- idea of  table structure found on StackOverFlow
	void createTables() {
		try {
			String tb1 = "USE " + this.dbName

					+ "; CREATE TABLE IF NOT EXISTS persons(id INT PRIMARY KEY, pers_name VARCHAR(50) NOT NULL, isAlive BOOLEAN NOT NULL, gender VARCHAR(1));";
			String tb2 = "USE " + this.dbName
					+ "; CREATE TABLE IF NOT EXISTS relation_types(id INT PRIMARY KEY, rel_type VARCHAR(50) NOT NULL);";
			String tb3 = "USE " + this.dbName + "; CREATE TABLE IF NOT EXISTS relations(" + "id INT PRIMARY KEY,"
					+ "pers_id INT," + "relative_id INT ," + "rel_type INT," + "reverse_rel_type INT,"
					+ "FOREIGN KEY (pers_id) REFERENCES persons(id),"
					+ "FOREIGN KEY (relative_id) REFERENCES persons(id),"
					+ "FOREIGN KEY (rel_type) REFERENCES relation_types(id),"
					+ "FOREIGN KEY (reverse_rel_type) REFERENCES relation_types(id));";
			this.execute(tb1);
			this.execute(tb2);
			this.execute(tb3);
		} catch (Exception e) {
			System.out.println("Problems with table creation!");
			e.printStackTrace();
		}
	}

//	inserting each created person in project person into DB table "persons"
	void inputPersons(int id, String persName, boolean isAlive, String gender) {
		try {
			if (con != null) {
				String query = "USE " + this.dbName
						+ "; INSERT IGNORE INTO persons (id, pers_name, isAlive, gender) VALUES (?, ?, ?, ?);";
				pstmt = this.con.prepareStatement(query);
				pstmt.setInt(1, id);
				pstmt.setString(2, persName);
				pstmt.setBoolean(3, isAlive);
				pstmt.setString(4, gender);
				pstmt.executeUpdate();
			} else {
				System.out.println("Database connection not established. Cannot input persons.");
			}
		} catch (Exception e) {
			System.out.println("Problems with data input - persons table!");
			e.printStackTrace();
		}
	}

//	inserting each relation types allowed in this project into DB table "relation_types"
	void inputRelationTypes() {
		try {
			if (con != null) {
				String query = "USE " + this.dbName + "; INSERT IGNORE INTO relation_types (id, rel_type) VALUES (?, ?);";
				for (int i = 0; i < Relations.rels.length; i++) {
					pstmt = this.con.prepareStatement(query);
					pstmt.setInt(1, i);
					pstmt.setString(2, Relations.rels[i]);
					pstmt.executeUpdate();
					Relations.relationTypes.put(Relations.rels[i], i);
				}
			} else {
				System.out.println("Database connection not established. Cannot input relation types.");
			}

		} catch (Exception e) {
			System.out.println("Problems with data input into relation_types table!");
			e.printStackTrace();
		}
	}

//	inserting each relation created in this project into DB table "relations"
	void inputRelations() {
		try {
			if (con != null) {
				String query = "USE " + this.dbName
						+ "; INSERT IGNORE INTO relations(id, pers_id, relative_id, rel_type, reverse_rel_type) VALUES (?, ?, ?, ?, ?);";
				int rowId = 0;
				for (Person p : Person.allPers) {
					if (p.father != null) {
						rowId++;
						pstmt = this.con.prepareStatement(query);
						pstmt.setInt(1, rowId);
						pstmt.setInt(2, p.id);
						pstmt.setInt(3, p.father.id);
						pstmt.setInt(4, Relations.relationTypes.get("father"));
						if (p.gender.equals("M")) {
							pstmt.setInt(5, Relations.relationTypes.get("son"));
						} else {
							pstmt.setInt(5, Relations.relationTypes.get("daughter"));
						}
						pstmt.executeUpdate();
					}
					if (p.mother != null) {
						rowId++;
						pstmt = this.con.prepareStatement(query);
						pstmt.setInt(1, rowId);
						pstmt.setInt(2, p.id);
						pstmt.setInt(3, p.mother.id);
						pstmt.setInt(4, Relations.relationTypes.get("mother"));
						if (p.gender.equals("M")) {
							pstmt.setInt(5, Relations.relationTypes.get("son"));
						} else {
							pstmt.setInt(5, Relations.relationTypes.get("daughter"));
						}
						pstmt.executeUpdate();
					}
					if (p.husband != null) {
						rowId++;
						pstmt = this.con.prepareStatement(query);
						pstmt.setInt(1, rowId);
						pstmt.setInt(2, p.id);
						pstmt.setInt(3, p.husband.id);
						pstmt.setInt(4, Relations.relationTypes.get("husband"));
						pstmt.setInt(5, Relations.relationTypes.get("wife"));
						pstmt.executeUpdate();
					}
					if (p.wife != null) {
						rowId++;
						pstmt = this.con.prepareStatement(query);
						pstmt.setInt(1, rowId);
						pstmt.setInt(2, p.id);
						pstmt.setInt(3, p.wife.id);
						pstmt.setInt(4, Relations.relationTypes.get("wife"));
						pstmt.setInt(5, Relations.relationTypes.get("husband"));
						pstmt.executeUpdate();
					}
					if (!p.siblings.isEmpty()) {
						for (Person s : p.siblings) {
							rowId++;
							pstmt = this.con.prepareStatement(query);
							pstmt.setInt(1, rowId);
							pstmt.setInt(2, p.id);
							pstmt.setInt(3, s.id);
							if (s.gender.equals("M")) {
								pstmt.setInt(4, Relations.relationTypes.get("brother"));
							} else {
								pstmt.setInt(4, Relations.relationTypes.get("sister"));
							}
							if (p.gender.equals("M")) {
								pstmt.setInt(5, Relations.relationTypes.get("brother"));
							} else {
								pstmt.setInt(5, Relations.relationTypes.get("sister"));
							}
							pstmt.executeUpdate();
						}
					}
					if (!p.kids.isEmpty()) {
						for (Person k : p.kids) {
							rowId++;
							pstmt = this.con.prepareStatement(query);
							pstmt.setInt(1, rowId);
							pstmt.setInt(2, p.id);
							pstmt.setInt(3, k.id);
							if (k.gender.equals("M")) {
								pstmt.setInt(4, Relations.relationTypes.get("son"));
							} else {
								pstmt.setInt(4, Relations.relationTypes.get("daughter"));
							}
							if (p.gender.equals("M")) {
								pstmt.setInt(5, Relations.relationTypes.get("father"));
							} else {
								pstmt.setInt(5, Relations.relationTypes.get("mother"));
							}
							pstmt.executeUpdate();
						}
					}

				}
			} else {
				System.out.println("Database connection not established. Cannot input relations.");
			}

		} catch (Exception e) {
			System.out.println("Problem inputting relations.");
			e.printStackTrace();
		}
	}

//	getting data about every person from DB and refilling Person.allPers hashset with objects
	void readPersTable() {
		try {
			if (stmt != null) {
				String query = "SELECT * FROM persons";
				this.rs = this.executeGet(query);
				if (rs != null) {
					while (rs.next()) {
						new Person(rs.getInt("id"), rs.getString("pers_name"), rs.getString("gender"),
								rs.getBoolean("isAlive"));
					}
					rs.close();
				}
			} else {
				System.out.println("Database connection not established. Cannot read persons table.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	getting data about relation types from DB and refilling list created before as well 
//	as reversing to ease relation reading 
	void readRelTypes() {
		try {
			if (stmt != null) {
				String query = "SELECT * FROM relation_types";
				this.rs = this.executeGet(query);
				if (rs != null) {
					while (rs.next()) {
						Relations.relationTypes.put(rs.getString("rel_type"), rs.getInt("id"));
						Relations.revRelationTypes.put(rs.getInt("id"), rs.getString("rel_type"));
					}
					rs.close();
				}
			} else {
				System.out.println("Database connection not established. Cannot read relation types.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
// getting data about relations from DB and setting relations in attributes of each person
	void readRelationData() {
		try {
			if (stmt != null) {
				String query = "SELECT * FROM relations";
				this.rs = this.executeGet(query);
				if (rs != null) {
					while (rs.next()) {
						int persId = rs.getInt("pers_id");
						int relativeId = rs.getInt("relative_id");
						int relType = rs.getInt("rel_type");
						Person mainPerson = Person.allPers.stream().filter(p -> p.id == persId).collect(Collectors.toList())
								.get(0);
						Person rel = Person.allPers.stream().filter(p -> p.id == relativeId).collect(Collectors.toList())
								.get(0);
						HashSet<Person> temp = new HashSet<Person>();
						temp.add(rel);
						Relations.relationStart(mainPerson, temp, Relations.revRelationTypes.get(relType));

					}
					rs.close();
				}
			} else {
				System.out.println("Database connection not established. Cannot read relation data.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}