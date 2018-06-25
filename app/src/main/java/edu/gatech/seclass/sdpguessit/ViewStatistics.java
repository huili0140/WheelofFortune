package edu.gatech.seclass.sdpguessit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ViewStatistics extends AppCompatActivity {

    private DatabaseHelper db;
    private String username;
    private Button getMyPuzzle;
    private Button getMyTournament;
    private Button getPuzzle;
    private Button getTournament;

    private String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        db = new DatabaseHelper(this);
        username = getIntent().getExtras().getString("username");

        getMyPuzzle = (Button) findViewById(R.id.buttonMyPuzzle);
        getMyTournament = (Button) findViewById(R.id.buttonMyTournament);
        getPuzzle = (Button) findViewById(R.id.buttonPuzzle);
        getTournament = (Button) findViewById(R.id.buttonTournament);
    }

    public void handleClickMyPuzzle(View view) {

        //getMyPuzzle
        Cursor resMyPuzzle = db.myPuzzleStatistics(username);
        resMyPuzzle.moveToFirst();
        int count = resMyPuzzle.getCount();
        list = new String[count + 1];
        list[0] = "PuzzleID   Prize";

        if (count == 0) {
            toastMessage("No puzzle in record. ");

        } else {

            for (int i = 1; i <= count; i++) {

                resMyPuzzle.moveToPosition(i - 1);

                if (resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("puzzle_id")) == 0) {
                    list[i] = "";
                } else {
                    list[i] = Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("puzzle_id")));
                    list[i] = list[i] + "                $" + Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("prize")));
                }
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewStatistics.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("My Puzzles");
            ListView lv = (ListView) convertView.findViewById(R.id.lv);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
            alertDialog.show();

        }
    }

    public void handleClickMyTournament(View view) {

        Cursor resMyPuzzle = db.myTournamentStatistics(username);
        int count = resMyPuzzle.getCount();
        list = new String[count + 1];
        list[0] = "Tournament    Prize";

        if (count == 0) {
            toastMessage("No tournament in record. ");

        } else {

            for (int i = 1; i <= count; i++) {

                resMyPuzzle.moveToPosition(i - 1);
                list[i] = resMyPuzzle.getString(resMyPuzzle.getColumnIndex("tournament_name"));
                list[i] = list[i] + "               $" +
                        Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("prize")));

            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewStatistics.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("My Tournaments");
            ListView lv = (ListView) convertView.findViewById(R.id.lv);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
            alertDialog.show();

        }
    }

    public void handleClickPuzzle(View view) {

        Cursor resMyPuzzle = db.puzzleStatistics(username);
        int count = resMyPuzzle.getCount();
        list = new String[count + 1];
        list[0] = "PuzzleID   TotalPLayer   TopPrize   Winner";

        if (count == 0) {
            toastMessage("No puzzle in record. ");

        } else {

            for (int i = 1; i <= count; i++) {

                resMyPuzzle.moveToPosition(i-1);

                if (resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("puzzle_id")) == 0) {
                    list[i] = "";
                } else {
                    list[i] = Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("puzzle_id")));
                    list[i] = list[i] + "            " + Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("totalPlayer")));
                    list[i] = list[i] + "            $" + Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("topPrize")));
                    list[i] = list[i] + "            " + resMyPuzzle.getString(resMyPuzzle.getColumnIndex("username"));
                }
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewStatistics.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("All Puzzles");
            ListView lv = (ListView) convertView.findViewById(R.id.lv);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
            alertDialog.show();

        }
    }

    public void handleClickTournament(View view) {

        Cursor resMyPuzzle = db.tournamentStatistics(username);
        int count = resMyPuzzle.getCount();
        list = new String[count + 1];
        list[0] = "Tournament   TotalPLayer   TopPrize   Winner";

        if (count == 0) {
            toastMessage("No tournament in record. ");

        } else {

            for (int i = 1; i <= count; i++) {

                resMyPuzzle.moveToPosition(i-1);
                list[i] = resMyPuzzle.getString(resMyPuzzle.getColumnIndex("tournament_name"));
                list[i] = list[i] + "            " + Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("totalPlayer")));
                list[i] = list[i] + "            $" + Integer.toString(resMyPuzzle.getInt(resMyPuzzle.getColumnIndex("topPrize")));
                list[i] = list[i] + "            " + resMyPuzzle.getString(resMyPuzzle.getColumnIndex("username"));

            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewStatistics.this);
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.list, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("All Tournaments");
            ListView lv = (ListView) convertView.findViewById(R.id.lv);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
            alertDialog.show();

        }
    }

    // toast message
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
