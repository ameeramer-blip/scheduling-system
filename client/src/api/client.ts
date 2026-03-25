import { http } from './http'
import type { ScheduleDto, TaskDto, UpsertScheduleRequest } from './types'

export const api = {
  tasks: () => http<TaskDto[]>('/api/tasks'),
  schedules: () => http<ScheduleDto[]>('/api/schedules'),
  createSchedule: (req: UpsertScheduleRequest) =>
    http<ScheduleDto>('/api/schedules', { method: 'POST', body: JSON.stringify(req) }),
  updateSchedule: (id: number, req: UpsertScheduleRequest) =>
    http<ScheduleDto>(`/api/schedules/${id}`, { method: 'PUT', body: JSON.stringify(req) }),
  deleteSchedule: (id: number) => http<void>(`/api/schedules/${id}`, { method: 'DELETE' }),
}

