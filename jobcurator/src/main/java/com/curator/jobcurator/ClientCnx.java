package com.curator.jobcurator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ClientCnx {
	
	private static String connectionString;
	private  static int connectionTimeoutMs;
	private static int sessionTimeoutMs;
	
	private static CuratorFramework client2;
	
    public static void init(String conn, int connectTimeOutMs, int sessionTimeOutMs) {
    	connectionString = conn;
    	connectionTimeoutMs = connectTimeOutMs;
    	sessionTimeoutMs = sessionTimeOutMs;  
    }
	
	
    public static CuratorFramework  createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs)
    {
        // using the CuratorFrameworkFactory.builder() gives fine grained control
        // over creation options. See the CuratorFrameworkFactory.Builder javadoc
        // details
        return CuratorFrameworkFactory.builder()
            .connectString(connectionString)
            .retryPolicy(retryPolicy)
            .connectionTimeoutMs(connectionTimeoutMs)
            .sessionTimeoutMs(sessionTimeoutMs)
            // etc. etc.
            .build();
    }
    
    public static CuratorFramework newClient() {
    	return createWithOptions(
				connectionString,
				new ExponentialBackoffRetry(1000, 3),
				connectionTimeoutMs,
				sessionTimeoutMs
				);
    }
    
    public static CuratorFramework getClient() {
    	if (client2 == null) {
    		client2 = ClientCnx.createWithOptions(
    				connectionString,
    				new ExponentialBackoffRetry(1000, 3),
    				connectionTimeoutMs,
    				sessionTimeoutMs
    				);
    	}
    	if(client2.getState() != CuratorFrameworkState.STARTED) {
    		long time = System.currentTimeMillis();
    		client2.start();
    		long time2 = System.currentTimeMillis();
    		System.out.println(time2 -time);
    	}
    	return client2;
    }

}
