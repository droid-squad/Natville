package me.jwill2385.natville;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class loginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
    }

    // when user clicks on login button they will be directed to main screen
    public void loginToMainScreen(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
