const API_BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080'

export async function http<T>(path: string, init?: RequestInit): Promise<T> {
  const method = (init?.method ?? 'GET').toUpperCase()

  const res = await fetch(`${API_BASE}${path}`, {
    ...init,
    cache: 'no-store',
    headers: {
      'Content-Type': 'application/json',
      ...(init?.headers ?? {}),
    },
  })

  // Always read body once as text — never use res.json() (breaks on empty body).
  const bodyText = await res.text()

  if (!res.ok) {
    let msg = `${res.status} ${res.statusText}`
    if (bodyText.trim()) {
      try {
        const data = JSON.parse(bodyText) as { message?: string }
        if (data?.message) msg = data.message
      } catch {
        // non-JSON error body
      }
    }
    throw new Error(msg)
  }

  if (res.status === 204 || res.status === 205) return undefined as T
  if (method === 'DELETE') return undefined as T
  if (!bodyText.trim()) return undefined as T

  try {
    return JSON.parse(bodyText) as T
  } catch {
    throw new Error(`Invalid JSON from ${path}`)
  }
}
