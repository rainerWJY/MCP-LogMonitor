<template>
  <div class="bg-gray-200 rounded-lg shadow-md p-6">
    <!-- Control Interface -->
    <div class="mb-6">
      <h3 class="text-xl font-semibold text-gray-800 mb-4">测试用控制控制界面</h3>
      
      <!-- Fault Type Toggle -->
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-2">故障类型切换</label>
        <div class="flex space-x-2">
          <button
            @click="setFaultType('CPU过载')"
            :disabled="loading"
            :class="[
              'px-4 py-2 rounded-md text-sm font-medium transition-colors',
              faultType === 'CPU过载'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            ]"
          >
            CPU类故障
          </button>
          <button
            @click="setFaultType('服务不可用')"
            :disabled="loading"
            :class="[
              'px-4 py-2 rounded-md text-sm font-medium transition-colors',
              faultType === '服务不可用'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            ]"
          >
            应用服务类故障
          </button>
        </div>
        <p class="text-xs text-gray-500 mt-1">当前选择: {{ faultType }}</p>
        
        <!-- Loading and Error States -->
        <div v-if="loading" class="mt-2">
          <p class="text-xs text-blue-600">🔄 正在同步...</p>
        </div>
        
        <div v-if="error" class="mt-2">
          <p class="text-xs text-red-600">❌ {{ error }}</p>
        </div>
      </div>

      <!-- Additional Control Buttons -->
      <div class="grid grid-cols-2 gap-3">
        <button 
          @click="refreshData"
          :disabled="refreshLoading"
          class="px-3 py-2 bg-green-600 text-white rounded-md text-sm font-medium hover:bg-green-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {{ refreshLoading ? '刷新中...' : '刷新数据' }}
        </button>
      </div>
    </div>

    <!-- Description Interface -->
    <div class="border-t pt-6">
      <h3 class="text-xl font-semibold text-gray-800 mb-4">说明界面</h3>
      
      <div class="prose prose-sm max-w-none">
        <h1 class="text-2xl font-bold text-gray-900 mb-3">helloworld</h1>
        
        <h2 class="text-xl font-semibold text-gray-800 mb-2">你好 二级页面</h2>
        
        <h3 class="text-lg font-medium text-gray-700 mb-2">三级页面</h3>
        
        <div class="mt-4 p-3 bg-gray-50 rounded-md">
          <p class="text-sm text-gray-600">
            <strong>说明:</strong> 这是一个控制面板组件，包含故障类型切换功能和说明文档。你可以通过按钮切换不同的故障类型，查看相关的控制选项和说明信息。
          </p>
        </div>

        <!-- Example Links -->
        <div class="mt-4 space-y-2">
          <a href="#" class="block text-blue-600 hover:text-blue-800 hover:underline">链接例子 1</a>
          <a href="#" class="block text-blue-600 hover:text-blue-800 hover:underline">链接例子 2</a>
          <a href="#" class="block text-blue-600 hover:text-blue-800 hover:underline">链接例子 3</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { alertService } from '@/services/alertService'
import { alertDataService } from '@/services/alertDataService'

// Define emits
const emit = defineEmits<{
  dataRefreshed: []
}>()

// Reactive data
const faultType = ref<string>('CPU过载')
const loading = ref(false)
const error = ref<string | null>(null)
const refreshLoading = ref(false)

// Methods
const setFaultType = async (type: 'CPU过载' | '服务不可用') => {
  try {
    loading.value = true
    error.value = null
    
    // Call backend API to set alert type
    const result = await alertService.setAlertType(type)
    faultType.value = result
    
    console.log('Alert type changed to:', result)
  } catch (err) {
    error.value = err instanceof Error ? err.message : '设置告警类型失败'
    console.error('Error setting alert type:', err)
  } finally {
    loading.value = false
  }
}

// Methods
const refreshData = async () => {
  try {
    refreshLoading.value = true
    error.value = null
    
    // Call backend API to refresh alert data
    const result = await alertDataService.refreshAlertData()
    console.log('Alert data refreshed:', result)
    
    // Emit event to notify parent component
    emit('dataRefreshed')
    
    // Refresh the page after successful data refresh
    setTimeout(() => {
      window.location.reload()
    }, 1000) // Wait 1 second to show success state
    
  } catch (err) {
    error.value = err instanceof Error ? err.message : '刷新数据失败'
    console.error('Error refreshing alert data:', err)
  } finally {
    refreshLoading.value = false
  }
};

// Load current alert type on component mount
onMounted(async () => {
  try {
    loading.value = true
    const currentType = await alertService.getCurrentAlertType()
    faultType.value = currentType
  } catch (err) {
    error.value = err instanceof Error ? err.message : '获取告警类型失败'
    console.error('Error loading current alert type:', err)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.prose h1 {
  margin-bottom: 0.75rem;
}

.prose h2 {
  margin-bottom: 0.5rem;
}

.prose h3 {
  margin-bottom: 0.5rem;
}
</style>
