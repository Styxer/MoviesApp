package com.example.ofir.movieapp.Utilities;

import android.util.Log;

import com.example.ofir.movieapp.BuildConfig;

import androidx.annotation.Nullable;
import timber.log.Timber;

public class Logging {

    public static final int MAX_LOG_LENGTH = 4000;

    public static void InitLogging() {
        if (BuildConfig.DEBUG) {//DEBUG TREE
            Timber.plant(new MyDebugTree());
            //add the line number to the lag

        } else {//RELEASE TREE
            Timber.plant(new ReleaseTree());
        }
    }

    private static class ReleaseTree extends Timber.Tree {

        @Override
        protected boolean isLoggable(@Nullable String tag, int priority) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
                return false;

            return true;
        }

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (isLoggable(tag, priority)) {
                if (message.length() < MAX_LOG_LENGTH) {
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, message);
                    } else {
                        Log.println(priority, tag, message);
                    }
                    return;
                }

                for (int i = 0, length = message.length(); i < length; i++) {
                    int newLine = message.indexOf('\n', i);
                    newLine = newLine != -1 ? newLine : length;
                    do{
                        int end = Math.min(newLine, i + MAX_LOG_LENGTH);
                        String part = message.substring(i, end);
                        if (priority == Log.ASSERT) {
                            Log.wtf(tag, part);
                        } else {
                            Log.println(priority, tag, part);
                        }
                        i = end;
                    }while (i < newLine);

                }
            }


        }
    }

    private static class MyDebugTree extends Timber.DebugTree {
        @Override
        protected String createStackElementTag(StackTraceElement element) {
            return String.format("(%s:%s)#%s",
                    element.getFileName(),
                    element.getLineNumber(),
                    element.getMethodName());
        }
    }
}
