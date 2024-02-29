package com.byaffe.learningking.controllers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TokenUtils {
	
	private static SecretKeySpec secretKey;
	private static byte[] key;
	
	public static String generateToken() {
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<=20; i++) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public static String generateVerificationCode() {
		final String NUMERIC_STRING = "0123456789";
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<=4; i++) {
			int character = (int)(Math.random()*NUMERIC_STRING.length());
			builder.append(NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	public static String generateEncryptionHint() {
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<=20; i++) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

	public static void setKey(String myKey) 
	{
	    MessageDigest sha = null;
	    try {
	        key = myKey.getBytes("UTF-8");
	        sha = MessageDigest.getInstance("SHA-1");
	        key = sha.digest(key);
	        key = Arrays.copyOf(key, 16); 
	        secretKey = new SecretKeySpec(key, "AES");
	    } 
	    catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } 
	    catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	}

	public static String encrypt(String strToEncrypt, String secret) {
	    try{
	        setKey(secret);
	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	    }catch (Exception e) {
	        System.out.println("Error while encrypting: " + e.toString());
	    }
	    return null;
	}	
	
	public static String decrypt(String strToDecrypt, String secret) {
        try{
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
	
}
