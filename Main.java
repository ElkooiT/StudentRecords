public class Main {

    public static void main(String[] args) {

        System.out.println("");
        System.out.println("        Student Record Management System       ");
        System.out.println("\n");

        StudentManager manager = new StudentManager();  // sets up dirs, loads existing data

        // Implement Add Student
        System.out.println("    Adding Students");
        manager.addStudent(new Student("S001", "Fikadu Molla",  "Computer Science", 3.85));
        manager.addStudent(new Student("S002", "Fik Mo",    "Software Engineering",      3.20));
        manager.addStudent(new Student("S003", "Fike Yafd",  "ITS",      3.95));
        manager.addStudent(new Student("S004", "David Copperfield",  "ITS",          2.75));
        manager.addStudent(new Student("S005", "Fi MY",    "Computer Science", 3.60));
        manager.addStudent(new Student("S001", "Alice Borderland",      "N/A",              0.0)); // dup test
        System.out.println();

        // ── 2. DISPLAY ALL 
        manager.displayAll();

        //  3. SEARCH 
        System.out.println("=== Search ===");
        Student found = manager.searchById("S003");
        System.out.println(found != null ? "[FOUND] " + found : "[NOT FOUND]");

        Student notFound = manager.searchById("S999");
        System.out.println(notFound != null ? "[FOUND] " + notFound : "[NOT FOUND] S999");
        System.out.println();

        //  4. UPDATE 
        System.out.println("=== Update ===");
        manager.updateStudent("S002", "Brian Nkosi", "Electrical Engineering", 3.45);
        System.out.println();

        //  5. BINARY FILE 
        System.out.println("=== Binary File ===");
        manager.saveToBinaryFile();
        manager.loadFromBinaryFile();
        System.out.println();

        //  6. OBJECT SERIALIZATION 
        System.out.println("=== Object Serialization ===");
        manager.serializeStudents();
        manager.deserializeStudents();
        System.out.println();

        //  7. REPORT 
        manager.generateReport();

        //  8. BACKUP 
        System.out.println("=== Buffered Backup ===");
        manager.createBackup();

        //  9. DELETE 
        System.out.println("=== Delete ===");
        manager.deleteStudent("S004");
        manager.deleteStudent("S999"); // not found test
        System.out.println();

        //  10. FINAL STATE 
        manager.displayAll();

        System.out.println("Done! Check the 'student_data' folder for all generated files.");
    }
}

/* you might notice that the report.txt shows total students as 5 but there are only 4 on the students.txt. 
this is beacuase the report is generated before the delete operation.
 the delete operation removes S004 from the students.txt but the report was generated when S004 was still in the list,
so it counts S004 in the total. if you want to see the updated report after deletion, 
you can call manager.generateReport() again after the delete operations in Main.java.
 */
