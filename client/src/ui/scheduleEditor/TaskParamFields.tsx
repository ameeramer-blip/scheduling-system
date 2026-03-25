import type { TaskParamDefinition } from '../../api/types'

type Props = {
  definitions: TaskParamDefinition[]
  values: Record<string, unknown>
  onChange: (next: Record<string, unknown>) => void
}

export function TaskParamFields(props: Props) {
  if (props.definitions.length === 0) {
    return <p className="muted mb-0">This task has no parameters.</p>
  }

  return (
    <div className="grid">
      {props.definitions.map((def) => (
        <div key={def.name}>
          <label htmlFor={`param-${def.name}`}>
            {def.name}
            {def.required ? ' (required)' : ' (optional)'} — {def.type}
          </label>
          <input
            id={`param-${def.name}`}
            value={String(props.values[def.name] ?? '')}
            onChange={(e) =>
              props.onChange({
                ...props.values,
                [def.name]: parseParamValue(def.type, e.target.value),
              })
            }
            placeholder={placeholderFor(def.type)}
          />
        </div>
      ))}
    </div>
  )
}

function placeholderFor(type: TaskParamDefinition['type']): string {
  if (type === 'NUMBER') return '123'
  if (type === 'BOOLEAN') return 'true or false'
  return 'Text'
}

function parseParamValue(type: TaskParamDefinition['type'], raw: string): unknown {
  if (type === 'NUMBER') {
    const n = Number(raw)
    return Number.isFinite(n) ? n : raw
  }
  if (type === 'BOOLEAN') {
    const lower = raw.toLowerCase()
    if (lower === 'true') return true
    if (lower === 'false') return false
    return raw
  }
  return raw
}

export function defaultValueForParamType(type: TaskParamDefinition['type']): unknown {
  switch (type) {
    case 'NUMBER':
      return 0
    case 'BOOLEAN':
      return false
    case 'STRING':
    default:
      return ''
  }
}
