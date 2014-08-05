package com.curator.jobcurator.lock;

import org.apache.curator.framework.CuratorFramework;


public interface LockCallback<T> {

	public T process(CuratorFramework client);
}
