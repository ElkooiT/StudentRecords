import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentManager {

    private static final String DATA_DIR        = "student_data";
    private static final String TEXT_FILE       = DATA_DIR + "/students.txt";
    private static final String BINARY_FILE     = DATA_DIR + "/students.dat";
    private static final String SERIAL_FILE     = DATA_DIR + "/students.ser";
    private static final String BACKUP_FILE     = DATA_DIR + "/backup/students_backup.txt";
    private static final String REPORT_FILE     = DATA_DIR + "/report.txt";


    private List<Student> students;

    public StudentManager() {
        students = new ArrayList<>();
        createDirectories();    // make sure folders exist before we try to write
        loadFromTextFile();     // load any previously saved students into memory
    }

    //  FILE CLASS USAGE — create dirs/files automatically

    private void createDirectories() {
        // File class lets us inspect and manipulate the filesystem.
        File dataDir   = new File(DATA_DIR);
        File backupDir = new File(DATA_DIR + "/backup");

        if (!dataDir.exists())   dataDir.mkdirs();
        if (!backupDir.exists()) backupDir.mkdirs();

        System.out.println("=== Directory Setup ===");
        displayFileProperties(dataDir);
    }

    // Display file properties — required by the spec

    public void displayFileProperties(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("  Name          : " + file.getName());
        System.out.println("  Absolute Path : " + file.getAbsolutePath());
        System.out.println("  Size (bytes)  : " + file.length());
        System.out.println("  Last Modified : " + sdf.format(new Date(file.lastModified())));
        System.out.println("  Is Directory  : " + file.isDirectory());
        System.out.println();
    }

    //  ADD STUDENT

    public void addStudent(Student s) {
        // Check for duplicate IDs
        for (Student existing : students) {
            if (existing.getStudentId().equals(s.getStudentId())) {
                System.out.println("[WARN] Student ID " + s.getStudentId() + " already exists.");
                return;
            }
        }
        students.add(s);
        saveToTextFile();   // keep disk in sync after every change
        System.out.println("[ADD] " + s);
    }

    //  SEARCH BY ID
    public Student searchById(String id) {
        for (Student s : students) {
            if (s.getStudentId().equals(id)) {
                return s;
            }
        }
        return null; // not found — caller must handle the null
    }

    //  UPDATE STUDENT

    public boolean updateStudent(String id, String newName, String newDept, double newGpa) {
        Student s = searchById(id);
        if (s == null) {
            System.out.println("[UPDATE] Student " + id + " not found.");
            return false;
        }
        s.setName(newName);
        s.setDepartment(newDept);
        s.setGpa(newGpa);
        saveToTextFile();
        System.out.println("[UPDATE] " + s);
        return true;
    }


    //  DELETE STUDENT

    public boolean deleteStudent(String id) {
        // removeIf is a clean way to delete by condition
        boolean removed = students.removeIf(s -> s.getStudentId().equals(id));
        if (removed) {
            saveToTextFile();
            System.out.println("[DELETE] Removed student ID: " + id);
        } else {
            System.out.println("[DELETE] Student " + id + " not found.");
        }
        return removed;
    }


    //  DISPLAY ALL

    public void displayAll() {
        System.out.println("\n=== All Students (" + students.size() + ") ===");
        if (students.isEmpty()) {
            System.out.println("  (no records)");
            return;
        }
        for (Student s : students) {
            System.out.println("  " + s);
        }
        System.out.println();
    }

    //  TEXT FILE — Scanner & PrintWriter

    public void saveToTextFile() {
        // try-with-resources: automatically closes the writer even if an exception is thrown.
        try (PrintWriter pw = new PrintWriter(new FileWriter(TEXT_FILE))) {
            for (Student s : students) {
                pw.println(s.getStudentId() + "|" + s.getName() + "|"
                         + s.getDepartment() + "|" + s.getGpa());
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Could not save text file: " + e.getMessage());
        }
    }


     //Load students from the .txt file using Scanner.

    public void loadFromTextFile() {
        File file = new File(TEXT_FILE);
        if (!file.exists()) return; // first run — nothing to load

        try (Scanner sc = new Scanner(file)) {
            students.clear();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|"); // split on the pipe character
                if (parts.length == 4) {
                    students.add(new Student(
                        parts[0], parts[1], parts[2], Double.parseDouble(parts[3])
                    ));
                }
            }
            System.out.println("[LOAD] Loaded " + students.size() + " students from text file.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("[ERROR] Could not load text file: " + e.getMessage());
        }
    }


    //  BINARY FILE — DataOutputStream & DataInputStream


//dat file
    public void saveToBinaryFile() {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(BINARY_FILE)))) {

            dos.writeInt(students.size()); // write count first so we know how many to read back
            for (Student s : students) {
                dos.writeUTF(s.getStudentId());
                dos.writeUTF(s.getName());
                dos.writeUTF(s.getDepartment());
                dos.writeDouble(s.getGpa());
            }
            System.out.println("[BINARY] Saved " + students.size() + " students to " + BINARY_FILE);
        } catch (IOException e) {
            System.err.println("[ERROR] Binary save failed: " + e.getMessage());
        }
    }

