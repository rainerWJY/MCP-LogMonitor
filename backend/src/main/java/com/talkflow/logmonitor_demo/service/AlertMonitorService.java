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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AlertMonitorService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertMonitorService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Random random = new Random();

    /**
     * Get detailed alert information
     * @param severity Alert severity level (CRITICAL, HIGH, MEDIUM, LOW)
     * @return Detailed alert information
     */
    @Tool(description = "Get detailed alert information filtered by severity level")
    public String getDetailedAlertInfo(String severity) {
        String application = "wmpooc"; // Fixed application name
        logger.info("🔍 Getting detailed alert info - severity: {}, application: {}", 
                   severity, application);
        
        try {
            // Generate mock alert data
            Map<String, Object> alertData = generateMockAlertData(null, severity, application);
            
            // 构建详细的告警信息
            StringBuilder result = new StringBuilder();
            result.append("🚨 详细告警信息\n");
            result.append("=".repeat(50)).append("\n\n");
            
            if (alertData.containsKey("alerts")) {
                @SuppressWarnings("unchecked")
                var alerts = (java.util.List<Map<String, Object>>) alertData.get("alerts");
                
                for (int i = 0; i < alerts.size(); i++) {
                    var alert = alerts.get(i);
                    result.append(String.format("📊 告警 #%d\n", i + 1));
                    result.append(String.format("   告警ID: %s\n", alert.get("alertId")));
                    result.append(String.format("   应用名称: %s\n", alert.get("application")));
                    result.append(String.format("   告警级别: %s\n", alert.get("severity")));
                    result.append(String.format("   告警类型: %s\n", alert.get("alertType")));
                    result.append(String.format("   告警标题: %s\n", alert.get("title")));
                    result.append(String.format("   告警描述: %s\n", alert.get("description")));
                    result.append(String.format("   发生时间: %s\n", alert.get("timestamp")));
                    result.append(String.format("   状态: %s\n", alert.get("status")));
                    
                    if (alert.containsKey("metrics")) {
                        @SuppressWarnings("unchecked")
                        var metrics = (Map<String, Object>) alert.get("metrics");
                        result.append("   📈 相关指标:\n");
                        result.append(String.format("      CPU使用率: %s%%\n", metrics.get("cpuUsage")));
                        result.append(String.format("      内存使用率: %s%%\n", metrics.get("memoryUsage")));
                        result.append(String.format("      磁盘使用率: %s%%\n", metrics.get("diskUsage")));
                        result.append(String.format("      网络延迟: %sms\n", metrics.get("networkLatency")));
                    }
                    
                    if (alert.containsKey("recommendations")) {
                        @SuppressWarnings("unchecked")
                        var recommendations = (java.util.List<String>) alert.get("recommendations");
                        result.append("   💡 处理建议:\n");
                        for (String rec : recommendations) {
                            result.append(String.format("      • %s\n", rec));
                        }
                    }
                    
                    result.append("\n");
                }
                
                result.append(String.format("📋 总计: %d 条告警\n", alerts.size()));
            } else {
                result.append("✅ 未找到匹配的告警信息\n");
            }
            
            result.append(String.format("\n⏰ 查询时间: %s\n", LocalDateTime.now().format(formatter)));
            
            logger.info("✅ Successfully retrieved alert info for {} alerts", 
                       alertData.containsKey("alerts") ? ((java.util.List<?>) alertData.get("alerts")).size() : 0);
            
            return result.toString();
            
        } catch (Exception e) {
            logger.error("❌ Error getting alert info: {}", e.getMessage(), e);
            return String.format("❌ 获取告警信息失败: %s", e.getMessage());
        }
    }

    /**
     * 生成模拟告警数据
     */
    private Map<String, Object> generateMockAlertData(String alertId, String severity, String application) {
        Map<String, Object> result = new HashMap<>();
        java.util.List<Map<String, Object>> alerts = new java.util.ArrayList<>();
        
        // 模拟告警数据
        String[] alertTypes = {"CPU过载", "服务不可用"};
        String[] severities = {"CRITICAL", "HIGH", "MEDIUM", "LOW"};
        String[] applications = {"user-service", "order-service", "payment-service", "inventory-service", "notification-service", "wmpooc"};
        String[] statuses = {"ACTIVE", "ACKNOWLEDGED", "RESOLVED"};
        
        int alertCount = random.nextInt(5) + 1; // 1-5条告警
        
        for (int i = 0; i < alertCount; i++) {
            Map<String, Object> alert = new HashMap<>();
            
            // 生成告警ID
            String generatedAlertId = alertId != null ? alertId : "ALERT-" + System.currentTimeMillis() + "-" + i;
            alert.put("alertId", generatedAlertId);
            
            // 生成应用名称
            String generatedApp = application != null ? application : applications[random.nextInt(applications.length)];
            alert.put("application", generatedApp);
            
            // 生成告警级别
            String generatedSeverity = severity != null ? severity : severities[random.nextInt(severities.length)];
            alert.put("severity", generatedSeverity);
            
            // Generate alert type
            String alertType = alertTypes[random.nextInt(alertTypes.length)];
            alert.put("alertType", alertType);
            
            // Generate alert title
            alert.put("title", "系统性能异常告警");
            
            // Generate alert description
            alert.put("description", "检测到系统性能指标异常，需要及时处理");
            
            // Generate timestamp
            LocalDateTime alertTime = LocalDateTime.now().minusMinutes(random.nextInt(60));
            alert.put("timestamp", alertTime.format(formatter));
            
            // Generate status
            alert.put("status", statuses[random.nextInt(statuses.length)]);
            
            // Generate metrics data based on alert type
            Map<String, Object> metrics = new HashMap<>();
            
            if ("CPU过载".equals(alertType)) {
                // For CPU overload: CPU usage must be between 80-100%
                metrics.put("cpuUsage", random.nextInt(21) + 80); // 80-100%
                metrics.put("memoryUsage", random.nextInt(30) + 70); // 70-100%
                metrics.put("diskUsage", random.nextInt(20) + 80); // 80-100%
                metrics.put("networkLatency", random.nextInt(100) + 50); // 50-150ms
            } else if ("服务不可用".equals(alertType)) {
                // For service unavailable: network latency must be 300ms or above
                metrics.put("cpuUsage", random.nextInt(40) + 60); // 60-100%
                metrics.put("memoryUsage", random.nextInt(30) + 70); // 70-100%
                metrics.put("diskUsage", random.nextInt(20) + 80); // 80-100%
                metrics.put("networkLatency", random.nextInt(700) + 300); // 300-1000ms
            }
            
            alert.put("metrics", metrics);
            
            // 生成处理建议
            java.util.List<String> recommendations = new java.util.ArrayList<>();
            recommendations.add("检查应用日志，分析异常原因");
            recommendations.add("重启相关服务实例");
            recommendations.add("增加资源配置或优化代码");
            recommendations.add("联系开发团队进行代码审查");
            alert.put("recommendations", recommendations);
            
            alerts.add(alert);
        }
        
        result.put("alerts", alerts);
        return result;
    }
}