package com.yc.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PerformanceHandler implements InvocationHandler {
    private Object target;

    public PerformanceHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PerformanceMonitor.begin(proxy.getClass().getName()+"."+method.getName());
        Object obj=method.invoke(target,args);
        PerformanceMonitor.end();
        return obj;
    }
}
