package com.curator.jobcurator.lock;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.curator.jobcurator.ClientCnx;
import com.curator.jobcurator.Constant.JkPath;


public class LockService<T> {
	private static final Logger LOG = LoggerFactory.getLogger(LockService.class);	
	private static final int DEFAULT_REPEAT_TIME = 5;
	private static final int DEFAULT_LOCK_TIME = 100;
//	private Client client;
	private final InterProcessMutex lock;
	private final String lockPath;
	private final CuratorFramework client;
	
	
	public LockService(CuratorFramework client, String lockPath) {
		this.client = client;
		this.lockPath = JkPath.LOCK + lockPath;
		this.lock = new InterProcessMutex(client, lockPath);
		
	}
	//default client
	public LockService(String lockPath) {
		
		this.client = ClientCnx.newClient();
		this.lockPath = JkPath.LOCK + lockPath;
		this.lock = new InterProcessMutex(client, lockPath);
		
	}
	
	
    public  T   doWork( LockCallback<T> cb) 
    {
    	T t = null;
    	try {
    		t = doWork(DEFAULT_LOCK_TIME, TimeUnit.MILLISECONDS, cb);
    	} catch(Exception e) {
    		
    	}
    	return t;
    }
	
    public  T   doWork(long time, TimeUnit unit, LockCallback<T> cb) throws Exception
    {
    	for (int i = 0; i < DEFAULT_REPEAT_TIME; i++) {
    	    if (lock.acquire(time, unit)) {
    	    	 try
    	         {
    	         	LOG.info(lockPath + " has the lock");
    	             return cb.process(client);
    	         }
    	         finally
    	         {
    	        	 LOG.info(lockPath + " releasing the lock");
    	             lock.release(); // always release the lock in a finally block
    	         }
    	    	
    	    }
    	}
        
        throw new IllegalStateException( " could not acquire the lock " + lockPath);
    }

 
}
