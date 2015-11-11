package com.example.a2016jkim.internetuse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
public ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bar = (ProgressBar)findViewById(R.id.progressBar);
        Button sleep = (Button)findViewById(R.id.sleep);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer t = 1000;
                new DownloadFilesTask(MainActivity.this).execute(t, 5);
            }
        });

        Button reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setProgress(0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class DownloadFilesTask extends AsyncTask<Integer, Integer, Integer> {
        DownloadFilesTask( AppCompatActivity activity)
        {
            context = activity;
        }
        private Context context;

        protected Integer doInBackground(Integer... time)  {
            Integer x = time[0];
            for (int i = 0; i < x+5; i+=3) {
                try {
                    Thread.sleep(3);
                    publishProgress(i);
                }
                catch (InterruptedException e) {}
            }
            return x;
        }

        protected void onProgressUpdate(Integer... progress) {
            bar.setProgress(progress[0]);
        }

        protected void onPostExecute(Integer time) {
            int duration = Toast.LENGTH_SHORT;
            CharSequence text = "Done";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();


        }
    }
}