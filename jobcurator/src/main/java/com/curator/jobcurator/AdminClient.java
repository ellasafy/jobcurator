package com.curator.jobcurator;

import java.util.Date;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class AdminClient implements Watcher{
	private ZooKeeper zk;
	
	private String hostPort;

	public AdminClient(String hostPort) {
		this.hostPort = hostPort;
	}
	@Override
	public void process(WatchedEvent e) {
		
	}
	
	public void listState() throws Exception  {
		try {
			Stat stat = new Stat();
			byte masterData[] = zk.getData("/path", false, stat);
			Date startDate = new Date(stat.getCtime());
			System.out.println("Master " + new String(masterData) + " since " + startDate);
		}catch (NoNodeException e) {
			System.out.println("no master");
		}
	}
}
