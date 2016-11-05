package au.edu.holmesglen.kirstine_n.tute3teacherlogin;

/**
 * Student: Kirstine B. Nielsen
 * Id:      100527988
 * Date:    04.11.2016
 * Name:    Tute 3 - Enrolment System
 * Version: 1
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static au.edu.holmesglen.kirstine_n.tute3teacherlogin.MainActivity.LOG_TAG;

/**
 * Class to help us interact with our database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "courseEnrolment";

    // Table Names
    private static final String TABLE_STUDENT = "student";
    private static final String TABLE_COURSE = "course";
    private static final String TABLE_COURSE_PARTICIPANT = "courseParticipant";

    // Common column names
    private static final String KEY_ID = "id";

    // TABLE_STUDENT column names
    private static final String KEY_FIRSTNAME = "firstName";
    private static final String KEY_LASTNAME = "lastName";
    private static final String KEY_FK_COURSE_ID = "courseId";

    // TABLE_COURSE column names
    private static final String KEY_COURSE_NAME = "courseName";

    // Table Create Statements
    // TABLE_STUDENT table create statement
    private static final String CREATE_TABLE_STUDENT = "CREATE TABLE "
            + TABLE_STUDENT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRSTNAME
            + " TEXT," + KEY_LASTNAME + " TEXT," + KEY_FK_COURSE_ID + " INTEGER)";

    // TABLE_COURSE table create statement
    private static final String CREATE_TABLE_COURSE = "CREATE TABLE "
            + TABLE_COURSE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COURSE_NAME
            + " TEXT)";

    // Insert courses hardcoded
    private static final String INSERT_RECORD_COURSE_NET = "INSERT INTO " + TABLE_COURSE +
            " VALUES (1, 'Networking');";

    private static final String INSERT_RECORD_COURSE_SOFT = "INSERT INTO " + TABLE_COURSE +
            " VALUES (2, 'Software Development');";


    /**
     * constructor
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_STUDENT);
        db.execSQL(CREATE_TABLE_COURSE);

        // inserts
        db.execSQL(INSERT_RECORD_COURSE_NET);
        db.execSQL(INSERT_RECORD_COURSE_SOFT);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_COURSE);

        // create new tables
        onCreate(db);
    }


    /**
     * create a student in db
     * @param student  student obj
     * @param courseId  foreign key
     * @return
     */
    public long createStudent(Student student, int courseId) {
        Log.i(LOG_TAG, "in createStudent");
        Log.i(LOG_TAG, "student has fk courseId: " + courseId);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, student.getId());
        values.put(KEY_FIRSTNAME, student.getFirstName());
        values.put(KEY_LASTNAME, student.getLastName());
        values.put(KEY_FK_COURSE_ID, courseId);

        // insert row
        long studId = db.insert(TABLE_STUDENT, null, values);

        Log.i(LOG_TAG, "student record created in db, student id: " + studId);
        return studId;
    }


    /**
     * get number of records from student table with a specific student id
     * will return zero or max one
     * @param studentId   the stud id to include in where clause
     * @return  integer representing number of records (zero or one)
     */
    public int getAmountOfStudentsById(int studentId) {
        int amount;

        String selectQuery = "SELECT * FROM " + TABLE_STUDENT + " WHERE " + KEY_ID
                + " = " + studentId +  ";";

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        amount = cursor.getCount();

        Log.i(LOG_TAG, "students records returned: " + amount);
        return amount;

    }


    /**
     * get all students in a certain course
     * @param courseId  the course id to include in where clause
     * @return  list of student objects
     */
    public List<Student> getAllStudentsByCourse(int courseId) {
        List<Student> students = new ArrayList<Student>();

        String selectQuery = "SELECT * FROM " + TABLE_STUDENT + " WHERE " + KEY_FK_COURSE_ID
                + " = " + courseId +  ";";

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
                student.setFirstName((cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME))));
                student.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));

                // adding to students list
                students.add(student);
            } while (cursor.moveToNext());
        }

        Log.i(LOG_TAG, "students records returned: " + students.size());
        return students;
    }


    /**
     * get course id for record that matches a specific course name
     * @param courseName  course to include in where clause
     * @return  integer representing the course id
     */
    public int getCourseId(String courseName) {
        int id;

        String selectQuery = "SELECT * FROM " + TABLE_COURSE + " WHERE " + KEY_COURSE_NAME
                + " = '" + courseName +  "';";

        Log.i(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        Log.i(LOG, "courseId: " + id);

        return id;
    }


    /**
     * handle the closing of our db
     */
    public void closeDB() {
        Log.i(LOG, "in closeDB");
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
