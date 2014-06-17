package com.curator.jobcurator;

import java.util.ArrayList;
import java.util.List;

public class ComTest {
	
	public static void main(String[] args) {
		new ComTest().testListTime();
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
