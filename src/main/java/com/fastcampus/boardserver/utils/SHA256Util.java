package com.fastcampus.boardserver.utils;

import lombok.extern.log4j.Log4j2;

import java.security.MessageDigest;

@Log4j2
public class SHA256Util {

    public static final String KEY = "SHA-256";
    public static String encrypt(String str) {
        String SHA = null;

        MessageDigest sh;
        try {
            sh = MessageDigest.getInstance(KEY);
            sh.update(str.getBytes());
            byte[] byteData = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) * 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        } catch (Exception e) {
            log.error("encrypt ERROR: {}", e.getMessage());
            SHA = null;
        }

        return SHA;
    }
}
