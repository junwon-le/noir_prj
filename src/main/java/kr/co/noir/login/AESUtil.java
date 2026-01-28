package kr.co.noir.login; // 패키지명은 본인 프로젝트에 맞게 수정

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component 
@PropertySource("classpath:application-dev.properties")
public class AESUtil {
	
	// 프로퍼티 값 주입 
    @Value("${crypto.aes.alg}")
    private String alg;

    @Value("${crypto.aes.key}")
    private String key;

    

    // 1. 암호화 (Encrypt) : 평문 -> 암호화된 Base64 문자열
    public String encrypt(String text) {
        try {
        	// 초기화 벡터(IV)도 16자리 (보통 키의 앞부분을 따서 쓰거나 랜덤 생성)
        	String iv = key.substring(0, 16);
        	
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted); // 문자열로 변환
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 2. 복호화 (Decrypt) : 암호화된 Base64 문자열 -> 평문
    public String decrypt(String cipherText) {
        try {
        	
        	String iv = key.substring(0, 16);
        	
            Cipher cipher = Cipher.getInstance(alg);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
            
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}