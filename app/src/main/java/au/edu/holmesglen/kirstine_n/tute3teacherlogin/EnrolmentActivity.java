package au.edu.holmesglen.kirstine_n.tute3teacherlogin;

/**
 * Student: Kirstine B. Nielsen
 * Id:      100527988
 * Date:    04.11.2016
 * Name:    Tute 3 - Enrolment System
 * Version: 1
 */

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static au.edu.holmesglen.kirstine_n.tute3teacherlogin.MainActivity.LOG_TAG;

/**
 * Handling the enrolment of students to course.
 * Teacher can input details about student and choose which course to enrol student in.
 * From this activity the Teacher can go to ClassListActivity on btn click
 */
public class EnrolmentActivity extends AppCompatActivity {

    public static  DatabaseHelper dbHelper;
    public static SQLiteDatabase db;

    // toast msg
    public static final String ENROLMENT_SUCCESS = "Student is enrolled";
    public static final String ID_NOT_UNIQUE = "Student ID must be unique";
    public static final String FILL_OUT_FIELDS = "Please fill out fields";
    public static final String RAD_GROUP_ZERO_CHECKED = "Please choose a Course";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrolment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // instantiate my database helper
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // find the relevant views
        final EditText etId = (EditText) findViewById(R.id.et_student_id_input);
        final EditText etFirstname = (EditText) findViewById(R.id.et_firstname_input);
        final EditText etLastname = (EditText) findViewById(R.id.et_lastname_input);
        final View llView = findViewById(R.id.ll_student_data_input);
        final Button btnEnrol = (Button) findViewById(R.id.btn_enrol);
        final Button btnClasslist = (Button) findViewById(R.id.btn_classlist);

        // radio group
        final RadioGroup radGroup = (RadioGroup) findViewById(R.id.radGroup_courses);

        // show input fields for student details when rad btn are checked
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.v(LOG_TAG, "Radio button clicked");

                // show linear layout containing the rest of the input form
                llView.setVisibility(View.VISIBLE);
            }
        });

        // click btn for enrol
        btnEnrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add details to db
                int id;
                String strId = etId.getText().toString();
                String firstName = etFirstname.getText().toString();
                String lastName = etLastname.getText().toString();
                Log.v(LOG_TAG, "id: " + strId);

                // no proper validation, rely on good user input
                if (!strId.equals("") && !firstName.equals("") && !lastName.equals("")) {

                    // convert id to integer
                    id = Integer.parseInt(strId);

                    // check for unique student id
                    if (isStudentIdUnique(id)) {
                        // instantiate Student object
                        Student student = new Student(id, firstName, lastName);

                        // depending on which rad btn for course is checked, get the fk
                        int fkCourseId = getIdToUseAsForeignKey(radGroup);
                        Log.v(LOG_TAG, "fkCourseId: " + fkCourseId);

                        dbHelper.createStudent(student, fkCourseId);

                        // empty input fields
                        etId.setText("");
                        etFirstname.setText("");
                        etLastname.setText("");

                        // msg user enrol success
                        Toast.makeText(EnrolmentActivity.this, ENROLMENT_SUCCESS, Toast.LENGTH_SHORT).show();
                    } else {
                        // inform user about student id already taken
                        Toast.makeText(EnrolmentActivity.this, ID_NOT_UNIQUE, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // msg to user to fill out input fields
                    Toast.makeText(EnrolmentActivity.this, FILL_OUT_FIELDS, Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnClasslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LOG_TAG, "classlist button clicked");

                // make sure a rad btn is checked (redundant since btn not shows if not checked)
                if (radGroup.getCheckedRadioButtonId() == -1) {
                    // No rad button is checked yet, so display msg to user
                    Toast.makeText(EnrolmentActivity.this, RAD_GROUP_ZERO_CHECKED, Toast.LENGTH_SHORT).show();
                } else {  // one rad btn is checked
                    startClassListActivity(getIdToUseAsForeignKey(radGroup));
                }
            }
        });

    }


    /**
     * based on which rad btn is selected in radio group get the text for the rad button
     * and get courseId for record with that course name, this will be the foreign key for
     * the student record
     *
     * @param radGroup  the group containing our rad btn's
     * @return  integer representing the foreign key to be used in student record
     */
    public int getIdToUseAsForeignKey(RadioGroup radGroup) {
        // in Student table foreign key course id
        int fkCourseId;

        // get the selected rad btn id
        int selectedId = radGroup.getCheckedRadioButtonId();

        // find the radiobutton and get the text
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String courseName = radioButton.getText().toString();

        // fetch foreign key for courseId in Student table matching this course name
        fkCourseId = dbHelper.getCourseId(courseName);

        return fkCourseId;
    }


    /**
     * check if id input is unique
     *
     * @param id  the id to check
     * @return  true if id is not present in student table, false if present
     */
    public boolean isStudentIdUnique(int id) {
        Log.v(LOG_TAG, "into isStudentIdUnique");

        boolean isUnique = false;

        // find out if entered id already exists in database
        if (dbHelper.getAmountOfStudentsById(id) < 1) {
            Log.v(LOG_TAG, "id is Unique");
            isUnique = true;
        } else {
            Log.v(LOG_TAG, "id is NOT unique");

        }
        return isUnique;
    }


    /**
     * create intent and start ClassListActivity showing students in a specific course
     *
     * @param courseId  integer representing the id for the specific course we are interested in
     */
    public void startClassListActivity(int courseId) {
        Intent intent;
        intent = new Intent(this, ClassListActivity.class);
        intent.putExtra("courseId", courseId);  // pass into activity
        startActivity(intent);
    }


    /**
     * make sure to close db on destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.closeDB();
    }
}  // end class EnrolmentActivity
