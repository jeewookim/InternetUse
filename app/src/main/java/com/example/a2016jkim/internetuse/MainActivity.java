package com.example.a2016jkim.internetuse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {
private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button dl = (Button)findViewById(R.id.download);
        dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("hi", "dfgs");
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Downloading file now");
                dialog.setIndeterminate(true);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setCancelable(true);
                new DownloadFilesTask(MainActivity.this).execute("http://shakespeare.mit.edu/hamlet/full.html");
            }
        });

        Button a = (Button)findViewById(R.id.a);
        a.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("hi", "asdf");
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


    public class DownloadFilesTask extends AsyncTask<String, Integer, String> {
        DownloadFilesTask( AppCompatActivity activity)
        {
            context = activity;
        }
        private Context context;

        protected String doInBackground(String...surl) {
            Log.i("got to", "execute");
            InputStream input = null;
            OutputStream output = null;
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            try {
                //CONNECTING TO INTERNET
                URL url = new URL(surl[0]);
                Log.i("url ", "is " + url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                System.out.println(connection.getResponseCode());

                //GETTING SIZE OF SITE
                int fileLength = tryGetFileSize(url);
                Log.i("url size ", " is: " + fileLength);


                //READING SITE
                input = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = "";


                long total = 0;
                while ((inputLine = in.readLine()) != null) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    stringBuilder.append(inputLine + "\n");
                    total = stringBuilder.length();
                    if (total > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    Log.i("Hamlet: ", "" + inputLine + "\n");
                }
                Log.i("total; ", "is " + total);


                in.close();

                //output = new FileOutputStream("hamlet.txt");

            } catch (Exception e) {
                return "";
//            }
//            finally{
//                try{
//                    if(output!=null)
//                        output.close();
//                    if(input!= null)
//                        input.close();
//                }
//                catch(IOException ignored){}
//            }

            }
            return stringBuilder.toString();
        }
        private int tryGetFileSize(URL url) {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                conn.getInputStream();
                return conn.getContentLength();
            } catch (IOException e) {
                return -1;
            } finally {
                conn.disconnect();
            }
        }
        protected void onProgressUpdate(Integer...i) {
            super.onProgressUpdate(i);
            // if we get here, length is known, now set indeterminate to false
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgress(i[0]);
        }

        protected void onPostExecute(String s) {
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("hamlet.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(s);
                    outputStreamWriter.close();
                    Log.i("length of sbuilder:", s.length() + "");
                    //Log.i("Hamlet: ", s + "");
                    Log.i("wrote to output file", ": yes" );
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
        }
    }
}