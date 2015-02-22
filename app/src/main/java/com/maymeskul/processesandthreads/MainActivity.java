package com.maymeskul.processesandthreads;

import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.Handler;


public class MainActivity extends ActionBarActivity {
    private Button btnAction;
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAction = (Button) findViewById(R.id.btn_action);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                btnAction.setText(text);
            }
        };
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void onClick(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    final int finalI = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            btnAction.setText("Running  " + finalI);
                        }
                    });


                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        //empty
                    }
                }
                Message message = new Message();
                message.obj = "Finished";

                handler.sendMessage(message);
            }
        }).start();
    }

    public class MyTask extends AsyncTask<String, Integer, String> {
        private String text;

        @Override//UI thread
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override//worker thread
        protected String doInBackground(String... params) {
            text = params[0];

            return null;
        }

        @Override//UI thread
        protected void onProgressUpdate(Integer... values) {
            btnAction.setText(text + " " + values[0]);
        }

        @Override//UI thread
        protected void onPostExecute(String s) {
            btnAction.setText("Finished");
        }
    }
}
