package edu.uga.cs.statescapitalquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * This is the View Quiz History Activity responsible for showing the user
 * past quizzes they have taken
 * This was developed with the help of Dr. Kochut's example projects
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class ViewQuizHistoryActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "ViewQuizHistoryActivity";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private QuizDataAdapter quizDataAdapter = null;
    private List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz_history);

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        quizDataAdapter = new QuizDataAdapter(this);

        new GetQuizDBTask().execute();
        //quizDataAdapter.close();
    }

    private class GetQuizDBTask extends AsyncTask<Void, Void, List<Quiz>> {

        protected List<Quiz> doInBackground(Void... params) {
            quizDataAdapter.open();
            //array list of obj quiz from inside a QuizDataAdapter object
            quizList = quizDataAdapter.retrieveQuizzes();

/*            //set text based on db query
            //use get(0) to get queried row
            question.setText("What is the capital of " + quizQuestionsList.get(0).getState() + "?");
            option1.setText(quizQuestionsList.get(0).getCapital());
            option2.setText(quizQuestionsList.get(0).getCity2());
            option3.setText(quizQuestionsList.get(0).getCity3());*/

            //Log.d(DEBUG_TAG, "GetQuizQuestionDBTask: Quiz Question retrieved: " + quizList.size());

            return quizList;
        }

        @Override
        protected void onPostExecute(List<Quiz> quizList) {
            super.onPostExecute(quizList);
            recyclerAdapter = new QuizRecyclerAdapter(quizList);
            recyclerView.setAdapter(recyclerAdapter);
            Log.e(DEBUG_TAG, "onPostExecute: after setAdapter");
        }
    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "ReviewJobLeadsActivity.onResume()" );
        // open the database in onResume
        if( quizDataAdapter != null )
            quizDataAdapter.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "ReviewJobLeadsActivity.onPause()" );
        // close the database in onPause
        if( quizDataAdapter != null )
            quizDataAdapter.close();
        super.onPause();
    }
}