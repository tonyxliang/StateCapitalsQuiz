package edu.uga.cs.statescapitalquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This is the Main Activity that houses the quiz menu
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class MainActivity extends AppCompatActivity {

    private Button start;
    private Button history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.startQuizButton);
        history = findViewById(R.id.viewQuizHistoryButton);

        start.setOnClickListener(new startButtonClickListener());
        history.setOnClickListener(new historyButtonClickListener());
    }

    private class startButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), NewQuizActivity.class);
            view.getContext().startActivity( intent );
        }
    }

    private class historyButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ViewQuizHistoryActivity.class);
            view.getContext().startActivity(intent);
        }
    }


}