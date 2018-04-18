package leaflet.miaoa.qmxh.leaflet_simple.utils;





import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCBC {  
	public static String utf = "utf-8";
	public static String content="wl886134";
	public static String key="tianzaishui81024";
	public static String iv="tianzaishui81024";
//    public static void main(String[] args) throws UnsupportedEncodingException {
//        //加密
//        byte[ ] encrypted=AES_CBC_Encrypt(content.getBytes("UTF-8"), key.getBytes("UTF-8"), iv.getBytes("UTF-8"));
//        System.out.println("加密后："+ Base64.encode(encrypted));
//        //解密
//        byte[ ] decrypted=AES_CBC_Decrypt(encrypted, key.getBytes("UTF-8"), iv.getBytes("UTF-8"));
//        System.out.println("解密后："+byteToHexString(decrypted));
//    }
    
    public static String aesEncrypt(String content) throws UnsupportedEncodingException{
    	//加密
        byte[ ] encrypted=AES_CBC_Encrypt(content.getBytes(utf), key.getBytes(utf), iv.getBytes(utf));
        System.out.println("加密后："+Base64.encode(encrypted));
        
        return Base64.encode(encrypted);
    }
    
    public static String aesDecrypt(byte[] encrypted) throws UnsupportedEncodingException{
    	//解密
    	byte[ ] decrypted=AES_CBC_Decrypt(encrypted, key.getBytes(utf), iv.getBytes(utf));
        System.out.println("解密后："+byteToHexString(decrypted));
        
        return byteToHexString(decrypted);
    }
    
    private static String byteToHexString(byte[] decrypted) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
        return new String(decrypted, "UTF-8");
	}
    
	public static String byteToString(byte[ ] byte1) throws UnsupportedEncodingException{
         return new String(byte1, "UTF-8");
    }
	
	
    public static byte[] AES_CBC_Encrypt(byte[] content, byte[] keyBytes, byte[] iv){
           
        try{
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,key, new IvParameterSpec(iv));
            byte[] result=cipher.doFinal(content);
            return result;
        }catch (Exception e) {
           System.out.println("exception:"+e.toString());
        }
        return null;
    }
    
    public static byte[] AES_CBC_Decrypt(byte[] content, byte[] keyBytes, byte[] iv){
        
        try{
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
            byte[] result=cipher.doFinal(content);
            return result;
        }catch (Exception e) {
            // TODO Auto-generated catchblock
            System.out.println("exception:"+e.toString());
        }
        return null;
    }
    
}