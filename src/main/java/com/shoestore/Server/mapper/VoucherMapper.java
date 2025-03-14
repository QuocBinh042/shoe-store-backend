package com.shoestore.Server.mapper;

import com.shoestore.Server.dto.request.VoucherDTO;
import com.shoestore.Server.entities.Voucher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    VoucherDTO toDto(Voucher entity);
    Voucher toEntity(VoucherDTO dto);
}
