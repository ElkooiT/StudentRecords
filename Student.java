import java.io.Serializable;

public class Student implements Serializable {

    // --- Why Serializable? ---
    // ObjectOutputStream needs this "marker interface" to convert the object
    // into bytes and write it to a binary file. Without it, you get a runtime error.

    private static final long serialVersionUID = 1L; // version stamp for serialization

    // --- Access Modifiers ---
    // private: no one outside this class can read/write these directly.
    // This is ENCAPSULATION — a core OOP pillar.
    // We control access through getters/setters, so we can add validation later.
    private String studentId;
    private String name;
    private String department;
    private double gpa;

    // --- Constructor ---
    // public: anyone can create a Student object.
    public Student(String studentId, String name, String department, double gpa) {
        this.studentId  = studentId;
        this.name       = name;
        this.department = department;
        this.gpa        = gpa;
    }

    // --- Getters (public) ---
    // Read-only access to private fields.
    public String getStudentId()  { return studentId; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
    public double getGpa()        { return gpa; }

    // --- Setters (public) ---
    // Write access — only for fields that are allowed to change.
    // (We don't expose a setStudentId because IDs should never change.)
    public void setName(String name)             { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setGpa(double gpa)               { this.gpa = gpa; }

    // --- toString ---
    // Called automatically whenever you print a Student object.
    @Override
    public String toString() {
        return String.format("ID: %-8s | Name: %-20s | Dept: %-15s | GPA: %.2f",
                studentId, name, department, gpa);
    }
}
