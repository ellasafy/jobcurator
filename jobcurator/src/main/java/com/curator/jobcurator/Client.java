package com.curator.jobcurator;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements Watcher {
	 private static final Logger LOG = LoggerFactory.getLogger(Client.class);	
	private ZooKeeper zk;
	
	private final static String hostPort = "";
	
	public static Client getInstance() {
	    return new Client(hostPort);
		
	}
	private  Client(String hostPort) {
		try {
			starZK();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void starZK() throws Exception {
		setZk(new ZooKeeper(hostPort,15000, this));
	}
	
	public void closeZK() {
		try {
			zk.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String queueCommand(String command) throws KeeperException, InterruptedException{
		while (true) {
			try {
				String name = getZk().create("/tasks/task-", command.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
				return name;
			} catch (NodeExistsException e) {
				LOG.info("node exits");
				return null;
			} catch (ConnectionLossException e) {
				
			}
		}
	}
	
	@Override
	public void process(WatchedEvent e) {
		System.out.println(e);
	}
	public ZooKeeper getZk() {
		return zk;
	}
	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}

}
