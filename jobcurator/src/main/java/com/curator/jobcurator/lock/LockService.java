package com.curator.jobcurator.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public class LockService {
	private static final Logger LOG = LoggerFactory.getLogger(LockService.class);	
	private CountDownLatch lock = new CountDownLatch(0);
	private ExecutorService ex = Executors.newFixedThreadPool(1);
	private Client client;
	
	public void doService(final String name, LockCallback cb){
		synchronized(this) {
			client = Client.getInstance();
			lock = new CountDownLatch(1);
			
			ex.execute(new Runnable() {
				public void run() {
					
				}
			});
			
			try {
				//wait for lock
				lock.await();
				//after lock service, do process
				if (LOG.isDebugEnabled()) {
					LOG.debug("got lock " + name + " start to process");
				}
				cb.process();
			} catch (InterruptedException e) {
				
			}
			client.closeZK();
		}
	}
	
	public void doLock(final String name) {
		
		client.getZk().create("/lock/"+name, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				lockCallBack, name);
				
	}
	
	StringCallback lockCallBack = new StringCallback() {
         public void processResult(int rc, String path, Object ctx, String name) {
        	 switch(Code.get(rc)) {
        	 case CONNECTIONLOSS:
        		 //connection loss, just try
        		 doLock((String)ctx);
        		 break;
        	 case OK:
        		 lock.countDown();
        		 break;
        	 case  NODEEXISTS:
        		 //public void exists(final String path, Watcher watcher,
//                 StatCallback cb, Object ctx)
        		 checkLockExists(path, ctx);
        		 break;
        	default:
        		LOG.error(KeeperException.create(rc).getMessage());
        		doLock((String)ctx);
        		break;
        	 }
		}
    };
    
    private void checkLockExists(final String path, Object ctx) {
    	client.getZk().exists(path, lockWatcher, lockStatCallback,ctx);
    }
    //public void processResult(int rc, String path, Object ctx, Stat stat);
    StatCallback lockStatCallback = new StatCallback() {
    	@Override
    	public void processResult(int rc, String path, Object ctx, Stat stat) {
    		if (Code.get(rc) == Code.NONODE) {
    			doLock((String)ctx);
    		} else {
    			
    		}
    	}
    };
    
    //wait, until release lock
    private Watcher lockWatcher = new Watcher() {
    	@Override
    	public void process(WatchedEvent e) {
    		if (e.getType() == EventType.NodeDeleted) {
    			
    		}
    	}
    };
 
}
