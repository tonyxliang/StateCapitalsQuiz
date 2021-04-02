package edu.uga.cs.statescapitalquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This is the New Quiz Activity that will start the quiz and
 * cycle through questions before going to the Results Activity
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class NewQuizActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "NewQuizActivity";

    private TextView question;
    private TextView notification;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton checked;
    private Button next;
    private RadioGroup radioGroup;

    int playCount = 0;
    public static int score;
    Random random = new Random();

    private QuizDataAdapter quizDataAdapter = null;
    private List<QuizQuestion> quizQuestionsList;
    private List<Quiz> quizList;
    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quiz);

        //get playCount from putExtras
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            playCount = 0;
            score = 0;
        } else {
            playCount = extras.getInt("playCount");
        }

        radioGroup = findViewById(R.id.buttonGroup);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        notification = findViewById(R.id.notification);
        next = findViewById(R.id.next);

        next.setOnClickListener(new nextButtonClickListener());

        quizDataAdapter = new QuizDataAdapter(this);

        quizDataAdapter.open();

        quiz = new Quiz();

        //asynctask to get quiz question
        new GetQuizQuestionDBTask().execute();

        //listener for radio button select
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                checked = group.findViewById(checkedId);
                notification.setText("You selected " + checked.getText());
                Log.e(DEBUG_TAG, "checked.getText: " + checked.getText());
                //this will set the QuizQuestion answer to selected radio button
                quizQuestionsList.get(0).setAnswer((String) checked.getText());
            }
        });
    }

    //function to uncheck the radio button group upon activity refresh
    public void uncheckRadio() {
        RadioGroup radioGroup = findViewById(R.id.buttonGroup);
        radioGroup.clearCheck();
    }

    //temporary function to go to next quiz question
    private class nextButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //testing, will stop at 3 questions
            if (playCount == 5) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    //no radio buttons are checked
                    notification.setText("Please select an option");
                } else {
                    //async tasks that will execute on diff thread than main ui
                    new StoreQuizQuestionDBTask().execute();
                    new StoreQuizResultsDBTask().execute();

                    //quizDataAdapter.close();

                    Intent newIntent = new Intent(view.getContext(), QuizResultsActivity.class);
                    //this putExtra doesn't work
                    //Log.e(DEBUG_TAG, "SCORE BEFORE PUT EXTRA " + score);
                    newIntent.putExtra("currentScore", score);
                    //Log.e(DEBUG_TAG, "SCORE AFTER PUT EXTRA " + score);
                    //prevent going to this activity by pressing system back button
                    finish();
                    //Log.e(DEBUG_TAG, "SCORE BEFORE STARTING INTENT " + quiz.getScore());
                    view.getContext().startActivity(newIntent);
                }
            } else {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    //no radio buttons are checked
                    notification.setText("Please select an option");
                } else {
                    //one of the radio buttons is checked

                    //this block is for the rounds before the last round in the quiz game
                    new StoreQuizQuestionDBTask().execute();
                    //quizDataAdapter.close();
                    Log.e(DEBUG_TAG, "StoreQuizQuestionDBTask: checking score " + score);
                    playCount++;
                    //playCount++ and put into extras for next activity start (refresh)
                    getIntent().putExtra("playCount", playCount);
                    //prevent reloading of this activity by pressing system back button (finish())
                    finish();
                    startActivity(getIntent());
                }
            }
        }
    }

    private class GetQuizQuestionDBTask extends AsyncTask<Void, Void, List<QuizQuestion>> {

        protected List<QuizQuestion> doInBackground(Void... params) {
            quizDataAdapter.open();
            //array list of quiz questions from inside a QuizDataAdapter object
            quizQuestionsList = quizDataAdapter.retrieveQuizQuestion();

            Log.d(DEBUG_TAG, "GetQuizQuestionDBTask: Quiz Question retrieved: " + quizQuestionsList.size());

            return quizQuestionsList;
        }

        @Override
        protected void onPostExecute(List<QuizQuestion> quizQuestionsList) {
            super.onPostExecute(quizQuestionsList);
            //set text based on db query
            //use get(0) to get queried row

            String o1 = quizQuestionsList.get(0).getCapital();
            String o2 = quizQuestionsList.get(0).getCity2();
            String o3 = quizQuestionsList.get(0).getCity3();
            ArrayList<String> strings = new ArrayList<>();
            strings.add(o1);
            strings.add(o2);
            strings.add(o3);
            Collections.shuffle(strings);

            question.setText("What is the capital of " + quizQuestionsList.get(0).getState() + "?");
            option1.setText(strings.get(0));
            option2.setText(strings.get(1));
            option3.setText(strings.get(2));
        }
    }

    private class StoreQuizQuestionDBTask extends AsyncTask<Void, Void, QuizQuestion> {
        protected QuizQuestion doInBackground(Void... voids) {
            quizDataAdapter.open();
            QuizQuestion quizQ = quizQuestionsList.get(0);
            //array list of quiz questions from inside a QuizDataAdapter object
            quizDataAdapter.storeQuizAnswer(quizQ);

            return quizQ;
        }
    }

    private class StoreQuizResultsDBTask extends AsyncTask<Void, Void, Quiz> {
        protected Quiz doInBackground(Void... voids) {
            quizDataAdapter.open();
            quiz = quizDataAdapter.storeQuizResult(quiz);

            Log.e(DEBUG_TAG, "StoreQuizResultsDBTask: Quiz results stored");

            Log.e(DEBUG_TAG, "SCORE IN ASYNCTASK " + quiz.getScore());
            //these probably dont work
            score = quiz.getScore();
            quiz.setScore(score);
            Log.e(DEBUG_TAG, "SCORE IN ASYNCTASK " + score);
            return quiz;
        }
    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "NewQuizActivity.onResume()" );
        // open the database in onResume
        if( quizDataAdapter != null )
            quizDataAdapter.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "NewQuizActivity.onPause()" );
        // close the database in onPause
        if( quizDataAdapter != null )
            //quizDataAdapter.close();
        super.onPause();
    }
}