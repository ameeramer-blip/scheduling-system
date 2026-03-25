import type { ScheduleDto, TaskDto } from '../api/types'
import { formatScheduleSummary, safeJsonParseObject, safeJsonString } from './format'

type Props = {
  schedules: ScheduleDto[]
  taskByKey: Map<string, TaskDto>
  onEdit: (row: ScheduleDto) => void
  onDelete: (id: number) => void
}

export function ScheduleTable(props: Props) {
  if (props.schedules.length === 0) return null

  return (
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Enabled</th>
          <th>Task</th>
          <th>Schedule</th>
          <th>Params</th>
          <th aria-label="Actions" />
        </tr>
      </thead>
      <tbody>
        {props.schedules.map((row) => (
          <ScheduleRow
            key={row.id}
            row={row}
            task={props.taskByKey.get(row.taskKey)}
            onEdit={() => props.onEdit(row)}
            onDelete={() => props.onDelete(row.id)}
          />
        ))}
      </tbody>
    </table>
  )
}

function ScheduleRow(props: {
  row: ScheduleDto
  task: TaskDto | undefined
  onEdit: () => void
  onDelete: () => void
}) {
  const { row } = props
  const displayName = props.task?.displayName ?? row.taskKey

  return (
    <tr>
      <td>
        <div className="cell-title">{row.name}</div>
        <div className="subtle">id={row.id}</div>
      </td>
      <td>
        <span className="pill">{row.enabled ? 'ENABLED' : 'DISABLED'}</span>
      </td>
      <td>
        <div>{displayName}</div>
        <div className="subtle">{row.taskKey}</div>
      </td>
      <td>
        <span className="pill">{row.scheduleType}</span>
        <div className="subtle mt-6">{formatScheduleSummary(row)}</div>
      </td>
      <td>
        <code className="code-inline">
          {safeJsonString(safeJsonParseObject(row.taskParamsJson), row.taskParamsJson ?? '{}')}
        </code>
      </td>
      <td className="table-actions">
        <div className="row row-end">
          <button type="button" onClick={props.onEdit}>
            Edit
          </button>
          <button type="button" className="danger" onClick={props.onDelete}>
            Delete
          </button>
        </div>
      </td>
    </tr>
  )
}
