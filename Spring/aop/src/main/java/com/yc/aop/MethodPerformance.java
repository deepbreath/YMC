package com.yc.aop;

public class MethodPerformance {
    private long begin;
    private long end;
    private String serviceMethod;

    public MethodPerformance(String serviceMethod) {
        this.begin = System.currentTimeMillis();
        this.serviceMethod=serviceMethod;
    }

    public void printPerformance(){
        this.end=System.currentTimeMillis()-begin;
        System.out.println(serviceMethod+"花费"+end+"毫秒");
    }
}
