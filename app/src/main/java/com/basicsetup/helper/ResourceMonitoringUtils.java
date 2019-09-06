package com.anisolutions.BeanLogin.utils;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.anisolutions.BeanLogin.enums.EnumBatteryChargingStatus;
import com.anisolutions.BeanLogin.helpers.ExternalStorageInfoUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

public class ResourceMonitoringUtils {

    /************* Get Network Information ******************/
    public static boolean isWifiConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            int networkType = activeNetwork.getType();
            if (networkType == ConnectivityManager.TYPE_WIFI) {
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    // SSID
    public static String getSSID(Context context) {
        String ssid = null;
        if(NetworkUtils.isNetworkConnected(context)){
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                int networkType = activeNetwork.getType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                    if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                        ssid = connectionInfo.getSSID();
                    }
                } else {
                    ssid = "N/A";
                }
                return ssid;
            } catch (Exception e) {
                e.printStackTrace();
                return ssid;
            }
        }else{
            ssid="N/A";
        }
        return ssid;
    }

    // BSSID
    public static String getBSSID(Context context) {
        String bssid = null;
        if(NetworkUtils.isNetworkConnected(context)){
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                int networkType = activeNetwork.getType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                    if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                        bssid = connectionInfo.getBSSID();
                    }
                } else {
                    bssid = "N/A";
                }
                return bssid;
            } catch (Exception e) {
                e.printStackTrace();
                return bssid;
            }
        }else{
            bssid = "N/A";
        }
       return bssid;
    }

    //MAC_ADDRESS
    public static String getMacAddress(Context context) {
        String macAddress = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            int networkType = activeNetwork.getType();
            if (networkType == ConnectivityManager.TYPE_WIFI) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    macAddress = connectionInfo.getMacAddress();
                }
            }
            return macAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return macAddress;
        }
    }

    //HIDDEN_SSID
    public static boolean getHiddenSSID(Context context) {
        boolean hiddenSSID = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            int networkType = activeNetwork.getType();
            if (networkType == ConnectivityManager.TYPE_WIFI) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    hiddenSSID = connectionInfo.getHiddenSSID();
                }
            }
            return hiddenSSID;
        } catch (Exception e) {
            e.printStackTrace();
            return hiddenSSID;
        }
    }

    //Network-Id
    public static int getNetworkId(Context context) {
        int networkID = 0;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            int networkType = activeNetwork.getType();
            if (networkType == ConnectivityManager.TYPE_WIFI) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    networkID = connectionInfo.getNetworkId();
                }
            }
            return networkID;
        } catch (Exception e) {
            e.printStackTrace();
            return networkID;
        }
    }

    //IP-Address
    public static String getIpAddress(Context context) {
        String ipAddress = null;
        if(NetworkUtils.isNetworkConnected(context)) {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                int networkType = activeNetwork.getType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                    int ip = connectionInfo.getIpAddress();
                    ipAddress = Formatter.formatIpAddress(ip);
                } else {
                    ipAddress = "N/A";
                }
                return ipAddress;
            } catch (Exception e) {
                e.printStackTrace();
                return ipAddress;
            }
        }else{
            ipAddress = "N/A";
        }
        return ipAddress;
    }

    // SubNetMask
    public static String getSubNetMask(Context context) {
        String subnetMask = null;
        if(NetworkUtils.isNetworkConnected(context)) {
            int ipAddress = 0;
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            int networkType = activeNetwork.getType();
            if (networkType == ConnectivityManager.TYPE_WIFI) {
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    ipAddress = connectionInfo.getIpAddress();
                }
                String checkclass = Formatter.formatIpAddress(ipAddress).substring(0, 3);
                int cc = Integer.parseInt(checkclass);
                if (cc > 0 && cc < 224) {
                    if (cc < 128) {
                        subnetMask = "255.0.0.0";
                    }
                    if (cc > 127 && cc < 192) {
                        subnetMask = "255.255.0.0";
                    }
                    if (cc > 191) {
                        subnetMask = "255.255.255.0";
                    }
                }
            } else {
                subnetMask = "N/A";
            }
        }else{
            subnetMask = "N/A";
        }
        return subnetMask;
    }

    //DownloadSpeed
    public static String getDownloadSpeed(Context context) {
        int downLoadSpeed = 0;
        String speed = null;
        if(NetworkUtils.isNetworkConnected(context)){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            NetworkCapabilities nc = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            }
            downLoadSpeed = nc.getLinkDownstreamBandwidthKbps();
            speed = formatSize(downLoadSpeed);
        }else{
            speed = "N/A";
        }

        return speed;
    }

    //UploadSpeed
    public static String getUploadSpeed(Context context) {
        int uploadSpeed = 0;
        String speed = null;
        if(NetworkUtils.isNetworkConnected(context)){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            NetworkCapabilities nc = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            }
            uploadSpeed = nc.getLinkUpstreamBandwidthKbps();
            speed = formatSize(uploadSpeed);
        }else{
            speed = "N/A";
        }

        return speed;
    }

    //TotalSpeed
    public static String getTotalSpeed(Context context) {
        int uploadSpeed = 0;
        int downloadSpeed = 0;
        int totalSpeed = 0;
        String speed = null;
        if(NetworkUtils.isNetworkConnected(context)) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            NetworkCapabilities nc = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            }
            uploadSpeed = nc.getLinkUpstreamBandwidthKbps();
            downloadSpeed = nc.getLinkDownstreamBandwidthKbps();
            totalSpeed = uploadSpeed + downloadSpeed;

            speed = formatSize(totalSpeed);
        }else{
            speed = "N/A";
        }
        return speed;
    }


    /************* Get System Uptime Information *****************/

    // Get date and time since last boot
    public static String getBootDateTime() {
        String bootTime = null;
        DateFormat simple = new SimpleDateFormat("dd/MM/yyyy , h:mm a");
        bootTime = simple.format(new Date(System.currentTimeMillis() - SystemClock.uptimeMillis()));
        return bootTime;
    }

    // Get time difference in hours and minutes
    public static String getUptimeInHours() {
        String upTime = null;
        long difference;
        difference = (Calendar.getInstance().getTimeInMillis() - (System.currentTimeMillis() - SystemClock.uptimeMillis()));
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        long hours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long mins = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        long seconds = difference / secondsInMilli;

        if(elapsedDays != 0) {
            upTime = hours + "h " + mins + "m " + seconds + "s ";
        }else{
            upTime = elapsedDays +"d "+hours + "hours  " + mins + "min " + seconds + "sec ";
        }
        return upTime;
    }

    /*********** Get BatteryStatus of the device **********/

    // IntentFilters to get battery info........
    public static void loadBatterySection(Context context, BroadcastReceiver batteryInfoReceiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        context.registerReceiver(batteryInfoReceiver, intentFilter);
    }

    // Battery Charging status ............
    public static String getBatteryChargingStatus(Intent intent) {
        String batteryStatus = null;
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                batteryStatus = EnumBatteryChargingStatus.CHARGING.name();
                break;

            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                batteryStatus = EnumBatteryChargingStatus.DISCHARGE.name();
                break;

            case BatteryManager.BATTERY_STATUS_FULL:
                batteryStatus = EnumBatteryChargingStatus.FULL.name();
                break;

            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                batteryStatus = EnumBatteryChargingStatus.UNKNOWN.name();
                break;

            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
            default:
                batteryStatus = EnumBatteryChargingStatus.NOT_CHARGING.name();
                break;
        }

        return batteryStatus;
    }

    // Battery Percentage ..............
    public static int getBatteryChargingPercentage(Intent intent) {
        int batteryPercentage = 0;

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level != -1 && scale != -1) {
            batteryPercentage = (int) ((level / (float) scale) * 100f);
        }
        return batteryPercentage;
    }



    public static long getAvailableInternalMemorySize(Context context) {
        long memeory = 0;
        ExternalStorageInfoUtils externalStorageInfoUtils = ExternalStorageInfoUtils.getSingletone(context);
        memeory = externalStorageInfoUtils
                .getInternalAvailableMemory();

        return memeory;
    }

    public static long getUsedInternalStorage(Context context) {
        long memeory = 0;
        ExternalStorageInfoUtils externalStorageInfoUtils = ExternalStorageInfoUtils.getSingletone(context);
        memeory = (externalStorageInfoUtils
                .getInternalTotalMemory() -
                externalStorageInfoUtils
                        .getInternalAvailableMemory());
        return memeory;

    }

    public static float getUsedInternalStorageInPercent(Context context) {

        long used = getUsedInternalStorage(context);
        long total = getTotalInternalMemorySize(context);

        if(total<=0){
            return 0;
        }
        return ((used * 100) / total);
    }

    public static float getFreeInternalStorageInPercent(Context context) {

        long available = getAvailableInternalMemorySize(context);
        long total = getTotalInternalMemorySize(context);

        if(total<=0){
            return 0;
        }
        return ((available * 100) / total);

    }

    public static long getTotalInternalMemorySize(Context context) {
        long memeory = 0;
        ExternalStorageInfoUtils externalStorageInfoUtils = ExternalStorageInfoUtils.getSingletone(context);
        memeory = externalStorageInfoUtils
                .getInternalTotalMemory();

        return memeory;
    }

    public static long getAvailableExternalMemorySize(Context context) {

        long memeory = 0;
        ExternalStorageInfoUtils externalStorageInfoUtils = ExternalStorageInfoUtils.getSingletone(context);
        memeory = externalStorageInfoUtils
                .getExternalAvailableMemory();

        return memeory;
    }

    public static long getUsedExternalStorage(Context context) {

        long memeory = 0;
        ExternalStorageInfoUtils externalStorageInfoUtils = ExternalStorageInfoUtils.getSingletone(context);
        memeory = (externalStorageInfoUtils
                .getExternalTotalMemory() -
                externalStorageInfoUtils
                        .getExternalAvailableMemory());

        return memeory;
    }

    public static long getTotalExternalMemorySize(Context context) {
        long memeory = 0;
        ExternalStorageInfoUtils externalStorageInfoUtils = ExternalStorageInfoUtils.getSingletone(context);
        memeory = externalStorageInfoUtils
                .getExternalTotalMemory();

        return memeory;
    }

    public static float getUsedExternalStorageInPercent(Context context) {

        long used = getUsedExternalStorage(context);
        long total = getTotalExternalMemorySize(context);
        if(total<=0){
            return 0;
        }
        return ((used * 100) / total);
    }

    public static float getFreeExternalStorageInPercent(Context context) {

        long available = getAvailableExternalMemorySize(context);
        long total = getTotalExternalMemorySize(context);

        if(total<=0){
            return 0;
        }
        return ((available * 100) / total);
    }


    public static String formatSize(long size) {

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

        sizeFloat = MathUtils.roundUsingDecimalFormat(sizeFloat,"#.#");
        StringBuilder resultBuffer = new StringBuilder(Double.toString(sizeFloat));

        /*int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }*/

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    /************ Get Memory information of the device  ************/

    public static String getTotalMemoryInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        return Math.round(memoryInfo.totalMem / (Math.pow(10, 9))) + "GB";

    }

    public static String getUsedMemoryInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        return formatSize((memoryInfo.totalMem - memoryInfo.availMem));
    }

    public static String getFreeMemoryInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        return formatSize(memoryInfo.availMem);
    }

    public static int getUsedMemoryInPercent(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        long usedMemory = (memoryInfo.totalMem - memoryInfo.availMem);
        long totalMemory = memoryInfo.totalMem;
        int usedPercent = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            usedPercent = Math.toIntExact((usedMemory * 100) / totalMemory);
        }
        return usedPercent;
    }

    public static int getFreeMemoryInPercent(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = memoryInfo.totalMem;
        long freeMEmory = memoryInfo.availMem;
        int usedPercent = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            usedPercent = Math.toIntExact((freeMEmory * 100) / totalMemory);
        }

        return usedPercent;
    }


    public static String getParticularAppMemoryUsed(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager actManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        actManager.getMemoryInfo(memoryInfo);

        long usedMemoryByApp = getRAM(context, android.os.Process.myPid());

        return formatSize(usedMemoryByApp);
    }

    public static int getParticularAppMemoryInPercent(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager actManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        actManager.getMemoryInfo(memoryInfo);

        long usedMemoryByApp = getRAM(context, android.os.Process.myPid());
        long totalMemory = memoryInfo.totalMem;
        int usedPercent = 0;

        usedPercent = Math.round((usedMemoryByApp * 100) / totalMemory);
        return usedPercent;


    }

    private static long getRAM(Context context, long pid){

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager actManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        actManager.getMemoryInfo(memoryInfo);

        int position = -1;
        long usedMemoryByApp = 0;

        int pidList[] = new int[actManager.getRunningAppProcesses().size()];
        for (int i = 0; i < actManager.getRunningAppProcesses().size(); i++){
            pidList[i] = actManager.getRunningAppProcesses().get(i).pid;
            Log.d("CPU","Pid:"+i+" -"+pidList[i]);

            if(pid == pidList[i]){
                position =i;
            }
        }


        Debug.MemoryInfo[] memoryInfoArray = actManager.getProcessMemoryInfo(pidList);
        if(position > -1){
            usedMemoryByApp = memoryInfoArray[position].getTotalPss();
        }

        //To convert KB in bytes multiply by 1024
        return usedMemoryByApp*1024;
    }
    /********* Get CPU usage ***************/

    private static int maxSamples = 2000;

    public static List<Float> getCpuTotal() {
        List<Float> cpuTotal = new ArrayList<>(maxSamples);
        List<Float> cpuAM = new ArrayList<>(maxSamples);
        BufferedReader reader = null;
        String memoryReaderString = null;
        String[] statArray = null;
        int pId = Process.myPid();
        long work = 0, total = 0, totalBefore = 0, totalT = 0, workT = 0, workBefore = 0, workAM = 0, workAMBefore = 0, workAMT = 0;
        try {
            reader = new BufferedReader(new FileReader("/proc/meminfo"));
            memoryReaderString = reader.readLine();
            while (memoryReaderString != null) {
                while (cpuTotal.size() >= maxSamples) {
                    cpuTotal.remove(cpuTotal.size() - 1);
                }
                memoryReaderString = reader.readLine();
            }

            reader.close();

            if (Build.VERSION.SDK_INT < 26) {
                reader = new BufferedReader(new FileReader("/proc/stat"));
                statArray = reader.readLine().split("[ ]+", 9);
                work = Long.parseLong(statArray[1]) + Long.parseLong(statArray[2]) + Long.parseLong(statArray[3]);
                total = work + Long.parseLong(statArray[4]) + Long.parseLong(statArray[5]) + Long.parseLong(statArray[6]) + Long.parseLong(statArray[7]);
                reader.close();

                Log.d("CPU", "String Array :- " + statArray);
                Log.d("CPU", "Work :- " + work);
                Log.d("CPU", "Total :- " + total);
            }

            reader = new BufferedReader(new FileReader("/proc/" + pId + "/stat"));
            statArray = reader.readLine().split("[ ]+", 18);
            workAM = Long.parseLong(statArray[13]) + Long.parseLong(statArray[14]) + Long.parseLong(statArray[15]) + Long.parseLong(statArray[16]);

            total = Long.parseLong(statArray[3]) + Long.parseLong(statArray[4]) + Long.parseLong(statArray[5]) + Long.parseLong(statArray[5]) +
                    Long.parseLong(statArray[6]) + Long.parseLong(statArray[7]) + Long.parseLong(statArray[8]) + Long.parseLong(statArray[9]) +
                    Long.parseLong(statArray[10]) + Long.parseLong(statArray[11]) + Long.parseLong(statArray[12]) + workAM;

            reader.close();


            Log.d("CPU", "WorkAM :- " + workAM);
            Log.d("CPU", "Total2 :- " + total);

            totalT = total - totalBefore;
            workT = work - workBefore;
            workAMT = workAM - workAMBefore;

            if (Build.VERSION.SDK_INT < 26) {
                cpuTotal.add(0, restrictPercentage(workT * 100 / (float) totalT));
            } else {
                cpuAM.add(0, restrictPercentage(workAMT * 100 / (float) totalT));
            }

            totalBefore = total;
            workBefore = work;
            reader.close();

            if (cpuTotal.isEmpty()) {
                return cpuAM;
            } else {
                return cpuTotal;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return cpuTotal;
    }

    private static float restrictPercentage(float percentage) {
        if (percentage > 100)
            return 100;
        else if (percentage < 0)
            return 0;
        else return percentage;
    }

    /*
         returns received data in Bytes
        * */
    public static long getReceivedNetworkDataUsage(){

        return (TrafficStats.getMobileRxBytes());
    }

    /*
     returns Transmitted data in Bytes
    * */
    public static long getTransmittedNetworkDataUsage(){

        return (TrafficStats.getMobileTxBytes());
    }

    /*
         returns received data in Bytes
        * */
    public static long getReceivedWifiDataUsage(){

        return ((TrafficStats.getTotalRxBytes()-TrafficStats.getMobileRxBytes()));
    }

    /*
     returns Transmitted data in Bytes
    * */
    public static long getTransmittedWifiDataUsage(){

        return ((TrafficStats.getTotalTxBytes()-TrafficStats.getMobileTxBytes()));
    }


    /*
        returns received data in Bytes
       * */
    public static long getReceivedTotalDataUsage(){

        return (TrafficStats.getTotalRxBytes());
    }

    /*
     returns Transmitted data in Bytes
    * */
    public static long getTransmittedTotalDataUsage(){

        return (TrafficStats.getTotalTxBytes());
    }

    /*
        returns received data in Bytes
       * */
    public static long getReceivedCurrentAppDataUsage(int uid){

        return (TrafficStats.getUidRxBytes(uid));
    }

    /*
     returns Transmitted data in Bytes
    * */
    public static long getTransmittedCurrentAppDataUsage(int uid){
        return (TrafficStats.getUidTxBytes(uid));
    }

}
