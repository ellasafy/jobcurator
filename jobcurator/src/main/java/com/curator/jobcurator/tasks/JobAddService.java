package com.curator.jobcurator.tasks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooDefs.Ids;
import org.codehaus.jackson.map.ObjectMapper;

import com.curator.jobcurator.Message;
import com.curator.jobcurator.SerializableUtil;
import com.curator.jobcurator.Constant.JkPath;
import com.curator.jobcurator.job.Job;
import com.curator.jobcurator.lock.LockCallback;
import com.curator.jobcurator.lock.LockService;

public class JobAddService {
	public String addJob(final byte[] dataBuffer)  {
		//build jk path
		final Job job = (Job)SerializableUtil.readObject(dataBuffer);
		Map<String, Object> mp = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
	
        //job对象无法解析，返回
		if (job == null) {
			mp.put("returnCode", Message.JOBERROR);
			try {
				return mapper.writeValueAsString(mp);
			} catch (Exception e) {
				return null;
			}
		}
		
		final String jobName = job.getName();
		final String zkJobName = JkPath.JOB + jobName;
		final String zkTaskName = JkPath.TASK + jobName;
		
		LockService<Map<String, Object>> service = new LockService<Map<String, Object>>(jobName);
		Map<String, Object> ret = service.doWork(new LockCallback<Map<String, Object>>() {
			private CuratorFramework client;
			
			final Map<String, Object> rsl = new HashMap<String, Object>();
			byte[] jobData;
			@Override
			public Map<String, Object> process(CuratorFramework cl) {
				if (client == null) {
					client = cl;
				}
	
			    String host = getHostProc();
			    if (host == null) {
			    	rsl.put("returnCode", Message.JOBERROR);
			    	return rsl;
			    }
			    job.setProcHostName(host);
				jobData = SerializableUtil.writeObject(job);
				
				//开始添加任务
				try {
					createJob(zkJobName, jobName);
				}catch (Exception e) {
					rsl.put("returnCode", Message.JOBERROR);
				}
				
				return rsl;
			}
			
	
			private void createJob(final String zkJobName, final String jobName) throws Exception {
				client.getZookeeperClient().getZooKeeper().create(zkJobName, jobData, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createJobCb, jobName);
			}
			
			StringCallback createJobCb = new StringCallback() {
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
				    switch (Code.get(rc)) {
				    case OK:
				    	try {
				    	   	addTask(zkTaskName, jobName);
				    	}catch(Exception e) {
				    		
				    	};
				 
				    	break;
				    case NODEEXISTS:
				    	rsl.put("returnCode", Message.JOBEXISTS);
				    	break;
				    default :
				    	try {
				    		createJob(path, (String)ctx);
				    	}catch (Exception e) {
				    		
				    	}
				        ;
				    	break;
				    }
				}
			};
		   
			//add job to /jk/slaves
			private void  addTask(final String zkTaskName, final String jobName) throws Exception{
				client.getZookeeperClient().getZooKeeper().create(zkTaskName, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, taskCallback, jobName);
			}
			
			StringCallback taskCallback = new StringCallback() {
				@Override
				public void processResult(int rc, String path, Object ctx, String name) {
				    switch (Code.get(rc)) {
				    case OK:
				    	rsl.put("returnCode", Message.JOBADDSUCCESS);
				    	break;
				    case NODEEXISTS:
				    	break;
				    default:
				    	try {
				    		addTask(zkTaskName, (String)ctx);
				    	}catch (Exception e) {
				    		
				    	}
				    	;
				    	break;
				    }
				}
			};
			
			
			//获取job执行的host
			private String getHostProc() {
				List<String> hosts = null;
				try {
//					hosts = client.getZk().getChildren(JkPath.MASTER, null);
					hosts = client.getChildren().forPath(JkPath.MASTER);
				} catch (KeeperException e) {
					if (e.code() == Code.CONNECTIONLOSS) {
							
					}
						
				} catch (Exception e) {
						
				}
				if (hosts != null && !hosts.isEmpty()) {
					Collections.shuffle(hosts);
					return hosts.get(0);
				} else {
					return null;
				}
					
			}
			
		});
		
		String result = null;
		try {
			result = mapper.writeValueAsString(ret);
		} catch (Exception e) {
			
		}
		return result;
	}

}
