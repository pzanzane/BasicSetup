package com.clevertrap.democode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Utilities available only on Linux Operating System.
 *
 * <p>
 * A typical use is to assign a thread to CPU monitoring:
 * </p>
 *
 * <pre>
 * &#064;Override
 * public void run() {
 *  while (CpuUtil.monitorCpu) {
 *
 *      LinuxUtils linuxUtils = new LinuxUtils();
 *
 *      int pid = android.os.Process.myPid();
 *      String cpuStat1 = linuxUtils.readSystemStat();
 *      String pidStat1 = linuxUtils.readProcessStat(pid);
 *
 *      try {
 *          Thread.sleep(CPU_WINDOW);
 *      } catch (Exception e) {
 *      }
 *
 *      String cpuStat2 = linuxUtils.readSystemStat();
 *      String pidStat2 = linuxUtils.readProcessStat(pid);
 *
 *      float cpu = linuxUtils.getSystemCpuUsage(cpuStat1, cpuStat2);
 *      if (cpu &gt;= 0.0f) {
 *          _printLine(mOutput, &quot;total&quot;, Float.toString(cpu));
 *      }
 *
 *      String[] toks = cpuStat1.split(&quot; &quot;);
 *      long cpu1 = linuxUtils.getSystemUptime(toks);
 *
 *      toks = cpuStat2.split(&quot; &quot;);
 *      long cpu2 = linuxUtils.getSystemUptime(toks);
 *
 *      cpu = linuxUtils.getProcessCpuUsage(pidStat1, pidStat2, cpu2 - cpu1);
 *      if (cpu &gt;= 0.0f) {
 *          _printLine(mOutput, &quot;&quot; + pid, Float.toString(cpu));
 *      }
 *
 *      try {
 *          synchronized (this) {
 *              wait(CPU_REFRESH_RATE);
 *          }
 *      } catch (InterruptedException e) {
 *          e.printStackTrace();
 *          return;
 *      }
 *  }
 *
 *  Log.i(&quot;THREAD CPU&quot;, &quot;Finishing&quot;);
 * }
 * </pre>
 */
public final class LinuxUtils {

    // Warning: there appears to be an issue with the column index with android linux:
    // it was observed that on most present devices there are actually
    // two spaces between the 'cpu' of the first column and the value of
    // the next column with data. The thing is the index of the idle
    // column should have been 4 and the first column with data should have index 1.
    // The indexes defined below are coping with the double space situation.
    // If your file contains only one space then use index 1 and 4 instead of 2 and 5.
    // A better way to deal with this problem may be to use a split method
    // not preserving blanks or compute an offset and add it to the indexes 1 and 4.

    private static final int FIRST_SYS_CPU_COLUMN_INDEX = 2;

    private static final int IDLE_SYS_CPU_COLUMN_INDEX = 5;

