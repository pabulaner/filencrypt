package de.pabulaner.filencrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class Secure {

    private static final String SHA = "SHA-256";

    private static final String ALGORITHM = "AES";

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private static final int IV_LENGTH = 12;

    private static final int TAG_LENGTH = 128;

    private static final int PASSWORD_LENGTH = 64;

    private final String password;

    public Secure(String password) {
        if (password != null) {
            this.password = password;
        } else {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[PASSWORD_LENGTH];

            random.nextBytes(bytes);
            this.password = UUID.nameUUIDFromBytes(bytes).toString();
        }
    }

    public byte[] encrypt(byte[] input) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];

        random.nextBytes(iv);

        SecretKeySpec key = createKey();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encrypted = cipher.doFinal(input);
        byte[] combined = new byte[iv.length + encrypted.length];

        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return combined;
    }

    public byte[] decrypt(byte[] input) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        byte[] encrypted = new byte[input.length - iv.length];

        System.arraycopy(input, 0, iv, 0, iv.length);
        System.arraycopy(input, iv.length, encrypted, 0, encrypted.length);

        SecretKey key = createKey();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        return cipher.doFinal(encrypted);
    }

    private SecretKeySpec createKey() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(SHA);
        byte[] key = digest.digest(password.getBytes());

        return new SecretKeySpec(key, ALGORITHM);
    }

    public String getPassword() {
        return password;
    }
}
