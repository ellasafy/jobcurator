package com.curator.jobcurator.tasks;

import java.util.HashMap;
import java.util.Map;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;

import com.curator.jobcurator.Client;
import com.curator.jobcurator.Constant.JkPath;
import com.curator.jobcurator.Message;
import com.curator.jobcurator.SerializableUtil;
import com.curator.jobcurator.job.Job;
import com.curator.jobcurator.lock.LockCallback;
import com.curator.jobcurator.lock.LockService;

public class JobService implements Watcher{
	
	public String addJob(final byte[] dataBuffer) {
		//build jk path
		Job job = (Job)SerializableUtil.readObject(dataBuffer);
		if (job == null) {
			return Message.JOBERROR;
		}
		
		final String jobName = job.getName();
		final String zkJobName = JkPath.JOB + jobName;
		final String zkTaskName = JkPath.TASK + jobName;
		
		
		final Map<String,String> msg = new HashMap<String, String>();
		new LockService().doService(jobName, new LockCallback() {
			private Client client;
			@Override
			public void process(Client cl) {
				if (client == null) {
					client = cl;
				}
				createJob(zkJobName, jobName);
			}
			
			private void setHost()
			
			private void createJob(final String zkJobName, final String jobName) {
				client.getZk().create(zkJobName, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createJobCb, jobName);
			}
			
			StringCallback createJobCb = new StringCallback() {
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
				    switch (Code.get(rc)) {
				    case OK:
				    	addTask(zkTaskName, jobName);
				    	break;
				    case NODEEXISTS:
				    	break;
				    default :
				    	createJob(path, (String)ctx);
				    	break;
				    }
				}
			};
		   
			//add job to /jk/slaves
			private void  addTask(final String zkTaskName, final String jobName) {
				client.getZk().create(zkTaskName, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, taskCallback, jobName);
			}
			
			StringCallback taskCallback = new StringCallback() {
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
				    switch (Code.get(rc)) {
				    case OK:
				    	break;
				    case NODEEXISTS:
				    	break;
				    default:
				    	addTask(zkTaskName, (String)ctx);
				    	break;
				    }
				}
			};
			
		});
		
		
		return null;
	}
	
	
	private String getRunHost() {
		
		return null;
	}
	

	
	
	@Override
	public void process(WatchedEvent event) {
		
	}

}
