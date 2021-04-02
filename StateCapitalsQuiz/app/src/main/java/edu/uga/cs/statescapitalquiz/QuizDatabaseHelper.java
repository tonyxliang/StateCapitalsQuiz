package edu.uga.cs.statescapitalquiz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.AsyncTask;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This is the DB helper used to create, open, and copy the DB to the app
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class QuizDatabaseHelper extends SQLiteOpenHelper {

    private static String TAG = "QuizDatabaseHelper"; // Tag just for the LogCat window
    private static String DB_NAME ="state_capitals.db"; // Database name
    private static int DB_VERSION = 1; // Database version
    private final File DB_FILE;
    private SQLiteDatabase database;
    private final Context context;

    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUESTIONS_COLUMN_ID = "questionID";
    public static final String QUESTIONS_COLUMN_STATE = "state";
    public static final String QUESTIONS_COLUMN_CAPITAL = "capital";
    public static final String QUESTIONS_COLUMN_CITY2 = "city2";
    public static final String QUESTIONS_COLUMN_CITY3 = "city3";
    public static final String QUIZZES_COLUMN_ID = "quizID";
    public static final String QUIZZES_COLUMN_DATE = "quizDate";
    public static final String QUIZZES_COLUMN_RESULT = "quizResult";

    public QuizDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_FILE = context.getDatabasePath(DB_NAME);
        this.context = context;
    }

    public void createDatabase() {
        // If the database does not exist, copy it from the assets.
        boolean dbExist = checkDatabase();
        if (dbExist) {
            Log.e(TAG, "createDatabase database exists, copying now");
            //copyDBAsync();
        }
        if(!dbExist) {
            this.getReadableDatabase();
            Log.e(TAG, "createDatabase not created");
            this.close();
            // Copy the database from assets
            copyDBAsync();
            Log.e(TAG, "createDatabase database created now");
        }
    }

    // Check that the database file exists in databases folder
    private boolean checkDatabase() {
        boolean exist = DB_FILE.exists();
        Log.e(TAG, "checkDatabase database exists in databases folder");
        return exist;
    }

    //asynctask to copy database to app database
    private class QuizDBCopyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                InputStream mInput = context.getAssets().open(DB_NAME);
                OutputStream mOutput = new FileOutputStream(DB_FILE);
                byte[] mBuffer = new byte[256000];
                int mLength;
                while ((mLength = mInput.read(mBuffer)) > 0) {
                    mOutput.write(mBuffer, 0, mLength);
                }
                mOutput.flush();
                mOutput.close();
                mInput.close();
                Log.e(TAG, "QuizDBCopyTask(async) database copied");
            }
            catch (Exception e) {
                Log.e(TAG, "quizdbcopytask fail:" + e);
            }
            return null;
        }
    }

    //function to do copy database asynctask
    private void copyDBAsync() {
        new QuizDBCopyTask().execute();
        Log.e(TAG, "copyDBAsync function - database copy initiated");
    }


    //open the database so we can query it
    public boolean openDatabase() throws SQLException {
        Log.e("DB_PATH-open database:", DB_FILE.getAbsolutePath());
        database = SQLiteDatabase.openDatabase(String.valueOf(DB_FILE), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        // database = SQLiteDatabase.openDatabase(DB_FILE, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return database != null;
    }

    @Override
    public synchronized void close() {
        if(database != null) {
            database.close();
        }
        super.close();
    }

    //unused functions as our database is predefined
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {

    }
}