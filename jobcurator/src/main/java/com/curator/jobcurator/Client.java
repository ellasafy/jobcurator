package com.curator.jobcurator;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class Client implements Watcher {
	private ZooKeeper zk;
	
	String hostPort;
	
	public Client(String hostPort) {
		this.hostPort = hostPort;
	}
	
	public void starZK() throws Exception {
		zk = new ZooKeeper(hostPort,15000, this);
	}
	public String queueCommand(String command) throws KeeperException {
		while (true) {
			try {
				String name = zk.create("/tasks/task-", command.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
				return name;
				break;
			} catch (NodeExistsException e) {
				throw new Exception(name + " already running");
			}
		}
	}

}
