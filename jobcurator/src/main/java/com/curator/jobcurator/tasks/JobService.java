package com.curator.jobcurator.tasks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.codehaus.jackson.map.ObjectMapper;

import com.curator.jobcurator.Constant.JkPath;
import com.curator.jobcurator.Message;
import com.curator.jobcurator.SerializableUtil;
import com.curator.jobcurator.job.Job;
import com.curator.jobcurator.lock.LockCallback;
import com.curator.jobcurator.lock.LockService;

public interface JobService {
	
	
}
