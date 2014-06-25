package com.curator.jobcurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	 private List<ZooKeeper> list = new ArrayList<ZooKeeper>();
	 private List<ZooKeeper> zkPool = Collections.synchronizedList(list);
	private final static String hostPort = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
	
	public static Client getInstance() {
	    return new Client(hostPort);
		
	}
	private  Client(String hostPort) {
		try {
			starZK();
		} catch (Exception e) {
			LOG.error("build connection error, " + e);
		}
	}
	
	public void starZK() throws IOException {
		zk = new ZooKeeper(hostPort,15000, this);
		if (zk != null) {
			zkPool.add(zk);
		}
	}
	
	public void closeZK() {
		try {
			System.out.println("close zk............................");
			
			zk.close();
			if (zk == null) {
				zkPool.remove(zk);
				System.out.println("============xxx" + zkPool.size() + zkPool);
			}
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
