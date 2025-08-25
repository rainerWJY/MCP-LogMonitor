// Alert interface
export interface Alert {
  id: string;
  priority: 'high' | 'medium' | 'low';
  title: string;
  status: 'pending' | 'processing' | 'resolved';
  channel: string;
  application: string;
  scenario: string;
  serviceGroup: string;
  firstAlertTime: string;
  affectedMachines: AffectedMachine[];
}

// Affected machine interface
export interface AffectedMachine {
  ip: string;
  applicationGroup: string;
  alertRule: string;
  latestAlertTime: string;
  alertEventCount: number;
  alertEventContent: string;
}

// Monitoring metric interface
export interface MonitoringMetric {
  ip: string;
  relatedChanges: number;
  relatedAlerts: number;
  anomalyClusters: number;
}

// SRE Agent interface
export interface SREAgent {
  name: string;
  status: 'idle' | 'thinking' | 'completed';
  currentTask: string;
  steps: AgentStep[];
}

// Agent step interface
export interface AgentStep {
  id: number;
  status: 'success' | 'processing' | 'failed';
  description: string;
  result?: string;
}

// Analysis result interface
export interface AnalysisResult {
  keyEvidence: string[];
  historicalSummary: string;
  relatedInfo: string;
}




