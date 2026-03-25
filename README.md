# MEMCYCO — scheduling system

## Repository layout (assignment mapping)

| Required in brief | This repository |
|-------------------|-----------------|
| **/frontend** | **`client/`** — React + TypeScript + Vite |
| **/backend** | **`server/`** — Spring Boot + JPA + Quartz |
| **/docker** | **`docker/`** — `docker-compose.yml` + this folder’s README |

The names `client` and `server` match the code layout used during development. If your reviewer requires literal folder names `frontend` and `backend`, close the IDE and rename:

- `client` → `frontend`
- `server` → `backend`

Then update `docker/docker-compose.yml` build `context` paths to `../frontend` and `../backend`.

---

## How to run the system

### Full stack with Docker Desktop (recommended)

1. Install and start **Docker Desktop**.
2. From this repository root:

```powershell
docker compose up --build
```

(The root `docker-compose.yml` includes `docker/docker-compose.yml`.)

3. Open **http://localhost** (UI) and **http://localhost:8080/api/tasks** (API).

Stop: `Ctrl+C`, then `docker compose down`.

### Backend + MySQL in Docker only (run API on the host)

```powershell
docker compose -f docker/docker-compose.yml up -d mysql
cd server
.\mvnw.cmd spring-boot:run
```

Or: `.\run-server-with-docker-db.ps1` from the repo root.

### Tests (no Docker required)

```powershell
cd server
.\mvnw.cmd test
```

Uses the `h2` profile (in-memory DB).

---

## Design decisions

- **API + UI separation:** REST JSON from Spring Boot; React SPA calls `VITE_API_BASE` (default `http://localhost:8080`). In Docker, the browser still uses `localhost:8080` because requests originate in the browser on the host, not inside the nginx container.
- **Scheduling:** Quartz runs predefined tasks (`LOG`, `DUMMY_EMAIL`) on triggers derived from schedule type (once, interval, weekly, cron). Job store is **in-memory**; schedules are persisted in MySQL and reloaded on startup.
- **MySQL in Docker:** Default credentials (`memcyco` / `memcyco_app`) match `docker/docker-compose.yml`; the API container connects with JDBC URL host **`mysql`** (Docker service name).
- **CORS:** Configured for local dev and Docker UI origins (`localhost`, `127.0.0.1`, Vite port `5173`).

---

## Assumptions

- Docker Desktop is available when using compose; JDK 17+ is available for local Maven runs.
- Port **80** (UI), **8080** (API), and **3306** (MySQL) are free, or you will change `ports` in `docker/docker-compose.yml`.
- `spring.jpa.hibernate.ddl-auto: update` is acceptable for the assignment (schema managed by Hibernate against MySQL).

---

## Git: connect this project to a remote repository

1. **Create an empty repo** on GitHub (or GitLab, etc.) — no README if you will push an existing tree, or allow merge.

2. **In this folder**, if Git is not initialized yet:

```powershell
cd C:\Users\amirA\IdeaProjects\MEMCYCO
git init
```

3. **Add a root `.gitignore`** (ignore `node_modules`, `target`, `.idea`, local env files). **Do not commit** secrets or `application-local.yml`.

4. **First commit and branch:**

```powershell
git add .
git commit -m "Initial commit: scheduling system"
git branch -M main
```

5. **Add remote and push** (replace URL with your repo):

```powershell
git remote add origin https://github.com/YOUR_USER/YOUR_REPO.git
git push -u origin main
```

If GitHub asks for credentials, use a **Personal Access Token** (PAT) instead of a password when using HTTPS.

6. **Clone elsewhere (another machine):**

```powershell
git clone https://github.com/YOUR_USER/YOUR_REPO.git
cd YOUR_REPO
```

---

More detail for the backend: see **`server/README.md`**.
