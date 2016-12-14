package com.inthub.baselibrary.common;

import java.io.UnsupportedEncodingException;

/**
 * 移位加解密方式
 * */
public class CipherTool
{
    public static int[] DE_KEY;
    public static int[] EN_KEY;

    static
    {
        int[] arrayOfInt1 = new int[8];
        arrayOfInt1[0] = 7;
        arrayOfInt1[1] = 2;
        arrayOfInt1[2] = 5;
        arrayOfInt1[3] = 4;
        arrayOfInt1[5] = 1;
        arrayOfInt1[6] = 3;
        arrayOfInt1[7] = 6;
        EN_KEY = arrayOfInt1;
        int[] arrayOfInt2 = new int[8];
        arrayOfInt2[0] = 4;
        arrayOfInt2[1] = 5;
        arrayOfInt2[2] = 1;
        arrayOfInt2[3] = 6;
        arrayOfInt2[4] = 3;
        arrayOfInt2[5] = 2;
        arrayOfInt2[6] = 7;
        DE_KEY = arrayOfInt2;
    }

    public static byte byteDecryption(byte nSrc)
    {
        byte nDst = 0;
        byte nBit = 0;
        int i;
        for (i = 0; i < 8; i++)
        {
            nBit = (byte) (1 << DE_KEY[i]);
            if ((nSrc & nBit) != 0)
                nDst |= (1 << i);
        }
        return nDst;
    }

    public static byte byteEncryption(byte nSrc)
    {
        byte nDst = 0;
        byte nBit = 0;
        int i;
        for (i = 0; i < 8; i++)
        {
            nBit = (byte) (1 << EN_KEY[i]);
            if ((nSrc & nBit) != 0)
                nDst |= (1 << i);
        }
        return nDst;
    }

    public static String getCipherString(String source) throws UnsupportedEncodingException
    {
        if (source.trim().equals(""))
        {
            return "";
        }
        String s = source;
        byte[] sb = s.getBytes("UTF-8");
        String d = new String(sb, "UTF-8");
        sb = d.getBytes("UTF-8");
        byte[] sbNew = new byte[sb.length];
        StringBuilder sbb = new StringBuilder();

        for (int i = 0; i < sb.length; i++)
        {
            byte t = byteEncryption(sb[i]);

            sbNew[i] = t;
            char c = (char) t;
            sbb.append(c);
        }

        // String ss=new String(sbb.toString().getBytes("UTF-8"),"UTF-8");
        return sbb.toString();
    }

    @Deprecated
    public static String getCipherStringForPerference(String source)
            throws UnsupportedEncodingException
    {
        byte[] arrayByteOfSource = source.getBytes("UTF-8");
        StringBuilder mStringBuildedr = new StringBuilder();
        int i = 0;
        while (true)
        {
            int j = arrayByteOfSource.length;
            if (i >= j)
            {
                byte[] arrayOfByte2 = mStringBuildedr.toString().getBytes();
                return new String(arrayOfByte2);
            }
            int k = byteEncryption(arrayByteOfSource[i]);
            StringBuilder localStringBuilder2 = mStringBuildedr.append(k);
            StringBuilder localStringBuilder3 = mStringBuildedr.append(",");
            i += 1;
        }
    }

    /**
     * @param cipherString
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getOriginString(String cipherString) throws UnsupportedEncodingException
    {
        if (cipherString.trim().equals(""))
        {
            return "";
        }
        String drr = cipherString;

        byte[] drrByte = new byte[drr.length()];
        for (int i = 0; i < drrByte.length; i++)
        {
            drrByte[i] = byteDecryption(Byte.valueOf((byte) drr.charAt(i)));
        }

        String des = new String(drrByte, "UTF-8");
        return des;
    }
}