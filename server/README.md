## Server (Spring Boot + Quartz)

### Full stack on Docker Desktop (UI + API + MySQL)

From the **MEMCYCO** repo root:

```powershell
docker compose up --build
```

- **UI:** http://localhost (port 80)
- **API:** http://localhost:8080/api/tasks

Stop: `docker compose down`.

### Quick start (Docker MySQL — recommended, server runs on host)



No local MySQL install. No `root` password to manage for this flow.



1. **Docker Desktop** (or Docker Engine) must be running.

2. From the **MEMCYCO** repo root:



```powershell
docker compose -f docker/docker-compose.yml up -d mysql
cd server
.\mvnw.cmd spring-boot:run
```



The app defaults **`application.yml`** to user `memcyco` / password `memcyco_app`, matching `docker-compose.yml`. The DB is created inside the container.



**One-shot** (starts DB then server): from repo root run `.\run-server-with-docker-db.ps1`.



If port **3306** is busy, change `docker-compose.yml` to map `3307:3306` and set `MYSQL_URL` to use `localhost:3307` (or run PowerShell with `$env:MYSQL_URL="...3307..."`).



**Check the API:**



```powershell

curl http://localhost:8080/api/tasks

```



### Where is the MySQL connection?



| What | Location |

|------|----------|

| Main config (defaults match Docker) | `src/main/resources/application.yml` — `spring.datasource.*` |

| Docker DB | `docker-compose.yml` (repo root) |

| Override env vars | `.env.example` in this folder |



### Requirements



- JDK 17+

- **Docker** (for MySQL in compose), **or** a MySQL 8 server you configure yourself via `MYSQL_*` env vars.



### Local MySQL (optional, not Docker)



Create database `memcyco_scheduler`, then set `MYSQL_USER`, `MYSQL_PASSWORD`, `MYSQL_URL` before `spring-boot:run` (see `.env.example`).



### Run



```powershell

.\mvnw.cmd spring-boot:run

```



Server: `http://localhost:8080`



### API



- `GET /api/tasks`

- `GET /api/schedules`

- `POST /api/schedules`

- `PUT /api/schedules/{id}`

- `DELETE /api/schedules/{id}`



### Tests



```powershell

.\mvnw.cmd test

```



Tests use the **`h2`** profile (in-memory DB) — MySQL/Docker not required for `mvn test`.



**What runs:**



- **Unit tests** — pure logic with no Spring context (`TaskDefinition`, `TaskParameterValidation`, `WeeklyCronExpression`, `ScheduleValidator`, `TaskParamsJson`).

- **Integration tests** — `@SpringBootTest` + `MockMvc` (`ApiIntegrationTest`, `SchedulesApiIntegrationTest`).


