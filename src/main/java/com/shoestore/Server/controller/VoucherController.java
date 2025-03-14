package com.shoestore.Server.controller;

import com.shoestore.Server.dto.request.VoucherDTO;
import com.shoestore.Server.service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @GetMapping("/eligible")
    public ResponseEntity<List<VoucherDTO>> getEligibleVouchers(@RequestParam BigDecimal orderValue) {
        List<VoucherDTO> vouchers = voucherService.getEligibleVouchers(orderValue);
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/by-voucher-id/{id}")
    public ResponseEntity<VoucherDTO> getVoucherById(@PathVariable int id) {
        VoucherDTO voucher = voucherService.getVoucherById(id);
        return (voucher != null) ? ResponseEntity.ok(voucher) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable int id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }
}
