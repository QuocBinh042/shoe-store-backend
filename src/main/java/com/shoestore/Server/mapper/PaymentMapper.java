package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.PaymentDTO;
import com.shoestore.Server.dto.response.PaymentResponse;
import com.shoestore.Server.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toDto(Payment entity);
    Payment toEntity(PaymentDTO dto);
    PaymentResponse toPaymentResponse(Payment payment);
}
