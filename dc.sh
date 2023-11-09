#!/bin/bash
set -e

docker compose \
    -f docker-compose.yml \
    -f monitoring/docker-compose-tracing.yml \
    -f monitoring/docker-compose-metrics.yml \
    -f monitoring/docker-compose-metrics-ui.yml \
    "$@"
