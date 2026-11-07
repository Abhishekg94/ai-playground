package com.aigents.start.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface OrderAgent {

    @UserMessage("""
                The customer request is:
                {{request}}
                Extract the order ID from request.
                Use the available tool to get the real order details.
                Return the complete order details only.

            """)
    @Agent(outputKey = "orderDetails", description = "Fetches current order details")
    String checkOrder(@V("request") String request);
}
