#!/usr/bin/env bash
set -euo pipefail

cleanup() {
  jobs -p | xargs -r kill
}
trap cleanup EXIT

echo "Starting backend on :8080"
( cd backend && mvn spring-boot:run ) &

echo "Starting frontend on :3000"
( cd frontend && npm run dev -- --host 0.0.0.0 --port 3000 ) &

wait