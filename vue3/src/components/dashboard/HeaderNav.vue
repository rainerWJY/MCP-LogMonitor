<template>
  <header class="bg-white shadow-sm border-b border-gray-200">
    <div class="max-w-8xl mx-auto px-2 sm:px-4 lg:px-6">
      <div class="flex justify-between items-center h-16">
        <!-- Logo and Title -->
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <h1 class="text-xl font-bold text-gray-900">智能监控系统</h1>
            <p class="text-sm text-gray-500">System Monitoring & Reliability Engineering</p>
          </div>
        </div>

        <!-- Navigation Tabs -->
        <nav class="hidden md:flex space-x-8">
          <a href="#" class="text-indigo-600 border-b-2 border-indigo-600 px-3 py-2 text-sm font-medium">
            我的监控
          </a>
          <a href="#" class="text-gray-500 hover:text-gray-700 px-3 py-2 text-sm font-medium">
            工作台
          </a>
          <a href="#" class="text-gray-500 hover:text-gray-700 px-3 py-2 text-sm font-medium">
            观测中心
          </a>
          <a href="#" class="text-gray-500 hover:text-gray-700 px-3 py-2 text-sm font-medium">
            应急中心
          </a>
          <a href="#" class="text-gray-500 hover:text-gray-700 px-3 py-2 text-sm font-medium">
            日志中心
          </a>
          <a href="#" class="text-gray-500 hover:text-gray-700 px-3 py-2 text-sm font-medium">
            风险中心
          </a>
          <a href="#" class="text-gray-500 hover:text-gray-700 px-3 py-2 text-sm font-medium">
            防抖中心
          </a>
          <a href="#" class="text-gray-500 hover:text-gray-700 px-3 py-2 text-sm font-medium">
            实时核对
          </a>
        </nav>

        <!-- Right Side Actions -->
        <div class="flex items-center space-x-4">
          <button class="text-sm text-blue-600 hover:text-blue-800">
            快速查看应用视角观测
          </button>
          <div class="flex items-center space-x-2">
            <button class="p-2 text-gray-400 hover:text-gray-500">
              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </button>
            <button class="p-2 text-gray-400 hover:text-gray-500">
              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
// Header navigation component
</script>
