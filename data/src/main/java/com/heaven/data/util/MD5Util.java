package com.heaven.data.util;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;

public class MD5Util {
	
	public MD5Util() {
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin){
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");

			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));

		} catch (Exception exception) { 
		    exception.printStackTrace();
		}
		return resultString;
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	

	public static String base64Encode(String str) {
		String encode = str;
		if(!TextUtils.isEmpty(str)) {
			encode = Base64.encodeToString(str.getBytes(),Base64.DEFAULT);
		}
		return encode;
	}
}
