package com.anisolutions.BeanLogin.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.anisolutions.BeanLogin.R;
import com.anisolutions.BeanLogin.helpers.Constants;
import com.anisolutions.BeanLogin.helpers.models.PairStorage;
import com.anisolutions.BeanLogin.helpers.models.RSA;
import com.anisolutions.BeanLogin.network.features.login.UserInfoResponse;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import static javax.crypto.Cipher.ENCRYPT_MODE;

public class EncryptionDecryptionUtils {

    private static final String TAG = "EncryptionDecryptionUti";
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static String getSHA256Hash(String data, Context context) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(Constants.ALGORITHMS.SHA_256);
        String text = data;

        // Change this to UTF-16 if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            md.update(text.getBytes(StandardCharsets.UTF_8));
        }
        byte[] digest = md.digest();

        String hex = String.format(Constants.ALGORITHMS.SHA_256_HEX_FORMAT, new BigInteger(1, digest));
        saveAESKEY(hex, context);

        return hex;
    }

    public static void saveAESKEY(String hexSHA256, Context context) {

        try {
            //byte[] finalAESKEY = EncryptionDecryptionUtils.encrypt(Constants.ALGORITHMS.D_IV.getBytes(), Constants.ALGORITHMS.D_KEY.getBytes(), hexSHA256.getBytes());
            //byte[] finalAESKEY = EncryptionDecryptionUtils.getEncryptedPassData(hexSHA256,context);
            String output =  EncryptionDecryptionUtils.getEncryptedPassData(hexSHA256,context);
            PreferenceUtils.getINSTANCE(context).putString(Constants.ALGORITHMS.AES_FINAL_ENCRYPTED_KEY, output);//Arrays.toString(finalAESKEY)

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, Constants.ALGORITHMS.AES);
        Cipher cipher = Cipher.getInstance(Constants.ALGORITHMS.AES_PKCS5_PADDING_ALGO);
        cipher.init(ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(bytes);
    }


    public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, Constants.ALGORITHMS.AES);
        Cipher cipher = Cipher.getInstance(Constants.ALGORITHMS.AES_PKCS5_PADDING_ALGO);//AES/CBC/PKCS5Padding
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);//ivSpec
        return cipher.doFinal(bytes);
    }


    public static byte[] decryptShared(byte[] ivBytes, byte[] keyBytes, byte[] bytes) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, Constants.ALGORITHMS.AES);
        Cipher cipher = Cipher.getInstance(Constants.ALGORITHMS.AES_PKCS5_PADDING_ALGO_SHARED);//AES/CBC/PKCS5Padding
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);//ivSpec
        return cipher.doFinal(bytes);
    }


    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buffer.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance(Constants.ALGORITHMS.SHA_256);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, Constants.ALGORITHMS.AES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static String getDecryptedPassData(String password, Context context) {
        byte[] decryptedKey;
        String passsData = "";
        String userDecryptKey = PreferenceUtils.getINSTANCE(context).getString(Constants.ALGORITHMS.DECRYPTED_USER_KEY);

        Log.d(TAG, "getDecryptedPassData: " + userDecryptKey + " length: " + userDecryptKey.length());
        try {
            byte[] hexArray = EncryptionDecryptionUtils.hexStringToByteArray(password);
            UserInfoResponse userInfoResponse = PreferenceUtils.getINSTANCE(context).getUserData();
            String upToNCharacters = userInfoResponse.getParameterValue().substring(0, Math.min(userInfoResponse.getParameterValue().length(), 16));
            Log.d(TAG, "getDecryptedPassData: iv key original " + userInfoResponse.getParameterValue() + " 16 bit key: " + upToNCharacters);
            decryptedKey = EncryptionDecryptionUtils.decrypt(upToNCharacters.getBytes(StandardCharsets.UTF_8), userDecryptKey.getBytes(StandardCharsets.UTF_8)
                    , hexArray);//byteData
            passsData = new String(decryptedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // }
        return passsData;
    }


    public static String getDecryptedSharedData(String password, Context context, String sharedKey,String ownerId) {
        Log.d("ONLINE","To Decrypt string :- "+password+" "+"Shared Key :- "+sharedKey+" "+"OwnerId :- "+ownerId);
        byte[] decryptedKey;
        String passsData = "";
        String userDecryptKey = "";

        Log.d(TAG, "getDecryptedPassData: " + userDecryptKey + " length: " + userDecryptKey.length());
        try {
            byte[] hexArray = EncryptionDecryptionUtils.hexStringToByteArray(password);
            UserInfoResponse userInfoResponse = PreferenceUtils.getINSTANCE(context).getUserData();
            userDecryptKey = decryptUserKey(sharedKey, context, userInfoResponse.getParameterValue());
            Log.d(TAG, "getDecryptedSharedData: shared key " + userDecryptKey);
            String upToNCharacters = ownerId.substring(0, Math.min(userInfoResponse.getParameterValue().length(), 16));
            //String upToNCharacters = userInfoResponse.getParameterValue().substring(0, Math.min(userInfoResponse.getParameterValue().length(), 16));
            decryptedKey = EncryptionDecryptionUtils.decryptShared(upToNCharacters.getBytes(StandardCharsets.UTF_8), userDecryptKey.getBytes(StandardCharsets.UTF_8)
                    , hexArray);//byteData
            return new String(decryptedKey);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ONLINE","Decryption Exception :- "+e.toString());
        }
        return passsData;
    }


    public static String getEncryptedSharedData(String password, Context context, String sharedKey,String ownerId) {
        byte[] encryptedKey;
        String passsData = "";
        String userDecryptKey = "";

        Log.d("ONLINE","To Encrypt string :- "+password+" "+"Shared Key :- "+sharedKey+" "+"OwnerId :- "+ownerId);
        try {
            UserInfoResponse userInfoResponse = PreferenceUtils.getINSTANCE(context).getUserData();
            userDecryptKey = decryptUserKey(sharedKey, context, userInfoResponse.getParameterValue());
            Log.d("USERKEY","userDecryptKey: "+userDecryptKey);
            //// TODO: 11/06/18 changed 
            String upToNCharacters = ownerId.substring(0, Math.min(ownerId.length(), 16));
            //String upToNCharacters = userInfoResponse.getParameterValue().substring(0, Math.min(userInfoResponse.getParameterValue().length(), 16));
            encryptedKey = EncryptionDecryptionUtils.encrypt(upToNCharacters.getBytes(StandardCharsets.UTF_8), userDecryptKey.getBytes(StandardCharsets.UTF_8), password.getBytes());
            passsData = EncryptionDecryptionUtils.byteArrayToHexString(encryptedKey);//new String(finalAESKEY);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ONLINE","Encryption Exception :-"+e.toString());
        }
        // }
        return passsData;
    }


    public static String getEncryptedPassData(String password, Context context) {
        byte[] encryptedKey;
        String passsData = "";
        String userDecryptKey = PreferenceUtils.getINSTANCE(context).getString(Constants.ALGORITHMS.DECRYPTED_USER_KEY);
        //Log.d("ENCRYPT", "----------------- Inside the getEncryptedData via userKey ---------------" );
        //Log.d("ENCRYPT","userDecryptKey: "+userDecryptKey+"\n"+ "Text: "+password);
        UserInfoResponse userInfoResponse = PreferenceUtils.getINSTANCE(context).getUserData();
        try {
            String upToNCharacters = userInfoResponse.getParameterValue().substring(0, Math.min(userInfoResponse.getParameterValue().length(), 16));
            encryptedKey = EncryptionDecryptionUtils.encrypt(upToNCharacters.getBytes(StandardCharsets.UTF_8), userDecryptKey.getBytes(StandardCharsets.UTF_8), password.getBytes());
           // Log.d("ENCRYPT","EncryptedData in Bytes: "+encryptedKey);
            passsData = EncryptionDecryptionUtils.byteArrayToHexString(encryptedKey);//new String(finalAESKEY);
           // Log.d("ENCRYPT","EncryptedData in hexString: "+passsData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passsData;
    }


    public static String encryptPass(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decryptPass(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }


    public static SecretKeySpec generateKey(String aliasText) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(Constants.ALGORITHMS.SHA_256);
        byte[] bytes = aliasText.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, Constants.ALGORITHMS.AES);

        return secretKeySpec;
    }

    public static String encryptCode(String message, String aliasText) throws Exception {
        SecretKeySpec secretKey = generateKey(aliasText);
        Cipher cipher = Cipher.getInstance(Constants.ALGORITHMS.AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        String encryptedText = Base64.encodeToString(cipherText, Base64.DEFAULT);
        return encryptedText;
    }

    public static String decryptCode(String encryptedText, String aliasText) throws Exception {
        SecretKeySpec secretKey = generateKey(aliasText);
        Cipher cipher = Cipher.getInstance(Constants.ALGORITHMS.AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptValue = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] decodeValue = cipher.doFinal(decryptValue);
        String decryptText = new String(decodeValue);
        return decryptText;
    }


    private static String decryptUserKey(String key, Context context, String userId) {
        Log.d(TAG, "decryptUserKey: shared key " + key);
        String decryption = "";
        try {
            byte[] decryptedKey = RSA.decryptBASE64(key);
            if (PreferenceUtils.getINSTANCE(context).getPairData(userId) != null) {
                PairStorage pairStorage = PreferenceUtils.getINSTANCE(context).getPairData(userId);
                PrivateKey privateKey = RSA.getPrivateKeyData(pairStorage.getPriKeyData());
                String decryptionData = RSA.RSADecrypt(decryptedKey, privateKey);
                Log.d(TAG, "decryptUserKey: " + decryptionData);
                return decryptionData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryption;
    }

}
