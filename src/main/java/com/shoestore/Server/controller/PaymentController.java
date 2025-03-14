package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.OrderDTO;
import com.shoestore.Server.dto.request.PaymentDTO;
import com.shoestore.Server.service.OrderService;
import com.shoestore.Server.service.PaymentService;
import com.shoestore.Server.utils.NetworkUtils;
import com.shoestore.Server.utils.VnPayHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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

    @GetMapping("/vn-pay/create-info")
    public ResponseEntity<Map<String, String>> getPay(@RequestParam("price") long price,
                                                      @RequestParam("code") String code) throws UnsupportedEncodingException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = price * 100;
        String bankCode = "NCB";

        String vnp_IpAddr = NetworkUtils.getIpAddress();
        System.out.println(vnp_IpAddr);
        String vnp_TmnCode = VnPayHelper.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", code);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + code);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", "vn");
//        String returnUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(VnPayUtils.vnp_ReturnUrl)
//                .toUriString();
        vnp_Params.put("vnp_ReturnUrl", VnPayHelper.vnp_ReturnUrl);
        System.out.println(VnPayHelper.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayHelper.hmacSHA512(VnPayHelper.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayHelper.vnp_PayUrl + "?" + queryUrl;
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<Map<String, String>> payCallbackHandler(HttpServletRequest request) {
        request.getParameterMap().forEach((key, value) -> System.out.println(key + ": " + Arrays.toString(value)));

        String status = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");

        Map<String, String> response = new HashMap<>();
        System.out.println("Status: " + status);
        if ("00".equals(status)) {
            OrderDTO order=orderService.getOrderByCode(txnRef);
            paymentService.updateStatus(order.getOrderID(),"Completed");
            response.put("message", "Payment successful");
        } else {
            response.put("message", "Payment failed");
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/vn-pay/return")
    public String vnpayReturn(@RequestParam Map<String, String> params) throws UnsupportedEncodingException {

        String secretKey = "VAG3TSYGQTUS77S0SGBFFFGOCULQCVRP";
        String vnp_SecureHash = params.get("vnp_SecureHash");

//        create hasdata
        StringBuilder hashData = new StringBuilder();
        params.remove("vnp_SecureHash");
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            String fieldValue = URLDecoder.decode(params.get(fieldName), "UTF-8");
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append("=").append(fieldValue).append("&");
            }
        }

        if (hashData.length() > 0) {
            hashData.deleteCharAt(hashData.length() - 1);
        }

        String calculatedHash = VnPayHelper.hmacSHA512(secretKey, hashData.toString());

        if (calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
            return "Mã bảo mật khớp!";
        } else {
            return "Mã bảo mật không khớp!";
        }
    }
}
