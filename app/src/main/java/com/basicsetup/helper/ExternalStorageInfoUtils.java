package com.clevertrap.democode;


import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExternalStorageInfoUtils {


    private static ExternalStorageInfoUtils INSTANCE;

    public static final ExternalStorageInfoUtils getSingletone(Context context){

        if(INSTANCE!=null)
        return INSTANCE;

        synchronized (ExternalStorageInfoUtils.class){

            if(INSTANCE!=null){
                return INSTANCE;
            }

            INSTANCE = new ExternalStorageInfoUtils(context);

            return INSTANCE;
        }
    }

    private String paths[] =null;

    private ExternalStorageInfoUtils(Context context){
        paths = getExternalStorageDirectories(context);
    }
    public boolean isExternalStorageRemovable(Context context){
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null)
            return true;
        else
            return false;
    }

    public long getInternalTotalMemory(){
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return statFs.getBlockCountLong()*statFs.getBlockSizeLong();
    }

    public long getInternalAvailableMemory(){
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return statFs.getAvailableBlocksLong()*statFs.getBlockSizeLong();
    }

    public long getExternalTotalMemory(){

        if(paths.length <= 0){
            return 0;
        }

        StatFs statFs = new StatFs(paths[0]);
        return statFs.getBlockCountLong()*statFs.getBlockSizeLong();
    }

    public long getExternalAvailableMemory(){
        if(paths.length <= 0){
            return 0;
        }

        StatFs statFs = new StatFs(paths[0]);
        return statFs.getAvailableBlocksLong()*statFs.getBlockSizeLong();
    }

    public String formatSize(long size) {

        double sizeFloat = size;
        String suffix = null;
        Log.d("CPU","Size: "+size);
        if (sizeFloat >= 1024) {
            suffix = "KB";
            sizeFloat /= 1024;
            if (sizeFloat >= 1024) {
                suffix = "MB";
                sizeFloat /= 1024;
                if (sizeFloat >= 1024) {
                    suffix = "GB";
                    sizeFloat /= 1024;
                }
            }
        }

        sizeFloat = roundUsingDecimalFormat(sizeFloat,"#.#");
        StringBuilder resultBuffer = new StringBuilder(Double.toString(sizeFloat));

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    private double roundUsingDecimalFormat(double value,String decimalFormaterString){
        DecimalFormat format = new DecimalFormat(decimalFormaterString);
        return Double.parseDouble(format.format(value));
    }

    /* returns external storage paths (directory of external memory card) as array of Strings */
    private String[] getExternalStorageDirectories(Context context) {

        List<String> results = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = context.getExternalFilesDirs(null);
            String internalRoot = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();

            for (File file : externalDirs) {
                if(file==null) //solved NPE on some Lollipop devices
                    continue;
                String path = file.getPath().split("/Android")[0];

                if(path.toLowerCase().startsWith(internalRoot))
                    continue;

                boolean addPath = false;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addPath = Environment.isExternalStorageRemovable(file);
                }
                else{
                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                }

                if(addPath){
                    results.add(path);
                }
            }
        }

        if(results.isEmpty()) { //Method 2 for all versions
            // better variation of: http://stackoverflow.com/a/40123073/5002496
            String output = "";
            try {
                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
                        .redirectErrorStream(true).start();
                process.waitFor();
                final InputStream is = process.getInputStream();
                final byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output = output + new String(buffer);
                }
                is.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if(!output.trim().isEmpty()) {
                String devicePoints[] = output.split("\n");
                for(String voldPoint: devicePoints) {
                    results.add(voldPoint.split(" ")[2]);
                }
            }
        }

        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d("WASTE", results.get(i) + " might not be extSDcard");
                    results.remove(i--);
                }
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
                    Log.d("WASTE", results.get(i)+" might not be extSDcard");
                    results.remove(i--);
                }
            }
        }

        String[] storageDirectories = new String[results.size()];
        for(int i=0; i<results.size(); ++i) storageDirectories[i] = results.get(i);

        return storageDirectories;
    }
}
