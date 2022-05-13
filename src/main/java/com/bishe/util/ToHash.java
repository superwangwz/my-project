package com.bishe.util;

/**
 * hash util
 */
public class ToHash {
    // 将字符串转成hash值
    public static String changeHash(String key) {
        // 数组大小一般取质数
        int arraySize = 11113;
        int hashCode = 0;
        String result = null;
        // 从字符串的左边开始计算
        for (int i = 0; i < key.length(); i++) {
            // 将获取到的字符串转换成数字，比如a的码值是97，则97-96=1
            int letterValue = key.charAt(i) - 96;
            // 就代表a的值，同理b=2；
            // 防止编码溢出，对每步结果都进行取模运算
            hashCode = ((hashCode << 5) + letterValue) % arraySize;
            result = String.valueOf(hashCode);
        }
        return result;
    }
}