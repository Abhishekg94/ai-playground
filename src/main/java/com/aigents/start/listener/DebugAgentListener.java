package com.aigents.start.listener;

import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.agentic.observability.AgentResponse;
import dev.langchain4j.agentic.scope.AgenticScope;

public class DebugAgentListener implements AgentListener {

    @Override
    public void afterAgenticScopeCreated(AgenticScope scope) {
        System.out.print("================SCOPE CREATED===================");
        System.out.print(scope);
    }

    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        System.out.println("\n================AGENT STARTE=============");
        System.out.println(request);
    }

    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("\n=============AGENT END======================");
        System.out.println(response);
        System.out.println("SCOPE : " + response.agenticScope());
    }

    @Override
    public void beforeAgenticScopeDestroyed(AgenticScope scope) {
        System.out.println("\n===========FINAL AGENT SCOPE=====================");
        System.out.println(scope);
    }

    @Override
    public boolean inheritedBySubagents() {
        return true;
    }

}
