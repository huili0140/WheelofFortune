package edu.gatech.seclass.sdpguessit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class SolvePuzzle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseHelper db;
    private String username;

    private String phrase;
    private int maxWrongGuess;
    private int puzzle_ID;
    private String phraseToShow;
    private String phraseToShowAgain;
    private String solvedPhrase;
    private int randomPrize;
    private int totalPrize;
    private int guessChances;

    private Spinner getSpinnerConsonant;
    private Spinner getSpinnerVowel;
    private TextView getShowPhrase;
    private TextView getRandomPrize;
    private TextView getTotalPrize;
    private TextView getGuessChances;
    private EditText getSolvedPhrase;
    private Button getButtonSolve;
    private Button getButtonExit;
    private String[] confirmation = new String[] {"Yes", "No"};
    private String confirmationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve_puzzle);

        db = new DatabaseHelper(this);
        username = getIntent().getExtras().getString("username");

        getShowPhrase = (TextView) findViewById(R.id.showPhrase);
        getRandomPrize = findViewById(R.id.randomPrize);
        getTotalPrize = findViewById(R.id.totalPrize);
        getGuessChances = findViewById(R.id.guessChances);
        getSolvedPhrase = findViewById(R.id.solvedPhrase);
        getButtonExit = findViewById(R.id.buttonExit);
        getButtonSolve = findViewById(R.id.buttonSolve);

        getSpinnerConsonant = (Spinner) findViewById(R.id.spinnerConsonant);
        getSpinnerVowel = (Spinner) findViewById(R.id.spinnerVowel);

        // Generate random puzzle
        Cursor res = db.getOtherPuzzle(username);
        int count = res.getCount();

        if (count == 0) {
            toastMessage("There are no more puzzles to play. ");
        } else {
            int randomPuzzle = randomNumber(count); // 0 - (count-1)
            res.moveToPosition(randomPuzzle);

            phrase = res.getString(res.getColumnIndex("phrase"));
            maxWrongGuess = res.getInt(res.getColumnIndex("max_wrong_guess"));
            puzzle_ID = res.getInt(res.getColumnIndex("puzzle_id"));

            // Populate the original puzzle frame
            phraseToShow = showPhrase(phrase);
            getShowPhrase.setText(phraseToShow);

            // Populate the original random prize
            randomPrize = 100 + 100 * randomNumber(10);
            getRandomPrize.setText("$" + Integer.toString(randomPrize));

            // Populate the total prize
            totalPrize = 0;
            getTotalPrize.setText("$0");

            //Populate the guessing chances
            getGuessChances.setText(Integer.toString(maxWrongGuess));


        }

        // Get consonant
        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.consonants, android.R.layout.simple_spinner_item);
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSpinnerConsonant.setAdapter(adapterC);
        getSpinnerConsonant.setOnItemSelectedListener(this);

        // Get vowel
        ArrayAdapter<CharSequence> adapterV = ArrayAdapter.createFromResource(this, R.array.vowels, android.R.layout.simple_spinner_item);
        adapterV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getSpinnerVowel.setAdapter(adapterV);
        getSpinnerVowel.setOnItemSelectedListener(this);

        // Disable spinner if necessary
        loadSpinner(maxWrongGuess, 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.spinnerConsonant)
        {
            String consonant = spinner.getItemAtPosition(i).toString();
            if (!consonant.equals("Select")) {
                phraseToShowAgain = showPhraseAgain(phrase, phraseToShow, consonant);

                if (phraseToShowAgain.equals(phraseToShow)) {

                    maxWrongGuess --;
                    getGuessChances.setText(Integer.toString(maxWrongGuess));

                    if (maxWrongGuess <= 0) {

                        if (maxWrongGuess == 0) {

                            toastMessage("No more change to guess. Enter the phrase. ");
                            loadSpinner(maxWrongGuess, totalPrize);
                        } else {

                            toastMessage("Wrong phrase. Game is over.");
                            totalPrize = 0;
                            gameOver(totalPrize);
                        }

                    } else {
                        toastMessage("Wrong guess, please try again. ");
                        loadSpinner(maxWrongGuess, totalPrize);
                    }
                } else {

                    // good guess, update total phrase
                    int n = numberOfDiffChar(phraseToShow, phraseToShowAgain);
                    totalPrize = totalPrize + randomPrize * n;
                    getTotalPrize.setText("$" + Integer.toString(totalPrize));

                    phraseToShow = phraseToShowAgain;
                    getShowPhrase.setText(phraseToShow);

                    // Populate the original random prize
                    randomPrize = 100 + 100 * randomNumber(10);
                    getRandomPrize.setText("$" + Integer.toString(randomPrize));
                }
            }
            loadSpinner(maxWrongGuess, totalPrize);
            getSpinnerConsonant.setSelection(0);
        }
        else if(spinner.getId() == R.id.spinnerVowel)
        {
            String vowel = spinner.getItemAtPosition(i).toString();

            if (!vowel.equals("Select")) {
                phraseToShowAgain = showPhraseAgain(phrase, phraseToShow, vowel);

                if (phraseToShowAgain.equals(phraseToShow)) {

                    maxWrongGuess --;
                    getGuessChances.setText(Integer.toString(maxWrongGuess));

                    if (maxWrongGuess <= 0) {

                        if (maxWrongGuess == 0) {

                            toastMessage("No more change to guess. Enter the phrase.");
                            loadSpinner(maxWrongGuess, totalPrize);
                        } else {

                            toastMessage("Wrong phrase. Game is over.");
                            totalPrize = 0;
                            gameOver(totalPrize);
                        }

                    } else {
                        toastMessage("Wrong guess, please try again. ");

                    }
                } else {

                    // good guess, update total phrase
                    totalPrize = totalPrize - 300;
                    getTotalPrize.setText("$" + Integer.toString(totalPrize));

                    phraseToShow = phraseToShowAgain;
                    getShowPhrase.setText(phraseToShow);
                }
            }
            loadSpinner(maxWrongGuess, totalPrize);
            getSpinnerVowel.setSelection(0);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public int randomNumber(int n) {
        Random r = new Random();
        return r.nextInt(n); // random integer [0, n-1)
    }


    @Override
    public void onBackPressed() {
        toastMessage("Exit only by pressing the Exit button. ");
        moveTaskToBack(false);

    }

    public String showPhrase(String phrase) {

        String result = "";

        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.toUpperCase().charAt(i) - 'A' <= 25 && phrase.toUpperCase().charAt(i) - 'A' >= 0) {
                result = result + "-";
            } else {
                result = result + phrase.toUpperCase().charAt(i);
            }
        }
        return result;
    }

    public String showPhraseAgain(String phrase, String phraseToShow, String letter) {

        if (letter.equals("Select")) return phraseToShow;
        String result = "";

        for (int i = 0; i < phrase.length(); i++) {
            if (phraseToShow.charAt(i) == '-' && phrase.toUpperCase().charAt(i) == letter.charAt(0)){
                result = result + letter;
            } else {
                result = result + phraseToShow.charAt(i);
            }
        }

        return result;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public int numberOfDiffChar(String phraseToShow, String phraseToShowAgain) {
        if (phraseToShow.length() != phraseToShowAgain.length()) return 0;

        int count = 0;
        for (int i = 0; i < phraseToShow.length(); i++) {
            if (phraseToShow.charAt(i) != phraseToShowAgain.charAt(i)) {
                count ++;
            }
        }
        return count;
    }

    public void loadSpinner (int maxWrongGuess, int totalPrize) {

        if (maxWrongGuess <= 0) {

            getSpinnerConsonant.setEnabled(false);
            getSpinnerVowel.setEnabled(false);
        } else {
            if (totalPrize < 300) {
                getSpinnerVowel.setEnabled(false);
            } else {
                getSpinnerVowel.setEnabled(true);
            }
        }
    }

    public void handleClickSolvePuzzle (View view) {

        solvedPhrase = getSolvedPhrase.getText().toString().toUpperCase();

        if (solvedPhrase.equals(phrase.toUpperCase()) && !solvedPhrase.equals(phraseToShow)) {

            toastMessage("Puzzle solved! Congratulations!");
            totalPrize = totalPrize + numberOfDiffChar(phraseToShow, phrase.toUpperCase()) * 1000;


        } else {
            toastMessage("Wrong guess. Game is over. ");
            totalPrize = 0;

        }

        gameOver(totalPrize);
    }

    public void handleClickExitPuzzle(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SolvePuzzle.this);
        mBuilder.setTitle("Exit the puzzle?");
        mBuilder.setSingleChoiceItems(confirmation,-1,new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            confirmationResult = confirmation[which];
        }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick (DialogInterface dialogInterface,int i){

                if (confirmationResult.equals("Yes")) {

                    // insert to the table of solve puzzle
                    totalPrize = 0;
                    boolean insertData = db.createSolvePuzzle(username, puzzle_ID, totalPrize);

                    Intent intent = new Intent(SolvePuzzle.this, MainMenu.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    public void gameOver(int totalPrize) {

        boolean insertData = db.createSolvePuzzle(username, puzzle_ID, totalPrize);

        getTotalPrize.setText("$" + Integer.toString(totalPrize));
        getSpinnerConsonant.setEnabled(false);
        getSpinnerVowel.setEnabled(false);

        getButtonExit.setEnabled(false);
        getButtonSolve.setEnabled(false);

        Intent intent = new Intent(SolvePuzzle.this, MainMenu.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

}
