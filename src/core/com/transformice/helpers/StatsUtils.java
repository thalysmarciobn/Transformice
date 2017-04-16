package com.transformice.helpers;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;

public class StatsUtils {

    private static final MemoryMXBean memMXbean = ManagementFactory.getMemoryMXBean();
    private static final ThreadMXBean threadMXbean = ManagementFactory.getThreadMXBean();

    public static CharSequence getMemUsage() {
        final double maxMem = memMXbean.getHeapMemoryUsage().getMax() / 1024.;
        final double allocatedMem = memMXbean.getHeapMemoryUsage().getCommitted() / 1024.;
        final double usedMem = memMXbean.getHeapMemoryUsage().getUsed() / 1024.;
        final double nonAllocatedMem = maxMem - allocatedMem;
        final double cachedMem = allocatedMem - usedMem;
        final double useableMem = maxMem - usedMem;
        final StringBuilder list = new StringBuilder();
        list.append("  Allowed Memory: .......... ").append((int) maxMem).append(" KB").append("\n\r");
        list.append("  Allocated: .............. ").append((int) allocatedMem).append(" KB (").append(((double) Math.round((allocatedMem / maxMem) * 1000000) / 10000)).append("%)").append("\n\r");
        list.append("  Non-Allocated: .......... ").append((int) nonAllocatedMem).append(" KB (").append((double) Math.round((nonAllocatedMem / maxMem) * 1000000) / 10000).append("%)").append("\n\r");
        list.append("  Allocated Memory: ........ ").append((int) allocatedMem).append(" KB").append("\n");
        list.append("  Used: ................... ").append((int) usedMem).append(" KB (").append((double) Math.round((usedMem / maxMem) * 1000000) / 10000).append("%)").append("\n\r");
        list.append("  Unused (cached): ........ ").append((int) cachedMem).append(" KB (").append((double) Math.round((cachedMem / maxMem) * 1000000) / 10000).append("%)").append("\n\r");
        list.append("  Useable Memory: .......... ").append((int) useableMem).append(" KB (").append((double) Math.round((useableMem / maxMem) * 1000000) / 10000).append("%)");
        return list;
    }

    public static CharSequence getThreadStats() {
        final StringBuilder list = new StringBuilder();
        final int threadCount = threadMXbean.getThreadCount();
        final int daemonCount = threadMXbean.getThreadCount();
        final int nonDaemonCount = threadCount - daemonCount;
        final int peakCount = threadMXbean.getPeakThreadCount();
        final long totalCount = threadMXbean.getTotalStartedThreadCount();
        list.append("  Live: ................... ").append(threadCount).append(" threads").append("\n\r");
        list.append("  Non-Daemon: ............. ").append(nonDaemonCount).append(" threads").append("\n\r");
        list.append("  Daemon: ................. ").append(daemonCount).append(" threads").append("\n\r");
        list.append("  Peak: ................... ").append(peakCount).append(" threads").append("\n\r");
        list.append("  Total Started: .......... ").append(totalCount).append(" threads");
        return list;
    }

    public static CharSequence getGCStats() {
        final StringBuilder list = new StringBuilder();
        for (final GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            list.append("  Garbage Collector (").append(gcBean.getName()).append(")\n\r");
            list.append("  Collection Count: ..... ").append(gcBean.getCollectionCount()).append("\n\r");
            list.append("  Collection Time: ...... ").append(gcBean.getCollectionTime()).append(" ms");
        }
        return list;
    }

    public static String millisecondsToDate(long time) {
        int SECOND = 1000;
        int MINUTE = 60 * SECOND;
        int HOUR = 60 * MINUTE;
        int DAY = 24 * HOUR;
        long ms = time;
        StringBuffer text = new StringBuffer("");
        if (ms > DAY) {
            long _DAY = ms / DAY;
            text.append(DAY).append(_DAY == 1 ? " day " : " days ");
            ms %= DAY;
        }
        if (ms > HOUR) {
            long _HOUR = ms / HOUR;
            text.append(ms / HOUR).append(_HOUR == 1 ? " hour " : " hours ");
            ms %= HOUR;
        }
        if (ms > MINUTE) {
            long _MINUTE = ms / MINUTE;
            text.append(_MINUTE).append(_MINUTE == 1 ? " minute " : " minutes ");
            ms %= MINUTE;
        }
        if (ms > SECOND) {
            long _SECOND = ms / SECOND;
            text.append(ms / SECOND).append(_SECOND == 1 ? " second " : " seconds ");
        }
        return text.toString();
    }
}
