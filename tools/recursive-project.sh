#!/bin/bash
set -e

for_each_project() {
  PROJECTS=("evervault-core" "evervault-cages" "evervault-enclaves" "evervault-inputs" "sampleapplication")
  for PROJECT in "${PROJECTS[@]}"
  do
    ./gradlew "$PROJECT:dependencies" "$@"
  done
}
