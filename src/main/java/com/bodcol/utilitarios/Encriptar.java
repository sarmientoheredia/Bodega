
package com.bodcol.utilitarios;

import java.security.MessageDigest;

/**
 *
 * @author Cbos- Com. Sarmiento H. Luis A.
 */
public class Encriptar {
    
    
    public static String sha512(String cadena){
        StringBuilder sb=new StringBuilder();
        try {
            MessageDigest md=MessageDigest.getInstance("SHA-512");
            md.update(cadena.getBytes());
            byte[] mb=md.digest();
            for(int i=0;i<mb.length;i++){
                sb.append(Integer.toString((mb[i] & 0xff)+0x10,16).substring(1));
                
            }
        } catch (Exception e) {
        }
        return  sb.toString();
    }
}
