package com.aigents.start.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface RefundAgent {

    @UserMessage("""
                    Cusotmer request :
                    {{request}}

                    Order information :
                    {{orderDetails}}

                    Determine the current order status from the order information.

                    Use the refund toll to check wether this order is eligible for refund.

                    Return only the refund decision and policy explanation.
            """)
    @Agent(outputKey = "refunDecision", description = "Check refund eligibiliy")
    String checkRefund(@V("request") String request, @V("orderDetails") String orderDetails);

}
