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
      <div class="prose prose-sm max-w-none">
        <h1 class="text-2xl font-bold text-gray-900 mb-3">使用说明：</h1>
        
        <h2 class="text-xl font-semibold text-gray-800 mb-2">场景说明：</h2>
        <p class="text-gray-700 mb-3">
          这是目前Jmanus产品最广泛应用的场景之一，日常巡检。简单来说，就是通过mcp分析获取告警日志，然后取处理建议，基于处理建议，和一系列的后续检查，最终推断出核心问题所在，给出用户建议。
        </p>
        
        <h2 class="text-xl font-semibold text-gray-800 mb-2">步骤说明：</h2>
        <div class="space-y-2 mb-3">
          <p class="text-gray-700"><strong>step1:</strong> 选择 CPU类故障 或者 应用类故障</p>
          <p class="text-gray-700"><strong>step2:</strong> 选择刷新数据（这时候系统会模拟生成告警数据）</p>
          <p class="text-gray-700"><strong>step3:</strong> 选右边的点我进行AI诊断，你将能看到详细的思考步骤。</p>
        </div>
        
        <h2 class="text-xl font-semibold text-gray-800 mb-2">DEMO说明：</h2>
        <p class="text-gray-700 mb-2">在右边展示的思考步骤中，具体体现了几个关键步骤：</p>
        <ol class="list-decimal list-inside space-y-1 mb-3 text-gray-700">
          <li>会先查询告警日志。</li>
          <li>会根据告警类型，查询对应的处理方法建议。</li>
          <li>根据处理方法建议，调用Mcp服务获取更多信息，以帮助判断和处理</li>
          <li>基于所有信息，给到用户建议。</li>
        </ol>
        
        <h2 class="text-xl font-semibold text-gray-800 mb-2">额外的扩展：</h2>
        <div class="space-y-2 mb-3">
          <p class="text-gray-700"><strong>1、</strong> 打开 <a href="http://localhost:18080" target="_blank" class="text-blue-600 hover:text-blue-800 hover:underline">http://localhost:18080</a></p>
          <p class="text-gray-700"><strong>2、</strong> 选回退，退回到首页</p>
          <p class="text-gray-700"><strong>3、</strong> 界面的其他案例：</p>
          <div class="ml-4 space-y-1">
            <p class="text-gray-700"><strong>3.1、</strong> 点击 【查询一个人的信息】（这用于展示无限上下文能力，能够获取网页中获取远超单次大模型处理能力的文本内容，通过jmanus的无限上下文能力进行处理）</p>
            <p class="text-gray-700"><strong>3.2、</strong> 点击【是AI小说创作】（这是一个用于展示无限文本输出能力的样例，展示的是Jmanus可以突破输出限制，输出超长的内容）</p>
          </div>
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
