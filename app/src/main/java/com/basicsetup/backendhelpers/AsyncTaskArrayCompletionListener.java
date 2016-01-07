package com.wassupondemand.dobe.backendhelpers;

import java.util.ArrayList;

/**
 * Created by pankaj on 7/8/15.
 */
// Defines protocol between caller and the called.
public interface AsyncTaskArrayCompletionListener<T> {
    void onTaskComplete(ArrayList<T> var1);
    void onException(Exception var1);
}
