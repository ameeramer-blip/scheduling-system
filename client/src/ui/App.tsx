import { useMemo, useState } from 'react'
import { api } from '../api/client'
import type { ScheduleDto } from '../api/types'
import { useSchedulesCatalog } from './hooks/useSchedulesCatalog'
import { Modal } from './Modal'
import { ScheduleEditor } from './ScheduleEditor'
import { ScheduleTable } from './ScheduleTable'

export function App() {
  const { tasks, schedules, loading, error, refresh } = useSchedulesCatalog()
  const [editorOpen, setEditorOpen] = useState(false)
  const [editing, setEditing] = useState<ScheduleDto | null>(null)

  const taskByKey = useMemo(() => new Map(tasks.map((t) => [t.key, t])), [tasks])

  async function handleDelete(id: number) {
    if (!window.confirm('Delete this schedule?')) return
    try {
      await api.deleteSchedule(id)
      await refresh()
    } catch (err) {
      window.alert(err instanceof Error ? err.message : String(err))
    }
  }

  function openCreate() {
    setEditing(null)
    setEditorOpen(true)
  }

  function openEdit(row: ScheduleDto) {
    setEditing(row)
    setEditorOpen(true)
  }

  const hasRows = schedules.length > 0

  return (
    <div className="container">
      <header className="page-header">
        <div>
          <h1 className="h1">Scheduling System</h1>
          <p className="muted mb-0">Manage when predefined tasks run (backend uses Quartz).</p>
        </div>
        <div className="row">
          <button type="button" onClick={() => void refresh()} disabled={loading}>
            Refresh
          </button>
          <button type="button" className="primary" onClick={openCreate}>
            New schedule
          </button>
        </div>
      </header>

      <div className="card">
        {loading ? <p>Loading…</p> : null}
        {error ? <p className="error">{error}</p> : null}

        {!loading && !hasRows ? <p className="muted mb-0">No schedules yet. Create one.</p> : null}

        <ScheduleTable schedules={schedules} taskByKey={taskByKey} onEdit={openEdit} onDelete={handleDelete} />
      </div>

      {editorOpen ? (
        <Modal
          title={editing ? `Edit schedule #${editing.id}` : 'Create schedule'}
          onClose={() => setEditorOpen(false)}
          footer={<p className="modal-tip">Tip: use “Every N minutes” for a quick demo.</p>}
        >
          <ScheduleEditor
            tasks={tasks}
            initial={editing}
            onCancel={() => setEditorOpen(false)}
            onSaved={async () => {
              setEditorOpen(false)
              await refresh()
            }}
          />
        </Modal>
      ) : null}
    </div>
  )
}
