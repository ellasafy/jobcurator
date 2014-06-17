package com.curator.jobcurator.tasks;

import java.util.List;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

public class Task {
	private ZooKeeper zk;
	private String serverId = "192.168.21.133";
	
	Watcher newTaskWatcher = new Watcher() {
		public void process(WatchedEvent e) {
			if (e.getType() == EventType.NodeChildrenChanged) {
				assert new String("assign/worker-"+serverId).equals(e.getPath());
				getTasks();
			}
		}
	};
	
	public void getTasks() {
		zk.getChildren("/assign/worker-"+serverId, newTaskWatcher,taskGetChildrenCallback, null);
	}
	
	ChildrenCallback taskGetChildrenCallback = new ChildrenCallback() {
		public void processResult(int rc, String path, Object ctx, List<String> children) {
			
		}
	};

}
