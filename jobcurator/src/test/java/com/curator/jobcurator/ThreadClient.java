package com.curator.jobcurator;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class ThreadClient {
	
	@Test
	public void clientTest() {
		String conn = "127.0.0.1:2181";
		int timeout = 5000;
		try {
			ClientCnx.init(conn, 5000, 15000);
//		CuratorFramework client1 = ClientCnx.createWithOptions(conn, new RetryPolicy() {
//			public boolean      allowRetry(int retryCount, long elapsedTimeMs, RetrySleeper sleeper) {
//				return false;
//			}
//		},5000, 15000);
//		client1.start();
//		CuratorFramework client2 = ClientCnx.createWithOptions(conn, new RetryPolicy() {
//			public boolean      allowRetry(int retryCount, long elapsedTimeMs, RetrySleeper sleeper) {
//				return false;
//			}
//		}, 5000, 15000);
//		client2.start();
		
		CuratorFramework client3 = ClientCnx.getClient();
		CuratorFramework client4 = ClientCnx.getClient();
//		client3.start();
//		client4.start();
		client3.create().withMode(CreateMode.EPHEMERAL).forPath("/tmp", "data".getBytes());
		client4.create().withMode(CreateMode.EPHEMERAL).forPath("/tmp2", "data".getBytes());
		
		TimeUnit.SECONDS.sleep(5);
//		
		client3.close();
//		client4.close();
//		
//		CuratorFramework client5 = ClientCnx.getClient();
//		client5.create().withMode(CreateMode.EPHEMERAL).forPath("/tmp23", "data".getBytes());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
