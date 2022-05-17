package com.bishe.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;


import static cn.hutool.crypto.symmetric.SM4.ALGORITHM_NAME;

public class Sm4Util {

    public static final int KEY_SIZE = 128;

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
     * @param keyData key
     * @param sourcePath  待加密的文件路径
     * @param targetPath  加密的文件路径
     */
    public static void encryptFile(byte[] keyData,String sourcePath,String targetPath){
        //加密文件
        try {
            Cipher cipher = generateCipher(Cipher.ENCRYPT_MODE,keyData);
            CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(sourcePath), cipher);
            FileUtil.writeFromStream(cipherInputStream, targetPath);
            IoUtil.close(cipherInputStream);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解密文件
     * @param sourcePath 待解密的文件路径
     * @param targetPath 解密后的文件路径
     */
    public static void decryptFile(byte[] keyData,String sourcePath, String targetPath) {
        FileInputStream in =null;
        ByteArrayInputStream byteArrayInputStream =null;
        OutputStream out = null;
        CipherOutputStream cipherOutputStream=null;
        try {
            in = new FileInputStream(sourcePath);
            byte[] bytes = IoUtil.readBytes(in);
            byteArrayInputStream = IoUtil.toStream(bytes);

            Cipher cipher = generateCipher(Cipher.DECRYPT_MODE,keyData);

            out = new FileOutputStream(targetPath);
            cipherOutputStream = new CipherOutputStream(out, cipher);
            IoUtil.copy(byteArrayInputStream, cipherOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }finally {
            IoUtil.close(cipherOutputStream);
            IoUtil.close(out);
            IoUtil.close(byteArrayInputStream);
            IoUtil.close(in);
        }
    }

    public static void main(String[] args) throws Exception {

        //生成Key
        byte[] bytes = generateKey(KEY_SIZE);
        String key = ByteUtils.toHexString(bytes);

        //原始文件
        String sp = "D:\\Git\\test\\my-project\\.file\\3a6281fc-f0b2-4ab8-b217-92a6220a48f2_音乐播放器系统设计.docx";
        //加密后文件
        String dp = "D:\\Git\\test\\my-project\\.file\\加密文件.docx";
        //解密后文件
        String dp2 = "D:\\Git\\test\\my-project\\.file\\解密文件.docx";

        byte[] keyData = ByteUtils.fromHexString(key);
        //加密文件
        encryptFile(keyData,sp,dp);

        //解密文件
        decryptFile(keyData,dp,dp2);
    }

}
