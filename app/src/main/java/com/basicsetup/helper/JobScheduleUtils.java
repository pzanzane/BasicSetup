package com.anisolutions.BeanLogin.utils;


import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.anisolutions.BeanLogin.helpers.Constants;
import com.anisolutions.BeanLogin.services.JobSchedulerService;

import java.util.Date;


public class JobScheduleUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JobInfo createJobSchedule(Context mContext, String message, long delayInMillis) {
        PersistableBundle bundle = new PersistableBundle();
        int time=0;
        if(PreferenceUtils.getINSTANCE(mContext).getInteger(Constants.JOBSCHEDULER_CLASS.JOBID)!=null)
        {
           time=PreferenceUtils.getINSTANCE(mContext).getInteger(Constants.JOBSCHEDULER_CLASS.JOBID);
        }
        else
        {
            time = (int) new Date().getTime();
            PreferenceUtils.getINSTANCE(mContext).putInteger(Constants.JOBSCHEDULER_CLASS.JOBID,time);
        }
        bundle.putInt(Constants.JOBSCHEDULER_CLASS.JOBID, time);
        bundle.putString(Constants.JOBSCHEDULER_CLASS.MESSAGE, message);
        bundle.putInt(Constants.JOBSCHEDULER_CLASS.JOBTYPE, 101);
        bundle.putString(Constants.JOBSCHEDULER_CLASS.TIMEDELAY, String.valueOf(delayInMillis));

        JobInfo.Builder builder = new JobInfo.Builder(time, new ComponentName(mContext, JobSchedulerService.class));
        builder.setMinimumLatency(delayInMillis);
        builder.setOverrideDeadline(delayInMillis);
        builder.setExtras(bundle);
        JobInfo jobInfo = builder.build();
        Log.d("WASTE", "JobInfo:" + jobInfo.toString() + " delayInMillis: " + delayInMillis);

        return jobInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JobInfo createJobScheduleForLocation(Context mContext, String message, long delayInMillis) {
        int time = (int) new Date().getTime();
        PersistableBundle bundle = new PersistableBundle();
        bundle.putInt("JOBID", time);
        bundle.putString("MESSAGE", message);
        bundle.putInt("JOBTYPE", 102);
        bundle.putString("TIMEDELAY", String.valueOf(delayInMillis));

        JobInfo.Builder builder = new JobInfo.Builder(time, new ComponentName(mContext, JobSchedulerService.class));
        builder.setMinimumLatency(delayInMillis);
        builder.setOverrideDeadline(delayInMillis);
        builder.setExtras(bundle);
        JobInfo jobInfo = builder.build();
        Log.d("WASTE", "JobInfo:" + jobInfo.toString() + " delayInMillis: " + delayInMillis);

        return jobInfo;
    }

}
