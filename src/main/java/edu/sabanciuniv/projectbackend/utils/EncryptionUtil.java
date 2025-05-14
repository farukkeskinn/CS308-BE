package edu.sabanciuniv.projectbackend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 *  🔐  EncryptionUtil  – geriye uyumlu
 *
 *  • encryptLegacy/decryptLegacy  → ESKİ 16‑bayt key & ECB (dokunma)
 *  • encryptBytes/decryptBytes    → YENİ AES‑256‑GCM
 *  • encryptString/decryptString  → yeni algoritma + Base64 yardımcıları
 */
@Component
public class EncryptionUtil {

    /* === 1) ———————————— LEGACY (ESKİ) ———————————— */
    private static final String LEGACY_ALGO = "AES/ECB/PKCS5Padding";
    private static final String LEGACY_KEY  = "1234567890123456";
    private static final SecretKeySpec LEGACY_SECRET =
            new SecretKeySpec(LEGACY_KEY.getBytes(), "AES");

    // Kullanılan yerler derlenmeye devam etsin
    public static String encryptLegacy(String data) {
        try {
            Cipher c = Cipher.getInstance(LEGACY_ALGO);
            c.init(Cipher.ENCRYPT_MODE, LEGACY_SECRET);
            return Base64.getEncoder().encodeToString(c.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Legacy encryption failed", e);
        }
    }
    public static String decryptLegacy(String enc) {
        try {
            Cipher c = Cipher.getInstance(LEGACY_ALGO);
            c.init(Cipher.DECRYPT_MODE, LEGACY_SECRET);
            byte[] dec = c.doFinal(Base64.getDecoder().decode(enc));
            return new String(dec);
        } catch (Exception e) {
            throw new RuntimeException("Legacy decryption failed", e);
        }
    }

    /* === 2) ———————————— YENİ AES‑256‑GCM ——————————— */
    private static final String GCM_ALGO  = "AES/GCM/NoPadding";
    private static final int    IV_LEN    = 12;              // 96‑bit
    private static final int    TAG_BITS  = 128;

    private final SecretKey   gcmKey;
    private final SecureRandom rng = new SecureRandom();

    // 32‑bayt anahtar .properties / ENV’den okunur
    public EncryptionUtil(@Value("${app.aes.key}") String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        this.gcmKey     = new SecretKeySpec(keyBytes, "AES");
    }

    /* ---- ham byte[] ---- */
    public byte[] encryptBytes(byte[] plain) {
        try {
            byte[] iv = new byte[IV_LEN];
            rng.nextBytes(iv);

            Cipher c = Cipher.getInstance(GCM_ALGO);
            c.init(Cipher.ENCRYPT_MODE, gcmKey, new GCMParameterSpec(TAG_BITS, iv));
            byte[] cipher = c.doFinal(plain);

            byte[] out = new byte[IV_LEN + cipher.length];
            System.arraycopy(iv, 0, out, 0, IV_LEN);
            System.arraycopy(cipher, 0, out, IV_LEN, cipher.length);
            return out;
        } catch (Exception e) {
            throw new RuntimeException("AES‑GCM encryption failed", e);
        }
    }

    public byte[] decryptBytes(byte[] ivPlusCipher) {
        try {
            byte[] iv     = Arrays.copyOfRange(ivPlusCipher, 0, IV_LEN);
            byte[] cipher = Arrays.copyOfRange(ivPlusCipher, IV_LEN, ivPlusCipher.length);

            Cipher c = Cipher.getInstance(GCM_ALGO);
            c.init(Cipher.DECRYPT_MODE, gcmKey, new GCMParameterSpec(TAG_BITS, iv));
            return c.doFinal(cipher);
        } catch (Exception e) {
            throw new RuntimeException("AES‑GCM decryption failed", e);
        }
    }

    /* ---- string kısayolları (Base64) ---- */
    public String encryptString(String plain) {
        return Base64.getEncoder().encodeToString(encryptBytes(plain.getBytes()));
    }
    public String decryptString(String b64) {
        byte[] dec = decryptBytes(Base64.getDecoder().decode(b64));
        return new String(dec);
    }
}