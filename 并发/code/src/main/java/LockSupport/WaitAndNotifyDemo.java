package LockSupport;

class MyThread extends Thread{


    public void run() {
       synchronized (this){
           System.out.println("before notify");
           notify();
           System.out.println("after notify");
       }
    }
}
public class WaitAndNotifyDemo {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread=new MyThread();
//        myThread.start();
//        Thread.sleep(3000);
        synchronized (myThread){
            try{
                myThread.start();
                Thread.sleep(3000);
                System.out.println("before wait");
                myThread.wait();
                System.out.println("after wait");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
