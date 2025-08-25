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
 * Service for querying application CPU usage history
 */
@Service
public class ApplicationCpuHistoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCpuHistoryService.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Random random = new Random();

    /**
     * Query CPU usage history for an application at the same time over the past n days
     * @param applicationName Application name to query
     * @param days Number of days to look back
     * @return CPU usage history data
     */
    @Tool(description = "Query CPU usage history for an application at the same time over the past n days")
    public String queryCpuUsageHistory(String applicationName, int days) {
        logger.info("🔍 Querying CPU usage history for application: {} over {} days", applicationName, days);
        
        try {
            // Validate parameters
            if (applicationName == null || applicationName.trim().isEmpty()) {
                return "❌ Application name cannot be empty";
            }
            
            if (days <= 0 || days > 30) {
                return "❌ Days must be between 1 and 30";
            }
            
            // Generate mock CPU usage history data
            Map<String, Object> historyData = generateMockCpuHistoryData(applicationName.trim(), days);
            
            // Build formatted response
            StringBuilder result = new StringBuilder();
            result.append(String.format("📊 应用 %s 最近 %d 天同一时刻CPU使用率历史\n", applicationName, days));
            result.append("=".repeat(60)).append("\n\n");
            
            if (historyData.containsKey("history")) {
                @SuppressWarnings("unchecked")
                var history = (List<Map<String, Object>>) historyData.get("history");
                
                for (int i = 0; i < history.size(); i++) {
                    var record = history.get(i);
                    result.append(String.format("📅 第 %d 天 (%s)\n", i + 1, record.get("date")));
                    result.append(String.format("   ⏰ 时间: %s\n", record.get("time")));
                    result.append(String.format("   💻 CPU使用率: %s%%\n", record.get("cpuUsage")));
                    result.append(String.format("   📈 趋势: %s\n", record.get("trend")));
                    result.append(String.format("   🏷️  状态: %s\n", record.get("status")));
                    
                    if (record.containsKey("details")) {
                        @SuppressWarnings("unchecked")
                        var details = (Map<String, Object>) record.get("details");
                        result.append("   📋 详细信息:\n");
                        result.append(String.format("      内存使用率: %s%%\n", details.get("memoryUsage")));
                        result.append(String.format("      磁盘使用率: %s%%\n", details.get("diskUsage")));
                        result.append(String.format("      网络延迟: %sms\n", details.get("networkLatency")));
                    }
                    
                    result.append("\n");
                }
                
                result.append(String.format("📋 总计: %d 天的数据\n", history.size()));
                
                // Add summary statistics
                if (historyData.containsKey("summary")) {
                    @SuppressWarnings("unchecked")
                    var summary = (Map<String, Object>) historyData.get("summary");
                    result.append("\n📊 统计摘要\n");
                    result.append("-".repeat(30)).append("\n");
                    result.append(String.format("   平均CPU使用率: %.1f%%\n", summary.get("avgCpuUsage")));
                    result.append(String.format("   最高CPU使用率: %s%%\n", summary.get("maxCpuUsage")));
                    result.append(String.format("   最低CPU使用率: %s%%\n", summary.get("minCpuUsage")));
                    result.append(String.format("   波动范围: %.1f%%\n", summary.get("variance")));
                }
            } else {
                result.append("✅ 未找到匹配的历史数据\n");
            }
            
            result.append(String.format("\n⏰ 查询时间: %s\n", LocalDateTime.now().format(formatter)));
            
            logger.info("✅ Successfully retrieved CPU usage history for {} days", days);
            
            return result.toString();
            
        } catch (Exception e) {
            logger.error("❌ Error querying CPU usage history: {}", e.getMessage(), e);
            return String.format("❌ 查询CPU使用率历史失败: %s", e.getMessage());
        }
    }

    /**
     * Generate mock CPU usage history data
     */
    private Map<String, Object> generateMockCpuHistoryData(String applicationName, int days) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> history = new ArrayList<>();
        
        // Get current time for reference
        LocalDateTime now = LocalDateTime.now();
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();
        
        // Generate history for each day
        for (int i = 0; i < days; i++) {
            Map<String, Object> record = new HashMap<>();
            
            // Calculate date for this record
            LocalDateTime recordDateTime = now.minusDays(i);
            String date = recordDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = String.format("%02d:%02d", currentHour, currentMinute);
            
            record.put("date", date);
            record.put("time", time);
            
            // Generate realistic CPU usage based on application type
            int cpuUsage;
            String trend;
            String status;
            
            if ("wmpooc".equals(applicationName)) {
                // wmpooc application has very high CPU usage (always > 90%)
                cpuUsage = random.nextInt(10) + 90; // 90-100%
                trend = random.nextBoolean() ? "↗️ 上升" : "↘️ 下降";
                status = "🚨 严重警告"; // Always critical for wmpooc
            } else if (applicationName.contains("service")) {
                // Service applications have moderate CPU usage
                cpuUsage = random.nextInt(40) + 30; // 30-70%
                trend = random.nextBoolean() ? "→ 稳定" : "↗️ 上升";
                status = cpuUsage > 60 ? "⚠️ 警告" : "✅ 正常";
            } else {
                // Other applications have lower CPU usage
                cpuUsage = random.nextInt(30) + 20; // 20-50%
                trend = "→ 稳定";
                status = "✅ 正常";
            }
            
            record.put("cpuUsage", cpuUsage);
            record.put("trend", trend);
            record.put("status", status);
            
            // Add detailed metrics
            Map<String, Object> details = new HashMap<>();
            details.put("memoryUsage", random.nextInt(30) + 50); // 50-80%
            details.put("diskUsage", random.nextInt(20) + 60); // 60-80%
            details.put("networkLatency", random.nextInt(50) + 20); // 20-70ms
            record.put("details", details);
            
            history.add(record);
        }
        
        result.put("history", history);
        
        // Calculate summary statistics
        Map<String, Object> summary = new HashMap<>();
        double avgCpuUsage = history.stream()
            .mapToInt(record -> (Integer) record.get("cpuUsage"))
            .average()
            .orElse(0.0);
        
        int maxCpuUsage = history.stream()
            .mapToInt(record -> (Integer) record.get("cpuUsage"))
            .max()
            .orElse(0);
            
        int minCpuUsage = history.stream()
            .mapToInt(record -> (Integer) record.get("cpuUsage"))
            .min()
            .orElse(0);
        
        double variance = maxCpuUsage - minCpuUsage;
        
        summary.put("avgCpuUsage", Math.round(avgCpuUsage * 10.0) / 10.0);
        summary.put("maxCpuUsage", maxCpuUsage);
        summary.put("minCpuUsage", minCpuUsage);
        summary.put("variance", Math.round(variance * 10.0) / 10.0);
        
        result.put("summary", summary);
        
        return result;
    }
}
