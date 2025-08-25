# 🚀 Streaming MCP Server - WebFlux + SSE

这是一个完全流式异步的MCP服务器，使用Spring WebFlux和Server-Sent Events (SSE)提供实时流式数据传输。

## ✨ 特性

- **🔄 异步流式处理** - 使用Project Reactor的Flux进行响应式流处理
- **📡 SSE传输** - Server-Sent Events实现实时数据流
- **⚡ 高性能** - 非阻塞I/O，支持高并发
- **🎯 实时更新** - 毫秒级的数据流更新
- **🛠️ 自动工具注册** - Spring AI自动将Flux方法转换为MCP工具

## 🏗️ 架构

```
Client (Browser) ←→ SSE Connection ←→ Spring WebFlux ←→ MCP Server ←→ Streaming Tools
```

## 🚀 快速开始

### 1. 启动服务器

```bash
cd backend
mvn spring-boot:run
```

服务器将在 `http://localhost:8080` 启动

### 2. 访问测试页面

打开浏览器访问：`http://localhost:8080`

### 3. 连接MCP服务器

1. 点击 "Connect to MCP Server" 按钮
2. 等待连接状态变为 "Connected"
3. 开始使用流式工具

## 🛠️ 可用工具

### 📡 getStreamingHelloWorld()
- **功能**: 流式Hello World消息
- **频率**: 每秒1条消息
- **持续时间**: 5秒
- **输出**: 5条时间戳消息

### 👋 getStreamingPersonalizedGreeting(name)
- **功能**: 流式个性化问候
- **频率**: 每300毫秒1条消息
- **持续时间**: 3秒
- **参数**: name (字符串，可选)
- **输出**: 10条个性化问候消息

### 💓 getStreamingServerInfo()
- **功能**: 流式服务器心跳
- **频率**: 每500毫秒1条消息
- **持续时间**: 20秒
- **输出**: 40条服务器状态消息

### 🔢 getStreamingCounter()
- **功能**: 实时计数器
- **频率**: 每秒递增
- **持续时间**: 无限
- **输出**: 实时计数数据

### 📊 getStreamingProgress()
- **功能**: 进度条模拟
- **频率**: 每200毫秒更新
- **持续时间**: 约20秒
- **输出**: 0-100%进度更新

## 🔧 技术配置

### 依赖
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
</dependency>
```

### 配置属性
```properties
# 服务器类型
spring.ai.mcp.server.type=ASYNC

# SSE端点
spring.ai.mcp.server.sse-endpoint=/mcp/sse
spring.ai.mcp.server.sse-message-endpoint=/mcp/message

# 请求超时
spring.ai.mcp.server.request-timeout=30s

# 服务器端口
server.port=8080
```

## 📡 SSE端点

- **SSE连接**: `GET /mcp/sse`
- **消息发送**: `POST /mcp/message`
- **静态页面**: `GET /` (index.html)

## 🎯 使用场景

### 1. 实时监控
- 服务器状态监控
- 系统性能指标
- 日志实时查看

### 2. 实时通知
- 进度更新
- 状态变化通知
- 事件广播

### 3. 数据流处理
- 实时数据分析
- 流式数据处理
- 实时报表生成

## 🔍 测试方法

### 浏览器测试
1. 访问 `http://localhost:8080`
2. 连接MCP服务器
3. 测试各种流式工具

### API测试
```bash
# 连接SSE流
curl -N http://localhost:8080/mcp/sse

# 调用工具
curl -X POST http://localhost:8080/mcp/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "tools/call",
    "params": {
      "name": "getStreamingHelloWorld",
      "arguments": {}
    }
  }'
```

## 🚀 扩展开发

### 添加新流式工具
```java
@Service
public class MyStreamingService {
    
    public Flux<String> getMyStreamingTool() {
        return Flux.interval(Duration.ofMillis(100))
            .take(50)
            .map(i -> "Custom message " + i);
    }
}
```

### 自定义流式逻辑
```java
public Flux<CustomData> getCustomStream() {
    return Flux.create(sink -> {
        // 自定义流式逻辑
        for (int i = 0; i < 100; i++) {
            sink.next(new CustomData(i, "data"));
            Thread.sleep(100);
        }
        sink.complete();
    });
}
```

## 🐛 故障排除

### 常见问题

1. **连接失败**
   - 检查服务器是否启动
   - 确认端口8080未被占用

2. **工具调用失败**
   - 检查MCP服务器状态
   - 查看服务器日志

3. **流式数据中断**
   - 检查网络连接
   - 确认客户端支持SSE

### 日志查看
```bash
# 查看详细日志
mvn spring-boot:run -Dlogging.level.com.talkflow.logmonitor_demo=DEBUG
```

## 📚 相关资源

- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Server-Sent Events Specification](https://html.spec.whatwg.org/multipage/server-sent-events.html)
- [Project Reactor Documentation](https://projectreactor.io/docs/core/release/reference/)
- [Spring AI MCP Documentation](https://docs.spring.io/spring-ai/reference/api/mcp.html)

## 🎉 总结

这个流式MCP服务器提供了：
- ✅ 完全异步的流式处理
- ✅ 实时SSE数据传输
- ✅ 高性能WebFlux架构
- ✅ 丰富的流式工具示例
- ✅ 直观的Web测试界面

现在你可以享受真正的流式异步MCP服务体验！🚀
