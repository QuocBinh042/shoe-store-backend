package com.shoestore.Server.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendOrderConfirmation(String to, String orderCode, String totalPrice) throws MessagingException;
}
