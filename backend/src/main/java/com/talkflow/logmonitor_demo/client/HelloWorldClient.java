/*
 * Copyright 2024 - 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.talkflow.logmonitor_demo.client;

import java.util.Map;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.WebClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

import org.springframework.web.reactive.function.client.WebClient;

/**
 * Test client for Hello World MCP Server
 */
public class HelloWorldClient {

    public static void main(String[] args) {
        System.out.println("🚀 Starting Hello World MCP Client Test...");
        
        var transport = WebClientStreamableHttpTransport.builder(WebClient.builder().baseUrl("http://localhost:8080/")).endpoint("streamable")
                .build();

        var client = McpClient.sync(transport).build();

        try {
            System.out.println("📡 Initializing MCP client...");
            client.initialize();

            System.out.println("🏓 Pinging server...");
            client.ping();
            System.out.println("✅ Server ping successful!");

            // List available tools
            System.out.println("\n📋 Listing available tools...");
            ListToolsResult toolsList = client.listTools();
            System.out.println("Available Tools = " + toolsList);

            // Test getHelloWorld tool
            System.out.println("\n👋 Testing getHelloWorld tool...");
            CallToolResult helloResult = client.callTool(new CallToolRequest("getHelloWorld",
                    Map.of("name", "Developer")));
            System.out.println("Hello World Response: " + helloResult);

            // Test getServerInfo tool
            System.out.println("\nℹ️ Testing getServerInfo tool...");
            CallToolResult serverInfoResult = client.callTool(new CallToolRequest("getServerInfo", Map.of()));
            System.out.println("Server Info Response: " + serverInfoResult);

            System.out.println("\n✅ All tests completed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error during testing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("🔌 Closing client...");
            client.closeGracefully();
        }
    }
}
