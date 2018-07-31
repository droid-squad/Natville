package me.jwill2385.natville;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText newUsername;
    private EditText newEmail;
    private EditText newPassword;
    private Button createAccount;
    private Button returnToLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        newUsername=findViewById(R.id.etNewUsername);
        newEmail=findViewById(R.id.etNewEmail);
        newPassword=findViewById(R.id.etNewPassword);
        createAccount=findViewById(R.id.bvCreateAccount);
        returnToLogIn=findViewById(R.id.bvReturnToLogIn);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                final String username = newUsername.getText().toString();
                final String email = newEmail.getText().toString();
                final String password = newPassword.getText().toString();

                signUp(username, email, password);
            }
        });

        returnToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, loginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //creates the user account by sending the taken editText info to the Parse server and saving it
    private void signUp(String username, String email, String password){
        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);


        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("SignUpActivity", "Signed Up Successfully!");


                    Intent intent= new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Log.e("SignUpActivity", "Sign Up Failed");
                    e.printStackTrace();

                }
            }
        });


    }
}
