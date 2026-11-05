package com.aigents.start.service;

import com.aigents.start.entity.Order;
import com.aigents.start.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Retrieves the status and delivery details of an order.
     *
     * @param orderId the numeric order ID
     * @return order status and shipping tracking details
     */
    public String getOrderStatus(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (orderOpt.isEmpty()) {
            return "Order not found with ID: " + orderId;
        }

        Order order = orderOpt.get();
        StringBuilder details = new StringBuilder();
        details.append(String.format("Order #%d (%s) for %s:\n",
                order.getOrderId(), order.getProductName(), order.getCustomerName()));
        details.append(String.format("- Status: %s\n", order.getStatus()));
        details.append(String.format("- Current Location: %s\n",
                order.getCurrentLocation() != null ? order.getCurrentLocation() : "Processing facility"));
        if (order.getExpectedDelivery() != null) {
            details.append(String.format("- Expected Delivery: %s\n", order.getExpectedDelivery()));
        }
        details.append(String.format("- Payment Status: %s\n", order.getPaymentStatus()));
        details.append(String.format("- Amount: $%.2f", order.getAmount()));

        return details.toString();
    }

    /**
     * Cancels an order if it is in a cancellable state.
     *
     * @param orderId the numeric order ID
     * @return result message
     */
    @Transactional
    public String cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (orderOpt.isEmpty()) {
            return "Cannot cancel. Order not found with ID: " + orderId;
        }

        Order order = orderOpt.get();
        String currentStatus = order.getStatus();

        if ("CANCELLED".equalsIgnoreCase(currentStatus)) {
            return "Order #" + orderId + " is already cancelled.";
        }

        if ("DELIVERED".equalsIgnoreCase(currentStatus) || "SHIPPED".equalsIgnoreCase(currentStatus)) {
            return String.format(
                    "Cannot cancel order #%d because it has already been %s. Please initiate a return request instead.",
                    orderId, currentStatus.toLowerCase());
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        return String.format("Order #%d (%s) has been successfully cancelled.", orderId, order.getProductName());
    }
}
