package com.example.ofir.movieapp.Threads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;

public class AsyncTaskActivity extends AppCompatActivity implements  IAsyncTaskEvents  {

    private FragmentManager manager;
    private CounterFragment counterFragment;
    private CounterAsyncTask counterAsyncTask;

    public static final int COUNT_TIME = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        manager = getSupportFragmentManager();
        if(counterFragment == null){
            counterFragment = new CounterFragment();
            Bundle bundle =  new Bundle();
            bundle.putString(Common.ASYNC_MAIN_CONTENT_KEY,getString(R.string.async_task_activity));
            counterFragment.setArguments (bundle);
            manager.beginTransaction()
                    .replace(R.id.counter_fragment, counterFragment)
                    .commit();

        }
    }

    @Override
    public void onPreExecute() {
        Toast.makeText(this, getString(R.string.async_pre_execute), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute() {
        Toast.makeText(this, getString(R.string.async_post_execute), Toast.LENGTH_SHORT).show();
        counterFragment.updateFragmentText(getString(R.string.async_done));
        counterAsyncTask = null;
    }

    @Override
    public void onProgressUpdate(Integer integer) {
        counterFragment.updateFragmentText(String.valueOf(integer));
    }

    @Override
    public void createAsyncTask() {
        Toast.makeText(this, getString(R.string.async_on_create), Toast.LENGTH_SHORT).show();
        counterAsyncTask = new CounterAsyncTask(this);
    }

    @Override
    public void startAsyncTask() {
        if (counterAsyncTask == null || counterAsyncTask.isCancelled()){
            Toast.makeText(this, getString(R.string.async_not_created), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.async_on_create), Toast.LENGTH_SHORT).show();
            counterAsyncTask.execute(COUNT_TIME);
        }
    }

    @Override
    public void cancelAsyncTask() {
        if (counterAsyncTask == null) {
            Toast.makeText(this, R.string.async_not_created, Toast.LENGTH_SHORT).show();
        } else {
            counterAsyncTask.cancel(true);
        }
    }

    @Override
    public void onCancelled() {
        Toast.makeText(this, getString(R.string.async_on_cancel), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (counterAsyncTask != null) {
            counterAsyncTask.cancel(false);
            counterAsyncTask = null;
        }
    }
}
