package com.basicsetup.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {

	private static final String MD5 = "md5";

	// Hashing algorithm to generate hash for signed url
	public static String md5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(MD5);
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
