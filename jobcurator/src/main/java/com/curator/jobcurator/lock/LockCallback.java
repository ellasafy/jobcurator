package com.curator.jobcurator.lock;

import com.curator.jobcurator.Client;

public interface LockCallback {

	public void process(Client client);
}
