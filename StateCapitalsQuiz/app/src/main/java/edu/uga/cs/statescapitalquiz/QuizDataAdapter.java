package edu.uga.cs.statescapitalquiz;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This is the adapter of the DatabaseHelper that is responsible for all database
 * IO operations
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class QuizDataAdapter {

    protected static final String TAG = "DataAdapter";
    private static final String DEBUG_TAG = "quizQ object";

    private final Context context;
    private SQLiteDatabase DB;
    private QuizDatabaseHelper DBHelper;

    private static final String[] allQuestionColumns = {
            QuizDatabaseHelper.QUESTIONS_COLUMN_ID,
            QuizDatabaseHelper.QUESTIONS_COLUMN_STATE,
            QuizDatabaseHelper.QUESTIONS_COLUMN_CAPITAL,
            QuizDatabaseHelper.QUESTIONS_COLUMN_CITY2,
            QuizDatabaseHelper.QUESTIONS_COLUMN_CITY3
    };

    private static final String[] allQuizColumns = {
            QuizDatabaseHelper.QUIZZES_COLUMN_ID,
            QuizDatabaseHelper.QUIZZES_COLUMN_DATE,
            QuizDatabaseHelper.QUIZZES_COLUMN_RESULT
    };

    private static final String[] quizIDCol = {
            QuizDatabaseHelper.QUIZZES_COLUMN_ID,
    };

    public QuizDataAdapter(Context context) {
        this.context = context;
        DBHelper = new QuizDatabaseHelper(context);
    }

    public QuizDataAdapter createDatabase() {
        DBHelper.createDatabase();
        return this;
    }

    public QuizDataAdapter open() throws SQLException {
        try {
            DBHelper.openDatabase();
            DB = DBHelper.getReadableDatabase();
            Log.e(TAG, "QuizDataAdapter open() getReadableDatabase done");
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {

        DBHelper.close();
    }

    // Retrieve quiz questions and return them as a List.
    // For the retrieved row, we create a new QuizQuestion (POJO object) instance and add it to the list.
    //quizQuestion is an array list of a single QuizQuestion object (quizQ in this case)
    public List<QuizQuestion> retrieveQuizQuestion() {
        ArrayList<QuizQuestion> quizQuestion = new ArrayList<>();
        Cursor cursor = null;
        Random random = new Random();
        int randomInt = random.nextInt(50);
        String sql;
        int fetchedID;
        int uniqueID;
        int currentQuizID;
        int fetchNum = 0;

        Log.d(DEBUG_TAG, "Trying retrieveQuizQuestion - before try statement");
        try {
            // Execute the select query and get the Cursor to iterate over the row
            cursor = DB.query(QuizDatabaseHelper.TABLE_QUESTIONS, allQuestionColumns,
                    null, null, null, null, null);
            Log.d(DEBUG_TAG, "Trying retrieveQuizQuestion - DB query done");
            //get a quiz question row if row count > 0
            if (cursor.getCount() > 0) {
                // get all attribute values (columns) of this quiz question
                Log.d(DEBUG_TAG, "cursor getCount > 0");
                cursor.moveToFirst();
                //using random here to produce random int for random row (random US state)
                cursor.moveToPosition(randomInt);

               /* sql = "SELECT * from quizzes ORDER BY quizID DESC LIMIT 1";
                cursor = DB.rawQuery(sql, null);
                if (cursor.moveToFirst()) {
                    currentQuizID = cursor.getInt(cursor.getColumnIndex("quizID"));
                } else {

                }

                sql = "SELECT * FROM quizquestion WHERE questionID = " + fetchedID + " AND quizID = " + currentQuizID;
                cursor = DB.rawQuery(sql, null);
                Log.e(DEBUG_TAG, "SQL OK");
                if (cursor.moveToFirst()) {
                    Log.e(DEBUG_TAG, "DUPLICATE FOUND!!");
                    uniqueID = cursor.getInt(cursor.getColumnIndex("questionID"));
                    while (fetchedID == uniqueID) {
                        cursor = DB.query(QuizDatabaseHelper.TABLE_QUESTIONS, allQuestionColumns,
                                null, null, null, null, null);
                        cursor.moveToFirst();
                        randomInt = random.nextInt(50);
                        cursor.moveToPosition(randomInt);
                        fetchedID = cursor.getInt(cursor.getColumnIndex("questionID"));
                        sql = "SELECT * FROM quizquestion WHERE questionID = " + fetchedID;
                        cursor = DB.rawQuery(sql, null);
                        cursor.moveToFirst();
                        uniqueID = cursor.getInt(cursor.getColumnIndex("questionID"));
                    }
                } else {
                    uniqueID = fetchedID;
                }*/

                //compare fetched question ID from initial sql query to
                //potential duplicate in table quizquestion
                //if duplicate, loop to find unique ID
                //else continue on
/*                do {
                    sql = "SELECT * FROM quizquestion WHERE questionID = " + fetchedID;
                    cursor = DB.rawQuery(sql, null);
                    if (cursor.moveToFirst()) {
                        sql = "SELECT & FROM quizquestion WHERE questionID = " + fetchedID;
                        cursor = DB.rawQuery(sql, null);
                        uniqueID = cursor.getInt(cursor.getColumnIndex("questionID"));
                    } else {
                        uniqueID = fetchedID;
                        break;
                    }
                } while(fetchedID == uniqueID);

                sql = "SELECT * FROM questions WHERE questionID = " + uniqueID;
                cursor = DB.rawQuery(sql, null);
                cursor.moveToFirst();*/

                fetchedID = cursor.getInt(cursor.getColumnIndex(QuizDatabaseHelper.QUESTIONS_COLUMN_ID));
                String state = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.QUESTIONS_COLUMN_STATE));
                String capital = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.QUESTIONS_COLUMN_CAPITAL));
                String city2 = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.QUESTIONS_COLUMN_CITY2));
                String city3 = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.QUESTIONS_COLUMN_CITY3));

                // create a new QuizQuestion object and set its state to the retrieved values
                QuizQuestion quizQ = new QuizQuestion(state, capital, city2, city3);

                quizQ.setId(fetchedID); // set the id (the primary key) of this object
                // add it to the list
                quizQuestion.add(quizQ);
                Log.d(DEBUG_TAG, "Retrieved quizQ obj: " + quizQ);
            }
            Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            //close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        //return a list of single quiz question
        return quizQuestion;
    }

    public List<Quiz> retrieveQuizzes() {
        ArrayList<Quiz> quiz = new ArrayList<>();
        Cursor cursor = null;
        Log.d(DEBUG_TAG, "Trying retrieveQuizzes - before try statement");

        try {
            // Execute the select query and get the Cursor to iterate over the row
            cursor = DB.query(QuizDatabaseHelper.TABLE_QUIZZES, allQuizColumns,
                    null, null, null, null, null);
            Log.d(DEBUG_TAG, "Trying retrieveQuiz - DB query done");

            //get a quiz row if row count > 0
            if (cursor.getCount() > 0) {
                //cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    // get all attribute values (columns) of this quiz
                    Log.d(DEBUG_TAG, "cursor getCount > 0");
                    int id = cursor.getInt(cursor.getColumnIndex(QuizDatabaseHelper.QUIZZES_COLUMN_ID));
                    String date = cursor.getString(cursor.getColumnIndex(QuizDatabaseHelper.QUIZZES_COLUMN_DATE));
                    int score = cursor.getInt(cursor.getColumnIndex(QuizDatabaseHelper.QUIZZES_COLUMN_RESULT));

                    // create a new Quiz object and set its state to the retrieved values
                    Quiz quizObj = new Quiz(date, score);
                    quizObj.setId(id);

                    // add it to the list
                    quiz.add(quizObj);
                    //Log.d(DEBUG_TAG, "Retrieved quizQ obj: " + quizObj);
                }
            }
            Log.d(DEBUG_TAG, "Number of records from DB: " + cursor.getCount());
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Exception caught: " + e);
        } finally {
            //close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of single quiz
        return quiz;
    }

    public QuizQuestion storeQuizAnswer(QuizQuestion quizQuestion) {
        Log.e(DEBUG_TAG, "storeQuizAnswer: function entered");
        //read correct answer from DB
        String userAnswer = quizQuestion.getAnswer();
        int currentQuizQuestionID = quizQuestion.getId();
        boolean isCorrect = false;
        Log.e(DEBUG_TAG, "storeQuizAnswer(): currentQuizQuestionID = " + currentQuizQuestionID);
        try {
            //get capital/correct answer
            String sql = "SELECT capital FROM questions WHERE questionID = " + currentQuizQuestionID;
            Cursor cursor = DB.rawQuery(sql, null);
            cursor.moveToFirst();
            String correctAnswer = cursor.getString(cursor.getColumnIndex("capital"));
            Log.e(DEBUG_TAG, "storeQuizAnswer(): correctAnswer = " + correctAnswer);
            Log.e(DEBUG_TAG, "storeQuizAnswer(): userAnswer = " + userAnswer);
            //correct answer if-statement
            if (userAnswer.trim().equals(correctAnswer.trim())) {
                Log.e(DEBUG_TAG, "storeQuizAnswer(): CORRECT ANSWER");
                isCorrect = true;

                //get latest quizID
                sql = "SELECT * from quizzes ORDER BY quizID DESC LIMIT 1";
                cursor = DB.rawQuery(sql, null);
                //quizCount > 0
                if (cursor.moveToFirst()) {
                    int currentQuizID = cursor.getInt(cursor.getColumnIndex("quizID"));
                    currentQuizID++;

                    ContentValues values = new ContentValues();
                    values.put("quizID", currentQuizID);
                    values.put("questionID", currentQuizQuestionID);
                    values.put("answer", userAnswer);
                    values.put("correct", isCorrect);

                    DB.insert("quizquestion", null, values);
                } else {
                    //quizCount = 0
                    int currentQuizID = 0;

                    ContentValues values = new ContentValues();
                    values.put("quizID", currentQuizID);
                    values.put("questionID", currentQuizQuestionID);
                    values.put("answer", userAnswer);
                    values.put("correct", isCorrect);

                    DB.insert("quizquestion", null, values);
                }
                cursor.close();
            } else {
                //incorrect answer branch
                Log.e(DEBUG_TAG, "storeQuizAnswer(): INCORRECT ANSWER");

                //get latest quizID
                sql = "SELECT * from quizzes ORDER BY quizID DESC LIMIT 1";
                cursor = DB.rawQuery(sql, null);
                //quizCount > 0
                if (cursor.moveToFirst()) {
                    int currentQuizID = cursor.getInt(cursor.getColumnIndex("quizID"));
                    currentQuizID++;

                    ContentValues values = new ContentValues();
                    values.put("quizID", currentQuizID);
                    values.put("questionID", currentQuizQuestionID);
                    values.put("answer", userAnswer);
                    values.put("correct", isCorrect);

                    DB.insert("quizquestion", null, values);

                    cursor.close();
                } else {
                    //quizCount = 0
                    int currentQuizID = 0;

                    ContentValues values = new ContentValues();
                    values.put("quizID", currentQuizID);
                    values.put("questionID", currentQuizQuestionID);
                    values.put("answer", userAnswer);
                    values.put("correct", isCorrect);

                    DB.insert("quizquestion", null, values);

                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "storeQuizAnswer(): not good - " + e);
        }
        return quizQuestion;
    }

    public Quiz storeQuizResult(Quiz quiz) {
        Log.e(DEBUG_TAG, "storeQuizResult: function entered");
        int currentQuizID = 0;
        Cursor cursor;
        int quizIDCheck;
        String sql;
        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        int correctCount = 0;

        //get correct answers from DB quizquestion table
        try {
            //get latest quizID
            sql = "SELECT * from quizzes ORDER BY quizID DESC LIMIT 1";
            cursor = DB.rawQuery(sql, null);

            //quizCount > 0; DB quizID is 1 less than current quiz attempt
            if (cursor.moveToFirst()) {
                currentQuizID = cursor.getInt(cursor.getColumnIndex("quizID"));
                currentQuizID++;
            } else {
                //quizCount = 0
                currentQuizID = 0;
            }

            //correct answer sql select
            sql = "SELECT * FROM quizquestion WHERE quizID = " + currentQuizID + " AND correct = 1";
            cursor = DB.rawQuery(sql, null);

            if (cursor.getCount() != 0) {
                Log.e(DEBUG_TAG, "storeQuizResult: RECORDS EXIST");
            } else {
                Log.e(DEBUG_TAG, "storeQuizResult: RECORDS DONT EXIST");
            }

            //enter this if-statement if there is at least 1 correct answer in quizquestion
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    //get correct answer count
                    do {
                        quizIDCheck = cursor.getInt(cursor.getColumnIndex("quizID"));
                        Log.e(DEBUG_TAG, "storeQuizResult(): quizIDCheck in DB = " + quizIDCheck);
                        correctCount++;
                        Log.e(DEBUG_TAG, "storeQuizResult(): correctCount = " + correctCount);
                    } while (cursor.moveToNext());
                }

                Log.e(DEBUG_TAG, "storeQuizResult: INSERT");

                //insert into quizzes table
                ContentValues values = new ContentValues();
                values.put("quizID", currentQuizID);
                values.put("quizDate", date);
                values.put("quizResult", correctCount);

                DB.insert("quizzes", null, values);

                quiz.setId(currentQuizID);
                quiz.setDate(date);
                quiz.setScore(correctCount);

                cursor.close();
            } else {
                //insert quiz into quizzes table with 0 as score (no correct answers in quizquestion)
                Log.e(DEBUG_TAG, "storeQuizResult: cursor count = 0");

                //insert into quizzes table
                ContentValues values = new ContentValues();
                values.put("quizID", currentQuizID);
                values.put("quizDate", date);
                values.put("quizResult", correctCount);

                DB.insert("quizzes", null, values);

                quiz.setId(currentQuizID);
                quiz.setDate(date);
                quiz.setScore(correctCount);

                cursor.close();
            }
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "storeQuizResult(): not good - " + e);
        }
        return quiz;
    }

    public List<Quiz> retrieveQuizResult() {
        Log.e(DEBUG_TAG, "retrieveQuizResult: function entered");
        //Log.e(DEBUG_TAG, "CHECKING CURRENTQUIZID - retrieveQuizResult(): " + quizObjList.get(0).getId());
        ArrayList<Quiz> quiz = new ArrayList<>();
        Cursor cursor;
        String sql;

        //get quiz results from DB quizzes table
        try {
            //get latest quizID for result activity score text
            sql = "SELECT * from quizzes ORDER BY quizID DESC LIMIT 1";
            cursor = DB.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex("quizID"));
                String date = cursor.getString(cursor.getColumnIndex("quizDate"));
                int score = cursor.getInt(cursor.getColumnIndex("quizResult"));

                //set latest quiz object data
                Quiz newQuizObj = new Quiz(date, score);
                newQuizObj.setId(id);
                quiz.add(newQuizObj);
            } else {
                Log.e(DEBUG_TAG, "retrieveQuizResult(): CURSOR EMPTY");
            }

            cursor.close();
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "retrieveQuizResult(): not good - " + e);
        }
        return quiz;
    }
}