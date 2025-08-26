#!/bin/bash

# 构建脚本 - 自动化构建和部署
# 作者: AI Assistant
# 版本: 1.0.0

set -e  # 遇到错误立即退出

echo "🚀 开始构建流程..."

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_step() {
    echo -e "${BLUE}[STEP]${NC} $1"
}

# 检查必要的命令是否存在
check_requirements() {
    print_message "检查构建环境..."
    
    if ! command -v node &> /dev/null; then
        print_error "Node.js 未安装，请先安装 Node.js"
        exit 1
    fi
    
    if ! command -v npm &> /dev/null && ! command -v pnpm &> /dev/null; then
        print_error "包管理器未安装，请安装 npm 或 pnpm"
        exit 1
    fi
    
    if ! command -v mvn &> /dev/null; then
        print_error "Maven 未安装，请先安装 Maven"
        exit 1
    fi
    
    print_message "环境检查通过 ✅"
}

# 步骤1: Vue3 构建
build_vue3() {
    print_step "步骤 1: 构建 Vue3 应用"
    
    cd vue3
    
    print_message "安装依赖..."
    if command -v pnpm &> /dev/null; then
        pnpm install
    else
        npm install
    fi
    
    print_message "开始构建..."
    if command -v pnpm &> /dev/null; then
        pnpm run build
    else
        npm run build
    fi
    
    if [ $? -eq 0 ]; then
        print_message "Vue3 构建成功 ✅"
    else
        print_error "Vue3 构建失败 ❌"
        exit 1
    fi
    
    cd ..
}

# 步骤2: 拷贝构建文件到 backend/static
copy_build_files() {
    print_step "步骤 2: 拷贝构建文件到 backend/static"
    
    # 检查 backend/static 目录是否存在
    if [ ! -d "backend/src/main/resources/static" ]; then
        print_message "创建 backend/static 目录..."
        mkdir -p backend/src/main/resources/static
    fi
    
    # 清空 static 目录
    print_message "清空 static 目录..."
    rm -rf backend/src/main/resources/static/*
    
    # 拷贝构建文件
    print_message "拷贝构建文件..."
    cp -r vue3/dist/* backend/src/main/resources/static/
    
    if [ $? -eq 0 ]; then
        print_message "文件拷贝成功 ✅"
        print_message "拷贝的文件数量: $(find backend/src/main/resources/static -type f | wc -l)"
    else
        print_error "文件拷贝失败 ❌"
        exit 1
    fi
}

# 步骤3: Maven 打包
build_backend() {
    print_step "步骤 3: Maven 打包"
    
    cd backend
    
    print_message "开始 Maven 打包..."
    mvn clean package -DskipTests
    
    if [ $? -eq 0 ]; then
        print_message "Maven 打包成功 ✅"
        print_message "JAR 文件位置: target/*.jar"
    else
        print_error "Maven 打包失败 ❌"
        exit 1
    fi
    
    cd ..
}

# 主函数
main() {
    print_message "开始执行构建脚本..."
    print_message "当前工作目录: $(pwd)"
    
    # 检查环境
    check_requirements
    
    # 执行构建步骤
    build_vue3
    copy_build_files
    build_backend
    
    print_message "🎉 所有构建步骤完成！"
    print_message "📦 后端 JAR 包位置: backend/target/"
    print_message "🌐 前端构建文件已集成到后端 static 目录"
}

# 执行主函数
main "$@"
