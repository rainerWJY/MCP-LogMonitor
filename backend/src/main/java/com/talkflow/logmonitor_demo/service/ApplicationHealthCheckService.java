package com.talkflow.logmonitor_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Service for checking application health status
 */
@Service
public class ApplicationHealthCheckService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationHealthCheckService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Random random = new Random();

    /**
     * Check application health status
     * @param applicationName Application name to check
     * @return Health check result
     */
    @Tool(description = "Check application health status")
    public String checkApplicationHealth(String applicationName) {
        logger.info("🔍 Checking health status for application: {}", applicationName);
        
        try {
            // Validate parameters
            if (applicationName == null || applicationName.trim().isEmpty()) {
                return "❌ Application name cannot be empty";
            }
            
            // Generate health check data
            Map<String, Object> healthData = generateHealthCheckData(applicationName.trim());
            
            // Build formatted response
            StringBuilder result = new StringBuilder();
            result.append(String.format("🏥 应用 %s 健康检查报告\n", applicationName));
            result.append("=".repeat(50)).append("\n\n");
            
            // Overall status
            String overallStatus = (String) healthData.get("overallStatus");
            result.append(String.format("📊 整体状态: %s\n", overallStatus));
            result.append(String.format("⏰ 检查时间: %s\n", healthData.get("checkTime")));
            result.append("\n");
            
            // Health check details
            if (healthData.containsKey("healthChecks")) {
                @SuppressWarnings("unchecked")
                var healthChecks = (List<Map<String, Object>>) healthData.get("healthChecks");
                
                result.append("🔍 健康检查详情:\n");
                for (int i = 0; i < healthChecks.size(); i++) {
                    var check = healthChecks.get(i);
                    result.append(String.format("%d. %s\n", i + 1, check.get("checkName")));
                    result.append(String.format("   状态: %s\n", check.get("status")));
                    result.append(String.format("   响应时间: %s\n", check.get("responseTime")));
                    result.append(String.format("   IP地址: %s\n", check.get("ipAddress")));
                    result.append("\n");
                }
            }
            
            // Failed checks summary
            if (healthData.containsKey("failedChecks")) {
                @SuppressWarnings("unchecked")
                var failedChecks = (List<String>) healthData.get("failedChecks");
                
                if (!failedChecks.isEmpty()) {
                    result.append("❌ 失败的检查项:\n");
                    for (String failedCheck : failedChecks) {
                        result.append(String.format("   • %s\n", failedCheck));
                    }
                    result.append("\n");
                }
            }
            
            logger.info("✅ Successfully completed health check for application: {}", applicationName);
            
            return result.toString();
            
        } catch (Exception e) {
            logger.error("❌ Error checking application health: {}", e.getMessage(), e);
            return String.format("❌ 健康检查失败: %s", e.getMessage());
        }
    }

    /**
     * Generate health check data
     */
    private Map<String, Object> generateHealthCheckData(String applicationName) {
        Map<String, Object> result = new HashMap<>();
        
        // Set check time
        result.put("checkTime", LocalDateTime.now().format(formatter));
        
        // Generate health checks based on application
        List<Map<String, Object>> healthChecks = new ArrayList<>();
        List<String> failedChecks = new ArrayList<>();
        
        if ("wmpooc".equals(applicationName)) {
            // wmpooc always fails health checks
            result.put("overallStatus", "🔴 不健康");
            
            // Generate failed health checks
            String[] checkNames = {"HTTP端点检查", "数据库连接检查", "缓存服务检查", "消息队列检查", "文件系统检查"};
            String[] ipAddresses = {"192.168.1.100", "192.168.1.101", "192.168.1.102", "10.0.0.50", "10.0.0.51"};
            
            for (int i = 0; i < 3; i++) { // 3 failed checks
                Map<String, Object> check = new HashMap<>();
                check.put("checkName", checkNames[i]);
                check.put("status", "❌ 失败");
                check.put("responseTime", "超时");
                check.put("ipAddress", ipAddresses[i]);
                healthChecks.add(check);
                failedChecks.add(checkNames[i]);
            }
            
        } else {
            // Other applications are healthy
            result.put("overallStatus", "🟢 健康");
            
            // Generate healthy health checks
            String[] checkNames = {"HTTP端点检查", "数据库连接检查", "缓存服务检查"};
            String[] ipAddresses = {"192.168.1.200", "192.168.1.201", "192.168.1.202"};
            
            for (int i = 0; i < 3; i++) {
                Map<String, Object> check = new HashMap<>();
                check.put("checkName", checkNames[i]);
                check.put("status", "✅ 通过");
                check.put("responseTime", random.nextInt(100) + 20 + "ms"); // 20-120ms
                check.put("ipAddress", ipAddresses[i]);
                healthChecks.add(check);
            }
        }
        
        result.put("healthChecks", healthChecks);
        result.put("failedChecks", failedChecks);
        
        return result;
    }
}
