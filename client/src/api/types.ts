export type ParamType = 'STRING' | 'NUMBER' | 'BOOLEAN'

export type TaskParamDefinition = {
  name: string
  type: ParamType
  required: boolean
}

export type TaskDto = {
  key: string
  displayName: string
  params: TaskParamDefinition[]
}

export type ScheduleType = 'ONCE' | 'INTERVAL' | 'WEEKLY' | 'CRON'

export type ScheduleDto = {
  id: number
  name: string
  enabled: boolean
  taskKey: string
  scheduleType: ScheduleType
  startAt: string | null
  intervalValue: number | null
  intervalUnit: string | null
  weeklyDays: string | null
  weeklyTime: string | null
  cronExpression: string | null
  taskParamsJson: string | null
  createdAt: string
  updatedAt: string
}

export type UpsertScheduleRequest = {
  name: string
  enabled: boolean
  taskKey: string
  scheduleType: ScheduleType
  startAt?: string | null
  intervalValue?: number | null
  intervalUnit?: string | null
  weeklyDays?: string | null
  weeklyTime?: string | null
  cronExpression?: string | null
  taskParamsJson?: string | null
}

