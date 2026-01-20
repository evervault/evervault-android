#!/bin/bash
set -e

PROJECTS=("evervault-core" "evervault-cages" "evervault-enclaves" "evervault-inputs" "sampleapplication")

# Update lockfiles for all projects
for PROJECT in "${PROJECTS[@]}"
do
  ./gradlew "$PROJECT:dependencies" --write-locks
done
