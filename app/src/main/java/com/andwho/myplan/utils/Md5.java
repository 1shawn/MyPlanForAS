package com.andwho.myplan.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 对外提供getMD5(String)方法
 * 
 * @author randyjia
 * 
 */
public class Md5 {

	public static String getMD5(String string) {

		byte[] hash;  
		  
	    try {  
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));  
	    } catch (NoSuchAlgorithmException e) {  
	        e.printStackTrace();  
	        return String.valueOf(string.hashCode());  
	    } catch (UnsupportedEncodingException e) {  
	        e.printStackTrace();  
	        return String.valueOf(string.hashCode());  
	    }  
	  
	    StringBuilder hex = new StringBuilder(hash.length * 2);  
	    for (byte b : hash) {  
	        if ((b & 0xFF) < 0x10)  
	            hex.append("0");  
	        hex.append(Integer.toHexString(b & 0xFF));  
	    }  
	  
	    return hex.toString();  
	}

	private static String getString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}
		return sb.toString();
	}
}
