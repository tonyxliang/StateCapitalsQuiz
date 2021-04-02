package edu.uga.cs.statescapitalquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * This is the Quiz Results Activity responsible for showing the user
 * the most recent quiz's final score
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class QuizResultsActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "QuizResultsActivity";

    Button toMenu;
    TextView score;

    private QuizDataAdapter quizDataAdapter = null;
    private List<Quiz> quizList;
    int currentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        toMenu = findViewById(R.id.toMenu);
        score = findViewById(R.id.score);

        toMenu.setOnClickListener(new QuizResultsActivity.menuButtonClickListener());

        quizDataAdapter = new QuizDataAdapter(this);

        new GetQuizResultsDBTask().execute();

        //for testing
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e( DEBUG_TAG, "GETEXTRAS: SCORE NOT RECEIVED FROM NEWQUIZACTIVITY!!!");
        } else {
            currentScore = extras.getInt("currentScore");
        }

        //quizDataAdapter.close();
    }

    private class menuButtonClickListener implements View.OnClickListener {

        //go to menu button
        @Override
        public void onClick(View view) {
            finish();
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class GetQuizResultsDBTask extends AsyncTask<Void, Void, List<Quiz>> {

        protected List<Quiz> doInBackground(Void... params) {
            quizDataAdapter.open();
            //array list of quiz questions from inside a QuizDataAdapter object
            quizList = quizDataAdapter.retrieveQuizResult();

            Log.e( DEBUG_TAG, "GetQuizDBTask: Quiz retrieved");

            //this is probably working to get current score
            currentScore = quizList.get(0).getScore();
            return quizList;
        }

        @Override
        protected void onPostExecute(List<Quiz> quizList) {
            super.onPostExecute(quizList);
            //set text based on db query
            //this is probably working to get current score
            currentScore = quizList.get(0).getScore();
            score.setText("Score: " + currentScore);
        }
    }
}