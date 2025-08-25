package com.talkflow.logmonitor_demo.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

import org.springframework.context.annotation.Bean;
import jakarta.annotation.PostConstruct;

@Configuration
public class McpServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(McpServerConfig.class);

    @PostConstruct
    public void init() {
        logger.info("🚀 MCP Server Configuration initialized");
    }

    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> myResources() {
        var systemInfoResource = new McpSchema.Resource(
                "system://info",
                "System Information",
                "Provides current system information",
                "application/json",null);

        var resourceSpecification = new McpServerFeatures.SyncResourceSpecification(
                systemInfoResource,
                (exchange, request) -> {
                    try {
                        var systemInfo = Map.of(
                                "timestamp", System.currentTimeMillis(),
                                "javaVersion", System.getProperty("java.version"),
                                "osName", System.getProperty("os.name"),
                                "availableProcessors", Runtime.getRuntime().availableProcessors());

                        String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);
                        return new McpSchema.ReadResourceResult(
                                List.of(new McpSchema.TextResourceContents(
                                        request.uri(),
                                        "application/json",
                                        jsonContent)));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to generate system info", e);
                    }
                });

        return List.of(resourceSpecification);
    }
    // Spring AI会自动检测StreamingHelloWorldService中的方法
    // 并将返回Flux的方法转换为异步MCP工具
}
