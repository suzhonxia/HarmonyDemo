package com.sun.os.tool;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class Logger {

    private static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00200, "Sun");

    public static void logI(String format, Object... args) {
        System.out.println(String.format(format, args));
//        HiLog.info(label, format, args);
    }

    public static void logD(String format, Object... args) {
        HiLog.debug(label, format, args);
    }

    public static void logE(String format, Object... args) {
        HiLog.error(label, format, args);
    }
}
