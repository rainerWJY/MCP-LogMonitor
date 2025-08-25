<template>
  <div class="bg-white rounded-lg shadow p-6">
    <!-- Alert Header -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center space-x-3">
        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
          高
        </span>
        <h3 class="text-lg font-medium text-gray-900">系统性能异常告警</h3>
      </div>
      <div class="flex items-center space-x-2">
        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
          {{ currentSeverity || 'ALL' }}
        </span>
        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
          {{ getStatusText('processing') }}
        </span>
      </div>
    </div>

    <!-- Basic Information -->
    <div class="grid grid-cols-2 gap-4 mb-6">
      <div>
        <dt class="text-sm font-medium text-gray-500">告警类型</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ currentAlertType || 'CPU过载' }}</dd>
      </div>
      <div>
        <dt class="text-sm font-medium text-gray-500">应用名称</dt>
        <dd class="mt-1 text-sm text-gray-900">wmpooc</dd>
      </div>
      <div>
        <dt class="text-sm font-medium text-gray-500">告警级别</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ currentSeverity || 'ALL' }}</dd>
      </div>
      <div>
        <dt class="text-sm font-medium text-gray-500">数据状态</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ dataStatus }}</dd>
      </div>
      <div class="col-span-2">
        <dt class="text-sm font-medium text-gray-500">最后刷新时间</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ alertData?.summary?.cacheTime || lastRefreshTime || '未刷新' }}</dd>
      </div>
    </div>

    <!-- Quick Recovery Configuration -->
    <div class="mb-6">
      <h4 class="text-sm font-medium text-gray-900 mb-3">快恢配置</h4>
      <div class="flex space-x-3">
        <button class="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          配置关联预案
        </button>
        <button class="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
          配置应急SOP文档
        </button>
      </div>
    </div>

    <!-- Alert List Table -->
    <div>
      <h4 class="text-sm font-medium text-gray-900 mb-3">告警列表</h4>
      <div v-if="loading" class="text-center py-4">
        <p class="text-gray-500">🔄 正在加载告警数据...</p>
      </div>
      <div v-else-if="error" class="text-center py-4">
        <p class="text-red-500">❌ {{ error }}</p>
      </div>
      <div v-else-if="alertData && alertData.alerts" class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警ID</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">应用名称</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警级别</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警类型</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">发生时间</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="alert in alertData.alerts" :key="alert.alertId" class="hover:bg-gray-50">
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">
                <div class="font-medium">{{ alert.alertId }}</div>
              </td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ alert.application }}</td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">
                <span :class="getSeverityClass(alert.severity)">{{ alert.severity }}</span>
              </td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ alert.alertType }}</td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">
                <span :class="getStatusClass(alert.status)">{{ alert.status }}</span>
              </td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ alert.timestamp }}</td>
            </tr>
          </tbody>
        </table>
        <div class="mt-4 text-sm text-gray-500">
          总计: {{ alertData?.summary?.totalCount || alertData?.alerts?.length || 0 }} 条告警
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { alertDataService } from '@/services/alertDataService'
import type { AlertData } from '@/services/alertDataService'

interface Props {
  currentSeverity?: string;
  currentAlertType?: string;
}

const props = defineProps<Props>();

// Reactive data
const alertData = ref<AlertData | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)
const lastRefreshTime = ref<string | null>(null)
const dataStatus = ref<string>('未加载')

// Methods
const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'pending': '待接手',
    'processing': '处理中',
    'resolved': '已解决',
    'ACTIVE': '活跃',
    'ACKNOWLEDGED': '已确认',
    'RESOLVED': '已解决'
  };
  return statusMap[status] || status;
};

const getSeverityClass = (severity: string) => {
  const classMap: Record<string, string> = {
    'CRITICAL': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-800',
    'HIGH': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-orange-100 text-orange-800',
    'MEDIUM': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800',
    'LOW': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800'
  };
  return classMap[severity] || 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-800';
};

const getStatusClass = (status: string) => {
  const classMap: Record<string, string> = {
    'ACTIVE': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-red-100 text-red-800',
    'ACKNOWLEDGED': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800',
    'RESOLVED': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800'
  };
  return classMap[status] || 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-800';
};

const loadAlertData = async (severity?: string) => {
  try {
    loading.value = true
    error.value = null
    dataStatus.value = '加载中...'
    
    const severityToUse = severity || props.currentSeverity || 'CRITICAL'
    const result = await alertDataService.getDetailedAlertInfoAsJson(severityToUse)
    
    // Use the JSON response directly
    alertData.value = result
    
    dataStatus.value = '已加载'
    lastRefreshTime.value = new Date().toLocaleString()
    
  } catch (err) {
    error.value = err instanceof Error ? err.message : '加载告警数据失败'
    dataStatus.value = '加载失败'
    console.error('Error loading alert data:', err)
  } finally {
    loading.value = false
  }
};

// Watch for changes in currentSeverity
watch(() => props.currentSeverity, (newSeverity) => {
  if (newSeverity) {
    loadAlertData(newSeverity)
  }
});

// Load data on component mount
onMounted(() => {
  loadAlertData()
});

// Expose methods for parent components
defineExpose({
  loadAlertData,
  refreshData: loadAlertData
});
</script>
