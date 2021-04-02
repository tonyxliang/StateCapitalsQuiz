package edu.uga.cs.statescapitalquiz;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show quizzes in the View Quiz History activity
 * This was also made with help from Dr. Kochut's example projects
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerAdapter.QuizHistoryHolder> {

    public static final String DEBUG_TAG = "QuizRecyclerAdapter";

    private List<Quiz> quizHistoryList;

    public QuizRecyclerAdapter(List<Quiz> quizHistoryList) {
        this.quizHistoryList = quizHistoryList;
    }

    class QuizHistoryHolder extends RecyclerView.ViewHolder {

        TextView quizID;
        TextView date;
        TextView score;

        public QuizHistoryHolder(View itemView) {
            super(itemView);

            quizID = itemView.findViewById(R.id.id);
            date = itemView.findViewById(R.id.date);
            score = itemView.findViewById(R.id.score);

        }
    }

    @Override
    public QuizHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_quizhistory, parent, false);
        return new QuizHistoryHolder(view);
    }

    // This method fills in the values of a holder to show a Quiz.
    // The position parameter indicates the position on the list of quizzes.
    @Override
    public void onBindViewHolder(QuizHistoryHolder holder, int position) {
        Quiz quiz = quizHistoryList.get(position);

        Log.d(DEBUG_TAG, "onBindViewHolder: " + quiz);

        int quizNumOffset = quiz.getId();
        quizNumOffset++;

        holder.quizID.setText("Quiz #: " + String.valueOf(quizNumOffset));
        holder.date.setText("Date Taken: " + String.valueOf(quiz.getDate()));
        holder.score.setText("Score: " + String.valueOf(quiz.getScore()));

    }

    @Override
    public int getItemCount() {
        return quizHistoryList.size();
    }
}
