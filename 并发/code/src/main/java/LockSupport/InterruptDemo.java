package LockSupport;

import java.util.concurrent.locks.LockSupport;

class InterruptThread extends Thread{
    private Object object;

    public  InterruptThread(Object object){
        this.object=object;
    }
    public void run(){
        System.out.println("before Interrupt");
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        Thread thread=(Thread) object;
        thread.interrupt();
        System.out.println("after interrupt");

    }
}

public class InterruptDemo {
    public static void main(String[] args) {
        InterruptThread interruptThread=new InterruptThread(Thread.currentThread());
        interruptThread.start();
        System.out.println("before park");
        LockSupport.park("ParkAndUnparkDemo");
        System.out.println("after park");
    }
}
