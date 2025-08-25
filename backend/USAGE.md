# MCP Hello World Server 使用指南

## 服务器信息

- **名称**: hello-world-mcp-server
- **版本**: 1.0.0
- **类型**: 同步服务器 (SYNC)
- **传输方式**: STDIO (标准输入输出)
- **状态**: ✅ 运行中

## 可用工具

你的MCP服务器提供以下三个工具：

### 1. getHelloWorld()
- **描述**: 获取Hello World消息
- **参数**: 无
- **返回**: "Hello, World! Welcome to the MCP Server."

### 2. getPersonalizedGreeting(String name)
- **描述**: 获取个性化问候消息
- **参数**: name (字符串，可选)
- **返回**: 个性化问候消息，如果未提供名字则使用"World"

### 3. getServerInfo()
- **描述**: 获取服务器信息
- **参数**: 无
- **返回**: 服务器状态信息

## 使用方法

### 方法1: 直接运行jar文件

```bash
# 在backend目录下
java -jar target/logmonitor-demo-0.0.1-SNAPSHOT.jar
```

### 方法2: 使用Maven运行

```bash
# 在backend目录下
mvn spring-boot:run
```

### 方法3: 配置MCP客户端

#### Claude Desktop配置
1. 打开Claude Desktop
2. 进入设置 -> MCP服务器
3. 添加新服务器：
   - **名称**: Hello World MCP Server
   - **命令**: `java -jar /path/to/logmonitor-demo-0.0.1-SNAPSHOT.jar`
   - **传输**: STDIO

#### 其他MCP客户端
根据客户端的配置要求，设置：
- 服务器类型: STDIO
- 执行命令: `java -jar target/logmonitor-demo-0.0.1-SNAPSHOT.jar`

## 测试服务器

运行测试脚本：
```bash
chmod +x test-mcp-server.sh
./test-mcp-server.sh
```

## 故障排除

### 常见问题

1. **端口冲突**: 这个服务器使用STDIO，不需要端口
2. **权限问题**: 确保jar文件有执行权限
3. **Java版本**: 需要Java 21或更高版本

### 日志查看

启动时添加调试信息：
```bash
java -jar target/logmonitor-demo-0.0.1-SNAPSHOT.jar --debug
```

## 扩展功能

要添加新工具，只需在`HelloWorldService`中添加新方法，Spring AI会自动将其注册为MCP工具。

## 技术细节

- **框架**: Spring Boot 3.5.5
- **Spring AI版本**: 1.0.1
- **MCP协议版本**: 2024-11-05
- **自动配置**: 启用所有能力（工具、资源、提示、完成）
