package com.bishe.util;

/**
 * zuc加密工具
 */
public class ZUCUtil {
    //线性反馈移位寄存器
    int[] LFSR = new int[16];
    //比特重组输出的4个32比特字
    long[] x = new long[4];
    //非线性函数f输出的32比特记忆单元变量
    long r1, r2;
    //s0盒和s1盒
    static int[] k = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    static int[] iv = {0x00,0x00,0x00,0x00,0x00,3,0x00,0x00,2,0x00,0x00,0x00,0x00,1,0x00,0x00};
    static int[][] s0 = {
            {0x3e, 0x72, 0x5b, 0x47, 0xca, 0xe0, 0x00, 0x33, 0x04, 0xd1, 0x54, 0x98, 0x09, 0xb9, 0x6d, 0xcb},
            {0x7b, 0x1b, 0xf9, 0x32, 0xaf, 0x9d, 0x6a, 0xa5, 0xb8, 0x2d, 0xfc, 0x1d, 0x08, 0x53, 0x03, 0x90},
            {0x4d, 0x4e, 0x84, 0x99, 0xe4, 0xce, 0xd9, 0x91, 0xdd, 0xb6, 0x85, 0x48, 0x8b, 0x29, 0x6e, 0xac},
            {0xcd, 0xc1, 0xf8, 0x1e, 0x73, 0x43, 0x69, 0xc6, 0xb5, 0xbd, 0xfd, 0x39, 0x63, 0x20, 0xd4, 0x38},
            {0x76, 0x7d, 0xb2, 0xa7, 0xcf, 0xed, 0x57, 0xc5, 0xf3, 0x2c, 0xbb, 0x14, 0x21, 0x06, 0x55, 0x9b},
            {0xe3, 0xef, 0x5e, 0x31, 0x4f, 0x7f, 0x5a, 0xa4, 0x0d, 0x82, 0x51, 0x49, 0x5f, 0xba, 0x58, 0x1c},
            {0x4a, 0x16, 0xd5, 0x17, 0xa8, 0x92, 0x24, 0x1f, 0x8c, 0xff, 0xd8, 0xae, 0x2e, 0x01, 0xd3, 0xad},
            {0x3b, 0x4b, 0xda, 0x46, 0xeb, 0xc9, 0xde, 0x9a, 0x8f, 0x87, 0xd7, 0x3a, 0x80, 0x6f, 0x2f, 0xc8},
            {0xb1, 0xb4, 0x37, 0xf7, 0x0a, 0x22, 0x13, 0x28, 0x7c, 0xcc, 0x3c, 0x89, 0xc7, 0xc3, 0x96, 0x56},
            {0x07, 0xbf, 0x7e, 0xf0, 0x0b, 0x2b, 0x97, 0x52, 0x35, 0x41, 0x79, 0x61, 0xa6, 0x4c, 0x10, 0xfe},
            {0xbc, 0x26, 0x95, 0x88, 0x8a, 0xb0, 0xa3, 0xfb, 0xc0, 0x18, 0x94, 0xf2, 0xe1, 0xe5, 0xe9, 0x5d},
            {0xd0, 0xdc, 0x11, 0x66, 0x64, 0x5c, 0xec, 0x59, 0x42, 0x75, 0x12, 0xf5, 0x74, 0x9c, 0xaa, 0x23},
            {0x0e, 0x86, 0xab, 0xbe, 0x2a, 0x02, 0xe7, 0x67, 0xe6, 0x44, 0xa2, 0x6c, 0xc2, 0x93, 0x9f, 0xf1},
            {0xf6, 0xfa, 0x36, 0xd2, 0x50, 0x68, 0x9e, 0x62, 0x71, 0x15, 0x3d, 0xd6, 0x40, 0xc4, 0xe2, 0x0f},
            {0x8e, 0x83, 0x77, 0x6b, 0x25, 0x05, 0x3f, 0x0c, 0x30, 0xea, 0x70, 0xb7, 0xa1, 0xe8, 0xa9, 0x65},
            {0x8d, 0x27, 0x1a, 0xdb, 0x81, 0xb3, 0xa0, 0xf4, 0x45, 0x7a, 0x19, 0xdf, 0xee, 0x78, 0x34, 0x60}
    };
    //s2盒和s3盒
    static int[][] s1 = {
            {0x55, 0xc2, 0x63, 0x71, 0x3b, 0xc8, 0x47, 0x86, 0x9f, 0x3c, 0xda, 0x5b, 0x29, 0xaa, 0xfd, 0x77},
            {0x8c, 0xc5, 0x94, 0x0c, 0xa6, 0x1a, 0x13, 0x00, 0xe3, 0xa8, 0x16, 0x72, 0x40, 0xf9, 0xf8, 0x42},
            {0x44, 0x26, 0x68, 0x96, 0x81, 0xd9, 0x45, 0x3e, 0x10, 0x76, 0xc6, 0xa7, 0x8b, 0x39, 0x43, 0xe1},
            {0x3a, 0xb5, 0x56, 0x2a, 0xc0, 0x6d, 0xb3, 0x05, 0x22, 0x66, 0xbf, 0xdc, 0x0b, 0xfa, 0x62, 0x48},
            {0xdd, 0x20, 0x11, 0x06, 0x36, 0xc9, 0xc1, 0xcf, 0xf6, 0x27, 0x52, 0xbb, 0x69, 0xf5, 0xd4, 0x87},
            {0x7f, 0x84, 0x4c, 0xd2, 0x9c, 0x57, 0xa4, 0xbc, 0x4f, 0x9a, 0xdf, 0xfe, 0xd6, 0x8d, 0x7a, 0xeb},
            {0x2b, 0x53, 0xd8, 0x5c, 0xa1, 0x14, 0x17, 0xfb, 0x23, 0xd5, 0x7d, 0x30, 0x67, 0x73, 0x08, 0x09},
            {0xee, 0xb7, 0x70, 0x3f, 0x61, 0xb2, 0x19, 0x8e, 0x4e, 0xe5, 0x4b, 0x93, 0x8f, 0x5d, 0xdb, 0xa9},
            {0xad, 0xf1, 0xae, 0x2e, 0xcb, 0x0d, 0xfc, 0xf4, 0x2d, 0x46, 0x6e, 0x1d, 0x97, 0xe8, 0xd1, 0xe9},
            {0x4d, 0x37, 0xa5, 0x75, 0x5e, 0x83, 0x9e, 0xab, 0x82, 0x9d, 0xb9, 0x1c, 0xe0, 0xcd, 0x49, 0x89},
            {0x01, 0xb6, 0xbd, 0x58, 0x24, 0xa2, 0x5f, 0x38, 0x78, 0x99, 0x15, 0x90, 0x50, 0xb8, 0x95, 0xe4},
            {0xd0, 0x91, 0xc7, 0xce, 0xed, 0x0f, 0xb4, 0x6f, 0xa0, 0xcc, 0xf0, 0x02, 0x4a, 0x79, 0xc3, 0xde},
            {0xa3, 0xef, 0xea, 0x51, 0xe6, 0x6b, 0x18, 0xec, 0x1b, 0x2c, 0x80, 0xf7, 0x74, 0xe7, 0xff, 0x21},
            {0x5a, 0x6a, 0x54, 0x1e, 0x41, 0x31, 0x92, 0x35, 0xc4, 0x33, 0x07, 0x0a, 0xba, 0x7e, 0x0e, 0x34},
            {0x88, 0xb1, 0x98, 0x7c, 0xf3, 0x3d, 0x60, 0x6c, 0x7b, 0xca, 0xd3, 0x1f, 0x32, 0x65, 0x04, 0x28},
            {0x64, 0xbe, 0x85, 0x9b, 0x2f, 0x59, 0x8a, 0xd7, 0xb0, 0x25, 0xac, 0xaf, 0x12, 0x03, 0xe2, 0xf2}
    };
    //15比特的字符串常量
    static short[] d = {0x44D7, 0x26BC, 0x626B, 0x135E, 0x5789, 0x35E2, 0x7135, 0x09AF, 0x4D78, 0x2F13, 0x6BC4, 0x1AF1, 0x5E26, 0x3C4D, 0x789A, 0x47AC};