// read from dat file
    public List<Student> loadFromBinaryFile() {
        List<Student> result = new ArrayList<>();
        File file = new File(BINARY_FILE);
        if (!file.exists()) {
            System.out.println("[BINARY] No binary file found.");
            return result;
        }

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(BINARY_FILE)))) {

            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                result.add(new Student(
                    dis.readUTF(),
                    dis.readUTF(),
                    dis.readUTF(),
                    dis.readDouble()
                ));
            }
            System.out.println("[BINARY] Loaded " + result.size() + " students from binary file.");
        } catch (IOException e) {
            System.err.println("[ERROR] Binary load failed: " + e.getMessage());
        }
        return result;
    }


    //  OBJECT SERIALIZATION — ObjectOutputStream & ObjectInputStream
// serialize the entire list to a .ser file
    public void serializeStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(SERIAL_FILE)))) {

            oos.writeObject(students); // write the whole list as one object
            System.out.println("[SERIAL] Serialized " + students.size() + " students to " + SERIAL_FILE);
        } catch (IOException e) {
            System.err.println("[ERROR] Serialization failed: " + e.getMessage());
        }
    }

// deserialize the list from the .ser file
    @SuppressWarnings("unchecked") // safe cast — we know we wrote a List<Student>
    public List<Student> deserializeStudents() {
        File file = new File(SERIAL_FILE);
        if (!file.exists()) {
            System.out.println("[SERIAL] No serialized file found.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(SERIAL_FILE)))) {

            List<Student> result = (List<Student>) ois.readObject();
            System.out.println("[SERIAL] Deserialized " + result.size() + " students.");
            return result;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR] Deserialization failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    //  BUFFERED STREAM BACKUP


// Create a backup of the text file using BufferedReader and BufferedWriter
    public void createBackup() {
        File source = new File(TEXT_FILE);
        if (!source.exists()) {
            System.out.println("[BACKUP] Source file not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(BACKUP_FILE))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("[BACKUP] Backup created at: " + BACKUP_FILE);
            displayFileProperties(new File(BACKUP_FILE));
        } catch (IOException e) {
            System.err.println("[ERROR] Backup failed: " + e.getMessage());
        }
    }

    //  REPORT GENERATION
    public void generateReport() {
        if (students.isEmpty()) {
            System.out.println("[REPORT] No students to report on.");
            return;
        }

        // Calculate stats
        int    total   = students.size();
        double highest = students.stream().mapToDouble(Student::getGpa).max().orElse(0);
        double lowest  = students.stream().mapToDouble(Student::getGpa).min().orElse(0);
        double average = students.stream().mapToDouble(Student::getGpa).average().orElse(0);

        Student topStudent = students.stream()
            .filter(s -> s.getGpa() == highest)
            .findFirst().orElse(null);
        Student lowStudent = students.stream()
            .filter(s -> s.getGpa() == lowest)
            .findFirst().orElse(null);

        // Build report string
        String report = String.format(
            "========== STUDENT REPORT ==========\n" +
            "Total Students : %d\n" +
            "Highest GPA    : %.2f  (%s)\n" +
            "Lowest GPA     : %.2f  (%s)\n" +
            "Average GPA    : %.2f\n" +
            "=====================================\n",
            total,
            highest, topStudent != null ? topStudent.getName() : "N/A",
            lowest,  lowStudent  != null ? lowStudent.getName()  : "N/A",
            average
        );

        System.out.println(report);

        // Save the report to a file
        try (PrintWriter pw = new PrintWriter(new FileWriter(REPORT_FILE))) {
            pw.print(report);
            System.out.println("[REPORT] Saved to " + REPORT_FILE);
        } catch (IOException e) {
            System.err.println("[ERROR] Could not save report: " + e.getMessage());
        }
    }
}
// END OF StudentManager.java
