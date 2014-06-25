package com.curator.jobcurator.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.curator.jobcurator.Client;

public class LockService<T> {
	private static final Logger LOG = LoggerFactory.getLogger(LockService.class);	
	private CountDownLatch lockWait = new CountDownLatch(0);
	private ExecutorService ex = Executors.newFixedThreadPool(1);
	private Client client;
	
	public  T doService(final String name, LockCallback<T> cb){
		Lock lock = new ReentrantLock();
		lock.lock();
		T ret =null;
		try {
			LOG.info("start.....................");
			client = Client.getInstance();
			lockWait = new CountDownLatch(1);
			
			ex.execute(new Runnable() {
				public void run() {
					try {
						doLock("/lock/"+name);
						System.out.println("thead ok");
					} catch (Exception e) {
						LOG.error("get an error from thread" + Thread.currentThread().getName());
					}
					
				}
			});
			
			try {
				//wait for lock
				lockWait.await();
				//after lock service, do process
				if (LOG.isDebugEnabled()) {
					LOG.debug("got lock " + name + " start to process");
				}
				ret = cb.process(client);
			} catch (InterruptedException e) {
				LOG.error(e + "");
			}
			return ret;
		}finally {
			client.closeZK();
			lock.unlock();
		}
	}
	
	protected void doLock(final String path) {
		client.getZk().create(path, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				lockCallBack, path);
				
	}
	
	StringCallback lockCallBack = new StringCallback() {
         public void processResult(int rc, String path, Object ctx, String name) {
        	 switch(Code.get(rc)) {
        	 case CONNECTIONLOSS:
        		 //connection loss, just try
        		 doLock((String)ctx);
        		 break;
        	 case OK:
        		 lockWait.countDown();
        		 break;
        	 case  NODEEXISTS:
        		 //public void exists(final String path, Watcher watcher,
//                 StatCallback cb, Object ctx)
        		 doLock((String)ctx);
        		 break;
        	default:
        		LOG.error("lock callback " + KeeperException.create(rc).getMessage());
        		doLock((String)ctx);
        		break;
        	 }
		}
    };
    
    protected void checkLockExists(final String path, Object ctx) {
    	client.getZk().exists(path, lockWatcher, lockStatCallback,ctx);
    }
    //public void processResult(int rc, String path, Object ctx, Stat stat);
    StatCallback lockStatCallback = new StatCallback() {
    	@Override
    	public void processResult(int rc, String path, Object ctx, Stat stat) {
    		if (Code.get(rc) == Code.NONODE) {
    			doLock((String)ctx);
    		} else {
    		   LOG.error(path + " is locked, waiting...");	
    		}
    	}
    };
    
    //wait, until release lock
    private Watcher lockWatcher = new Watcher() {
    	@Override
    	public void process(WatchedEvent e) {
    		System.out.println("watcher ..................." + e.getPath());
    		if (e.getType() == EventType.NodeDeleted) {
    			doLock(e.getPath());
    		}
    	}
    };
 
}
