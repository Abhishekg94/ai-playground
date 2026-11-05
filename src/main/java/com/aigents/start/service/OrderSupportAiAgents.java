package com.aigents.start.service;

import dev.langchain4j.service.SystemMessage;

public interface OrderSupportAiAgents {

    @SystemMessage("""
            You are a polite, helpful e-commerce customer support assistant.

            When a customer asks about an order or refund/cancellation,
            ALWAYS use the available tools to look up the real order information.
            Do not guess or assume order status.
            Keep your response concise, friendly, and helpful.
            """)
    String chat(String message);
}
