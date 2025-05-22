package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.VoucherDTO;
import com.shoestore.Server.entities.Voucher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VoucherService{
    VoucherDTO getVoucherById(int id);
    void deleteVoucher(int voucherID);
    List<VoucherDTO> getAllVouchers();
    List<VoucherDTO> getEligibleVouchers(int userId,BigDecimal orderValue);
    List<VoucherDTO> createVouchers(List<VoucherDTO> voucherDTOList);

}
