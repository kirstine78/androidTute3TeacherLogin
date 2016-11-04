package au.edu.holmesglen.kirstine_n.tute3teacherlogin;

/**
 * Student: Kirstine B. Nielsen
 * Id:      100527988
 * Date:    04.11.2016
 * Name:    Tute 3 - Enrolment System
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Handling the login of teacher.
 * Will check if login credentials are ok.
 * If ok then user is directed to another Activity.
 * Not ok credentials leads to no redirect. Displays login error message to user.
 */
public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "Kirsti";

    // decl constants
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String USERNAME = "usernameKey";
    public static final String PASSWORD = "passwordKey";

    // decl ref to SharedPreferences class
    SharedPreferences sharedPreferences;

    // decl ref to edt input
    public EditText etUsername;
    public EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get ref to edit text
        etUsername = (EditText) findViewById(R.id.et_username_input);
        etPassword = (EditText) findViewById(R.id.et_password_input);

        // set up preferences collection
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        // button login
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if there are any stored values for username and password
                // if there are
                if (areCredentialsStoredInSharedPreferences()) {

                    // compare user input with stored values
                    if (areCredentialsCorrect()) {
                        // logged in ok, start EnrolmentActivity
                        startEnrolmentActivity();
                    } else {  // not ok
                        // display toast msg indicating wrong credentials
                        Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {  // username and password are currently NOT saved in Shared preferences
                    // save "teacher" and "pass", because that is business rule for this app
                    saveCredentialsInSharedPreferences(view);

                    // compare user input with stored values
                    if (areCredentialsCorrect()) {
                        // logged in ok, start EnrolmentActivity
                        startEnrolmentActivity();
                    } else {  // not ok
                        // display toast msg indicating wrong credentials
                        Toast.makeText(MainActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }  // end onCreate


    public boolean areCredentialsCorrect() {

        // compare input with storage values for username and password
        boolean okCredentials = etUsername.getText().toString().equals(getUsernameFromSharedPreferencesStorage()) &&
                etPassword.getText().toString().equals(getPasswordFromSharedPreferencesStorage());

        return okCredentials;
    }


    // custom method to get preferences
    public boolean areCredentialsStoredInSharedPreferences() {

        boolean isStored = false;

        // To retrieve an already saved shared preference we use the contains() method
        // to check that the key value is stored in the sharedpreferences collection
        if (sharedPreferences.contains(USERNAME) && sharedPreferences.contains(PASSWORD)) {
            isStored = true;
        }
        return isStored;
    }


    public String getUsernameFromSharedPreferencesStorage() {

        String username = "";

        // To retrieve an already saved shared preference we use the contains() method
        // to check that the key value is stored in the sharedpreferences collection
        if (sharedPreferences.contains(USERNAME)) {
            username = sharedPreferences.getString(USERNAME, "");
        }
        return username;
    }


    public String getPasswordFromSharedPreferencesStorage() {

        String password = "";

        // To retrieve an already saved shared preference we use the contains() method
        // to check that the key value is stored in the sharedpreferences collection
        if (sharedPreferences.contains(PASSWORD)) {
            password = sharedPreferences.getString(PASSWORD, "");
        }
        return password;
    }


    public void saveCredentialsInSharedPreferences(View view) {
        String username = "teacher";
        String password = "pass";

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // format is: editor.putString("key", "value");
        // in our example the key/value is:
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);

        editor.commit();
    }


    public void startEnrolmentActivity() {
        Intent i;
        i = new Intent(this, EnrolmentActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
