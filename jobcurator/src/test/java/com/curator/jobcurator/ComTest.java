package com.curator.jobcurator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ComTest {
	static final AtomicReference<Coe> coe = new AtomicReference<Coe>();
	public static void main(String[] args) {
		coe.set(Coe.IN);
		if (coe.compareAndSet(Coe.IN, Coe.OUT)) {
			System.out.println(coe.get());
		}
		
		
		
	}
	static enum Coe {
		IN(1),
		OUT(2);
		private int id;
		private Coe(int id) {
			this.id = id;
		}
		
		public String toString() {
			return this.name() + this.id;
		}
		
	}
	public void testListTime() {
		List<String> list = new ArrayList<String>();
		List<String> cur = new ArrayList<String>();
		for (int i =0 ;i < 100; i++) {
			list.add(i+"id");
		}
		for (int i=0; i < 100; i++) {
			cur.add(i + "idss");
		}
		cur.add(88+"id");
		
		System.out.println(System.currentTimeMillis());
		for (String id : cur) {
			if (list.contains(id)) {
				System.out.println(System.currentTimeMillis());
			}
		}
	}

}
