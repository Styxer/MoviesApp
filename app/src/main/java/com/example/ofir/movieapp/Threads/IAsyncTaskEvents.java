package com.example.ofir.movieapp.Threads;

public interface IAsyncTaskEvents {
    void onPreExecute();
    void onPostExecute();
    void onProgressUpdate(Integer integer);

    void createAsyncTask();
    void startAsyncTask();
    void cancelAsyncTask();
    void  onCancelled();
}
