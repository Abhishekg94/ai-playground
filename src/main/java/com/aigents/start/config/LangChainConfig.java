package com.aigents.start.config;

import com.aigents.start.agents.OrderAgent;
import com.aigents.start.agents.RefundAgent;
import com.aigents.start.agents.ResponseAgent;
import com.aigents.start.agents.WorkflowSupportAgent;
import com.aigents.start.listener.DebugAgentListener;
import com.aigents.start.service.OrderAiAgents;
import com.aigents.start.service.OrderSupportAiAgents;
import com.aigents.start.tool.OrderTool;
import com.aigents.start.tool.RefundTool;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LangChainConfig {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model-name}")
    private String modelName;

    @Bean
    public ChatModelListener chatModelListener() {
        return new ChatModelListener() {

            @Override
            public void onRequest(ChatModelRequestContext context) {
                System.out.println("Sending request to Gemini: " + modelName);
            }

            @Override
            public void onResponse(ChatModelResponseContext context) {
                if (context.chatResponse() != null) {
                    System.out.println("Gemini response received");

                    if (context.chatResponse().tokenUsage() != null) {
                        System.out.println("Token Usage: "
                                + context.chatResponse().tokenUsage());
                    }
                }
            }

            @Override
            public void onError(ChatModelErrorContext context) {
                System.err.println("Gemini API Error: "
                        + context.error().getMessage());
            }
        };
    }

    @Bean
    public ChatModel chatModel(ChatModelListener listener) {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.7)
                .maxOutputTokens(2048)
                .returnThinking(true)
                .sendThinking(true)
                .listeners(List.of(listener))
                .build();
    }

    @Bean
    public OrderAiAgents orderAiAgents(ChatModel model) {
        return AiServices.builder(OrderAiAgents.class)
                .chatModel(model)
                .build();
    }

    @Bean
    public OrderSupportAiAgents orderSupportAiAgents(
            ChatModel model,
            OrderTool orderTool) {

        return AiServices.builder(OrderSupportAiAgents.class)
                .chatModel(model)
                .tools(orderTool)
                .build();
    }

    @Bean
    public OrderAgent orderAgent(
            ChatModel model,
            OrderTool orderTool) {

        return AgenticServices
                .agentBuilder(OrderAgent.class)
                .chatModel(model)
                .tools(orderTool)
                .outputKey("orderDetails")
                .build();
    }

    @Bean
    public RefundAgent refundAgent(
            ChatModel model,
            RefundTool refundTool) {

        return AgenticServices
                .agentBuilder(RefundAgent.class)
                .chatModel(model)
                .tools(refundTool)
                .outputKey("refundDecision")
                .build();
    }

    @Bean
    public ResponseAgent responseAgent(
            ChatModel model) {

        return AgenticServices.agentBuilder(ResponseAgent.class)
                .chatModel(model)
                .outputKey("finalResponse")
                .build();
    }

    @Bean
    public WorkflowSupportAgent workflowSupportAgent(
            OrderAgent orderAgent,
            RefundAgent refundAgent,
            ResponseAgent responseAgent) {
        return AgenticServices.sequenceBuilder(WorkflowSupportAgent.class)
                .subAgents(orderAgent, refundAgent, responseAgent)
                .outputKey("finalResponse")
                .listener(new DebugAgentListener())
                .build();
    }
}