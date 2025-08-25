// Alert Data API service for fetching detailed alert information
const API_BASE_URL = 'http://localhost:8080/api/alerts'

export interface AlertData {
  alerts: AlertItem[]
  summary?: any
}

export interface AlertItem {
  alertId: string
  application: string
  severity: string
  alertType: string
  title: string
  description: string
  timestamp: string
  status: string
  metrics?: {
    cpuUsage: number
    memoryUsage: number
    diskUsage: number
    networkLatency: number
  }
}

export const alertDataService = {
  // Get detailed alert info for a specific severity as JSON
  async getDetailedAlertInfoAsJson(severity: string): Promise<AlertData> {
    try {
      const response = await fetch(`${API_BASE_URL}/detailed/json?severity=${encodeURIComponent(severity)}`)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      const data = await response.json()
      
      // Handle error response from backend
      if (data.error) {
        throw new Error(data.error)
      }
      
      // Transform the backend response to match our AlertData interface
      return {
        alerts: data.alerts || [],
        summary: {
          totalCount: data.totalCount || 0,
          severity: data.severity,
          application: data.application,
          queryTime: data.queryTime,
          cacheTime: data.cacheTime
        }
      }
    } catch (error) {
      console.error('Error fetching detailed alert info as JSON:', error)
      throw error
    }
  },



  // Refresh alert data
  async refreshAlertData(): Promise<string> {
    try {
      const response = await fetch(`${API_BASE_URL}/refresh`)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      return await response.text()
    } catch (error) {
      console.error('Error refreshing alert data:', error)
      throw error
    }
  }
}
