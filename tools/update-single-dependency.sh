#!/bin/bash
set -ex

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" &>/dev/null && pwd)"
source "$SCRIPT_DIR/recursive-project.sh"

for_each_project "--update-locks" "$1"
