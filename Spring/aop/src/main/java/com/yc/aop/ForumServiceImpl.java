package com.yc.aop;

public class ForumServiceImpl implements ForumService{
    @Override
    public void removeTopic(int topic) {
        try{
            Thread.sleep(20);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeForum(int forum) {
        try{
            Thread.sleep(40);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
