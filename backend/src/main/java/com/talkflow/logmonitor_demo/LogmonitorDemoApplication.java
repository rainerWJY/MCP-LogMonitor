package com.talkflow.logmonitor_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;

import com.talkflow.logmonitor_demo.service.HelloWorldService;
import com.talkflow.logmonitor_demo.service.AlertMonitorService;

@SpringBootApplication
public class LogmonitorDemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(LogmonitorDemoApplication.class);

    public static void main(String[] args) {
        logger.info("🚀 Starting MCP Alert Monitor Server...");
        logger.info("📡 Transport: WebFlux + STREAMABLE");
        logger.info("⚡ Server Type: ASYNC");
        logger.info("🌐 Port: 8080");

        ApplicationContext context = SpringApplication.run(LogmonitorDemoApplication.class, args);

        logger.info("✅ MCP Alert Monitor Server started successfully!");
        logger.info("📱 Test page available at: http://localhost:8080");
        logger.info("🔌 STREAMABLE endpoint: http://localhost:8080/streamable");
        logger.info("📊 Available services: HelloWorld, AlertMonitor");
        logger.info("🛠️ Available tools: 3");
    }

    @Bean
    public ToolCallbackProvider helloWorldTools(HelloWorldService helloWorldService) {
        logger.info("🔧 Registering HelloWorldService tools with MCP server");
        return MethodToolCallbackProvider.builder()
            .toolObjects(helloWorldService)
            .build();
    }

    @Bean
    public ToolCallbackProvider alertMonitorTools(AlertMonitorService alertMonitorService) {
        logger.info("🚨 Registering AlertMonitorService tools with MCP server");
        return MethodToolCallbackProvider.builder()
            .toolObjects(alertMonitorService)
            .build();
    }
}
