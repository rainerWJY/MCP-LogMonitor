
// Execution Data API service for fetching execution details from jmanus
const API_BASE_URL = 'http://localhost:18080/api'

export interface ExecutionData {
  planExecutionRecords: PlanExecutionRecord[]
  summary?: ExecutionSummary
}

export interface ExecutionSummary {
  totalPlans: number
  completedPlans: number
  runningPlans: number
  totalAgents: number
  totalThinkActSteps: number
  queryTime: string
}


export interface PlanExecutionRecord {
  id: number
  currentPlanId: string
  rootPlanId?: string
  thinkActRecordId?: number
  title: string
  userRequest: string
  startTime: string
  endTime?: string
  steps: string[]
  currentStepIndex: number
  completed: boolean
  summary?: string
  agentExecutionSequence: AgentExecutionRecord[]
  modelName: string
}

export interface AgentExecutionRecord {
  id: number
  conversationId: string
  agentName: string
  agentDescription: string
  startTime: string
  endTime?: string
  maxSteps: number
  currentStep: number
  status: ExecutionStatus
  thinkActSteps: ThinkActRecord[]
  agentRequest: string
  result?: string
  errorMessage?: string
  modelName: string
}

export interface ThinkActRecord {
  id: number
  parentExecutionId: number
  thinkStartTime: string
  thinkEndTime?: string
  actStartTime?: string
  actEndTime?: string
  thinkInput: string
  thinkOutput?: string
  actionNeeded: boolean
  actionDescription?: string
  actionResult?: string
  status: ExecutionStatus
  errorMessage?: string
  toolName?: string
  toolParameters?: string
  actToolInfoList: ActToolInfo[]
}

export interface ActToolInfo {
  name: string
  parameters: string
  result?: string
  id: string
}

export enum ExecutionStatus {
  IDLE = 'IDLE',
  RUNNING = 'RUNNING',
  FINISHED = 'FINISHED'
}

export const executionDataService = {
  // Get execution details for a specific plan ID
  async getExecutionDetails(planId: string): Promise<PlanExecutionRecord> {
    try {
      // Use the correct endpoint from backend: /api/executor/details/{planId}
      const url = `${API_BASE_URL}/executor/details/${encodeURIComponent(planId)}`
      console.log('🔍 Fetching execution details from:', url)
      
      const response = await fetch(url)
      if (!response.ok) {
        if (response.status === 404) {
          throw new Error(`Plan execution not found: ${planId}`)
        }
        if (response.status === 500) {
          console.error('❌ Backend internal server error for plan:', planId)
          throw new Error(`Backend internal server error: ${response.status}`)
        }
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      const data = await response.json()
      
      // Handle error response from backend
      if (data.error) {
        throw new Error(data.error)
      }
      
      console.log('✅ Execution details fetched successfully for plan:', planId)
      return data
    } catch (error) {
      console.error('Error fetching execution details:', error)
      throw error
    }
    },

  // Execute plan by template ID using POST method
  async executePlanByTemplate(planTemplateId: string, rawParam?: string): Promise<{
    planId: string
    status: string
    message: string
  }> {
    try {
      // Use POST method with /executePlanByTemplateId endpoint
      const url = `${API_BASE_URL}/plan-template/executePlanByTemplateId`
      
      // Prepare request body as expected by backend
      const requestBody: any = {
        planTemplateId: planTemplateId
      }
      
      if (rawParam) {
        requestBody.rawParam = rawParam
      }
      
      console.log('🌐 Making POST request to:', url)
      console.log('📤 Request body:', requestBody)
      
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      })
      
      if (!response.ok) {
        if (response.status === 400) {
          const errorData = await response.json()
          throw new Error(errorData.error || 'Bad request')
        }
        if (response.status === 404) {
          throw new Error('Plan template not found')
        }
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      const data = await response.json()
      console.log('📥 Response received:', data)
      
      if (data.error) {
        throw new Error(data.error)
      }
      
      return data
    } catch (error) {
      console.error('Error executing plan by template:', error)
      throw error
    }
  }
}
