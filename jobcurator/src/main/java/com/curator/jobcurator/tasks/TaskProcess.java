package com.curator.jobcurator.tasks;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

import com.curator.jobcurator.Client;
import com.curator.jobcurator.Constant.JkPath;

public class TaskProcess implements Runnable{
	private Client client;
	private String host;
	private final String hostPath = JkPath.TASK + host;
	private AtomicBoolean next = new AtomicBoolean(false);
	
	public TaskProcess() throws Exception {
		this.host = InetAddress.getLocalHost().getHostAddress();
		client = createClient();
	}
	private synchronized boolean isClientClosed() {
		if (client == null) {
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized Client createClient() {
		if (isClientClosed()) {
			client = Client.getInstance();
		} 
		return client;
	}
	
	public void run() {
		while (true) {
			if (next.get()) {
				try {
					registerWatcher();
					next.compareAndSet(true, false);
				}catch (KeeperException e) {
					
				}catch (Exception e) {
					
				}
			}
		
		}
	
		
	}
	
	public void registerWatcher() throws KeeperException, InterruptedException {
		
		client.getZk().exists(hostPath, new Watcher() {
			@Override
			public void process(WatchedEvent e) {
				next.compareAndSet(false, true);
				if (e.getType() == EventType.NodeChildrenChanged) {
					
				}
			}
		});
	}

	
}
