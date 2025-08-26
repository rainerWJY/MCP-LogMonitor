package com.talkflow.logmonitor_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import java.awt.Desktop;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.talkflow.logmonitor_demo.service.AlertMonitorService;
import com.talkflow.logmonitor_demo.service.AlertRecommendationService;
import com.talkflow.logmonitor_demo.service.ApplicationCpuHistoryService;
import com.talkflow.logmonitor_demo.service.ApplicationHealthCheckService;

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
        logger.info("📊 Available services: HelloWorld, AlertMonitor, AlertRecommendation, ApplicationCpuHistory, ApplicationHealthCheck");
        logger.info("🛠️ Available tools: 6");
    }


    @Bean
    public ToolCallbackProvider alertMonitorTools(
            AlertMonitorService alertMonitorService,
            AlertRecommendationService alertRecommendationService,
            ApplicationCpuHistoryService applicationCpuHistoryService,
            ApplicationHealthCheckService applicationHealthCheckService) {
        logger.info("🚨 Registering Alert Monitor tools with MCP server");
        return MethodToolCallbackProvider.builder()
            .toolObjects(alertMonitorService, alertRecommendationService, 
                        applicationCpuHistoryService, applicationHealthCheckService)
            .build();
    }
    
    /**
     * 应用启动完成后自动打开浏览器
     */
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowserAfterStartup(ApplicationReadyEvent event) {
        // 延迟2秒打开浏览器，确保服务完全启动
        CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(() -> {
            try {
                // 获取应用端口
                Environment env = event.getApplicationContext().getEnvironment();
                String port = env.getProperty("server.port", "8080");
                String contextPath = env.getProperty("server.servlet.context-path", "");
                
                String url = "http://localhost:" + port + contextPath;
                logger.info("🌐 Auto-opening browser to: {}", url);
                
                // 首先尝试使用 Desktop API
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(url));
                        logger.info("✅ Successfully opened browser via Desktop API");
                        return;
                    }
                } catch (Throwable e) {
                    logger.warn("⚠️ Failed to open browser using Desktop API, trying Runtime command execution", e);
                }
                
                // 如果 Desktop API 失败，尝试使用 Runtime 命令执行
                String os = System.getProperty("os.name").toLowerCase();
                Runtime rt = Runtime.getRuntime();
                
                try {
                    if (os.contains("mac")) {
                        // macOS 特定命令
                        rt.exec(new String[] { "open", url });
                        logger.info("✅ Successfully opened browser via macOS open command");
                    } else if (os.contains("win")) {
                        // Windows 特定命令
                        rt.exec(new String[] { "cmd", "/c", "start", url });
                        logger.info("✅ Successfully opened browser via Windows command");
                    } else if (os.contains("nix") || os.contains("nux")) {
                        // Linux 特定命令，尝试几种常见的浏览器打开方法
                        String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror", "netscape",
                                "opera", "links", "lynx" };
                        
                        StringBuilder cmd = new StringBuilder();
                        for (int i = 0; i < browsers.length; i++) {
                            if (i == 0) {
                                cmd.append(String.format("%s \"%s\"", browsers[i], url));
                            } else {
                                cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                            }
                        }
                        
                        rt.exec(new String[] { "sh", "-c", cmd.toString() });
                        logger.info("✅ Attempted to open browser via Linux command");
                    } else {
                        logger.warn("⚠️ Unknown operating system, cannot auto-open browser, please manually access: {}", url);
                    }
                } catch (Throwable e) {
                    logger.error("❌ Failed to open browser via Runtime command execution", e);
                    logger.info("💡 Please manually access in browser: {}", url);
                }
                
            } catch (Exception e) {
                logger.error("❌ Failed to open browser: {}", e.getMessage());
                logger.info("💡 Please manually open: http://localhost:8080");
            }
        });
    }
}
