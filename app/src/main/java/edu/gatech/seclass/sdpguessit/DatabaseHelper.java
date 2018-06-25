package edu.gatech.seclass.sdpguessit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by huili on 2/25/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Guess.db";

    // Table Names
    public static final String TABLE_PLAYER = "PLayer";
    public static final String TABLE_PUZZLE = "Puzzle";
    public static final String TABLE_SOLVE_PUZZLE = "SolvePuzzle";
    public static final String TABLE_TOURNAMENT = "Tournament";
    public static final String TABLE_SOLVE_TOURNAMENT = "SolveTournament";

    // Common column names
    public static final String COL_USERNAME = "username";
    public static final String COL_PUZZLE_ID = "puzzle_id";
    public static final String COL_TOURNAMENT_NAME = "tournament_name";

    // Column Names in Player
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_EMAIL = "email";

    // Column Names in Puzzle
    public static final String COL_PHRASE = "phrase";
    public static final String COL_MAX_WRONG_GUESS = "max_wrong_guess";
    public static final String COL_CREATED_BY = "created_by";

    // NO EXTRA Column Names in Tournament

    // Column Names in SOLVED_PUZZLE
    public static final String COL_PRIZE = "prize";

    // Statements to create the tables
    private static final String CREATE_TABLE_PLAYER = "CREATE TABLE "
            + TABLE_PLAYER + "(" + COL_USERNAME + " TEXT PRIMARY KEY," + COL_FIRST_NAME
            + " TEXT," + COL_LAST_NAME + " TEXT," + COL_EMAIL
            + " TEXT" + ")";

    private static final String CREATE_TABLE_PUZZLE = "CREATE TABLE "
            + TABLE_PUZZLE + "(" + COL_PUZZLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_PHRASE
            + " TEXT," + COL_MAX_WRONG_GUESS + " INTEGER," + COL_CREATED_BY
            + " TEXT" + ")";

    private static final String CREATE_TABLE_TOURNAMENT = "CREATE TABLE "
            + TABLE_TOURNAMENT + "(" + COL_TOURNAMENT_NAME + " TEXT," + COL_PUZZLE_ID
            + " INTEGER," + COL_CREATED_BY + " TEXT,"
            + " PRIMARY KEY (" + COL_TOURNAMENT_NAME + "," + COL_PUZZLE_ID + "))";

    private static final String CREATE_TABLE_SOLVE_PUZZLE = "CREATE TABLE "
            + TABLE_SOLVE_PUZZLE + "(" + COL_USERNAME + " TEXT," + COL_PUZZLE_ID
            + " INTEGER," + COL_PRIZE + " INTEGER,"
            + " PRIMARY KEY (" + COL_USERNAME + "," + COL_PUZZLE_ID + "))";

    private static final String CREATE_TABLE_SOLVE_TOURNAMENT = "CREATE TABLE "
            + TABLE_SOLVE_TOURNAMENT + "(" + COL_USERNAME + " TEXT," + COL_TOURNAMENT_NAME + " TEXT,"
            + COL_PRIZE + " INTEGER, "
            + " PRIMARY KEY (" + COL_USERNAME + "," + COL_TOURNAMENT_NAME + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_PUZZLE);
        db.execSQL(CREATE_TABLE_SOLVE_PUZZLE);
        db.execSQL(CREATE_TABLE_SOLVE_TOURNAMENT);
        db.execSQL(CREATE_TABLE_TOURNAMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUZZLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURNAMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLVE_PUZZLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLVE_TOURNAMENT);

        // create new tables
        onCreate(db);
    }

    // Creating a player
    public boolean createPlayer(String username, String firstName, String lastName, String email) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_FIRST_NAME, firstName);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_EMAIL, email);

        long result = db.insert(TABLE_PLAYER, null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Creating a puzzle
    public boolean createPuzzle(String phrase, int maxWrongGuess, String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PHRASE, phrase);
        values.put(COL_MAX_WRONG_GUESS, maxWrongGuess);
        values.put(COL_CREATED_BY, username);

        long result = db.insert(TABLE_PUZZLE, null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Creating a puzzle
    public boolean createTournament(String tournament_name, int puzzle_ID, String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TOURNAMENT_NAME, tournament_name);
        values.put(COL_PUZZLE_ID, puzzle_ID);
        values.put(COL_CREATED_BY, username);

        long result = db.insert(TABLE_TOURNAMENT, null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean createSolvePuzzle(String username, int puzzle_ID, int totalPrize) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PUZZLE_ID, puzzle_ID);
        values.put(COL_PRIZE, totalPrize);

        long result = db.insert(TABLE_SOLVE_PUZZLE, null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateSolvePuzzle(String username, int puzzle_ID, int totalPrize) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PUZZLE_ID, puzzle_ID);
        values.put(COL_PRIZE, totalPrize);

        int result = db.update(TABLE_SOLVE_PUZZLE, values, "puzzle_ID="+puzzle_ID,null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getPlayerUsername() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_USERNAME + " from " + TABLE_PLAYER, null);
        return res;
    }

    public Cursor getPuzzleID() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_PUZZLE_ID + " FROM " + TABLE_PUZZLE, null);
        res.moveToLast();
        return res;
    }

    // Get the puzzle that the current user created or not played
    public Cursor getSelfPuzzle(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        // select puzzle_id from puzzle where created_by = username
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_PUZZLE + " WHERE " +
                COL_CREATED_BY + " == \"" + username + "\" or " + COL_PUZZLE_ID + " in (select " +
                COL_PUZZLE_ID + " from " + TABLE_SOLVE_PUZZLE + " where " + COL_USERNAME + " == \"" +
                username + "\")", null);
        //Cursor res = db.rawQuery("select * from puzzle where created_by == \"aaa\"", null);
        return res;
    }

    // Get the puzzle other than those the current user created or already played
    public Cursor getOtherPuzzle(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        //select puzzle_id from puzzle where created_by != username
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_PUZZLE + " WHERE " +
                COL_CREATED_BY + " != \"" + username + "\" and " + COL_PUZZLE_ID + " not in (select " +
                COL_PUZZLE_ID + " from " + TABLE_SOLVE_PUZZLE + " where " + COL_USERNAME + " == \"" +
                username + "\")", null);
        return res;
    }

    public Cursor getTournament(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        //select tournaments that contain
        //1) no puzzles created by the player 2) at least one puzzle not played
        Cursor res = db.rawQuery("select distinct tournament_name from tournament where puzzle_id not in " +
                "(select puzzle_id from solvepuzzle where username == \"" + username + "\") except " +
                "select distinct tournament_name from tournament where puzzle_id in " +
                "(select puzzle_id from puzzle where created_by == \"" + username + "\") except " +
                "select tournament_name from solvetournament where username == \"" +
                username + "\"", null);

        return res;
    }

    public Cursor getSolveTournament(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select distinct tournament_name from tournament where puzzle_id not in " +
                "(select puzzle_id from solvepuzzle where username == \"" + username + "\") except " +
                "select distinct tournament_name from tournament where puzzle_id in " +
                "(select puzzle_id from puzzle where created_by == \"" + username + "\") intersect " +
                "select tournament_name from solvetournament where username == \"" +
                username + "\"", null);

        return res;
    }

    public boolean joinTournament(String username, String tournament) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_TOURNAMENT_NAME, tournament);

        long result = db.insert(TABLE_SOLVE_TOURNAMENT, null, values);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getOtherPuzzleinTournament(String username, String tournament) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery( "select * from puzzle where puzzle_id in (select " +
                "puzzle_id from tournament where tournament_name == \"" + tournament +
                "\" except select puzzle_id from solvepuzzle where username == \"" + username +
                "\")", null);

        return res;
    }

    public Cursor getTournamentPrize(String username, String tournament) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select sum(prize) as sumPrize from solvepuzzle where username == \"" +
                username + "\" and puzzle_id in (select puzzle_id from tournament where tournament_name == \"" +
                tournament + "\")", null);

        return res;

    }

    public void insertTournamentPrize(String username, String tournament, int prize) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(COL_PRIZE, prize);

        String[] args = new String[]{username, tournament};
        db.update(TABLE_SOLVE_TOURNAMENT, newValues, "username=? AND tournament_name=?", args);
    }

    public Cursor myPuzzleStatistics(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select puzzle_id, prize from solvepuzzle where username == \"" +
                username + "\" order by puzzle_id", null);
        //Cursor res = db.rawQuery("select username from solvepuzzle", null);

        return res;

    }

    public Cursor myTournamentStatistics(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select tournament_name, prize from solvetournament where username == \"" +
                username + "\" and prize is not null order by tournament_name", null);

        return res;

    }

    public Cursor puzzleStatistics(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select R1.puzzle_id, R2.totalPlayer, R2.topPrize, R1.username from " +
                "solvepuzzle R1 join (select puzzle_id, count(username) as totalPlayer, max(prize) as topPrize" +
                " from solvepuzzle group by puzzle_id) R2 on R1.puzzle_id == R2.puzzle_id " +
                "and R1.prize == R2.topPrize order by R1.puzzle_id", null);

        return res;

    }

    public Cursor tournamentStatistics(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select R1.tournament_name, R2.totalPlayer, R2.topPrize, R1.username from " +
                "solvetournament R1 join (select tournament_name, count(username) as totalPlayer, max(prize) as topPrize" +
                " from solvetournament group by tournament_name) R2 on R1.tournament_name == R2.tournament_name  and " +
                "R1.prize == R2.topPrize where R1.prize is not null order by R1.tournament_name", null);

        return res;

    }

    public Cursor checkTournamentName () {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select tournament_name from Tournament", null);
        return res;
    }
}
