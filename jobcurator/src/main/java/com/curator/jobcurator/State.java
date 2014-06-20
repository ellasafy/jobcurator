package com.curator.jobcurator;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public enum State {
	//init, wait for start
	INIT(0),
    
	RUN(1),
	
	OVER(2),
	
	EXPECTION(3),
	
	FAIL(4),
	
	ERROR(5),
	
	STOP(6),
	
	WAITFORDELETION(7);

	private int status;

	State(int status) {
		this.status = status;
	}

	private static final Map<Integer, State> lookup = new HashMap<Integer, State>();

	static {
		for (State c : EnumSet.allOf(State.class))
			lookup.put(c.status, c);
	}

	public static State get(int status) {
         return lookup.get(status);
	}
	
	public String toString() {
		return this.name() + " :" + this.status;
	}
	

}
