package com.curator.jobcurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker implements Watcher{
	 private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
	 private List<ZooKeeper> list = new ArrayList<ZooKeeper>();
	 private List<ZooKeeper> zkPool = Collections.synchronizedList(list);

	 private ZooKeeper zk;
	 String hostPort;
	 String serverId = "192.168.21.133";
	 
	 public Worker(String hostPort) {
		 this.hostPort = hostPort;
	 }
	 
	 public void startZK() throws IOException {
		 zk = new ZooKeeper(hostPort, 15000, this);
		 if (zk != null) {
			 zkPool.add(zk);
		 }
	 }
	 
	 @Override
	 public void process(WatchedEvent e) {
		 LOG.info(e.toString() + "," + hostPort);
	 }
	 
	 
	 public void register() {
		 zk.create("/workers/worker-" + serverId, "Idle".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
				 createWorkerCallback, null);
	 }
	 
	 /**
	  * deal creation callback
	  */
	 StringCallback createWorkerCallback = new StringCallback() {
		 @Override
		 public void processResult(int rc, String path, Object ctx, String name) {
			 switch(Code.get(rc)) {
			 case CONNECTIONLOSS:
				 register();
				 break;
			 case OK:
				 LOG.info("registered successfully: " + serverId);
				 break;
			 case NODEEXISTS:
				 LOG.info("already regitered: " + serverId);
				 break;
			default:
				LOG.error("something wrong ", KeeperException.create(Code.get(rc), path));
			 }
		 }
	 };
	 
	 StatCallback statusUpdateCallback = new StatCallback() {
		@Override
		 public void processResult(int rc, String path, Object ctx, Stat stat) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				updateStatus(path, (String)ctx);
				return;
			case OK:
				LOG.info("update successfully");
				break;
			default:
				LOG.error("error", KeeperException.create(rc) );
			}
		 }
	 };
	 
	 private String status;
	 
	  synchronized private void updateStatus(String name,String status) {
		 if (status == this.status) {
			 zk.setData("/workers/" + name, status.getBytes(), -1, statusUpdateCallback, status);
		 }
	 }
	  
	 public void setStatus(String status) {
		 this.status = status;
		 updateStatus("path", status);
	 }
	 
	 public static void main(String[] args) throws Exception {
		 Worker w = new Worker("localhost:2181");
		 w.startZK();
		 w.register();
		 Thread.sleep(3000);
	 }
}
