package com.shoestore.Server.jobs;

import com.shoestore.Server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderAutoCancelJob {

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 300000)
    public void runAutoCancel() {
        orderService.cancelUnpaidVNPayOrders();
    }
}
