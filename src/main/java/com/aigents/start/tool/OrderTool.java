package com.aigents.start.tool;

import com.aigents.start.service.OrderService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class OrderTool {

    private final OrderService orderService;

    public OrderTool(OrderService orderService) {
        this.orderService = orderService;
    }

    @Tool("Get current status, tracking location, delivery date, amount, and payment status for an order")
    public String getOrderStatus(@P("The numeric ID of the order, e.g. 1") Long orderId) {
        System.out.println(">>> [TOOL EXECUTION] getOrderStatus called for orderId: " + orderId);
        return this.orderService.getOrderStatus(orderId);
    }

    @Tool("Cancel an order by its numeric ID")
    public String cancelOrder(@P("The numeric ID of the order to cancel, e.g. 1") Long orderId) {
        System.out.println(">>> [TOOL EXECUTION] cancelOrder called for orderId: " + orderId);
        return this.orderService.cancelOrder(orderId);
    }
}
