package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    List<Voucher> findByMinOrderValueLessThanEqualAndStatusTrueAndStartDateBeforeAndEndDateAfter(
            BigDecimal orderValue, LocalDateTime now1, LocalDateTime now2);
}

