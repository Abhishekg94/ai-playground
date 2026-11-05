package com.aigents.start.config;

import com.aigents.start.service.OrderAiAgents;
import com.aigents.start.service.OrderSupportAiAgents;
import com.aigents.start.tool.OrderTool;
import dev.langchain4j.model.chat.ChatLanguageModel;
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
                if (context.response() != null) {
                    System.out.println("Gemini response received");

                    if (context.response().tokenUsage() != null) {
                        System.out.println("Token Usage: "
                                + context.response().tokenUsage());
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
    public ChatLanguageModel chatLanguageModel(
            ChatModelListener listener) {

        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.7)
                .maxOutputTokens(2048)
                .maxRetries(1)
                .listeners(List.of(listener))
                .build();
    }

    @Bean
    public OrderAiAgents orderAiAgents(
            ChatLanguageModel model) {

        return AiServices.builder(OrderAiAgents.class)
                .chatLanguageModel(model)
                .build();
    }

    @Bean
    public OrderSupportAiAgents orderSupportAiAgents(
            ChatLanguageModel model,
            OrderTool orderTool) {

        return AiServices.builder(OrderSupportAiAgents.class)
                .chatLanguageModel(model)
                .tools(orderTool)
                .build();
    }
}