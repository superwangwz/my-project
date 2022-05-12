package com.bishe.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Sm4Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String ALGORITHM_NAME = "SM4";
    public static final String DEFAULT_KEY = "random_seed";

    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(DEFAULT_KEY, DEFAULT_KEY_SIZE);
    }

    public static byte[] generateKey(String seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(seed, DEFAULT_KEY_SIZE);
    }

    public static byte[] generateKey(String seed, int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        if (null != seed && !"".equals(seed)) {
            random.setSeed(seed.getBytes());
        }
        kg.init(keySize, random);
        return kg.generateKey().getEncoded();
    }

    /**
     *
     * @param algorithmName 加密算法
     * @param key SM4加密key
     * @param iv SM4加密iv
     * @param data // 加密数据
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String algorithmName, byte[] key, byte[] iv, byte[] data) throws Exception {
        return sm4core(algorithmName, Cipher.ENCRYPT_MODE, key, iv, data);
    }

    /**
     * @description 解密
     */
    public static byte[] decrypt(String algorithmName, byte[] key, byte[] iv, byte[] data) throws Exception {
        return sm4core(algorithmName, Cipher.DECRYPT_MODE, key, iv, data);
    }

    private static byte[] sm4core(String algorithmName, int type, byte[] key, byte[] iv, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        if (algorithmName.contains("/ECB/")) {
            cipher.init(type, sm4Key);
        } else {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(type, sm4Key, ivParameterSpec);
        }
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {

        byte[] key = Sm4Util.generateKey();

        byte[] iv = "1234567890123456".getBytes(StandardCharsets.UTF_8);

        String text = "wwz";

        String s = "SM4";

        byte[] encrypt = encrypt(s, key, iv, text.getBytes());
        System.out.println("encrypt = " + encrypt);
    }
}
