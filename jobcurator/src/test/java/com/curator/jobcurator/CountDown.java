package com.curator.jobcurator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDown {
	final CountDownLatch count = new CountDownLatch(1);
	
	public static void main(String[] args) {
		CountDown c = new CountDown();
		try {
			
			c.getSome();
			TimeUnit.SECONDS.sleep(5);
			
	        c.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	public void getSome() throws InterruptedException {
		new Thread(new Runnable() {
		    @Override
			public void run() {
		    	System.out.println("start");
		    	try {
		    		count.await();
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
			
				System.out.println("end"); 
		   }
		}).start();;
	}
	
	public void countDown () {
		System.out.println("count down");
		count.countDown();
	}
}
