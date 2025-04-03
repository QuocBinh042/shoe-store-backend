package com.shoestore.Server.mapper;

import com.shoestore.Server.enums.PaymentMethod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumMapper {
    default PaymentMethod map(String value) {
        return value == null ? null : PaymentMethod.valueOf(value);
    }
}
