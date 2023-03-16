package com.jjjhs.reggie.common;


public class BaseContext{
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setThreadId(Long id) { threadLocal.set(id); }

    public static Long getThreadId() { return threadLocal.get(); }
}
