package com.curator.jobcurator.lock;

import com.curator.jobcurator.Client;

public interface LockCallback<T> {

	public T process(Client client);
}
