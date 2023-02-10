package com.greedy.emp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Test {

	public static void main(String[] args) {

		Properties prop = new Properties();
		
		try {
			prop.storeToXML(new FileOutputStream("src/com/greedy/emp/employee-query.xml"), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
