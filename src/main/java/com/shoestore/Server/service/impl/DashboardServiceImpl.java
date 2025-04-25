package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.response.*;
import com.shoestore.Server.entities.Product;
import com.shoestore.Server.enums.RoleType;
import com.shoestore.Server.repositories.OrderDetailRepository;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.ProductRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.service.DashboardService;
import com.shoestore.Server.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaginationService paginationService;

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
                        (String) r[0],
                        ((Number) r[1]).doubleValue(),
                        ((Number) r[2]).longValue()
                ))
                .toList();

        var status = orderRepository.fetchOrderStatusCounts(start, end);

        var dto = new RevenueOrdersResponse();
        dto.setRevenueSeries(series);
        dto.setOrderStatus(status);
        return dto;
    }

    @Override
    public PaginationResponse<BestSellerResponse> getBestSellers(int page, int pageSize) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(30);

        Pageable pageable = paginationService.createPageable(page, pageSize);

        Page<BestSellerResponse> dtoPage = productRepository.fetchRawBestSellers(start, end, pageable);

        return paginationService.paginate(dtoPage);
    }

    @Override
    public PaginationResponse<StockAlertResponse> getStockAlerts(int threshold, int page, int pageSize) {
        Pageable pageable = paginationService.createPageable(page, pageSize);
        Page<StockAlertResponse> dtoPage = productRepository.findLowStock(threshold, pageable);
        return paginationService.paginate(dtoPage);
    }

    @Override
    public List<CustomerGrowthResponse> getCustomerGrowth(int year) {
        return orderRepository.findCustomerGrowth(year)
                .stream()
                .map(r -> new CustomerGrowthResponse(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        ((Number) r[2]).longValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerRetentionResponse> getCustomerRetention(int year) {
        // Lấy danh sách [userId, month] cho tất cả orders trong năm
        Map<String, Set<Integer>> usersByMonth = orderRepository.findMonthlyUsers(year).stream()
                .collect(Collectors.groupingBy(
                        rec -> (String) rec[1],
                        LinkedHashMap::new,  // giữ thứ tự tháng
                        Collectors.mapping(rec -> ((Number) rec[0]).intValue(), Collectors.toSet())
                ));

        List<CustomerRetentionResponse> retention = new ArrayList<>();
        String prevMonth = null;

        // Duyệt theo thứ tự tháng
        for (String month : usersByMonth.keySet()) {
            if (prevMonth != null) {
                Set<Integer> prev = usersByMonth.get(prevMonth);
                Set<Integer> curr = usersByMonth.get(month);

                // Tính giao nhau
                Set<Integer> inter = new HashSet<>(curr);
                inter.retainAll(prev);

                // Tính tỷ lệ
                double rate = prev.isEmpty()
                        ? 0.0
                        : (inter.size() * 100.0 / prev.size());

                // Làm tròn 1 chữ số thập phân
                double rounded = Math.round(rate * 10) / 10.0;
                retention.add(new CustomerRetentionResponse(month, rounded));
            }
            prevMonth = month;
        }

        return retention;
    }

    @Override
    public CustomerMetricsResponse getCustomerMetrics(int year) {
        List<CustomerRetentionResponse> retList = getCustomerRetention(year);
        double retentionRate = retList.isEmpty() ? 0.0 : retList.get(retList.size() - 1).retentionRate();

        double avgLifetime = Optional.ofNullable(orderRepository.findAvgCustomerLifetimeValue(year)).orElse(0.0);
        double repeatRate = Optional.ofNullable(orderRepository.findRepeatPurchaseRate(year)).orElse(0.0);

        return new CustomerMetricsResponse(
                Math.round(retentionRate * 10) / 10.0,
                Math.round(avgLifetime * 100) / 100.0,
                Math.round(repeatRate * 10) / 10.0
        );
    }
    @Override
    public PaginationResponse<InventoryForecastResponse> getInventoryForecast(int page, int pageSize) {
        Pageable pageable = paginationService.createPageable(page, pageSize);
        // Fetch paged products
        Page<Product> productPage = productRepository.findAll(pageable);

        Page<InventoryForecastResponse> dtoPage = productPage.map(product -> {
            int currentStock = productRepository.getTotalStockByProductId(product.getProductID());
            int totalSold = orderDetailRepository.getTotalSoldByProductId(product.getProductID());
            double ratio = currentStock == 0
                    ? 0.0
                    : ((double) totalSold / currentStock) * 100;
            double roundedRatio = Math.round(ratio * 10.0) / 10.0;
            String urgency = totalSold == 0 ? "Low" : calculateRestockUrgency(roundedRatio);
            return new InventoryForecastResponse(
                    product.getProductName(),
                    currentStock,
                    totalSold,
                    roundedRatio,
                    urgency
            );
        });

        return paginationService.paginate(dtoPage);
    }

    private String calculateRestockUrgency(double ratio) {
        if (ratio <= 10) return "Critical";
        else if (ratio <= 30) return "High";
        else if (ratio <= 60) return "Medium";
        else return "Low";
    }

}
