package com.shoestore.Server.service.impl;

import com.shoestore.Server.dto.response.KpiResponse;
import com.shoestore.Server.enums.RoleType;
import com.shoestore.Server.repositories.OrderRepository;
import com.shoestore.Server.repositories.UserRepository;
import com.shoestore.Server.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public KpiResponse getKpiOverview() {
        Double revenue = orderRepository.sumTotalAmount();
        double totalRevenue = revenue != null ? revenue : 0.0;
        long totalOrders = orderRepository.count();
        double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;

        LocalDateTime startOfMonth = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();

        long newCustomers = userRepository
                .countByRoleTypeAndCreatedAtAfter(RoleType.CUSTOMER, startOfMonth);

        KpiResponse dto = new KpiResponse();
        dto.setTotalRevenue(totalRevenue);
        dto.setTotalOrders(totalOrders);
        dto.setAvgOrderValue(avgOrderValue);
        dto.setNewCustomers(newCustomers);
        return dto;
    }
}
