package com.curator.jobcurator.tasks;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

import com.curator.jobcurator.Client;
import com.curator.jobcurator.Constant.JkPath;
import com.curator.jobcurator.lock.LockCallback;
import com.curator.jobcurator.lock.LockService;

public class JobService implements Watcher{
	
	public void addJob(final String jobName) {
		final String zkJobName = JkPath.JOB + jobName;
		new LockService().doService(jobName, new LockCallback() {
			@Override
			public void process(Client client) {
				createJob(client, zkJobName, jobName);
			}
			
		});
		
	}
	
	private void createJob(Client client, final String zkJobName, final String jobName) {
		client.getZk().create(zkJobName, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createCb, jobName);
	}
	
	StringCallback createCb = new StringCallback() {
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
		    switch (Code.get(rc)) {
		    case CONNECTIONLOSS:
//		    	createJob()
		    }
		}
	};
	
	
	@Override
	public void process(WatchedEvent event) {
		
	}

}
