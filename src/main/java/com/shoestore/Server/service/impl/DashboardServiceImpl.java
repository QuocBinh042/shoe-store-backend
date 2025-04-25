package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.response.KpiItemResponse;
import com.shoestore.Server.dto.response.KpiResponse;
import com.shoestore.Server.dto.response.RevenueOrdersResponse;
import com.shoestore.Server.dto.response.RevenueSeriesResponse;
import com.shoestore.Server.enums.RoleType;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public KpiResponse getKpiOverview(String timeFrame) {
        LocalDate now = LocalDate.now();
        LocalDate currentStart, currentEnd, prevStart, prevEnd;

        if ("weekly".equalsIgnoreCase(timeFrame)) {
            currentStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            currentEnd = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            prevStart = currentStart.minusWeeks(1);
            prevEnd = currentEnd.minusWeeks(1);
        } else {
            currentStart = now.withDayOfMonth(1);
            currentEnd = now.withDayOfMonth(now.lengthOfMonth());
            prevStart = currentStart.minusMonths(1).withDayOfMonth(1);
            prevEnd = prevStart.withDayOfMonth(prevStart.lengthOfMonth());
        }

        BigDecimal currRev = orderRepository.sumTotalBetween(currentStart, currentEnd)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);
        long currOrders = orderRepository.countByOrderDateBetween(currentStart, currentEnd);
        BigDecimal currAvg = currOrders > 0
                ? currRev.divide(BigDecimal.valueOf(currOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        long currNewCustomer = userRepository.countByRoleTypeAndCreatedAtBetween(
                RoleType.CUSTOMER,
                currentStart.atStartOfDay(),
                currentEnd.atTime(23, 59, 59));

        // Tính cho kỳ trước
        BigDecimal prevRev = orderRepository.sumTotalBetween(prevStart, prevEnd)
                .map(BigDecimal::valueOf)
                .orElse(BigDecimal.ZERO);
        long prevOrders = orderRepository.countByOrderDateBetween(prevStart, prevEnd);
        BigDecimal prevAvg = prevOrders > 0
                ? prevRev.divide(BigDecimal.valueOf(prevOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        long prevNewCustomer = userRepository.countByRoleTypeAndCreatedAtBetween(
                RoleType.CUSTOMER,
                prevStart.atStartOfDay(),
                prevEnd.atTime(23, 59, 59));

        List<KpiItemResponse> items = List.of(
                buildItem("totalRevenue", currRev, prevRev),
                buildItem("totalOrders", BigDecimal.valueOf(currOrders), BigDecimal.valueOf(prevOrders)),
                buildItem("avgOrderValue", currAvg, prevAvg),
                buildItem("newCustomers", BigDecimal.valueOf(currNewCustomer), BigDecimal.valueOf(prevNewCustomer))
        );

        // Đóng gói và trả về
        KpiResponse response = new KpiResponse();
        response.setItems(items);
        return response;
    }

    private KpiItemResponse buildItem(String key, BigDecimal current, BigDecimal previous) {
        KpiItemResponse item = new KpiItemResponse();
        item.setKey(key);
        item.setCurrent(current);
        item.setPrevious(previous);

        BigDecimal change;
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            change = BigDecimal.ZERO;
        } else {
            change = current.subtract(previous)
                    .divide(previous, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        item.setChangePercent(change);
        return item;
    }

    @Override
    public RevenueOrdersResponse getRevenueAndOrders(String timeFrame) {
        LocalDate now = LocalDate.now();
        LocalDate start, end;
        start = switch (timeFrame.toLowerCase()) {
            case "daily" -> {
                end = now;
                yield now.minusDays(30);
            }
            case "weekly" -> {
                end = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                yield end.minusWeeks(8).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            }
            case "yearly" -> {
                end = now;
                yield now.minusYears(3).withDayOfYear(1);
            }
            default -> {
                end = now.withDayOfMonth(now.lengthOfMonth());
                yield end.minusMonths(11).withDayOfMonth(1);
            }
        };

        List<Object[]> raw = orderRepository.fetchRawRevenueSeries(timeFrame, start, end);
        List<RevenueSeriesResponse> series = raw.stream()
                .map(r -> new RevenueSeriesResponse(
                        (String)    r[0],
                        ((Number)   r[1]).doubleValue(),
                        ((Number)   r[2]).longValue()
                ))
                .toList();

        var status = orderRepository.fetchOrderStatusCounts(start, end);

        var dto = new RevenueOrdersResponse();
        dto.setRevenueSeries(series);
        dto.setOrderStatus(status);
        return dto;
    }
}
