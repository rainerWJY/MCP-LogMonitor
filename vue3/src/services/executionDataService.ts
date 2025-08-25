
// Execution Data API service for fetching execution details from jmanus
const API_BASE_URL = 'http://localhost:8080/api/executions'

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
  subPlanExecutionRecord?: PlanExecutionRecord
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
      const response = await fetch(`${API_BASE_URL}/details/${encodeURIComponent(planId)}`)
      if (!response.ok) {
        if (response.status === 404) {
          throw new Error(`Plan execution not found: ${planId}`)
        }
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      const data = await response.json()
      
      // Handle error response from backend
      if (data.error) {
        throw new Error(data.error)
      }
      
      return data
    } catch (error) {
      console.error('Error fetching execution details:', error)
      throw error
    }
  },

  // Execute plan by template ID
  async executePlanByTemplate(planTemplateId: string, rawParam?: string): Promise<Map<string, any>> {
    try {
      // Build URL with query parameters
      const url = new URL(`${API_BASE_URL}/execute/${encodeURIComponent(planTemplateId)}`)
      if (rawParam) {
        url.searchParams.append('rawParam', rawParam)
      }
      
      const response = await fetch(url.toString(), {
        method: 'GET'
      })
      
      if (!response.ok) {
        if (response.status === 400) {
          const errorData = await response.json()
          throw new Error(errorData.error || 'Bad request')
        }
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      const data = await response.json()
      
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
