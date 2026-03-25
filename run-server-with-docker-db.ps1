# Start MySQL in Docker, then run the Spring Boot server (same DB user as docker-compose.yml).
# Usage: from MEMCYCO repo root, in PowerShell:
#   .\run-server-with-docker-db.ps1

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

Write-Host "Starting MySQL container only (not server/client images)..." -ForegroundColor Cyan
docker compose -f docker/docker-compose.yml up -d mysql

Write-Host "Waiting for MySQL to accept connections..." -ForegroundColor Cyan
$deadline = (Get-Date).AddMinutes(2)
do {
  try {
    docker exec memcyco-mysql mysqladmin ping -h 127.0.0.1 -u root -pmemcyco_root_change_me --silent 2>$null
    if ($LASTEXITCODE -eq 0) { break }
  } catch { }
  if ((Get-Date) -gt $deadline) {
    Write-Error "MySQL did not become ready in time. Check: docker compose -f docker/docker-compose.yml logs mysql"
    exit 1
  }
  Start-Sleep -Seconds 2
} while ($true)

Write-Host "Starting Spring Boot (server)..." -ForegroundColor Cyan
Set-Location (Join-Path $root "server")
# Defaults match application.yml + docker-compose; explicit env for clarity
$env:MYSQL_USER = "memcyco"
$env:MYSQL_PASSWORD = "memcyco_app"
$env:MYSQL_URL = "jdbc:mysql://localhost:3306/memcyco_scheduler?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
.\mvnw.cmd spring-boot:run
