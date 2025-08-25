<template>
  <div class="bg-white rounded-lg shadow p-6">
    <!-- Task Execution Header -->
    <div class="flex items-center justify-between mb-6">
      <h3 class="text-lg font-medium text-gray-900">SRE 诊断Agent</h3>
      <div class="flex items-center space-x-3">
        <div class="flex flex-col items-end space-y-1">
          <span :class="getStatusClass(executionStatus)">
            {{ executionStatus }}
          </span>
          <span v-if="currentStatus === 'processing'" class="text-xs text-gray-500">
            {{ currentPlanId ? `Plan ID: ${currentPlanId}` : 'Processing...' }}
          </span>
          <span v-if="currentStatus === 'completed'" class="text-xs text-green-600 font-medium">
            ✅ Execution Completed
          </span>
        </div>
        <div class="flex space-x-2">
          <button 
            @click="executeTask"
            :disabled="isExecuting"
            class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
                      <span v-if="isExecuting">🔄 AI诊断中...</span>
          <span v-else>🤖 点我进行AI诊断</span>
          </button>
          
          <button 
            v-if="currentStatus === 'completed' || currentStatus === 'processing'"
            @click="resetExecution"
            class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            🔄 Reset
          </button>
        </div>
      </div>
    </div>

    <!-- Hidden Task Input -->
    <input 
      v-model="planTemplateId"
      type="hidden"
    />


    <!-- Parsed Output -->
    <div v-if="parsedOutput" class="mb-6">
      <h4 class="text-sm font-medium text-gray-900 mb-3">诊断建议</h4>
      <div class="bg-green-50 rounded-md p-4">
        <div class="text-sm text-green-700 prose prose-sm max-w-none" v-html="renderedMarkdown"></div>
      </div>
    </div>

    <!-- Think-Act Records with ActToolInfo -->
    <div v-if="thinkActRecords.length > 0">
      <h4 class="text-sm font-medium text-gray-900 mb-3">思考过程：</h4>
      <div class="space-y-4">
        <div 
          v-for="(record, index) in thinkActRecords" 
          :key="record.id || index"
          class="border border-gray-200 rounded-lg p-4"
        >
          <div class="flex items-center justify-between mb-3">
            <h5 class="text-sm font-medium text-gray-900">
              思考轮次 ： #{{ index + 1 }}
            </h5>
            <span :class="getStatusClass(record.status)">
              {{ record.status }}
            </span>
          </div>
          
          <!-- ActToolInfo Display -->
          <div v-if="record.actToolInfoList && record.actToolInfoList.length > 0" class="space-y-3">
            <div 
              v-for="(toolInfo, toolIndex) in record.actToolInfoList" 
              :key="toolInfo.id || toolIndex"
              class="bg-blue-50 rounded-md p-3"
            >
              <h6 class="text-sm font-medium text-blue-900 mb-2">工具调用 #{{ toolIndex + 1 }}</h6>
              
              <div class="grid grid-cols-1 gap-2 text-sm">
                <div>
                  <span class="font-medium text-gray-700">工具名:</span>
                  <span class="ml-2 text-gray-900">{{ toolInfo.name }}</span>
                </div>
                
                <div>
                  <span class="font-medium text-gray-700">参数列表:</span>
                  <span class="ml-2 text-gray-900">{{ toolInfo.parameters }}</span>
                </div>
                
                <div v-if="toolInfo.result">
                  <span class="font-medium text-gray-700">Result:</span>
                  <div class="mt-1 p-2 bg-white rounded border text-gray-800 text-xs">
                    <div v-html="formatToolResult(toolInfo.result)"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div v-else class="text-gray-500 text-sm italic">
            No tool execution information available
          </div>
        </div>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="isExecuting" class="text-center py-8">
      <div class="inline-flex items-center px-4 py-2 text-sm text-gray-600">
        <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        Executing task... Please wait
      </div>
    </div>

    <!-- Error State -->
    <div v-if="error" class="mt-4 p-4 bg-red-50 border border-red-200 rounded-md">
      <div class="flex">
        <div class="flex-shrink-0">
          <svg class="h-5 w-5 text-red-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
          </svg>
        </div>
        <div class="ml-3">
          <h3 class="text-sm font-medium text-red-800">Error</h3>
          <div class="mt-2 text-sm text-red-700">{{ error }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onUnmounted } from 'vue'
import { executionDataService } from '@/services/executionDataService'
import type { ThinkActRecord } from '@/services/executionDataService'
import { marked } from 'marked'

