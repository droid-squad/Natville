package me.jwill2385.natville;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class loginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        signUp = findViewById(R.id.btnSignUp);
        etUsername.setSingleLine();
        etPassword.setSingleLine();


        //persist the user so they don't have to log in every time they open the app
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(loginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

            login(username, password);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    // when user clicks on login button they will be directed to main screen
    public void loginToMainScreen(View view){
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        login(username, password);
    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
                    Log.d("LoginActivity", "Login Successful!");

                    Intent i = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Log.e("LoginActivity","Login Failed");
                    e.printStackTrace();
                }
            }
        });
    }
}
