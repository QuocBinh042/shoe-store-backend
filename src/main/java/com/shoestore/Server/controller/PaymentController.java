package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.request.PaymentDTO;
import com.shoestore.Server.service.OrderService;
import com.shoestore.Server.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<PaymentDTO> addPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO savePaymentDTO = paymentService.addPayment(paymentDTO);
            return ResponseEntity.ok(savePaymentDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/by-order-id/{order-id}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable("order-id") int id) {
        PaymentDTO paymentDTO = paymentService.getPaymentByOrderId(id);
        return paymentDTO != null ? ResponseEntity.ok(paymentDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAll();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/vn-pay/get-link-pay")
    public ResponseEntity<?> pay(HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.createVnPayPayment(request));
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<Void> payCallbackHandler(@RequestParam Map<String, String> params) {
        String status = params.get("vnp_ResponseCode");
        String txnRef = params.get("vnp_TxnRef");
        String amount = params.get("vnp_Amount");
        String vnpTmnCode = params.get("vnp_TmnCode");
        if ("00".equals(status)) {
            OrderDTO order = orderService.getOrderByCode(txnRef);
            paymentService.updateStatus(order.getOrderID(), "SUCCESS");
        }

        String redirectUrl = "http://localhost:3000/payment-result?"
                + "vnp_ResponseCode=" + status
                + "&vnp_TxnRef=" + txnRef
                + "&vnp_Amount=" + amount
                + "&vnp_TmnCode=" + vnpTmnCode;

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }
}
