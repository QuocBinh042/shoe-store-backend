package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.ReceiptDTO;
import com.shoestore.Server.entities.Receipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {
    ReceiptDTO toDto(Receipt receipt);
    Receipt toEntity(ReceiptDTO receiptDTO);
}
