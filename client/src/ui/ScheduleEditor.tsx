import { useEffect, useMemo, useState } from 'react'
import { api } from '../api/client'
import type { ScheduleDto, ScheduleType, TaskDto } from '../api/types'
import { safeJsonParseObject } from './format'
import { buildUpsertRequest, type ScheduleFormState } from './scheduleEditor/buildUpsertRequest'
import { isoToDatetimeLocal } from './scheduleEditor/datetime'
import { ScheduleTimingSection } from './scheduleEditor/ScheduleTimingSection'
import { defaultValueForParamType, TaskParamFields } from './scheduleEditor/TaskParamFields'

type Props = {
  tasks: TaskDto[]
  initial: ScheduleDto | null
  onCancel: () => void
  onSaved: () => void
}

export function ScheduleEditor(props: Props) {
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const [name, setName] = useState(props.initial?.name ?? '')
  const [enabled, setEnabled] = useState(props.initial?.enabled ?? true)
  const [taskKey, setTaskKey] = useState(props.initial?.taskKey ?? props.tasks[0]?.key ?? 'LOG')
  const [scheduleType, setScheduleType] = useState<ScheduleType>(props.initial?.scheduleType ?? 'INTERVAL')

  const [startAt, setStartAt] = useState(isoToDatetimeLocal(props.initial?.startAt ?? null))
  const [intervalValue, setIntervalValue] = useState<number>(props.initial?.intervalValue ?? 1)
  const [intervalUnit, setIntervalUnit] = useState(props.initial?.intervalUnit ?? 'MINUTES')
  const [weeklyDays, setWeeklyDays] = useState(props.initial?.weeklyDays ?? '1,3,5')
  const [weeklyTime, setWeeklyTime] = useState(props.initial?.weeklyTime ?? '09:00')
  const [cronExpression, setCronExpression] = useState(props.initial?.cronExpression ?? '0 */1 * ? * *')

  const [params, setParams] = useState<Record<string, unknown>>(() => safeJsonParseObject(props.initial?.taskParamsJson))

  const selectedTask = useMemo(() => props.tasks.find((t) => t.key === taskKey) ?? null, [props.tasks, taskKey])

  useEffect(() => {
    const def = props.tasks.find((t) => t.key === taskKey)
    if (!def) return
    setParams((prev) => {
      const next = { ...prev }
      for (const p of def.params) {
        if (next[p.name] === undefined) {
          next[p.name] = defaultValueForParamType(p.type)
        }
      }
      return next
    })
  }, [taskKey, props.tasks])

  async function handleSave() {
    setSaving(true)
    setError(null)
    try {
      const form: ScheduleFormState = {
        name,
        enabled,
        taskKey,
        scheduleType,
        startAt,
        intervalValue,
        intervalUnit,
        weeklyDays,
        weeklyTime,
        cronExpression,
        params,
      }

      const req = buildUpsertRequest(form, selectedTask?.params ?? [])

      if (props.initial) {
        await api.updateSchedule(props.initial.id, req)
      } else {
        await api.createSchedule(req)
      }
      props.onSaved()
    } catch (err) {
      setError(err instanceof Error ? err.message : String(err))
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="editor">
      {error ? <div className="error mb-10">{error}</div> : null}

      <div className="grid">
        <div>
          <label htmlFor="schedule-name">Name</label>
          <input
            id="schedule-name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="e.g. Morning log"
          />
        </div>

        <div>
          <span className="label-block">Enabled</span>
          <div className="checkbox-row">
            <input
              id="schedule-enabled"
              type="checkbox"
              checked={enabled}
              onChange={(e) => setEnabled(e.target.checked)}
            />
            <label htmlFor="schedule-enabled">Run when enabled</label>
          </div>
        </div>

        <div>
          <label htmlFor="schedule-task">Task</label>
          <select id="schedule-task" value={taskKey} onChange={(e) => setTaskKey(e.target.value)}>
            {props.tasks.map((t) => (
              <option key={t.key} value={t.key}>
                {t.displayName}
              </option>
            ))}
          </select>
          <p className="field-hint">{selectedTask ? `Key: ${selectedTask.key}` : 'Loading tasks…'}</p>
        </div>

        <div>
          <label htmlFor="schedule-type">How often</label>
          <select
            id="schedule-type"
            value={scheduleType}
            onChange={(e) => setScheduleType(e.target.value as ScheduleType)}
          >
            <option value="ONCE">One-time</option>
            <option value="INTERVAL">Every N minutes or hours</option>
            <option value="WEEKLY">Weekly</option>
            <option value="CRON">Cron (advanced)</option>
          </select>
        </div>
      </div>

      <ScheduleTimingSection
        scheduleType={scheduleType}
        startAt={startAt}
        onStartAtChange={setStartAt}
        intervalValue={intervalValue}
        onIntervalValueChange={setIntervalValue}
        intervalUnit={intervalUnit}
        onIntervalUnitChange={setIntervalUnit}
        weeklyDays={weeklyDays}
        onWeeklyDaysChange={setWeeklyDays}
        weeklyTime={weeklyTime}
        onWeeklyTimeChange={setWeeklyTime}
        cronExpression={cronExpression}
        onCronExpressionChange={setCronExpression}
      />

      <section className="card mt-12">
        <h2 className="section-title">Task parameters</h2>
        {selectedTask ? (
          <TaskParamFields definitions={selectedTask.params} values={params} onChange={setParams} />
        ) : (
          <p className="muted mb-0">No task selected.</p>
        )}
      </section>

      <div className="row row-end mt-12">
        <button type="button" onClick={props.onCancel} disabled={saving}>
          Cancel
        </button>
        <button type="button" className="primary" onClick={handleSave} disabled={saving}>
          {saving ? 'Saving…' : 'Save'}
        </button>
      </div>
    </div>
  )
}
