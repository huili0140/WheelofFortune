package edu.gatech.seclass.sdpguessit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class CreatePlayer extends AppCompatActivity {

    private DatabaseHelper db;

    private EditText getUsername;
    private EditText getFirstName;
    private EditText getLastName;
    private EditText getEmail;
    private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);

        db = new DatabaseHelper(this);

        getFirstName = (EditText)findViewById(R.id.firstName);
        getLastName = (EditText)findViewById(R.id.lastName);
        getUsername = (EditText)findViewById(R.id.username);
        getEmail = (EditText)findViewById(R.id.email);
        buttonCreate = (Button)findViewById(R.id.buttonCreate);
    }

    public void handleClickCreatePLayer (View view) {

        String username = getUsername.getText().toString();
        String firstName = getFirstName.getText().toString();
        String lastName = getLastName.getText().toString();
        String email = getEmail.getText().toString();

        if (getFirstName.length() == 0 || getLastName.length() == 0 || getEmail.length() == 0) {

            toastMessage("User information cannot be null. ");


        } else {

            if (getUsername.length() != 0) {

                boolean insertData = db.createPlayer(username, firstName, lastName, email);
                if (insertData) {

                    toastMessage("Player successfully created.");

                    // go back to log in
                    Intent intent = new Intent(CreatePlayer.this, Login.class);
                    startActivity(intent);
                    finish();

                } else {
                    toastMessage("Failed to create player. Username is already taken. ");
                }

            } else {
                getUsername.setError("Username cannot be null.");
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
