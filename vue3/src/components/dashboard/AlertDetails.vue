<template>
  <div class="bg-white rounded-lg shadow p-6">
    <!-- Alert Header -->
    <div class="flex items-center justify-between mb-4">
      <div class="flex items-center space-x-3">
        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
          高
        </span>
        <h3 class="text-lg font-medium text-gray-900">{{ alert.title }}</h3>
      </div>
      <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
        {{ getStatusText(alert.status) }}
      </span>
    </div>

    <!-- Basic Information -->
    <div class="grid grid-cols-2 gap-4 mb-6">
      <div>
        <dt class="text-sm font-medium text-gray-500">告警渠道</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ alert.channel }}</dd>
      </div>
      <div>
        <dt class="text-sm font-medium text-gray-500">应用</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ alert.application }}</dd>
      </div>
      <div>
        <dt class="text-sm font-medium text-gray-500">应急场景</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ alert.scenario }}</dd>
      </div>
      <div>
        <dt class="text-sm font-medium text-gray-500">服务组</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ alert.serviceGroup }}</dd>
      </div>
      <div class="col-span-2">
        <dt class="text-sm font-medium text-gray-500">首次告警时间</dt>
        <dd class="mt-1 text-sm text-gray-900">{{ alert.firstAlertTime }}</dd>
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
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警对象</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警规则</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">最近告警时间</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警事件数</th>
              <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">告警事件内容</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="machine in alert.affectedMachines" :key="machine.ip" class="hover:bg-gray-50">
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">
                <div>
                  <div class="font-medium">{{ machine.ip }}</div>
                  <div class="text-gray-500">{{ machine.applicationGroup }}</div>
                </div>
              </td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ machine.alertRule }}</td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ machine.latestAlertTime }}</td>
              <td class="px-3 py-2 whitespace-nowrap text-sm text-gray-900">{{ machine.alertEventCount }}</td>
              <td class="px-3 py-2 text-sm text-gray-900 max-w-xs">
                <div class="truncate" :title="machine.alertEventContent">
                  {{ machine.alertEventContent }}
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Alert } from '@/types/dashboard';

interface Props {
  alert: Alert;
}

defineProps<Props>();

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'pending': '待接手',
    'processing': '处理中',
    'resolved': '已解决'
  };
  return statusMap[status] || status;
};
</script>
