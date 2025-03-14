package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.request.ReceiptDTO;
import com.shoestore.Server.entities.Receipt;
import com.shoestore.Server.mapper.ReceiptMapper;
import com.shoestore.Server.repositories.ReceiptRepository;
import com.shoestore.Server.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    @Override
    public ReceiptDTO addReceipt(ReceiptDTO receiptDTO) {
        Receipt receipt = receiptMapper.toEntity(receiptDTO);
        Receipt savedReceipt = receiptRepository.save(receipt);
        return receiptMapper.toDto(savedReceipt);
    }
}
