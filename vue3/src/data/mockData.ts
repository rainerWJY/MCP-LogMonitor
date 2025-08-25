import type { Alert, MonitoringMetric, SREAgent, AnalysisResult } from '@/types/dashboard';

// Mock alert data
export const mockAlert: Alert = {
  id: 'alert-001',
  priority: 'high',
  title: 'wmpooc[内存利用率]_alarmTemplate_内存使用率_应用RULE_1',
  status: 'pending',
  channel: 'sunfire',
  application: 'wmpooc',
  scenario: '其他场景',
  serviceGroup: '系统监控/核心服务/存储/中枢',
  firstAlertTime: '2025-06-23 13:05',
  affectedMachines: [
    {
      ip: '33.4.243.252',
      applicationGroup: 'wmpoochost',
      alertRule: 'alarmTemplate_内存使用率_应用RULE_1',
      latestAlertTime: '25-06-24 16:56',
      alertEventCount: 1510,
      alertEventContent: '内存利用率异常，当前值94.608%，持续2分钟超过94%'
    },
    {
      ip: '33.39.190.115',
      applicationGroup: 'wmpoochost',
      alertRule: 'alarmTemplate_内存使用率_应用RULE_1',
      latestAlertTime: '25-06-24 16:56',
      alertEventCount: 971,
      alertEventContent: '内存利用率异常，当前值94.477%，持续2分钟超过94%'
    },
    {
      ip: '33.7.101.105',
      applicationGroup: 'wmpoochost',
      alertRule: 'alarmTemplate_内存使用率_应用RULE_1',
      latestAlertTime: '25-06-24 16:56',
      alertEventCount: 1346,
      alertEventContent: '内存利用率异常，当前值95.342%，持续2分钟超过94%'
    }
  ]
};

// Mock monitoring metrics
export const mockMetrics: MonitoringMetric[] = [
  { ip: '33.60.142.136', relatedChanges: 0, relatedAlerts: 0, anomalyClusters: 0 },
  { ip: '33.7.214.76', relatedChanges: 0, relatedAlerts: 0, anomalyClusters: 0 },
  { ip: '33.6.216.97', relatedChanges: 0, relatedAlerts: 0, anomalyClusters: 0 },
  { ip: '33.4.243.252', relatedChanges: 0, relatedAlerts: 0, anomalyClusters: 0 },
  { ip: '33.39.190.115', relatedChanges: 0, relatedAlerts: 0, anomalyClusters: 0 }
];

// Mock SRE Agent
export const mockSREAgent: SREAgent = {
  name: 'RootCauseAnalyst',
  status: 'completed',
  currentTask: '协助开发者解决应急预警任务,定位问题根因',
  steps: [
    {
      id: 1,
      status: 'success',
      description: '获取详细告警信息'
    },
    {
      id: 2,
      status: 'success',
      description: '查询历史处理记录，了解类似告警的处理方式'
    },
    {
      id: 3,
      status: 'success',
      description: '查询应用相关变更事件，识别内存利用率异常的可能原因'
    },
    {
      id: 4,
      status: 'success',
      description: '完成调用相关工具，收集信息'
    }
  ]
};

// Mock analysis result
export const mockAnalysisResult: AnalysisResult = {
  keyEvidence: [
    '内存利用率持续超过94%',
    '多台机器同时出现异常',
    '告警事件数量较多'
  ],
  historicalSummary: '任务曾因单机问题导致内存利用率高而完结。',
  relatedInfo: '任务历史记录显示,之前的问题是由单机内存使用过高引起的,但未发现与当前情况直接相关的traceId。'
};
