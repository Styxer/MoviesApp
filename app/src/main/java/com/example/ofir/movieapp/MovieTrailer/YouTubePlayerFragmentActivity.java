package com.example.ofir.movieapp.MovieTrailer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ofir.movieapp.BuildConfig;
import com.example.ofir.movieapp.R;
import com.example.ofir.movieapp.Utilities.Common;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;


import androidx.fragment.app.FragmentManager;
import timber.log.Timber;

public class YouTubePlayerFragmentActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {

    //private YouTubePlayerView youTubeView;


    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.youtube_player_fragment);

        //initializing and adding YouTubePlayerFragment
        android.app.FragmentManager fm = getFragmentManager(); 
        String tag = YouTubePlayerFragment.class.getSimpleName();
        YouTubePlayerFragment playerFragment = (YouTubePlayerFragment) fm.findFragmentByTag(tag);
        if (playerFragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            playerFragment = YouTubePlayerFragment.newInstance();
            ft.add(android.R.id.content, playerFragment, tag);
            ft.commit();
        }

        playerFragment.initialize(BuildConfig.YOUTUBE_API_TOKEN, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    Intent intent = getIntent();
                    if (intent.hasExtra(Common.MOVE_ID_KEY)) {
                        String videoID = intent.getStringExtra(Common.MOVE_ID_KEY);
                        youTubePlayer.loadVideo(videoID);
                    }

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(getParent(), RECOVERY_DIALOG_REQUEST).show();
                } else {
                    String errorMessage = String.format(
                            getString(R.string.error_player), youTubeInitializationResult.toString());
                    Toast.makeText(getParent(), errorMessage, Toast.LENGTH_LONG).show();
                    Timber.e(errorMessage);
                }

            }
        });

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


        Intent intent = getIntent();
        if (intent.hasExtra(Common.MOVE_ID_KEY)) {

            String videoID = intent.getStringExtra(Common.MOVE_ID_KEY);
            youTubePlayer.loadVideo(videoID);
        }
        // Hiding player controls
        //  youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            Timber.e(errorMessage);
        }
    }


}
