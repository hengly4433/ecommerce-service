#!/usr/bin/env sh
set -e

# Read secrets from Swarm if present
if [ -f "/run/secrets/db_password" ]; then
  export SPRING_DATASOURCE_PASSWORD="$(cat /run/secrets/db_password)"
fi
if [ -f "/run/secrets/jwt_secret" ]; then
  export APP_SECURITY_JWT_BASE64_SECRET="$(cat /run/secrets/jwt_secret)"
fi

# If using *_FILE pattern (optional), honor it:
if [ -n "${SPRING_DATASOURCE_PASSWORD_FILE:-}" ] && [ -f "$SPRING_DATASOURCE_PASSWORD_FILE" ]; then
  export SPRING_DATASOURCE_PASSWORD="$(cat "$SPRING_DATASOURCE_PASSWORD_FILE")"
fi
if [ -n "${APP_SECURITY_JWT_BASE64_SECRET_FILE:-}" ] && [ -f "$APP_SECURITY_JWT_BASE64_SECRET_FILE" ]; then
  export APP_SECURITY_JWT_BASE64_SECRET="$(cat "$APP_SECURITY_JWT_BASE64_SECRET_FILE")"
fi

# Start the app
exec java ${JAVA_OPTS:-} -jar /app/app.jar
