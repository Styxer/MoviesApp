package com.example.ofir.movieapp.Threads;

import android.os.AsyncTask;
import android.os.SystemClock;

public class CounterAsyncTask extends AsyncTask<Integer,Integer ,Void> {

    private IAsyncTaskEvents taskEvents;
    public static final int UPDATE_UI_INTERVAL_IN_MS = 1000;

    public CounterAsyncTask(IAsyncTaskEvents taskEvents) {
        this.taskEvents = taskEvents;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(taskEvents != null){
            taskEvents.onProgressUpdate(values[0]);
        }
    }

    @Override
    protected Void doInBackground(Integer... integers) {

        int counter = integers[0];
        for (int i = 0; i < counter ; i++) {
            if(isCancelled())
                return null ;
            publishProgress(i);
            SystemClock.sleep(UPDATE_UI_INTERVAL_IN_MS);

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (taskEvents != null) {
            taskEvents.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (taskEvents != null) {
            taskEvents.onPostExecute();
        }
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        if (taskEvents != null) {
            taskEvents.onCancelled();
        }
    }
}
