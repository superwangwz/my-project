package com.bishe.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class Sm2Util {

    private static SM2 SM_2 = null;

    static {
        try {
            BouncyCastleProvider BC = new BouncyCastleProvider();
            // 公钥
            String publicKeyStr = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE6/ZmNaBRnZnisjtfxxh2r5F7MXjr2Rjf6wi5++WkUqU0APNadWN+jcZZeupwrkpOaS" +
                    "+epBpYFwaHhlMGIbUzKw==";

            // 私钥
            String privateKeyStr = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgsTwYNqhtSjlpYDQnaKaEQbn9kVdSW3RJnFejbKTYa5KgCgYIKoEcz1UBgi2hRANCAATr9mY1oFGdmeKyO1/HGHavkXsxeOvZGN/rCLn75aRSpTQA81p1Y36Nxll66nCuSk5pL56kGlgXBoeGUwYhtTMr";
            KeyFactory keyFactory = KeyFactory.getInstance("EC", BC);
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyStr)));
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)));
            SM_2 = SmUtil.sm2(privateKey, publicKey);
        } catch (Exception e) {
            log.info("SM2密钥初始化错误", e);
        }
    }

    /**
     * 文件加/解密
     *
     * @param source   原始文件路径
     * @param out      输出路径
     * @param fileName 输出文件名
     * @param action   行为（true为加密，false为解密）
     */
    public static void encryptionOrDecryption(String source, String out, String fileName, boolean action) {
        byte[] bytes = FileUtil.fileToByte(source);
        byte[] data;
        if (action) {
            data = SM_2.encrypt(bytes, KeyType.PublicKey);
        } else {
            data = SM_2.decrypt(bytes, KeyType.PrivateKey);
        }
        FileUtil.byteToFile(data, out, fileName);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 加密
        encryptionOrDecryption("D:\\Git\\test\\my-project\\.file\\3a6281fc-f0b2-4ab8-b217-92a6220a48f2_音乐播放器系统设计.docx","D:\\Git\\test\\my-project\\.file","encrypt.docx",true);
        // 解密
        encryptionOrDecryption("D:\\Git\\test\\my-project\\.file\\encrypt.docx","D:\\Git\\test\\my-project\\.file","decrypt.docx",false);
    }
}
