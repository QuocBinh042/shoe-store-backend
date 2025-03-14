package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    @Query("SELECT v FROM Voucher v WHERE " +
            "(v.startDate >= :startDate OR :startDate IS NULL) AND " +
            "(v.endDate <= :endDate OR :endDate IS NULL)")
    List<Voucher> findVouchersByDateRange(@Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
    List<Voucher> findByMinOrderValueLessThanEqualAndStatusTrueAndStartDateBeforeAndEndDateAfter(
            BigDecimal orderValue, LocalDateTime now1, LocalDateTime now2);
}

