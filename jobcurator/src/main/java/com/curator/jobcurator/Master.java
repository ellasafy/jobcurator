package com.curator.jobcurator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Master implements Watcher {
	 private static final Logger LOG = LoggerFactory.getLogger(Master.class);	
	 
	 private ZooKeeper zk;
	
	private String hostPort;
	
	private String serverId = "192.168.21.130";
	
	private boolean isLeader = false;
	
	public StringCallback stringCb = new StringCallback() {
			@Override
			 public void processResult(int rc, String path, Object ctx, String name) {
				 switch (Code.get(rc)) {
				 case CONNECTIONLOSS:
					 try {
						 checkMaster();
					 }catch (Exception e) {
						 e.printStackTrace();
					 }
			
					 return;
				 case OK:
					 isLeader = true;
					 System.out.println("ok");
					 break;
			     default:
			    	 isLeader = false;
						 
				 }
				 System.out.println("i am " + (isLeader ? " " : " not ") + "the leader");
			 }
	};
	
	public void createParent(String path, byte[] data) {
		zk.create(path, data, Ids.OPEN_ACL_UNSAFE, 
				CreateMode.PERSISTENT,
				createParentCallback, data);
	}
	
	public StringCallback createParentCallback  = new StringCallback() {
		@Override
		public void processResult(int rc,String path, Object ctx, String name) {
			switch (Code.get(rc)) {
			case CONNECTIONLOSS:
				createParent(path, (byte[]) ctx);
				break;
			case OK:
				System.out.println("parent created");
				break;
			case NODEEXISTS:
				System.out.println("already exits");
				break;
			default:
				System.out.println("wrong" + KeeperException.create(Code.get(rc),path));
			}
		}
	};
	
	public void bootstrap() {
		
	}
	
	public boolean checkMaster() throws  InterruptedException, KeeperException{
		while (true) {
			try {
				Stat stat = new Stat();
				byte data[] = zk.getData("master", false, stat);
				isLeader = new String(data).equals(serverId);
				return true;
			} catch (NoNodeException e) {
				return false;
			} catch (ConnectionLossException e) {
				
			}
		}
	}
	
	Master(String hostPort) {
		this.hostPort = hostPort;
	}
	
	public void startZK() throws IOException {
		zk = new ZooKeeper(hostPort, 15000, this);
	}
	
	public void stopZK() throws  InterruptedException {
		zk.close();
	}
	
	public void runForMaster() throws InterruptedException,KeeperException {
//			zk.create("/master", serverId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zk.create("/master", serverId.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
					stringCb,stringCb);
		
	}
	
	@Override
	public void process(WatchedEvent e) {
		
	}
	
	
	public static void main(String[] args) {
		Master m = new Master("192.168.21.130:2181");
		try {
			m.startZK();
			m.runForMaster();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if (m.isLeader) {
			System.out.println("is leader");
		}else {
			System.out.println("note leader");
		}
		try {
			TimeUnit.SECONDS.sleep(5);
			m.stopZK();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
