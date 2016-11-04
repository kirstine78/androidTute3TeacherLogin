package au.edu.holmesglen.kirstine_n.tute3teacherlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static au.edu.holmesglen.kirstine_n.tute3teacherlogin.MainActivity.LOG_TAG;

public class ClassListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.v(LOG_TAG, "in class list activity");

        // we need this to get the param passed from Enrolment Activity
        Intent intent = getIntent();

        // put one as default
        int courseId = intent.getIntExtra("courseId", 1);  // get the param passed from Enrolment Activity
        Log.v(LOG_TAG, "courseId: " + courseId);

        TextView tvHeadline = (TextView) findViewById(R.id.tv_classlist);

        // find out which headline to apply to text view
        if (courseId == 1) {
            tvHeadline.setText("Students enrolled in Networking");
        } else {
            tvHeadline.setText("Students enrolled in Software");
        }

        // get all student objects by course id
        List<Student> studentsList = EnrolmentActivity.dbHelper.getAllStudentsByCourse(courseId);

        // prepare string array to hold only names and course
        String[] values = new String[studentsList.size()];

        for (int i = 0; i < studentsList.size(); i++){
            String s;
            Log.v(LOG_TAG, "for loop i: " + i);
            if (courseId == 1)
            {
                s = " NET";
            }
            else
            {
                s = " SOFT";
            }
            values[i] = studentsList.get(i).getFirstName() + " " + studentsList.get(i).getLastName() + s;
        }

        ListView listView = (ListView) findViewById(R.id.myClassList);

        // Define an Adapter - to format the array for a ListView.
        // first param
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // assign adapter to ListView
        listView.setAdapter(adapter);
    }
}
