package au.edu.holmesglen.kirstine_n.tute3teacherlogin;

/**
 * Created by Kirsti on 4/11/2016.
 */

public class Course {
    int id;
    String course;

    // constructors
    public Course() {
    }

    public Course(String course) {
        this.course = course;
    }

    public Course(int id, String course) {
        this.id = id;
        this.course = course;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
