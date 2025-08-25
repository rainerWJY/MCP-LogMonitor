<template>
  <div class="bg-white rounded-lg shadow p-6">
    <!-- SRE Agent Header -->
    <div class="flex items-center justify-between mb-6">
      <h3 class="text-lg font-medium text-gray-900">SRE Agent</h3>
      <button class="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
        重新触发
      </button>
    </div>

    <!-- Deep Thinking Section -->
    <div class="mb-6">
      <div class="flex items-center justify-between mb-3">
        <h4 class="text-sm font-medium text-gray-900">深度思考</h4>
        <button class="text-xs text-blue-600 hover:text-blue-800">
          切换为debug模式
        </button>
      </div>
      
      <div class="bg-blue-50 rounded-lg p-4">
        <div class="space-y-3">
          <div>
            <div class="text-sm font-medium text-blue-900">问题:</div>
            <div class="text-sm text-blue-800">{{ agent.currentTask }}</div>
          </div>
          <div>
            <div class="text-sm font-medium text-blue-900">智能代理:</div>
            <div class="text-sm text-blue-800">{{ agent.name }}</div>
          </div>
          <div>
            <div class="text-sm font-medium text-blue-900">描述:</div>
            <div class="text-sm text-blue-800">我是一个经验丰富的SRE工程师,擅长定位预警任务的根因,很高兴帮助你</div>
          </div>
          <div>
            <div class="text-sm font-medium text-blue-900">执行结果:</div>
            <div class="text-sm text-blue-800">{{ getStatusText(agent.status) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Steps/Thoughts -->
    <div class="mb-6">
      <h4 class="text-sm font-medium text-gray-900 mb-3">执行步骤</h4>
      <div class="space-y-3">
        <div v-for="step in agent.steps" :key="step.id" class="flex items-start space-x-3">
          <div class="flex-shrink-0">
            <span class="inline-flex items-center justify-center h-6 w-6 rounded-full text-xs font-medium"
                  :class="getStepStatusClass(step.status)">
              {{ step.id }}
            </span>
          </div>
          <div class="flex-1 min-w-0">
            <div class="text-sm text-gray-900">{{ step.description }}</div>
            <div v-if="step.result" class="text-sm text-gray-500 mt-1">{{ step.result }}</div>
          </div>
          <div class="flex-shrink-0">
            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="getStepStatusBadgeClass(step.status)">
              {{ getStepStatusText(step.status) }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Analysis Results -->
    <div>
      <h4 class="text-sm font-medium text-gray-900 mb-3">分析结果</h4>
      <div class="space-y-4">
        <!-- Key Evidence -->
        <div>
          <h5 class="text-sm font-medium text-gray-700 mb-2">关键证据或疑点:</h5>
          <ul class="list-disc list-inside space-y-1">
            <li v-for="(evidence, index) in analysisResult.keyEvidence" :key="index" 
                class="text-sm text-gray-600">{{ evidence }}</li>
          </ul>
        </div>

        <!-- Historical Summary -->
        <div>
          <h5 class="text-sm font-medium text-gray-700 mb-2">【历史预警经验总结】</h5>
          <p class="text-sm text-gray-600">{{ analysisResult.historicalSummary }}</p>
        </div>

        <!-- Related Information -->
        <div>
          <h5 class="text-sm font-medium text-gray-700 mb-2">【其他根因关联信息】</h5>
          <div class="bg-gray-50 rounded p-3">
            <div class="text-xs text-gray-500 mb-1">Source Tool: queryHistoryAlerts</div>
            <div class="text-xs text-gray-500 mb-1">Field Information:</div>
            <p class="text-sm text-gray-600">{{ analysisResult.relatedInfo }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { SREAgent, AnalysisResult } from '@/types/dashboard';

interface Props {
  agent: SREAgent;
  analysisResult: AnalysisResult;
}

defineProps<Props>();

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'idle': '空闲',
    'thinking': '思考中...',
    'completed': '已完成'
  };
  return statusMap[status] || status;
};

const getStepStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'success': 'SUCCESS',
    'processing': 'PROCESSING',
    'failed': 'FAILED'
  };
  return statusMap[status] || status;
};

const getStepStatusClass = (status: string) => {
  const statusMap: Record<string, string> = {
    'success': 'bg-green-100 text-green-800',
    'processing': 'bg-blue-100 text-blue-800',
    'failed': 'bg-red-100 text-red-800'
  };
  return statusMap[status] || 'bg-gray-100 text-gray-800';
};

const getStepStatusBadgeClass = (status: string) => {
  const statusMap: Record<string, string> = {
    'success': 'bg-green-100 text-green-800',
    'processing': 'bg-blue-100 text-blue-800',
    'failed': 'bg-red-100 text-red-800'
  };
  return statusMap[status] || 'bg-gray-100 text-gray-800';
};
</script>
