package com.shoestore.Server.service;

import com.shoestore.Server.dto.request.ReceiptDTO;
import com.shoestore.Server.entities.Receipt;

public interface ReceiptService {
    ReceiptDTO addReceipt(ReceiptDTO receipt);
}
