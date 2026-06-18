# Student Record Management System
**OOP Assignment — Java File I/O & Streams**

---

## Project Structure

```
StudentRecords/
├── Student.java          # Data class (the blueprint for a student)
├── StudentManager.java   # Operations class (add, search, update, delete, I/O)
├── Main.java             # Entry point — runs a demo of every requirement
└── student_data/         # Auto-created at runtime
    ├── students.txt      # Text file storage
    ├── students.dat      # Binary file storage
    ├── students.ser      # Serialized object storage
    ├── report.txt        # Generated GPA report
    └── backup/
        └── students_backup.txt
```

---

## How to Run

**Requirements:** JDK 8 or higher installed.

```bash
# 1. Navigate to the project folder
cd StudentRecords

# 2. Compile all Java files
javac *.java

# 3. Run the program
java Main
```

The program runs automatically — no user input needed.  
All output files are created inside a `student_data/` folder in the same directory.

---

## What Each File Does

### `Student.java`
Defines what a student **is**. Contains:
- Private fields: `studentId`, `name`, `department`, `gpa`
- A constructor to create students
- Getters and setters for controlled access
- `toString()` for clean printing
- Implements `Serializable` so objects can be written to `.ser` files

### `StudentManager.java`
Defines what you can **do** with students. Contains all CRUD operations and every file I/O method:

| Method | What it does |
|---|---|
| `addStudent(Student)` | Adds a student, rejects duplicates |
| `searchById(String)` | Returns a Student or null |
| `updateStudent(...)` | Updates name, dept, GPA by ID |
| `deleteStudent(String)` | Removes a student by ID |
| `displayAll()` | Prints all students to console |
| `saveToTextFile()` | Writes to `.txt` using PrintWriter |
| `loadFromTextFile()` | Reads from `.txt` using Scanner |
| `saveToBinaryFile()` | Writes to `.dat` using DataOutputStream |
| `loadFromBinaryFile()` | Reads from `.dat` using DataInputStream |
| `serializeStudents()` | Writes objects to `.ser` using ObjectOutputStream |
| `deserializeStudents()` | Reads objects from `.ser` using ObjectInputStream |
| `createBackup()` | Copies `.txt` to backup using BufferedReader/Writer |
| `generateReport()` | Calculates and saves GPA stats |
| `displayFileProperties(File)` | Shows name, path, size, last modified |

### `Main.java`
Demos every requirement in order. No menu — just runs top to bottom.

---

## Requirements Checklist

| Requirement | Status |
|---|---|
| Student class (ID, Name, Department, GPA) | ✅ `Student.java` |
| Add Student | ✅ `addStudent()` |
| Search Student by ID | ✅ `searchById()` |
| Update Student Information | ✅ `updateStudent()` |
| Delete Student | ✅ `deleteStudent()` |
| Display All Students | ✅ `displayAll()` |
| Text Files — Scanner & PrintWriter | ✅ `loadFromTextFile()` / `saveToTextFile()` |
| Binary Files — DataInputStream & DataOutputStream | ✅ `saveToBinaryFile()` / `loadFromBinaryFile()` |
| Object Serialization — ObjectInputStream & ObjectOutputStream | ✅ `serializeStudents()` / `deserializeStudents()` |
| Report: Total, Highest, Lowest, Average GPA | ✅ `generateReport()` |
| File class — auto-create directories/files | ✅ `createDirectories()` |
| File class — display file properties | ✅ `displayFileProperties()` |
| Buffered Streams for backup | ✅ `createBackup()` |
| Exception Handling | ✅ try-catch on every I/O method |

---

## Key OOP Concepts Used

**Encapsulation** — All fields in `Student` are `private`. Nothing outside the class can read or change them directly; everything goes through getters and setters.

**Single Responsibility Principle** — `Student` only knows about student data. `StudentManager` only knows about operations. Each class has one job.

**Serializable interface** — A marker interface that gives Java permission to convert a `Student` object into bytes for binary storage.

**try-with-resources** — Used on every file operation. Guarantees the file is closed properly even if an exception occurs, preventing data corruption and resource leaks.
