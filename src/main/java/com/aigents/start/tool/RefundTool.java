package com.aigents.start.tool;

import org.springframework.stereotype.Component;

import com.aigents.start.service.RefundService;

import dev.langchain4j.agent.tool.Tool;

@Component
public class RefundTool {

    private RefundService refundService;

    RefundTool(RefundService refundService) {
        this.refundService = refundService;
    }

    @Tool("Check wether a order is eligible for a refund based on order status")
    public String refundEligibility(String orderStatus) {
        System.out.println("\n>>> REFUDN TOOL CALLED -> " + orderStatus);
        return refundService.checkEligibility(orderStatus).toString();
    }
}
