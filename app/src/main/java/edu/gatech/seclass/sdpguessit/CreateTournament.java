package edu.gatech.seclass.sdpguessit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateTournament extends AppCompatActivity {

    private DatabaseHelper db;
    private String username;

    Button getButtonSelect;
    Button getButtonInsertTournament;
    TextView getSelection;
    EditText getTournamentName;
    String[] listPuzzle;
    int[] listPuzzleID;
    boolean[] checkedPuzzle;
    ArrayList<Integer> mPuzzles = new ArrayList<>();
    int limit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tournament);

        db = new DatabaseHelper(this);
        username = getIntent().getExtras().getString("username");

        getButtonSelect = findViewById(R.id.buttonSelect);
        getButtonInsertTournament = findViewById(R.id.buttonInsertTournament);
        getSelection = findViewById(R.id.selection);
        getTournamentName = findViewById(R.id.tournamentName);

        // Generate list of puzzles
        Cursor res = db.getSelfPuzzle(username);
        int count = res.getCount();

        if (count == 0) {
            toastMessage("There are no puzzles to select. ");
            getButtonSelect.setEnabled(false);
            getButtonInsertTournament.setEnabled(false);


        } else {
            res.moveToPosition(0);
            //getSelection.setText(Integer.toString(res.getCount()));

            listPuzzle = new String[res.getCount()];
            listPuzzleID = new int[res.getCount()];

            int i = 0;
            while (res.isAfterLast() == false) {

                listPuzzle[i] = res.getString(res.getColumnIndex("phrase"));
                listPuzzleID[i] = res.getInt(res.getColumnIndex("puzzle_id"));
                res.moveToNext();
                i++;
            }


            //listPuzzle = new String[]{"puzzle1", "puzzle2", "puzzle3"};
            checkedPuzzle = new boolean[listPuzzle.length];

            getButtonSelect.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    limit = 0;

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateTournament.this);
                    mBuilder.setTitle("Select Puzzles");

                    mBuilder.setMultiChoiceItems(listPuzzle,checkedPuzzle,new DialogInterface.OnMultiChoiceClickListener()

                    {
                        @Override
                        public void onClick (DialogInterface dialogInterface, int position, boolean isChecked)
                        {
                            if (isChecked) {
                                if (limit < 5) {
                                    // add the puzzle to the selection
                                    mPuzzles.add(position);
                                    checkedPuzzle[position] = isChecked;
                                    limit ++;

                                } else {
                                    toastMessage("Select no more than 5 puzzles. ");
                                    checkedPuzzle[position]  = false;
                                    ((AlertDialog) dialogInterface).getListView().setItemChecked(position, false);
                                }
                            } else {

                                // if the puzzle is already selected then remove it
                                mPuzzles.remove((mPuzzles.indexOf(position)));
                                checkedPuzzle[position]  = isChecked;
                                limit --;
                            }
                        }
                    });

                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick (DialogInterface dialogInterface,int i){
                            String puzzles = "";
                            for (int n = 0; n < mPuzzles.size(); n++) {
                                puzzles = puzzles + listPuzzle[mPuzzles.get(n)];
                                if (n < mPuzzles.size() - 1) {
                                    puzzles = puzzles + ", ";
                                }
                            }
                            getSelection.setText(puzzles);
                        }
                    });

                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }

            });
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void handleClickInsertTournament (View view) {

        String tournamentName = getTournamentName.getText().toString();
        Cursor res = db.checkTournamentName();

        // check if the name has been used
        boolean usedName = false;
        int n = 0;
        res.moveToPosition(n);
        while (res.isAfterLast() == false) {

            if (res.getString(res.getColumnIndex("tournament_name")).equals(tournamentName)) {
                usedName = true;
            }
            res.moveToNext();
            n++;
        }



        if (usedName) {
            toastMessage("Tournament name is already taken. Enter another name. ");

        } else if (tournamentName.equals("")) {

            toastMessage("Enter tournament name. ");
        } else {

            if (mPuzzles.size() == 0) {
                toastMessage("Select at least one puzzle. ");

            } else {
                for (int i = 0; i < mPuzzles.size(); i++) {

                    boolean insertData = db.createTournament(tournamentName, listPuzzleID[mPuzzles.get(i)], username);
                }

                toastMessage("Tournament " + tournamentName + " is successfully created.");

                // go back to main menu
                Intent intent = new Intent(CreateTournament.this, MainMenu.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        }
    }
}
