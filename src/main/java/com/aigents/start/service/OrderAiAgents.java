package com.aigents.start.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface OrderAiAgents {

        // 1. Core Chat with System Prompt & User Prompt
        // @SystemMessage("""
        // You are a polite, helpful customer service AI agent for an e-commerce order
        // management system.
        // Rules:
        // 1. Assist customers with order inquiries, shipping status, tracking, returns,
        // and refunds.
        // 2. If an order number is mentioned, refer to it explicitly.
        // 3. Keep responses concise, clear, and professional.
        // 4. Never invent tracking numbers or credit card details.
        // """)
        @UserMessage("Customer inquiry: {{message}}")
        String chat(String message);

        // 2. Chat with Customer Context (Templatized User Prompt)
        @SystemMessage("""
                        You are a customer support agent. Address the customer politely by name and assist with their order request.
                        """)
        @UserMessage("""
                        Customer Name: {{customerName}}
                        Order ID: {{orderId}}
                        Inquiry: {{message}}
                        """)
        String chatWithContext(
                        @V("customerName") String customerName,
                        @V("orderId") String orderId,
                        @V("message") String message);

        // 3. Query Re-writing / Transformation Agent
        @SystemMessage("""
                        You are an AI query rewriting assistant for an e-commerce customer support
                        system.
                        Your task:
                        - Take an ambiguous, vague, or short user query.
                        - Rewrite it into a complete, clear, and context-rich standalone question
                        optimized for support lookup.
                        - Do NOT answer the question. Only output the rewritten query without quotes
                        or preamble.
                        Example input: "where is it?"
                        Example output: "What is the current shipment and tracking status of my
                        recent order?"
                        """)
        @UserMessage("Original query: {{originalQuery}}")
        String rewriteQuery(@V("originalQuery") String originalQuery);
}
