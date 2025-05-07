package com.shoestore.Server.service;


public interface EmailService {
    void sendOrderSuccessEmail(String to, String customerName, String orderCode);
    void sendVerificationCodeEmail(String to, String customerName);
    String generateVerificationCode();
}
