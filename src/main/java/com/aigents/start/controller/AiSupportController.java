package com.aigents.start.controller;

import com.aigents.start.service.OrderAiAgents;
import com.aigents.start.service.OrderService;
import com.aigents.start.service.OrderSupportAiAgents;
import com.aigents.start.agents.WorkflowSupportAgent;

import opennlp.tools.stemmer.snowball.finnishStemmer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai-support")
public class AiSupportController {

    private final OrderAiAgents orderAiAgents;
    private final OrderSupportAiAgents orderSupportAiAgents;
    private final OrderService orderService;
    private final WorkflowSupportAgent workflowSupportAgent;

    public AiSupportController(
            OrderAiAgents orderAiAgents,
            OrderSupportAiAgents orderSupportAiAgents,
            OrderService orderService,
            WorkflowSupportAgent workflowSupportAgent) {
        this.orderAiAgents = orderAiAgents;
        this.orderSupportAiAgents = orderSupportAiAgents;
        this.orderService = orderService;
        this.workflowSupportAgent = workflowSupportAgent;
    }

    /**
     * Direct database lookup for an order by its ID.
     * Example: GET /api/ai-support/get-order?orderId=1
     */
    @GetMapping("/get-order")
    public String getOrder(@RequestParam Long orderId) {
        return orderService.getOrderStatus(orderId);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return orderAiAgents.chat(message);
    }

    @GetMapping("/order")
    public String order(@RequestParam String message) {
        return orderSupportAiAgents.chat(message);
    }

    @GetMapping("/workflow-chat")
    public String workFlowChat(@RequestParam String message) {
        return workflowSupportAgent.handle(message);
    }

    @GetMapping("/chat-context")
    public String chatWithContext(
            @RequestParam String customerName,
            @RequestParam String orderId,
            @RequestParam String message) {
        return orderAiAgents.chatWithContext(customerName, orderId, message);
    }

    @GetMapping("/rewrite")
    public Map<String, String> rewriteQuery(@RequestParam String query) {
        String rewritten = orderAiAgents.rewriteQuery(query);
        return Map.of(
                "originalQuery", query,
                "rewrittenQuery", rewritten);
    }

    @GetMapping("/smart-chat")
    public Map<String, String> smartChat(@RequestParam String message) {
        String rewrittenQuery = orderAiAgents.rewriteQuery(message);
        String response = orderAiAgents.chat(rewrittenQuery);
        return Map.of(
                "originalMessage", message,
                "rewrittenQuery", rewrittenQuery,
                "response", response);
    }
}
