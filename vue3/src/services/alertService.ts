// Alert API service for connecting with backend AlertController
const API_BASE_URL = 'http://localhost:8080/api/alerts'

export interface AlertType {
  current: string
  available: string[]
}

export const alertService = {
  // Get current alert type
  async getCurrentAlertType(): Promise<string> {
    try {
      const response = await fetch(`${API_BASE_URL}/types/current`)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      return await response.text()
    } catch (error) {
      console.error('Error fetching current alert type:', error)
      throw error
    }
  },

  // Set alert type
  async setAlertType(alertType: string): Promise<string> {
    try {
      const response = await fetch(`${API_BASE_URL}/types/set?alertType=${encodeURIComponent(alertType)}`)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      return await response.text()
    } catch (error) {
      console.error('Error setting alert type:', error)
      throw error
    }
  },

  // Get all available alert types
  async getAvailableAlertTypes(): Promise<string[]> {
    try {
      const response = await fetch(`${API_BASE_URL}/types/available`)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      return await response.json()
    } catch (error) {
      console.error('Error fetching available alert types:', error)
      throw error
    }
  }
}
