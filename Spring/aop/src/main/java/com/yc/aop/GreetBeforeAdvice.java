package com.yc.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class GreetBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        String clientName= (String) args[0];
        System.out.println("Hello World Mr."+clientName);
    }
}
