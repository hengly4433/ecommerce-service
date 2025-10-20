#!/usr/bin/env bash
set -euo pipefail

# Load .env.prod (export all)
if [ -f ".env.prod" ]; then
  set -a
  . ./.env.prod
  set +a
else
  echo "Missing .env.prod; create it next to docker-stack.yml"
  exit 1
fi

# Ensure Swarm & network (no-op if already exist)
docker swarm init 2>/dev/null || true
docker network create --driver overlay --attachable edge 2>/dev/null || true

# Helper: (re)create secret from env var
recreate_secret () {
  local name="$1" ; local value="$2"
  if docker secret ls --format '{{.Name}}' | grep -qx "$name"; then
    docker secret rm "$name" >/dev/null
  fi
  printf "%s" "$value" | docker secret create "$name" - >/dev/null
}

# Create secrets from .env.prod
recreate_secret db_password "${SPRING_DATASOURCE_PASSWORD}"
recreate_secret jwt_secret "${APP_SECURITY_JWT_BASE64_SECRET}"

# Deploy (env substitution applies to non-secret env references)
docker stack deploy -c docker-stack.yml ecommerce --with-registry-auth

docker service ls
docker service ps ecommerce_app
