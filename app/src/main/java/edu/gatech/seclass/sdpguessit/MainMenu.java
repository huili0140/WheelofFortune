package edu.gatech.seclass.sdpguessit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    String username;

    private Button buttonCreatePuzzle;
    private Button buttonSolvePuzzle;
    private Button buttonCreatTournament;
    private Button buttonJoinTournament;
    private Button buttonViewStat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        username = getIntent().getExtras().getString("username");
    }

    public void handleClickCreatePuzzle (View view) {
        Intent intent = new Intent(MainMenu.this, CreatePuzzle.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    public void handleClickCreateTournament (View view) {
        Intent intent = new Intent(MainMenu.this, CreateTournament.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    public void handleClickSolvePuzzle (View view) {
        Intent intent = new Intent(MainMenu.this, SolvePuzzle.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    public void handleClickJoinTournament (View view) {
        Intent intent = new Intent(MainMenu.this, JoinTournament.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    public void handleClickViewStatistics (View view) {
        Intent intent = new Intent(MainMenu.this, ViewStatistics.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
