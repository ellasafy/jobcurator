package com.curator.jobcurator.io;

public class ObjectLock extends Thread{
     private static final Object obj = new Object();
     
     public static void main(String[] args) {
//    	 Thread t1 = new Thread(new ObjectLock(), "t1");
//    	 Thread t2 = new Thread(new ObjectLock(), "t2");
//    	 Thread t3 = new Thread(new ObjectLock(), "t3");
//    	 t1.start();
//    	 t2.start();
//    	 t3.start();
    	 
    	 Integer t1 = new Integer(1);
    	 Integer t2 = new Integer(1);
    	 System.out.println(t1==t2);
     }
     public void run() {
    	 synchronized(obj) {
    		 System.out.println("first " + Thread.currentThread().getName());
    		 System.out.println("second " + Thread.currentThread().getName());
    	 }
     }
}
