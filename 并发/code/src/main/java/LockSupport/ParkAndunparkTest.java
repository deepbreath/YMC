package LockSupport;

import java.util.concurrent.locks.LockSupport;

class MyThreadPark extends Thread{
    private Object object;


    public MyThreadPark(Object object){
        this.object=object;
    }

    public void run(){
        System.out.println("Before unpark");
        LockSupport.unpark((Thread) object);
        System.out.println("After unpark");
    }
}
public class ParkAndunparkTest {
    public static void main(String[] args) {
        MyThreadPark myThreadPark=new MyThreadPark(Thread.currentThread());
        myThreadPark.start();
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("before park");
        LockSupport.park("ParkAndUnparkDemo");
        System.out.println("after Park");
    }
}
