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
package com.talkflow.logmonitor_demo.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HelloWorldService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Get a personalized hello world message
     * @param name The name to greet
     * @return A personalized hello world message
     */
    @Tool(description = "Get a personalized hello world message with current timestamp")
    public String getHelloWorld(String name) {
        String greeting = name != null && !name.trim().isEmpty() ? name.trim() : "World";
        String timestamp = LocalDateTime.now().format(formatter);
        
        return String.format("""
            Hello, %s! 👋
            
            Welcome to the MCP Hello World Server!
            Current time: %s
            
            This is a simple demonstration of Spring AI MCP server capabilities.
            """, greeting, timestamp);
    }

    /**
     * Get server information
     * @return Server status and information
     */
    @Tool(description = "Get server information and status")
    public String getServerInfo() {
        String timestamp = LocalDateTime.now().format(formatter);
        
        return String.format("""
            🚀 MCP Hello World Server
            
            Status: Running
            Current time: %s
            Java version: %s
            OS: %s
            
            Available tools:
            - getHelloWorld(name): Get personalized greeting
            - getServerInfo(): Get server information
            """, 
            timestamp,
            System.getProperty("java.version"),
            System.getProperty("os.name"));
    }

    public static void main(String[] args) {
        HelloWorldService service = new HelloWorldService();
        System.out.println(service.getHelloWorld("Developer"));
        System.out.println(service.getServerInfo());
    }
}
