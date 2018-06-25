package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePuzzle extends AppCompatActivity {

    private DatabaseHelper db;
    private String username;

    private EditText getPhrase;
    private EditText getMaxWrongGuess;
    private Button buttonCreatePuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_puzzle);

        db = new DatabaseHelper(this);
        username = getIntent().getExtras().getString("username");

        getPhrase = (EditText)findViewById(R.id.phrase);
        getMaxWrongGuess = (EditText) findViewById(R.id.maxWrongGuess);
        buttonCreatePuzzle = (Button)findViewById(R.id.buttonCreatePuzzle);
    }

    public void handleClickCreatePuzzle (View view) {

        String phrase = getPhrase.getText().toString();
        String maxWrongGuessString = getMaxWrongGuess.getText().toString();

        if (getMaxWrongGuess.length() != 0) {

            int maxWrongGuess = Integer.parseInt(maxWrongGuessString);
            if (getPhrase.length() != 0 && phrase.length() <= 100 && maxWrongGuess >= 0 && maxWrongGuess <= 10) {

                boolean insertData = db.createPuzzle(phrase, maxWrongGuess, username);
                if (insertData) {

                    Cursor res = db.getPuzzleID();
                    //toastMessage("Puzzle successfully created.");
                    toastMessage("Puzzle successfully created with ID " + Integer.toString(res.getInt(0)));

                    // go back to mainmenu
                    Intent intent = new Intent(CreatePuzzle.this, MainMenu.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();

                } else {
                    toastMessage("Failed to create player");
                }

            } else if (getPhrase.length() == 0) {
                toastMessage("Enter Puzzle Phrase.");

            } else if (maxWrongGuess < 0 || maxWrongGuess > 10){
                toastMessage("Enter maxWrongGuess between 0 to 10. ");
            } else if (phrase.length() > 100) {

                toastMessage("Enter a phrase with less than 100 characters. ");

            } else {
                toastMessage("Error with your input. ");
            }

        } else {
            toastMessage("Enter Maximum Wrong Guess. ");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
