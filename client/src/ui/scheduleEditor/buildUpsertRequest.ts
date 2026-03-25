import type { ScheduleType, TaskParamDefinition, UpsertScheduleRequest } from '../../api/types'
import { fromDatetimeLocalToIso } from './datetime'

export type ScheduleFormState = {
  name: string
  enabled: boolean
  taskKey: string
  scheduleType: ScheduleType
  startAt: string
  intervalValue: number
  intervalUnit: string
  weeklyDays: string
  weeklyTime: string
  cronExpression: string
  params: Record<string, unknown>
}

export function buildUpsertRequest(
  form: ScheduleFormState,
  paramDefs: TaskParamDefinition[]
): UpsertScheduleRequest {
  const name = form.name.trim()
  if (!name) throw new Error('Name is required')

  validateRequiredParams(paramDefs, form.params)

  const base: UpsertScheduleRequest = {
    name,
    enabled: form.enabled,
    taskKey: form.taskKey,
    scheduleType: form.scheduleType,
    taskParamsJson: JSON.stringify(form.params ?? {}),
  }

  switch (form.scheduleType) {
    case 'ONCE':
      if (!form.startAt) throw new Error('Start date/time is required for a one-time schedule')
      return { ...base, startAt: fromDatetimeLocalToIso(form.startAt) }

    case 'INTERVAL':
      if (!form.intervalValue || form.intervalValue <= 0) {
        throw new Error('Interval must be greater than zero')
      }
      return {
        ...base,
        intervalValue: form.intervalValue,
        intervalUnit: form.intervalUnit,
      }

    case 'WEEKLY':
      if (!form.weeklyDays.trim()) throw new Error('Days are required (e.g. 1,3,5 for Mon/Wed/Fri)')
      if (!form.weeklyTime.trim()) throw new Error('Time is required (HH:mm)')
      return {
        ...base,
        weeklyDays: form.weeklyDays,
        weeklyTime: form.weeklyTime,
      }

    case 'CRON':
      if (!form.cronExpression.trim()) throw new Error('Cron expression is required')
      return { ...base, cronExpression: form.cronExpression }

    default:
      throw new Error('Unsupported schedule type')
  }
}

function validateRequiredParams(defs: TaskParamDefinition[], values: Record<string, unknown>) {
  for (const def of defs) {
    if (!def.required) continue
    const v = values[def.name]
    if (v === null || v === undefined || String(v).trim() === '') {
      throw new Error(`Missing required parameter: ${def.name}`)
    }
  }
}
