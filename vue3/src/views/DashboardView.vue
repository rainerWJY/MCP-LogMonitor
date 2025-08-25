<template>
  <div class="min-h-screen bg-gray-100">
    <!-- Header Navigation -->
    <HeaderNav />
    
    <!-- Main Dashboard Content -->
    <main class="max-w-8xl mx-auto py-6 px-2 sm:px-4 lg:px-6">
      <!-- Page Title -->
      <div class="mb-6">
        <h1 class="text-2xl font-bold text-gray-900">监控仪表板</h1>
        <p class="mt-1 text-sm text-gray-500">实时监控系统状态和告警信息</p>
      </div>

      <!-- Dashboard Grid -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Left Column: Alert Details -->
        <div class="lg:col-span-1">
          <AlertDetails :alert="alert" />
        </div>

        <!-- Middle Column: Observation Analysis -->
        <div class="lg:col-span-1">
          <ObservationAnalysis :metrics="metrics" />
        </div>

        <!-- Right Column: SRE Agent -->
        <div class="lg:col-span-1">
          <SREAgent :agent="sreAgent" :analysis-result="analysisResult" />
        </div>
      </div>

      <!-- Additional Info Section -->
      <div class="mt-6">
        <div class="bg-white rounded-lg shadow p-6">
          <h3 class="text-lg font-medium text-gray-900 mb-4">系统概览</h3>
          <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div class="bg-blue-50 rounded-lg p-4">
              <div class="text-2xl font-bold text-blue-600">3</div>
              <div class="text-sm text-blue-800">受影响机器</div>
            </div>
            <div class="bg-yellow-50 rounded-lg p-4">
              <div class="text-2xl font-bold text-yellow-600">51</div>
              <div class="text-sm text-yellow-800">历史预警</div>
            </div>
            <div class="bg-green-50 rounded-lg p-4">
              <div class="text-2xl font-bold text-green-600">2</div>
              <div class="text-sm text-green-800">处理记录</div>
            </div>
            <div class="bg-purple-50 rounded-lg p-4">
              <div class="text-2xl font-bold text-purple-600">0</div>
              <div class="text-sm text-purple-800">关联预案</div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import HeaderNav from '@/components/dashboard/HeaderNav.vue';
import AlertDetails from '@/components/dashboard/AlertDetails.vue';
import ObservationAnalysis from '@/components/dashboard/ObservationAnalysis.vue';
import SREAgent from '@/components/dashboard/SREAgent.vue';
import { mockAlert, mockMetrics, mockSREAgent, mockAnalysisResult } from '@/data/mockData';

// Reactive data
const alert = ref(mockAlert);
const metrics = ref(mockMetrics);
const sreAgent = ref(mockSREAgent);
const analysisResult = ref(mockAnalysisResult);

// Component lifecycle
onMounted(() => {
  console.log('Dashboard mounted with mock data');
});
</script>
