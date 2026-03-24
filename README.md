# 🎓 Student CRM

**Module 8/9 In-Class Lab Project**  
A Java-based Student Customer Relationship Manager with GUI input, exception handling, and collections.

---

## 👥 Team Members & Roles

| Role | Responsibilities |
|---|---|
| **Front End Engineer** | GUI design, Swing components, table rendering |
| **Exception Handling Engineer** | Input validation, custom exceptions, error messages |
| **QA / Repository Master** | Testing, bug tracking, GitHub management, README |

---

## 📁 Project Structure

```
module8-9/
└── Student/
    └── src/
        ├── Main.java                          # GUI entry point
        ├── Student.java                       # Student data model
        └── Exception/
            └── StudentNotExistException.java  # Custom exception
```

---

## ⚙️ How to Run

### Requirements
- Java JDK 8 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

### Run in an IDE
1. Clone or download this repository
2. Open the `Student/src/` folder as a Java project
3. Run `Main.java`

### Run from Command Line
```bash
# Navigate to the src folder
cd Student/src

# Compile all files
javac -d . Exception/StudentNotExistException.java Student.java Main.java

# Run the program
java Main
```

---

## 🖥️ How to Use

### Add a Student
1. Fill in the **Name** field (letters only, min 2 characters)
2. Enter **Age** (must be a whole number between 1 and 120)
3. Select **Gender** from the dropdown (Male / Female / Other)
4. Select **Grade** from the dropdown (A / B / C / D / F)
5. Click **Add** — the student appears in the table on the right

### Search
- Type in the **Search by name** box to filter the student list in real time

### Delete a Student
1. Click on a student row in the table to select it
2. Click **Delete Selected**
3. Confirm in the dialog box

### Clear the Form
- Click **Clear** to reset all input fields

---

## 🔒 Exception Handling

The program handles the following input errors:

| Situation | Error Message |
|---|---|
| Name left empty | `Name cannot be empty.` |
| Name too short (< 2 chars) | `Name must be at least 2 characters.` |
| Name contains numbers or symbols | `Name can only contain letters, spaces, hyphens, and apostrophes.` |
| Age is not a number (e.g. `abc`) | `Age must be a whole number (e.g. 20).` |
| Age out of range | `Age must be between 1 and 120.` |
| Delete without selecting a row | `Please select a student from the list first.` |
| Student not found in system | `StudentNotExistException` is thrown and caught |

All errors are displayed in a popup dialog so the program never crashes on bad input.

---

## 📦 Collections Used

```java
// Stores all student records in insertion order
ArrayList<Student> studentList = new ArrayList<>();
```

Students are added, removed, and searched using this list. No database is required — all data is stored in memory during the session.

---

## 🧩 Class Overview

### `Student.java`
Data model with four fields:

| Field | Type | Description |
|---|---|---|
| `name` | `String` | Student's full name |
| `age` | `int` | Student's age |
| `gender` | `int` | 1 = Male, 2 = Female, 3 = Other |
| `grade` | `char` | Letter grade: A, B, C, D, or F |

### `StudentNotExistException.java`
Custom `RuntimeException` thrown when attempting to delete a student who cannot be found in the list.

### `Main.java`
Swing GUI application containing:
- Input form (left panel)
- Student table with color-coded grades (right panel)
- Real-time search/filter
- Full exception handling on all inputs

---

## ✅ QA Test Cases

### Normal Flow
| Test | Expected Result |
|---|---|
| Add student with valid inputs | Student appears in table |
| Search by partial name | Table filters in real time |
| Delete a selected student | Student removed from table |
| Click Clear | All fields reset |

### Exception / Edge Cases
| Input | Expected Error |
|---|---|
| Name = *(empty)* | Popup: Name cannot be empty |
| Name = `J` | Popup: min 2 characters |
| Name = `J0hn!!` | Popup: letters only |
| Age = `twenty` | Popup: must be a whole number |
| Age = `0` or `999` | Popup: must be between 1 and 120 |
| Delete with no row selected | Popup: select a student first |