    /**
     * 初始化（输出密钥前的工作）
     *
     * @param k  16字节密钥
     * @param iv 16字节初始向量
     */
    private void init(int[] k, int[] iv) {
        //密钥装入
        for (int i = 0; i < 16; i++) {
            LFSR[i] = (k[i] << 23) | (d[i] << 8) | iv[i];
        }
        r1 = 0;
        r2 = 0;
        long w;
        for (int i = 0; i < 32; i++) {
            bitReconstruction();
            w = f();
            LFSRWithInitialisationMode((int) (w >> 1));
        }
        //以下三步为工作阶段输出密钥前的步骤
        bitReconstruction();
        f();
        LFSRWithWorkMode();
    }

    /**
     * 生成一个密钥
     *
     * @return 输出的密钥
     */
    public long generate() {
        bitReconstruction();
        long z = f() ^ x[3];
        LFSRWithWorkMode();
        return z;
    }

    /**
     * 构造方法
     *
     * @param k  16字节密钥
     * @param iv 16字节初始向量
     */
    public ZUCUtil(int[] k, int[] iv) {
        init(k, iv);
    }

    /**
     * LFSR初始化模式
     *
     * @param u f的输出舍弃最低位
     */
    private void LFSRWithInitialisationMode(int u) {
        int v = (int) ((((long) LFSR[15] << 15) + ((long) LFSR[13] << 17) + ((long) LFSR[10] << 21) + ((long) LFSR[4] << 20) + ((long) LFSR[0] << 8) + LFSR[0]) % 0x7fffffffL);
        int s16 = (int) (((long) v + u) % 0x7fffffffL);
        if (s16 == 0) {
            s16 = 0x7fffffff;
        }
        System.arraycopy(LFSR, 1, LFSR, 0, 15);
        LFSR[15] = s16;
    }

