package com.curator.jobcurator;

public class StringUtil {

	public static void main(String[] args) {
		System.out.println(StringScramble("rkqodlw"	, "world"));
	}
	
	public static boolean StringScramble(String str1, String str2) {
		int len = str2.length();
		for (int i=0;i < len;i++) {
			if (str1.indexOf(str2.charAt(i)) == -1){
				return false;
			}
		}
		return true;
	}
	public static int division(int num1, int num2) {
		
		int index = num1/2 + 1;
		if (num1 > num2){
			index = num2/2 + 1;
		}
		
		int max = 1;
		for (int i = 2; i <= index; i++) {
			if (num1 %i == 0 && num2 %i ==0) {
				max = i;
			}
		}
		
		return max;
		
	}
}
