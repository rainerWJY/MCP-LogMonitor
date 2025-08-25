package com.talkflow.logmonitor_demo.controller;

import com.talkflow.logmonitor_demo.service.AlertMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for alert types selection
 */
@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class AlertController {

    @Autowired
    private AlertMonitorService alertMonitorService;

    /**
     * Get current selected alert type
     * @return Current alert type
     */
    @GetMapping("/types/current")
    public ResponseEntity<String> getCurrentAlertType() {
        return ResponseEntity.ok(alertMonitorService.getCurrentAlertType());
    }

    /**
     * Set alert type (CPU过载 or 服务不可用)
     * @param alertType The alert type to set
     * @return Current alert type
     */
    @PostMapping("/types/current")
    public ResponseEntity<String> setAlertType(@RequestBody String alertType) {
        try {
            String result = alertMonitorService.setAlertType(alertType);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all available alert types
     * @return List of all available alert types
     */
    @GetMapping("/types/available")
    public ResponseEntity<List<String>> getAvailableAlertTypes() {
        return ResponseEntity.ok(Arrays.asList(alertMonitorService.getAvailableAlertTypes()));
    }
}
