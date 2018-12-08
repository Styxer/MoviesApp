package com.example.ofir.movieapp.Threads;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import timber.log.Timber;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;

/**
 * A simple {@link Fragment} subclass.
 */
public class CounterFragment extends Fragment implements View.OnClickListener {

    private TextView mainText;
    private Button createAsyncBtn, startAsyncBtn, cancelAsyncBtn;

    private  IAsyncTaskEvents taskEvents;

    public CounterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_counter, container, false);

        mainText = view.findViewById(R.id.main_asnyc_content);

        createAsyncBtn = view.findViewById(R.id.create_async_btn);
        startAsyncBtn = view.findViewById(R.id.start_async_btn);
        cancelAsyncBtn = view.findViewById(R.id.cancel_async_btn);

        createAsyncBtn.setOnClickListener(this);
        startAsyncBtn.setOnClickListener(this);
        cancelAsyncBtn.setOnClickListener(this);

        //get main textValue
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(Common.ASYNC_MAIN_CONTENT_KEY)){
            String mainContext = bundle.getString(Common.ASYNC_MAIN_CONTENT_KEY);

            mainText.setText(mainContext);
        }else{
            Timber.e("bundle is empty or missing key");
        }

        return  view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity mainActivity = getActivity();
        if( mainActivity instanceof IAsyncTaskEvents){
            taskEvents = (IAsyncTaskEvents)mainActivity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        taskEvents  = null;
    }

    @Override
    public void onClick(View v) {
        if (taskEvents == null || !isAdded())
            return;

        switch (v.getId()){
            case R.id.create_async_btn:
                taskEvents.createAsyncTask();
                break;

            case R.id.start_async_btn:
                taskEvents.startAsyncTask();
                break;

            case R.id.cancel_async_btn:
                taskEvents.cancelAsyncTask();
                break;
        }
    }

    public void updateFragmentText(String string) {
        if(mainText != null) {
            mainText.setText(string);
        }
    }
}
