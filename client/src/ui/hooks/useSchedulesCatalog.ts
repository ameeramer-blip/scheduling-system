import { useCallback, useEffect, useState } from 'react'
import { api } from '../../api/client'
import type { ScheduleDto, TaskDto } from '../../api/types'

export type SchedulesCatalogState = {
  tasks: TaskDto[]
  schedules: ScheduleDto[]
  loading: boolean
  error: string | null
  refresh: () => Promise<void>
}

/**
 * Loads predefined tasks + saved schedules from the API.
 */
export function useSchedulesCatalog(): SchedulesCatalogState {
  const [tasks, setTasks] = useState<TaskDto[]>([])
  const [schedules, setSchedules] = useState<ScheduleDto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const refresh = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const [taskList, scheduleList] = await Promise.all([api.tasks(), api.schedules()])
      setTasks(taskList)
      setSchedules(scheduleList)
    } catch (err) {
      setError(err instanceof Error ? err.message : String(err))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void refresh()
  }, [refresh])

  return { tasks, schedules, loading, error, refresh }
}
