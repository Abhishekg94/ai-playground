package com.aigents.start.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResponseAgent {

    @UserMessage("""

                   Original  Customer request :
                    {{request}}

                    Order Details :
                    {{orderDetails}}

                    Refund decision :
                    {{refundDecision}}

                    Create a short, clear and helpful response for the customer.

                    Do not invent any information.
            """)

    @Agent(outputKey = "response", description = "Create a response")
    String creeateRsponse(@V("request") String request, @V("orderDetails") String orderDetail,
            @V("refundDecision") String refundDecision);
}
