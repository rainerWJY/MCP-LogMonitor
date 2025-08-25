package com.talkflow.logmonitor_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for providing alert recommendations based on alert types
 */
@Service
public class AlertRecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertRecommendationService.class);
    
    // Historical recommendations for different alert types
    private static final Map<String, List<String>> ALERT_RECOMMENDATIONS = new HashMap<>();
    
    static {
        // CPU过载 recommendations
        ALERT_RECOMMENDATIONS.put("CPU过载", List.of(
            "查询最近5天cpu的使用情况，如果5天内都超过70%，那就要增加机器，否则就是偶发cpu过载，建议继续观察"
        ));
        
        // 服务不可用 recommendations
        ALERT_RECOMMENDATIONS.put("服务不可用", List.of(
            "检查服务是否可用，如果不可用，则建议重启应用，如果可用，那可能是简单的网络闪断，建议继续观察"
        ));
    }

    /**
     * Get historical recommendations based on alert type
     * @param alertType The type of alert (CPU过载 or 服务不可用)
     * @return Historical recommendations for the alert type
     */
    @Tool(description = "Get historical recommendations for specific alert types")
    public String getAlertRecommendations(String alertType) {
        logger.info("🔍 Getting recommendations for alert type: {}", alertType);
        
        try {
            if (alertType == null || alertType.trim().isEmpty()) {
                return "❌ Alert type cannot be empty";
            }
            
            List<String> recommendations = ALERT_RECOMMENDATIONS.get(alertType.trim());
            
            if (recommendations == null) {
                return String.format("❌ No recommendations found for alert type: %s\nAvailable types: %s", 
                    alertType, String.join(", ", ALERT_RECOMMENDATIONS.keySet()));
            }
            
            // Build formatted response
            StringBuilder result = new StringBuilder();
            result.append(String.format("💡 告警类型: %s 的处理建议\n", alertType));
            result.append("=".repeat(50)).append("\n\n");
            
            for (int i = 0; i < recommendations.size(); i++) {
                result.append(String.format("%d. %s\n", i + 1, recommendations.get(i)));
            }
            
            result.append(String.format("\n📋 总计: %d 条建议\n", recommendations.size()));
            
            logger.info("✅ Successfully retrieved {} recommendations for alert type: {}", 
                       recommendations.size(), alertType);
            
            return result.toString();
            
        } catch (Exception e) {
            logger.error("❌ Error getting recommendations for alert type {}: {}", alertType, e.getMessage(), e);
            return String.format("❌ 获取告警建议失败: %s", e.getMessage());
        }
    }

    /**
     * Get all available alert types that have recommendations
     * @return List of alert types with recommendations
     */
    public List<String> getAvailableAlertTypes() {
        return List.copyOf(ALERT_RECOMMENDATIONS.keySet());
    }
}
