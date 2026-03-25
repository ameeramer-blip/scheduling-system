import type { ScheduleType } from '../../api/types'

type Props = {
  scheduleType: ScheduleType
  startAt: string
  onStartAtChange: (v: string) => void
  intervalValue: number
  onIntervalValueChange: (v: number) => void
  intervalUnit: string
  onIntervalUnitChange: (v: string) => void
  weeklyDays: string
  onWeeklyDaysChange: (v: string) => void
  weeklyTime: string
  onWeeklyTimeChange: (v: string) => void
  cronExpression: string
  onCronExpressionChange: (v: string) => void
}

export function ScheduleTimingSection(props: Props) {
  const t = props.scheduleType

  return (
    <section className="card mt-12">
      <h2 className="section-title">When it runs</h2>

      {t === 'ONCE' && (
        <div className="grid grid-1">
          <div>
            <label htmlFor="start-at">Start (your local time)</label>
            <input
              id="start-at"
              type="datetime-local"
              value={props.startAt}
              onChange={(e) => props.onStartAtChange(e.target.value)}
            />
            <p className="field-hint">The server stores this as an ISO timestamp (UTC).</p>
          </div>
        </div>
      )}

      {t === 'INTERVAL' && (
        <div className="grid">
          <div>
            <label htmlFor="interval-n">Every</label>
            <input
              id="interval-n"
              type="number"
              min={1}
              value={props.intervalValue}
              onChange={(e) => props.onIntervalValueChange(Number(e.target.value))}
            />
          </div>
          <div>
            <label htmlFor="interval-unit">Unit</label>
            <select
              id="interval-unit"
              value={props.intervalUnit}
              onChange={(e) => props.onIntervalUnitChange(e.target.value)}
            >
              <option value="MINUTES">Minutes</option>
              <option value="HOURS">Hours</option>
            </select>
          </div>
        </div>
      )}

      {t === 'WEEKLY' && (
        <div className="grid">
          <div>
            <label htmlFor="weekly-days">Days (1 = Monday … 7 = Sunday)</label>
            <input
              id="weekly-days"
              value={props.weeklyDays}
              onChange={(e) => props.onWeeklyDaysChange(e.target.value)}
              placeholder="1,3,5"
            />
          </div>
          <div>
            <label htmlFor="weekly-time">Time</label>
            <input
              id="weekly-time"
              value={props.weeklyTime}
              onChange={(e) => props.onWeeklyTimeChange(e.target.value)}
              placeholder="09:00"
            />
          </div>
          <p className="field-hint grid-span-2">
            Weekly rules are turned into a Quartz cron in UTC on the server.
          </p>
        </div>
      )}

      {t === 'CRON' && (
        <div className="grid grid-1">
          <div>
            <label htmlFor="cron">Cron (Quartz)</label>
            <input
              id="cron"
              value={props.cronExpression}
              onChange={(e) => props.onCronExpressionChange(e.target.value)}
              placeholder="0 */5 * ? * *"
            />
            <p className="field-hint">Order: second, minute, hour, day-of-month, month, day-of-week.</p>
          </div>
        </div>
      )}
    </section>
  )
}
