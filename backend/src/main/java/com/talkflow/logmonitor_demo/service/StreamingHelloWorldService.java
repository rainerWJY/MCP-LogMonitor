package com.talkflow.logmonitor_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
@Service
public class StreamingHelloWorldService {
    
    private static final Logger logger = LoggerFactory.getLogger(StreamingHelloWorldService.class);

    /**
     * 流式Hello World消息 - 每秒发送一条消息，持续5秒
     */
    @Tool(name = "getStreamingHelloWorld", description = "Streams Hello World messages")
    public String getStreamingHelloWorld() {
        logger.info("🚀 Starting getStreamingHelloWorld stream - will send 5 messages every 1 second");
        return "Hello World";
    }


    @Tool(name = "addNumbers", description = "计算两个数字的和")
    public int addNumbers(int a, int b) {
        return a + b;
    }
}
