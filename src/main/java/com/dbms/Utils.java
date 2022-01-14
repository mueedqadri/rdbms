package com.dbms;

import java.security.MessageDigest;

public class Utils {

    public static String encryptToSha256(final String base){
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            System.out.println("Couldn't create SHA256 Hash");
            System.exit(0);
            return "" ;
        }
    }
}