    /**
     * LFSR工作模式
     */
    private void LFSRWithWorkMode() {
        int s16 = (int) ((((long) LFSR[15] << 15) + ((long) LFSR[13] << 17) + ((long) LFSR[10] << 21) + ((long) LFSR[4] << 20) + ((long) LFSR[0] << 8) + LFSR[0]) % 0x7fffffffL);
        if (s16 == 0) {
            s16 = 0x7fffffff;
        }
        System.arraycopy(LFSR, 1, LFSR, 0, 15);
        LFSR[15] = s16;
    }

    /**
     * 比特重组
     */
    private void bitReconstruction() {
        //s15的高16位和s14的低16位
        x[0] = ((LFSR[15] & 0x7fff8000L) << 1) | (LFSR[14] & 0xFFFFL);
        //s11的第16位和s9的高16位
        x[1] = ((LFSR[11] & 0xffffL) << 16) | (LFSR[9] >>> 15);
        //s7的第16位和s5的高16位
        x[2] = ((LFSR[7] & 0xffffL) << 16) | (LFSR[5] >>> 15);
        //s2的第16位和s0的高16位
        x[3] = ((LFSR[2] & 0xffffL) << 16) | (LFSR[0] >>> 15);
    }

    /**
     * 非线性函数
     *
     * @return 32比特字w
     */
    private long f() {
        long w = ((x[0] ^ r1) + r2) % 0x100000000L;
        long w1 = (r1 + x[1]) % 0x100000000L;
        long w2 = r2 ^ x[2];
        r1 = s(l1(((w1 << 16) & 0xffff0000L) | w2 >> 16));
        r2 = s(l2(((w2 << 16) & 0xffff0000L) | w1 >> 16));
        return w;
    }

    /**
     * s盒变换
     *
     * @param w 32位输入
     * @return 32位输出
     */
    private static long s(long w) {
        int[] y = new int[4];
        //将输入分为4个8bits，分别输入4个s盒，高4位为行数，低4位为列数
        y[0] = s0[(int) (w >> 28)][(int) ((w >> 24) & 0xf)];
        y[1] = s1[(int) ((w >> 20) & 0xf)][(int) ((w >> 16) & 0xf)];
        y[2] = s0[(int) ((w >> 12) & 0xf)][(int) ((w >> 8) & 0xf)];
        y[3] = s1[(int) ((w >> 4) & 0xf)][(int) (w & 0xf)];
        return ((long) y[0] << 24) | (y[1] << 16) | (y[2] << 8) | y[3];
    }

    /**
     * 线性变换1
     *
     * @param x 32bits
     * @return 32bits
     */
    private static long l1(long x) {
        return x ^ shiftLeft(x, 2) ^ shiftLeft(x, 10) ^ shiftLeft(x, 18) ^ shiftLeft(x, 24);
    }

    /**
     * 线性变换2
     *
     * @param x 32bits
     * @return 32bits
     */
    private static long l2(long x) {
        return x ^ shiftLeft(x, 8) ^ shiftLeft(x, 14) ^ shiftLeft(x, 22) ^ shiftLeft(x, 30);
    }

    /**
     * 循环左移
     *
     * @param x 32bits
     * @param n 左移位数
     * @return 输出32bits
     */
    private static long shiftLeft(long x, int n) {
        return ((x << n) | (x >> (32 - n))) & 0xffffffffL;
    }

    //加密方法
    public static String encrypt(String input){
        ZUCUtil zuc = new ZUCUtil(k, iv);
        int length = input.length();
        long[] key_stream = new long[length];
        for (int i = 0; i < length; i++) {
            key_stream[i] = zuc.generate();
        }
        long[] result = new long[length];
        char[] chars = input.toCharArray();
        String encrypt_result = "";
        for (int i = 0; i < length; i++) {
            result[i] = chars[i] ^ key_stream[i];
            encrypt_result = encrypt_result + "," + result[i];
        }
        return encrypt_result.substring(1);
    }

    //解密方法
    public static String encryption(String result){
        String[] split = result.split(",");
        ZUCUtil zuc = new ZUCUtil(k, iv);
        int length = split.length;
        long[] key_stream = new long[length];
        for (int i = 0; i < length; i++) {
            key_stream[i] = zuc.generate();
        }
        char[] input = new char[length];
        String inputStr = "";
        for (int i = 0; i < length; i++) {
            input[i] = (char) (Long.parseLong(split[i]) ^ key_stream[i]);
            inputStr = inputStr + input[i];
        }
        return inputStr;
    }

    public static void main(String[] args) {
        //加密
        String encrypt = encrypt("123");
        System.out.println("encrypt = " + encrypt);
        //解密
        String encryption = encryption(encrypt);
        System.out.println("encryption = " + encryption);
    }
}

