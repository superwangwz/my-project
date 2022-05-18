package com.bishe.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.UUID;

import static cn.hutool.crypto.symmetric.SM4.ALGORITHM_NAME;

public class Sm4Util {

    public static final int KEY_SIZE = 128;

    public static byte[] keyData = new byte[0];

    public static void init(){
        if (keyData.length != 0){
            return;
        }
        //生成Key
        byte[] bytes = new byte[0];
        try {
            bytes = generateKey(KEY_SIZE);
            String key = ByteUtils.toHexString(bytes);
            keyData = ByteUtils.fromHexString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }


    static{
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
            //No such provider: BC
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    //生成 Cipher
    public static Cipher generateCipher(int mode,byte[] keyData) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance("SM4/ECB/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(keyData, "SM4");
        cipher.init(mode, sm4Key);
        return cipher;
    }


    /**
     * 加密文件
     */
    public static byte[] encryptFile(byte[] data){
        init();
        CipherInputStream cipherInputStream = null;
        //加密文件
        try {
            Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE,keyData);
             cipherInputStream = new CipherInputStream(IoUtil.toStream(data), cipher);

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e)  {
            e.printStackTrace();
        }
        return IoUtil.readBytes(cipherInputStream);
    }


    /**
     * 解密文件
     */
    public static byte[] decryptFile(byte[] data) {
        init();
        ByteArrayInputStream byteArrayInputStream =null;
        OutputStream out = null;
        CipherOutputStream cipherOutputStream=null;
        String dirPath = ProjectUtils.getDirPath();
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[1024];
        String targetPath =  dirPath+"\\"+UUID.randomUUID().toString();
        try {
            byteArrayInputStream = IoUtil.toStream(data);

            Cipher cipher = generateCipher(Cipher.DECRYPT_MODE,keyData);

            out = new FileOutputStream(targetPath);
            cipherOutputStream = new CipherOutputStream(out, cipher);
            IoUtil.copy(byteArrayInputStream, cipherOutputStream);
        } catch (IOException | NoSuchProviderException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(cipherOutputStream);
            IoUtil.close(out);
            IoUtil.close(byteArrayInputStream);
        }
        try {
             fileInputStream = new FileInputStream(targetPath);
             bytes = IoUtil.readBytes(fileInputStream);
            fileInputStream.close();
            FileUtil.del(targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        //原始文件
        String sp = "C:\\Users\\zfj\\Desktop\\新建文本文档.txt";

        //加密文件
        byte[] bytes1 = encryptFile(IoUtil.readBytes(new FileInputStream(sp)));
        System.out.println("new String(bytes1) = " + new String(bytes1));
        //解密文件
        byte[] bytes2 = decryptFile(bytes1);
        System.out.println("new String(bytes2) = " + new String(bytes2));
    }

}
