package com.example.loginserver;

public abstract class EncryptionAlgorithm {
    abstract public String encrypt(String plainText) throws Exception;
    abstract public String decrypt(String encryptedText) throws Exception;
}
