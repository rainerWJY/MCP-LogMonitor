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
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.StringBuilder;

@Service
public class AlertMonitorService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertMonitorService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Random random = new Random();
    
    // Alert type configuration
    private static final String[] ALL_ALERT_TYPES = {"CPU过载", "服务不可用"};
    private String currentAlertType = "CPU过载"; // Default selection
    
    // In-memory storage for alert data
    private Map<String, Object> cachedAlertData = new HashMap<>();
    private LocalDateTime lastRefreshTime = null;

    /**
     * Get detailed alert information as JSON structure
     * @param severity Alert severity level (CRITICAL, HIGH, MEDIUM, LOW)
     * @return Detailed alert information as Map structure
     */
    public Map<String, Object> getDetailedAlertInfoAsJson(String severity) {
        String application = "wmpooc"; // Fixed application name
        logger.info("🔍 Getting detailed alert info as JSON - severity: {}, application: {}", 
                   severity, application);
        
        try {
            // Check if we have cached data, if not generate it
            if (cachedAlertData.isEmpty()) {
                logger.info("⚠️ Cache is empty, triggering data refresh...");
                refreshAlertData();
            }
            
            // Get alert data from cache and filter by severity if specified
            logger.info("🔍 Filtering alert data by severity: {}", severity);
            Map<String, Object> alertData = filterAlertDataBySeverity(cachedAlertData, severity);
            
            int filteredCount = alertData.containsKey("alerts") ? ((java.util.List<?>) alertData.get("alerts")).size() : 0;
            logger.info("📊 Filtered result: {} alerts match severity '{}'", filteredCount, severity);
            
            // Add metadata to the response
            Map<String, Object> result = new HashMap<>();
            result.put("alerts", alertData.get("alerts"));
            result.put("totalCount", filteredCount);
            result.put("severity", severity);
            result.put("application", application);
            result.put("queryTime", LocalDateTime.now().format(formatter));
            result.put("cacheTime", lastRefreshTime != null ? lastRefreshTime.format(formatter) : null);
            
            logger.info("✅ Successfully retrieved alert info as JSON for {} alerts", filteredCount);
            
            return result;
            
        } catch (Exception e) {
            logger.error("❌ Error getting alert info as JSON: {}", e.getMessage(), e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", e.getMessage());
            errorResult.put("severity", severity);
            return errorResult;
        }
    }

    /**
     * Get detailed alert information
     * @param severity Alert severity level (CRITICAL, HIGH, MEDIUM, LOW)
     * @return Detailed alert information
     */
    @Tool(description = "Get detailed alert information filtered by severity level")
    public String getDetailedAlertInfo(@ToolParam(description = "Alert severity level to filter by (CRITICAL, HIGH, MEDIUM, LOW)", required = true) String severity) {
        String application = "wmpooc"; // Fixed application name
        logger.info("🔍 Getting detailed alert info - severity: {}, application: {}", 
                   severity, application);
        logger.info("📊 Cache status: {} alerts available", 
                   cachedAlertData.containsKey("alerts") ? ((java.util.List<?>) cachedAlertData.get("alerts")).size() : 0);
        
        try {
            // Check if we have cached data, if not generate it
            if (cachedAlertData.isEmpty()) {
                logger.info("⚠️ Cache is empty, triggering data refresh...");
                refreshAlertData();
            } else {
                logger.info("✅ Using cached data for response");
            }
            
            // Get alert data from cache and filter by severity if specified
            logger.info("🔍 Filtering alert data by severity: {}", severity);
            Map<String, Object> alertData = filterAlertDataBySeverity(cachedAlertData, severity);
            
            int filteredCount = alertData.containsKey("alerts") ? ((java.util.List<?>) alertData.get("alerts")).size() : 0;
            logger.info("📊 Filtered result: {} alerts match severity '{}'", filteredCount, severity);
            
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
            
            // Add cache information
            if (lastRefreshTime != null) {
                result.append(String.format("💾 数据缓存时间: %s\n", lastRefreshTime.format(formatter)));
            }
            
            logger.info("✅ Successfully retrieved alert info for {} alerts", 
                       alertData.containsKey("alerts") ? ((java.util.List<?>) alertData.get("alerts")).size() : 0);
            
            return result.toString();
            
        } catch (Exception e) {
            logger.error("❌ Error getting alert info: {}", e.getMessage(), e);
            return String.format("❌ 获取告警信息失败: %s", e.getMessage());
        }
    }

    /**
     * Get current selected alert type
     * @return Current alert type
     */
    public String getCurrentAlertType() {
        return currentAlertType;
    }

    /**
     * Set alert type (CPU过载 or 服务不可用)
     * @param alertType The alert type to set
     * @return Current alert type
     */
    public String setAlertType(String alertType) {
        logger.info("🔄 Setting alert type from '{}' to '{}'", currentAlertType, alertType);
        
        if (java.util.Arrays.asList(ALL_ALERT_TYPES).contains(alertType)) {
            String previousType = currentAlertType;
            currentAlertType = alertType;
            logger.info("✅ Alert type changed successfully: {} -> {}", previousType, currentAlertType);
            logger.info("💡 Note: New alerts will be generated with type '{}'", currentAlertType);
            return currentAlertType;
        } else {
            logger.error("❌ Invalid alert type '{}'. Must be one of: {}", alertType, String.join(", ", ALL_ALERT_TYPES));
            throw new IllegalArgumentException("Invalid alert type. Must be 'CPU过载' or '服务不可用'");
        }
    }

    /**
     * Get all available alert types
     * @return List of all available alert types
     */
    public String[] getAvailableAlertTypes() {
        return ALL_ALERT_TYPES.clone();
    }

    /**
     * Refresh alert data by generating new mock data and storing it in memory
     */
    public void refreshAlertData() {
        logger.info("🔄 Starting alert data refresh process...");
        logger.info("📊 Current alert type: {}", currentAlertType);
        logger.info("⏰ Last refresh time: {}", lastRefreshTime != null ? lastRefreshTime.format(formatter) : "Never");
        
        try {
            logger.info("🔧 Generating new mock alert data for all severities...");
            // Generate new mock alert data for all severities
            Map<String, Object> newAlertData = generateMockAlertDataForAllSeverities();
            
            int alertCount = newAlertData.containsKey("alerts") ? ((java.util.List<?>) newAlertData.get("alerts")).size() : 0;
            logger.info("📈 Generated {} new alerts", alertCount);
            logger.info("🎯 Target: 20-28 alerts (5-7 per severity level)");
            
            // Update cached data
            logger.info("💾 Updating cached alert data...");
            cachedAlertData = newAlertData;
            lastRefreshTime = LocalDateTime.now();
            
            logger.info("✅ Alert data refresh completed successfully");
            logger.info("📊 Total alerts in cache: {}", alertCount);
            logger.info("⏰ New refresh time: {}", lastRefreshTime.format(formatter));
            
        } catch (Exception e) {
            logger.error("❌ Error refreshing alert data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to refresh alert data", e);
        }
    }

    /**
     * Get last refresh time
     * @return Last refresh time
     */
    public LocalDateTime getLastRefreshTime() {
        return lastRefreshTime;
    }

    /**
     * Generate mock alert data for all severities
     */
    private Map<String, Object> generateMockAlertDataForAllSeverities() {
        logger.info("🔧 Starting mock alert data generation...");
        logger.info("🎯 Target alert type: {}", currentAlertType);
        
        Map<String, Object> result = new HashMap<>();
        java.util.List<Map<String, Object>> allAlerts = new java.util.ArrayList<>();
        
        String[] severities = {"CRITICAL", "HIGH", "MEDIUM", "LOW"};
        String[] applications = {"wmpooc", "order-service"}; // Only keep wmpooc and order-service
        String[] statuses = {"ACTIVE", "ACKNOWLEDGED", "RESOLVED"};
        
        logger.info("📋 Available severities: {}", String.join(", ", severities));
        logger.info("🏗️ Available applications: {}", String.join(", ", applications));
        logger.info("📊 Available statuses: {}", String.join(", ", statuses));
        
        int totalAlerts = 0;
        
        // Generate alerts for each severity level
        for (String severityLevel : severities) {
            int alertCount = random.nextInt(3) + 5; // 5-7 alerts per severity (minimum 5)
            logger.info("🔍 Generating {} alerts for severity level: {}", alertCount, severityLevel);
            
            for (int i = 0; i < alertCount; i++) {
                Map<String, Object> alert = new HashMap<>();
                
                // Generate alert ID
                String generatedAlertId = "ALERT-" + System.currentTimeMillis() + "-" + severityLevel + "-" + i;
                alert.put("alertId", generatedAlertId);
                
                // Generate application name
                String generatedApp = applications[random.nextInt(applications.length)];
                alert.put("application", generatedApp);
                
                // Set severity
                alert.put("severity", severityLevel);
                
                // Use current selected alert type
                alert.put("alertType", currentAlertType);
                
                // Generate alert title and description
                alert.put("title", "系统性能异常告警");
                alert.put("description", "检测到系统性能指标异常，需要及时处理");
                
                // Generate timestamp
                LocalDateTime alertTime = LocalDateTime.now().minusMinutes(random.nextInt(60));
                alert.put("timestamp", alertTime.format(formatter));
                
                // Generate status
                alert.put("status", statuses[random.nextInt(statuses.length)]);
                
                // Generate metrics data based on current alert type
                Map<String, Object> metrics = new HashMap<>();
                
                if ("CPU过载".equals(currentAlertType)) {
                    // For CPU overload: CPU usage must be between 80-100%
                    metrics.put("cpuUsage", random.nextInt(21) + 80); // 80-100%
                    metrics.put("memoryUsage", random.nextInt(30) + 70); // 70-100%
                    metrics.put("diskUsage", random.nextInt(20) + 80); // 80-100%
                    metrics.put("networkLatency", random.nextInt(100) + 50); // 50-150ms
                } else if ("服务不可用".equals(currentAlertType)) {
                    // For service unavailable: network latency must be 300ms or above
                    metrics.put("cpuUsage", random.nextInt(40) + 60); // 60-100%
                    metrics.put("memoryUsage", random.nextInt(30) + 70); // 70-100%
                    metrics.put("diskUsage", random.nextInt(20) + 80); // 80-100%
                    metrics.put("networkLatency", random.nextInt(700) + 300); // 300-1000ms
                }
                
                alert.put("metrics", metrics);
                allAlerts.add(alert);
                totalAlerts++;
                
                logger.debug("📝 Generated alert: ID={}, Severity={}, Type={}, App={}", 
                           generatedAlertId, severityLevel, currentAlertType, generatedApp);
            }
        }
        
                    logger.info("📊 Data generation completed. Total alerts generated: {}", totalAlerts);
            logger.info("📈 Expected range: 20-28 alerts (5-7 per severity level)");
            logger.info("💾 Storing generated data in result map...");
            
            result.put("alerts", allAlerts);
            return result;
    }

    /**
     * Filter alert data by severity
     */
    private Map<String, Object> filterAlertDataBySeverity(Map<String, Object> alertData, String severity) {
        logger.info("🔍 Starting severity filtering process...");
        logger.info("🎯 Filter criteria: severity = '{}'", severity);
        
        if (severity == null || severity.trim().isEmpty()) {
            logger.info("ℹ️ No severity filter applied, returning all data");
            return alertData; // Return all data if no severity filter
        }
        
        Map<String, Object> filteredData = new HashMap<>();
        filteredData.putAll(alertData);
        
        if (alertData.containsKey("alerts")) {
            @SuppressWarnings("unchecked")
            var allAlerts = (java.util.List<Map<String, Object>>) alertData.get("alerts");
            
            logger.info("📊 Total alerts before filtering: {}", allAlerts.size());
            
            java.util.List<Map<String, Object>> filteredAlerts = allAlerts.stream()
                .filter(alert -> severity.equals(alert.get("severity")))
                .collect(java.util.stream.Collectors.toList());
            
            logger.info("✅ Filtering completed. {} alerts match severity '{}'", filteredAlerts.size(), severity);
            
            filteredData.put("alerts", filteredAlerts);
        } else {
            logger.warn("⚠️ No alerts found in alert data");
        }
        
        return filteredData;
    }

    /**
     * Generate mock alert data
     */
    private Map<String, Object> generateMockAlertData(String alertId, String severity, String application) {
        Map<String, Object> result = new HashMap<>();
        java.util.List<Map<String, Object>> alerts = new java.util.ArrayList<>();
        
        // 模拟告警数据
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
            
            // Use current selected alert type
            alert.put("alertType", currentAlertType);
            
            // Generate alert title
            alert.put("title", "系统性能异常告警");
            
            // Generate alert description
            alert.put("description", "检测到系统性能指标异常，需要及时处理");
            
            // Generate timestamp
            LocalDateTime alertTime = LocalDateTime.now().minusMinutes(random.nextInt(60));
            alert.put("timestamp", alertTime.format(formatter));
            
            // Generate status
            alert.put("status", statuses[random.nextInt(statuses.length)]);
            
            // Generate metrics data based on current alert type
            Map<String, Object> metrics = new HashMap<>();
            
            if ("CPU过载".equals(currentAlertType)) {
                // For CPU overload: CPU usage must be between 80-100%
                metrics.put("cpuUsage", random.nextInt(21) + 80); // 80-100%
                metrics.put("memoryUsage", random.nextInt(30) + 70); // 70-100%
                metrics.put("diskUsage", random.nextInt(20) + 80); // 80-100%
                metrics.put("networkLatency", random.nextInt(100) + 50); // 50-150ms
            } else if ("服务不可用".equals(currentAlertType)) {
                // For service unavailable: network latency must be 300ms or above
                metrics.put("cpuUsage", random.nextInt(40) + 60); // 60-100%
                metrics.put("memoryUsage", random.nextInt(30) + 70); // 70-100%
                metrics.put("diskUsage", random.nextInt(20) + 80); // 80-100%
                metrics.put("networkLatency", random.nextInt(700) + 300); // 300-1000ms
            }
            
            alerts.add(alert);
        }
        
        result.put("alerts", alerts);
        return result;
    }
}