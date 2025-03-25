package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.request.ReceiptDTO;
import com.shoestore.Server.entities.CartItem;
import com.shoestore.Server.entities.Order;
import com.shoestore.Server.entities.Payment;
import com.shoestore.Server.entities.Receipt;
import com.shoestore.Server.service.OrderService;
import com.shoestore.Server.service.PaymentService;
import com.shoestore.Server.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReceipt(@RequestBody ReceiptDTO receipt) {
        return ResponseEntity.ok(receiptService.addReceipt(receipt));
    }
}
