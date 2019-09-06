package com.basicsetup.helper;


import android.os.Environment;
import android.util.Log;

import com.basicsetup.helper.AppExecutor;
import com.basicsetup.helper.IOUtils;

import java.io.File;
import java.util.Calendar;

public class LoggerFile {

    private static boolean isLoggingEnabled = true;
    public static String filePath = null;

    static {
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"BasicSetup";
    }
    public static void d(String tag,String message){

        AppExecutor.getINSTANCE().getLoggerIO().execute(()->{
            Log.d(tag,message);

            if(isLoggingEnabled)
            print(tag,message,"=========================================================");
                });

    }

    public static void e(String tag,String message){
        AppExecutor.getINSTANCE().getLoggerIO().execute(()->{
            Log.e(tag,message);

            if(isLoggingEnabled)
            print(tag,message,".........................................................");
        });
    }

    private static void print(String tag,String message,String logSeperator){

            StringBuilder builder = new StringBuilder();
            builder.append("\u200B");
            builder.append("\n\n\n");
            builder.append(logSeperator);
            builder.append("\n");
            builder.append(tag);
            builder.append("\n");
            builder.append(Calendar.getInstance().getTime().toString());
            builder.append("\n\n");
            builder.append(message);


            IOUtils.writeToFile(builder.toString(), filePath,"COMMON_LOG_FILE");
            IOUtils.writeToFile(builder.toString(), filePath,tag);


    }
}
