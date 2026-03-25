import type { ReactNode } from 'react'

type Props = {
  title: string
  children: ReactNode
  onClose: () => void
  footer?: ReactNode
}

export function Modal(props: Props) {
  return (
    <div className="modal-backdrop" onMouseDown={props.onClose} role="presentation">
      <div className="modal" onMouseDown={(e) => e.stopPropagation()} role="dialog" aria-labelledby="modal-title">
        <div className="row-between">
          <h2 id="modal-title" className="modal-title">
            {props.title}
          </h2>
          <button type="button" onClick={props.onClose}>
            Close
          </button>
        </div>
        <div className="modal-body">{props.children}</div>
        {props.footer ? <div className="modal-footer">{props.footer}</div> : null}
      </div>
    </div>
  )
}
