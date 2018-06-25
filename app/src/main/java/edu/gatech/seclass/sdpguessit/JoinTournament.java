package edu.gatech.seclass.sdpguessit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JoinTournament extends AppCompatActivity {

    private DatabaseHelper db;
    private String username;
    private String tournament;
    private String tournamentContinue;

    Button getButtonSelectTournament;
    Button getButtonJoin;
    TextView getSelection;
    Button getButtonSelectContinue;
    Button getButtonContinue;
    TextView getSelectionContinue;
    String[] listTournament;
    String[] listContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_tournament);

        db = new DatabaseHelper(this);
        username = getIntent().getExtras().getString("username");


        getButtonSelectTournament = findViewById(R.id.buttonSelectTournament);
        getButtonJoin = findViewById(R.id.buttonJoin);
        getSelection = findViewById(R.id.selection);
        getButtonSelectContinue = findViewById(R.id.buttonSelectContinue);
        getButtonContinue = findViewById(R.id.buttonContinue);
        getSelectionContinue = findViewById(R.id.selectionContinue);

        // Generate list of tournaments to join
        Cursor res = db.getTournament(username);
        int count = res.getCount();

        // Generate list of tournaments to continue
        Cursor resContinue = db.getSolveTournament(username);
        int countContinue = resContinue.getCount();

        if (count == 0) {

            getButtonSelectTournament.setEnabled(false);
            getButtonJoin.setEnabled(false);

        } else {

            res.moveToPosition(0);
            listTournament = new String[res.getCount()];

            int i = 0;
            while (res.isAfterLast() == false) {

                listTournament[i] = res.getString(res.getColumnIndex("tournament_name"));
                res.moveToNext();
                i++;
            }

            getButtonSelectTournament.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(JoinTournament.this);
                    mBuilder.setTitle("Select Tournament to Join");
                    mBuilder.setSingleChoiceItems(listTournament,-1,new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            tournament = listTournament[which];
                        }
                    });

                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick (DialogInterface dialogInterface,int i){

                            getSelection.setText("Your selection is " + tournament);
                        }
                    });

                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }

            });
        }

        if (countContinue == 0) {

            getButtonSelectContinue.setEnabled(false);
            getButtonContinue.setEnabled(false);

        } else {

            resContinue.moveToPosition(0);
            listContinue = new String[resContinue.getCount()];

            int i = 0;
            while (resContinue.isAfterLast() == false) {

                listContinue[i] = resContinue.getString(resContinue.getColumnIndex("tournament_name"));
                resContinue.moveToNext();
                i++;
            }

            getButtonSelectContinue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(JoinTournament.this);
                    mBuilder.setTitle("Select Tournament to Continue");
                    mBuilder.setSingleChoiceItems(listContinue,-1,new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            tournamentContinue = listContinue[which];
                        }
                    });

                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener()

                    {
                        @Override
                        public void onClick (DialogInterface dialogInterface,int i){

                            getSelectionContinue.setText("Your selection is " + tournamentContinue);
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

    public void handleClickJoinTournament (View view) {

        // insert the tournament into solvetournament table
        boolean insertData = db.joinTournament(username, tournament);

        Intent intent=new Intent(JoinTournament.this, JoinTournamentSelectPuzzle.class);
        Bundle extras = new Bundle();
        extras.putString("username", username);
        extras.putString("tournament", tournament);
        intent.putExtras(extras);
        startActivity(intent);
        finish();

    }

    public void handleClickContinueTournament (View view) {

        Intent intent=new Intent(JoinTournament.this, JoinTournamentSelectPuzzle.class);
        Bundle extras = new Bundle();
        extras.putString("username", username);
        extras.putString("tournament", tournamentContinue);
        intent.putExtras(extras);
        startActivity(intent);
        finish();

    }
}

