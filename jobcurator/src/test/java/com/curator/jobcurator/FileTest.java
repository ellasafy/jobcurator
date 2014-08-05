package com.curator.jobcurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class FileTest {
	
	public static void main(String[] args) {
		File file = new File("/home/cstor/tmp/test2");
		File[] files = file.listFiles();
		
		for (File f : files) {
			System.out.println(f.getAbsolutePath());
			
			System.out.println(f.getName());
			
		}
	}

}
