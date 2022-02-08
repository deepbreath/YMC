package com.yc.aop;

public class WaiterImpl implements Waiter{
    @Override
    public void greatTo(String name) {
        System.out.println("greet to "+name+"......");
    }

    @Override
    public void serverTo(String name) {
        System.out.println("server to"+name+"......");
    }
}
