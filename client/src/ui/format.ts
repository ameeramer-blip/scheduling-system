import type { ScheduleDto } from '../api/types'

export function formatScheduleSummary(s: ScheduleDto): string {
  switch (s.scheduleType) {
    case 'ONCE':
      return s.startAt ? `Start at ${formatIsoLocal(s.startAt)}` : 'Start at (missing)'
    case 'INTERVAL':
      return s.intervalValue && s.intervalUnit
        ? `Every ${s.intervalValue} ${s.intervalUnit.toLowerCase()}`
        : 'Interval (missing)'
    case 'WEEKLY':
      return `${s.weeklyDays ?? '(days?)'} @ ${s.weeklyTime ?? '(time?)'}`
    case 'CRON':
      return s.cronExpression ?? '(missing)'
    default:
      return ''
  }
}

export function formatIsoLocal(iso: string): string {
  const d = new Date(iso)
  if (Number.isNaN(d.getTime())) return iso
  return d.toLocaleString()
}

export function safeJsonString(value: unknown, fallback = '{}'): string {
  try {
    return JSON.stringify(value ?? {}, null, 0)
  } catch {
    return fallback
  }
}

export function safeJsonParseObject(raw: string | null | undefined): Record<string, unknown> {
  if (!raw) return {}
  try {
    const v = JSON.parse(raw) as unknown
    if (v && typeof v === 'object' && !Array.isArray(v)) return v as Record<string, unknown>
    return {}
  } catch {
    return {}
  }
}

