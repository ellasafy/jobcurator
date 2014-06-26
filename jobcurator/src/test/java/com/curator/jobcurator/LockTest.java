package com.curator.jobcurator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.curator.jobcurator.lock.LockCallback;
import com.curator.jobcurator.lock.LockService;

public class LockTest extends Thread{
   static List<String> list = new ArrayList<String>();
	static AtomicInteger  ss = new AtomicInteger();
	static CountDownLatch latch = new CountDownLatch(100);
	public static void main(String[] args) {
		long cur = System.currentTimeMillis();
		for (int i =0; i< 100; i++) {
			new Thread(new LockTest(), i+"id").start();
		}
		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("==============" + ss.get());
	     System.out.println(list.size() + ": " + list) ;
       long now = System.currentTimeMillis();
       System.out.println(cur + ": " + now + ": " + (now -cur));
	}
	public void run() {
//		System.out.println(Thread.currentThread().getName() + " ....start");
//	      LockService lock = new LockService();
//	         lock.doService("localhost", new LockCallback() {
//	        	public void  process(Client client) {
//	        		System.out.println("get u****************************" + Thread.currentThread().getName());
//	        		list.add(Thread.currentThread().getName());
//	        		ss.incrementAndGet();
//	        		latch.countDown();
//	        	}
//	         }
//	         );
	}
}
