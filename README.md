# Family Tree Application

## About This Project
This is a Java console application I developed as coursework for my university exam in 2022. It manages family relationships using user input and stores data in a MySQL database.

## What It Does
- Add family members and their relationships using simple sentences
- Store family data in a MySQL database
- View information about family members and their connections
- Handle basic family relationships like parents, children, siblings, and spouses

## Requirements

### What You Need
- **Java 16+**
- **MySQL Server** running on your computer
- **MySQL Connector JAR file** (included in project)

### Quick Setup
1. Make sure MySQL is running on `localhost:3306`
2. Default login: username `root`, no password
3. The app will create the database automatically

## How to Use

### Starting the App
Run the `Main.java` file. You'll see a menu with 3 options:
1. **Add person** - Create new family relationships
2. **Print info** - View family information  
3. **Exit program** - Save and quit

### Adding Family Relationships

#### Input Format
Type sentences like this:
```
PersonName(Gender) has/had [ammount] [relationship] - RelativeName1 RelativeName2
```

#### Example Sentences
```
John(M) has a father - Robert
Mary(F) has two brothers - Tom Mike  
Sarah(F) has a husband - David
Peter(M) has 3 sisters - Anna Lisa Emma
Lisa(F) had a mother - Margaret
David(M) had two sons - John Mark
```

#### Important Rules
- **Gender**: Use `(M)` for male, `(F)` for female (capital letters!)
- **Living vs Dead**: Use `has` for living relatives, `had` for deceased relatives
- **Numbers**: Write `a`, `one`, `two`, `three` etc. or use digits `1`, `2`, `3`
- **Count must match**: If you say "two brothers", give exactly two names
- **Use the dash**: Always put ` - ` between relationship and names
- **Person status**: `has/had` determines if the main person is alive or dead

### Supported Relationships
- `father`, `mother` (parents)
- `son`, `daughter` (children)  
- `brother`, `sister` (siblings)
- `husband`, `wife` (spouses)

### Viewing Information
1. Choose option 2 from the menu
2. Select a person by their ID number
3. See their family connections

## Example Session
```
Welcome to FamilyTree!
Choose an action:
1 - Add person

Enter text:
John(M) has a father - Robert

Choose an action:  
2 - Print info

Choose a person:
1 - John
2 - Robert

Print info:
Chosen person: John. He has a father Robert.
```

### Common Errors
- `Count mismatch`: Make sure number of names matches the count you specified
- `Database connection failed`: Check if MySQL is running
- `Invalid gender format`: Use capital letters (M) or (F), not lowercase

## Project Files
- `Main.java` - Application entry point
- `Person.java` - Represents family members
- `Relations.java` - Handles family relationships
- `DB.java` - Database operations
- `Menu.java` - User interface
- `UserInput.java` - Input handling
- `wordsToNums.txt` - Maps number words to digits

## Tips for Using
1. **Start simple**: Add basic relationships first (parents, children)
2. **Check your typing**: Wrong format will cause errors
3. **Save your work**: Choose "Save & exit" when you're done
4. **Restart if stuck**: If you get errors, restart the program

## Database
The app creates three tables automatically:
- `persons` - Stores people and basic info
- `relation_types` - Stores relationship types
- `relations` - Stores who is related to whom

Your data will be saved between sessions if you choose to save when exiting.

## Academic Context
This project demonstrates:
- Object-oriented programming in Java
- Database integration with MySQL
- Input validation and error handling
- Console-based user interfaces
- File I/O operations

Created as part of my university programming coursework in 2022.
