package edu.uga.cs.statescapitalquiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the Splash Activity launched at the start of the app
 * that will check for database existence and copy a predefined db file
 * in case of non-existence
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        QuizDataAdapter DBHelper = new QuizDataAdapter(this);
        //createDatabase will make the DBHelper check if DB exists, then it will async copy
        //this is used only once and so it resides in the splash activity
        DBHelper.createDatabase();

        //timed splash activity that will transition to MainActivity after time elapse in sleep()
        Thread timer=new Thread()
        {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }
}