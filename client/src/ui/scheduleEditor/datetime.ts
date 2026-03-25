/** Converts an ISO instant from the API into `datetime-local` value (local browser time). */
export function isoToDatetimeLocal(iso: string | null): string {
  if (!iso) return ''
  const d = new Date(iso)
  if (Number.isNaN(d.getTime())) return ''
  const pad = (n: number) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

/** `datetime-local` has no timezone; `Date` interprets it in local time, then we send UTC ISO to the API. */
export function fromDatetimeLocalToIso(local: string): string {
  const d = new Date(local)
  if (Number.isNaN(d.getTime())) throw new Error('Invalid start date/time')
  return d.toISOString()
}
