<template>
  <div class="bg-white rounded-lg shadow p-6">
    <!-- Tabs -->
    <div class="border-b border-gray-200 mb-6">
      <nav class="-mb-px flex space-x-8">
        <a href="#" class="border-indigo-500 text-indigo-600 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
          观测分析
        </a>
        <a href="#" class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
          历史预警 51
        </a>
        <a href="#" class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
          关联预案 0
        </a>
        <a href="#" class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
          处理记录 2
        </a>
      </nav>
    </div>

    <!-- Time Range and Filters -->
    <div class="mb-6">
      <div class="flex items-center justify-between mb-4">
        <div class="flex items-center space-x-4">
          <span class="text-sm text-gray-500">时间范围:</span>
          <span class="text-sm font-medium">2025/06/23 12:35 - 2025/06/23 13:35</span>
        </div>
        <div class="flex space-x-2">
          <button class="px-3 py-1 text-xs border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
            告警前后30min
          </button>
          <button class="px-3 py-1 text-xs border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
            告警前后1h
          </button>
          <button class="px-3 py-1 text-xs border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
            最新数据(最近1h)
          </button>
        </div>
      </div>
      
      <div class="flex space-x-2">
        <button class="px-3 py-1 text-xs border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
          对比昨天
        </button>
        <button class="px-3 py-1 text-xs border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
          对比上周
        </button>
        <button class="px-3 py-1 text-xs border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
          应用观测
        </button>
        <button class="px-3 py-1 text-xs border border-gray-300 rounded text-gray-700 hover:bg-gray-50">
          前往Sunfire
        </button>
      </div>
    </div>

    <!-- Metrics Table -->
    <div class="mb-6">
      <h4 class="text-sm font-medium text-gray-900 mb-3">监控指标</h4>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">IP地址</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">相关变更</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">相关预警</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">异常聚类</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="metric in metrics" :key="metric.ip" class="hover:bg-gray-50">
              <td class="px-3 py-2 whitespace-nowrap text-sm font-medium text-gray-900">{{ metric.ip }}</td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ metric.relatedChanges }}</td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ metric.relatedAlerts }}</td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ metric.anomalyClusters }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Performance Graph -->
    <div>
      <h4 class="text-sm font-medium text-gray-900 mb-3">system.mem.util</h4>
      <div class="bg-gray-50 rounded-lg p-4 h-64 flex items-center justify-center">
        <div class="text-center">
          <div class="text-4xl text-gray-400 mb-2">📊</div>
          <p class="text-gray-500">内存利用率监控图表</p>
          <p class="text-sm text-gray-400">Y轴: 0-100% | 时间范围: 12:35-13:35</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { MonitoringMetric } from '@/types/dashboard';

interface Props {
  metrics: MonitoringMetric[];
}

defineProps<Props>();
</script>
