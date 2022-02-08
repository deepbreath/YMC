package com.yc.aop;

public class PerformanceMonitor {
    private static ThreadLocal<MethodPerformance> performanceRec=new ThreadLocal<>();

    public static void begin(String method){
        System.out.println("begin monitor.....");
        MethodPerformance mp=new MethodPerformance(method);
        performanceRec.set(mp);

    }
    public static void end (){
        System.out.println("end monitor...");
        MethodPerformance mp=performanceRec.get();
        mp.printPerformance();
    }

}
