package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private DatabaseHelper db;


    private EditText getUsername;
    private Button buttonLogin;
    private Button buttonCreatePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);

        getUsername = (EditText)findViewById(R.id.username);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonCreatePlayer = (Button)findViewById(R.id.buttonCreatePlayer);

    }

    public void handleClickLogin (View view) {

        String username = getUsername.getText().toString();
        Cursor res = db.getPlayerUsername();
        boolean foundUser = false;

        if (getUsername.length() == 0) {
            toastMessage("Please enter username. ");
        }

        if (res.getCount() == 0) {
            toastMessage("Please create new player.");
        }

        while (res.moveToNext()) {
            if (username.equals(res.getString(0))) {
                foundUser = true;
                Intent intent = new Intent(Login.this, MainMenu.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        }

        if (foundUser == false) toastMessage("Username not found.");
    }

    public void handleClickCreatePlayer (View view) {
        Intent intent = new Intent(Login.this, CreatePlayer.class);
        startActivity(intent);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