// Configure marked for better rendering
marked.setOptions({
  breaks: true,
  gfm: true
})

// Reactive data
const planTemplateId = ref('planTemplate-1756109892045')
const isExecuting = ref(false)
const executionResult = ref<string | null>(null)
const parsedOutput = ref<string | null>(null)
const thinkActRecords = ref<ThinkActRecord[]>([])
const error = ref<string | null>(null)
const currentPlanId = ref<string | null>(null)
const currentStatus = ref<string>('idle')

// Computed properties
const executionStatus = computed(() => {
  if (isExecuting.value) return 'EXECUTING'
  if (currentStatus.value === 'completed') return 'COMPLETED'
  if (currentStatus.value === 'processing') return 'PROCESSING'
  if (executionResult.value) return 'SUBMITTED'
  return 'IDLE'
})

const renderedMarkdown = computed(() => {
  if (!parsedOutput.value) return ''
  try {
    return marked(parsedOutput.value)
  } catch (e) {
    console.error('Error rendering markdown:', e)
    return parsedOutput.value
  }
})

const formatToolResult = (result: string): string => {
  try {
    // Parse the JSON result
    const parsed = JSON.parse(result)
    
    // Extract the output field if it exists
    let content = parsed.output || result
    
    // If content is still a string, try to parse it as JSON again
    if (typeof content === 'string') {
      try {
        const innerParsed = JSON.parse(content)
        if (Array.isArray(innerParsed) && innerParsed.length > 0 && innerParsed[0].text) {
          content = innerParsed[0].text
        }
      } catch (e) {
        // If inner parsing fails, use the content as is
      }
    }
    
          // Clean up escape characters and format the content
      if (typeof content === 'string') {
        console.log('Original content:', content)
        
        // Remove HTML tags if present
        let cleanContent = content.replace(/<[^>]*>/g, '')
        
        // Replace escaped characters - handle literal \n strings
        cleanContent = cleanContent
          .replace(/\\n/g, '\n')  // Replace literal \n with actual newlines
          .replace(/\\"/g, '"')
          .replace(/^"|"$/g, '')
        
        console.log('After cleaning:', cleanContent)
        
        // Convert actual newlines to HTML line breaks
        const result = cleanContent.replace(/\n/g, '<br>')
        console.log('After replacing \\n:', result)
        
        return result
      }
    
    return JSON.stringify(content, null, 2)
  } catch (e) {
    console.error('Error formatting tool result:', e)
    return result
  }
}

// Methods
const getStatusClass = (status: string) => {
  const classMap: Record<string, string> = {
    'IDLE': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-800',
    'EXECUTING': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800',
    'PROCESSING': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800',
    'SUBMITTED': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800',
    'COMPLETED': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800',
    'RUNNING': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800',
    'FINISHED': 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-green-100 text-green-800'
  }
  return classMap[status] || 'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-gray-100 text-gray-800'
}

const parseOutput = (output: string): string => {
  try {
    // Remove the outer quotes and escape characters
    let cleanedOutput = output.replace(/^"|"$/g, '').replace(/\\"/g, '"').replace(/\\\\n/g, '\n')
    
    // Try to parse as JSON if it looks like JSON
    if (cleanedOutput.startsWith('[') || cleanedOutput.startsWith('{')) {
      try {
        const parsed = JSON.parse(cleanedOutput)
        if (Array.isArray(parsed) && parsed.length > 0 && parsed[0].text) {
          cleanedOutput = parsed[0].text
        }
      } catch (e) {
        // If JSON parsing fails, use the cleaned string as is
      }
    }
    
    return cleanedOutput
  } catch (e) {
    return output
  }
}

const executeTask = async () => {
  if (!planTemplateId.value.trim()) {
    error.value = 'Please enter a plan template ID'
    return
  }

  try {
    console.log('🚀 Starting task execution for plan template:', planTemplateId.value)
    isExecuting.value = true
    error.value = null
    executionResult.value = null
    parsedOutput.value = null
    thinkActRecords.value = []
    currentPlanId.value = null
    currentStatus.value = 'idle'

    // Execute the task
    console.log('📤 Submitting task to backend...')
    const result = await executionDataService.executePlanByTemplate(planTemplateId.value)
    console.log('✅ Task submitted successfully:', result)
    executionResult.value = JSON.stringify(result, null, 2)

    // Extract plan ID and status from result
    currentPlanId.value = result.planId
    currentStatus.value = result.status
    console.log('📋 Plan ID assigned:', currentPlanId.value, 'Status:', currentStatus.value)
    
    // Start monitoring the execution
    console.log('🔍 Starting execution monitoring...')
    startExecutionMonitoring()

  } catch (err) {
    console.error('❌ Task execution failed:', err)
    error.value = err instanceof Error ? err.message : 'Failed to execute task'
  } finally {
    isExecuting.value = false
  }
}

const fetchExecutionDetails = async () => {
  if (!currentPlanId.value) {
    console.log('⚠️ No plan ID available, skipping fetch')
    return
  }

  try {
    console.log('🔍 Fetching execution details for plan:', currentPlanId.value)
    const planRecord = await executionDataService.getExecutionDetails(currentPlanId.value)
    console.log('📊 Execution details received:', {
      planId: planRecord.currentPlanId,
      completed: planRecord.completed,
      currentStep: planRecord.currentStepIndex,
      totalSteps: planRecord.steps?.length || 0,
      agentCount: planRecord.agentExecutionSequence?.length || 0
    })
    
    // Extract all ThinkActRecord from all agents
    const allThinkActRecords: ThinkActRecord[] = []
    
    if (planRecord.agentExecutionSequence) {
      planRecord.agentExecutionSequence.forEach((agent, agentIndex) => {
        console.log(`🤖 Agent ${agentIndex + 1}:`, {
          name: agent.agentName,
          status: agent.status,
          currentStep: agent.currentStep,
          maxSteps: agent.maxSteps,
          thinkActStepsCount: agent.thinkActSteps?.length || 0
        })
        
        if (agent.thinkActSteps) {
          allThinkActRecords.push(...agent.thinkActSteps)
        }
      })
    }
    
    thinkActRecords.value = allThinkActRecords
    console.log('📝 Total ThinkActRecords found:', allThinkActRecords.length)

    // Parse output if available
    if (planRecord.summary) {
      console.log('📄 Summary available, parsing output...')
      parsedOutput.value = parseOutput(planRecord.summary)
      console.log('✅ Output parsed successfully')
    }

    // Check if execution is completed
    if (planRecord.completed) {
      console.log('🎉 Plan execution completed!')
      stopExecutionMonitoring()
      currentStatus.value = 'completed'
    }

  } catch (err) {
    console.error('❌ Error fetching execution details:', err)
    // Don't show error to user as this is just for monitoring
  }
}

// Execution monitoring variables
let monitoringInterval: NodeJS.Timeout | null = null
let isMonitoring = false

const startExecutionMonitoring = () => {
  if (isMonitoring) {
    console.log('⚠️ Execution monitoring already active')
    return
  }
  
  console.log('🔍 Starting execution monitoring...')
  isMonitoring = true
  
  // Start monitoring after a longer delay to allow backend to process
  setTimeout(() => {
    fetchExecutionDetails()
  }, 5000) // Wait 5 seconds before first check
  
  // Set up periodic monitoring every 5 seconds
  monitoringInterval = setInterval(() => {
    if (currentPlanId.value && !isExecuting.value) {
      console.log('⏰ Periodic execution check...')
      fetchExecutionDetails()
    } else {
      console.log('⚠️ Skipping periodic check - no plan ID or execution in progress')
    }
  }, 5000) // Check every 5 seconds
  
  console.log('✅ Execution monitoring started with 5-second interval')
}

const stopExecutionMonitoring = () => {
  if (!isMonitoring) {
    console.log('⚠️ Execution monitoring not active')
    return
  }
  
  console.log('🛑 Stopping execution monitoring...')
  isMonitoring = false
  
  if (monitoringInterval) {
    clearInterval(monitoringInterval)
    monitoringInterval = null
    console.log('✅ Monitoring interval cleared')
  }
  
  console.log('✅ Execution monitoring stopped')
}

const resetExecution = () => {
  console.log('🔄 Resetting execution state...')
  
  // Stop monitoring
  stopExecutionMonitoring()
  
  // Reset all state
  executionResult.value = null
  parsedOutput.value = null
  thinkActRecords.value = []
  error.value = null
  currentPlanId.value = null
  currentStatus.value = 'idle'
  
  console.log('✅ Execution state reset complete')
}

// Clean up monitoring when component is unmounted
onUnmounted(() => {
  console.log('🧹 Component unmounting, cleaning up monitoring...')
  stopExecutionMonitoring()
})
</script>