    /** Return the first line of /proc/stat or null if failed. */
    private String readSystemStat() {

        RandomAccessFile reader = null;
        String load = null;

        try {
            reader = new RandomAccessFile("/proc/stat", "r");
            load = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return load;
    }

    /**
     * Compute and return the total CPU usage, in percent.
     *
     * @param start
     *            first content of /proc/stat. Not null.
     * @param end
     *            second content of /proc/stat. Not null.
     * @return 12.7 for a CPU usage of 12.7% or -1 if the value is not
     *         available.
     * @see {@link #readSystemStat()}
     */
    private float getSystemCpuUsage(String start, String end) {
        String[] stat = start.split("\\s");
        long idle1 = getSystemIdleTime(stat);
        long up1 = getSystemUptime(stat);

        stat = end.split("\\s");
        long idle2 = getSystemIdleTime(stat);
        long up2 = getSystemUptime(stat);

        // don't know how it is possible but we should care about zero and
        // negative values.
        float cpu = -1f;
        if (idle1 >= 0 && up1 >= 0 && idle2 >= 0 && up2 >= 0) {
            if ((up2 + idle2) > (up1 + idle1) && up2 >= up1) {
                cpu = (up2 - up1) / (float) ((up2 + idle2) - (up1 + idle1));
                cpu *= 100.0f;
            }
        }

        return cpu;
    }

    /**
     * Return the sum of uptimes read from /proc/stat.
     *
     * @param stat
     *            see {@link #readSystemStat()}
     */
    private long getSystemUptime(String[] stat) {
        /*
         * (from man/5/proc) /proc/stat kernel/system statistics. Varies with
         * architecture. Common entries include: cpu 3357 0 4313 1362393
         *
         * The amount of time, measured in units of USER_HZ (1/100ths of a
         * second on most architectures, use sysconf(_SC_CLK_TCK) to obtain the
         * right value), that the system spent in user mode, user mode with low
         * priority (nice), system mode, and the idle task, respectively. The
         * last value should be USER_HZ times the second entry in the uptime
         * pseudo-file.
         *
         * In Linux 2.6 this line includes three additional columns: iowait -
         * time waiting for I/O to complete (since 2.5.41); irq - time servicing
         * interrupts (since 2.6.0-test4); softirq - time servicing softirqs
         * (since 2.6.0-test4).
         *
         * Since Linux 2.6.11, there is an eighth column, steal - stolen time,
         * which is the time spent in other operating systems when running in a
         * virtualized environment
         *
         * Since Linux 2.6.24, there is a ninth column, guest, which is the time
         * spent running a virtual CPU for guest operating systems under the
         * control of the Linux kernel.
         */

        // with the following algorithm, we should cope with all versions and
        // probably new ones.
        long l = 0L;

        for (int i = FIRST_SYS_CPU_COLUMN_INDEX; i < stat.length; i++) {
            if (i != IDLE_SYS_CPU_COLUMN_INDEX ) { // bypass any idle mode. There is currently only one.
                try {
                    l += Long.parseLong(stat[i]);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    return -1L;
                }
            }
        }

        return l;
    }

    /**
     * Return the sum of idle times read from /proc/stat.
     *
     * @param stat
     *            see {@link #readSystemStat()}
     */
    private long getSystemIdleTime(String[] stat) {
        try {
            return Long.parseLong(stat[IDLE_SYS_CPU_COLUMN_INDEX]);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return -1L;
    }

    /** Return the first line of /proc/pid/stat or null if failed. */
    private String readProcessStat(int pid) {

        RandomAccessFile reader = null;
        String line = null;

        try {
            reader = new RandomAccessFile("/proc/" + pid + "/stat", "r");
            line = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return line;
    }

    /**
     * Compute and return the CPU usage for a process, in percent.
     *
     * <p>
     * The parameters {@code totalCpuTime} is to be the one for the same period
     * of time delimited by {@code statStart} and {@code statEnd}.
     * </p>
     *
     * @param start
     *            first content of /proc/pid/stat. Not null.
     * @param end
     *            second content of /proc/pid/stat. Not null.
     * @return the CPU use in percent or -1f if the stats are inverted or on
     *         error
     * @param uptime
     *            sum of user and kernel times for the entire system for the
     *            same period of time.
     * @return 12.7 for a cpu usage of 12.7% or -1 if the value is not available
     *         or an error occurred.
     * @see {@link #readProcessStat(int)}
     */
    private float getProcessCpuUsage(String start, String end, long uptime) {

        String[] stat = start.split("\\s");
        long up1 = getProcessUptime(stat);

        stat = end.split("\\s");
        long up2 = getProcessUptime(stat);

        float ret = -1f;
        if (up1 >= 0 && up2 >= up1 && uptime > 0.) {
            ret = 100.f * (up2 - up1) / (float) uptime;
        }

        return ret;
    }

    /**
     * Decode the fields of the file {@code /proc/pid/stat} and return (utime +
     * stime)
     *
     * @param stat
     *            obtained with {@link #readProcessStat(int)}
     */
    private long getProcessUptime(String[] stat) {
        return Long.parseLong(stat[14]) + Long.parseLong(stat[15]);
    }

    /**
     * Decode the fields of the file {@code /proc/pid/stat} and return (cutime +
     * cstime)
     *
     * @param stat
     *            obtained with {@link #readProcessStat(int)}
     */
    private long getProcessIdleTime(String[] stat) {
        return Long.parseLong(stat[16]) + Long.parseLong(stat[17]);
    }

    /**
     * Return the total CPU usage, in percent.
     * <p>
     * The call is blocking for the time specified by elapse.
     * </p>
     *
     * @param elapse
     *            the time in milliseconds between reads.
     * @return 12.7 for a CPU usage of 12.7% or -1 if the value is not
     *         available.
     */
    public float syncGetSystemCpuUsage(long elapse) {

        String stat1 = readSystemStat();
        if (stat1 == null) {
            return -1.f;
        }

        try {
            Thread.sleep(elapse);
        } catch (Exception e) {
        }

        String stat2 = readSystemStat();
        if (stat2 == null) {
            return -1.f;
        }

        return getSystemCpuUsage(stat1, stat2);
    }

    /**
     * Return the CPU usage of a process, in percent.
     * <p>
     * The call is blocking for the time specified by elapse.
     * </p>
     *
     * @param pid
     * @param elapse
     *            the time in milliseconds between reads.
     * @return 6.32 for a CPU usage of 6.32% or -1 if the value is not
     *         available.
     */
    public float syncGetProcessCpuUsage(int pid, long elapse) {

        String pidStat1 = readProcessStat(pid);
        String totalStat1 = readSystemStat();
        if (pidStat1 == null || totalStat1 == null) {
            return -1.f;
        }

        try {
            Thread.sleep(elapse);
        } catch (Exception e) {
            e.printStackTrace();
            return -1.f;
        }

        String pidStat2 = readProcessStat(pid);
        String totalStat2 = readSystemStat();
        if (pidStat2 == null || totalStat2 == null) {
            return -1.f;
        }

        String[] toks = totalStat1.split("\\s");
        long cpu1 = getSystemUptime(toks);

        toks = totalStat2.split("\\s");
        long cpu2 = getSystemUptime(toks);

        return getProcessCpuUsage(pidStat1, pidStat2, cpu2 - cpu1);
    }

}
