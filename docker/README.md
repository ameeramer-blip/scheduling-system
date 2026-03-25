# Docker (delivery)

This folder holds **`docker-compose.yml`** for the full stack: MySQL, Spring Boot API, React UI (nginx).

From the **repository root** (`MEMCYCO/`):

```powershell
docker compose -f docker/docker-compose.yml up --build
```

From **`docker/`**:

```powershell
docker compose up --build
```

Dockerfiles live next to each component: `../server/Dockerfile`, `../client/Dockerfile`.

See the root **`README.md`** for design notes and assumptions.
