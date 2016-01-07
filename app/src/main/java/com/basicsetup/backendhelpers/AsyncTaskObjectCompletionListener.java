package com.wassupondemand.dobe.backendhelpers;

/**
 * Created by pankaj on 7/8/15.
 */
// Defines protocol between caller and the called.
public interface AsyncTaskObjectCompletionListener<T> {
    void onTaskComplete(T var1);
    void onException(Exception var1);
}
