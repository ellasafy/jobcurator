package com.curator.jobcurator;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Master implements Watcher {
	
	private ZooKeeper zk;
	
	@Override
	public void process(WatchedEvent e) {
		
	}
	
	

}
