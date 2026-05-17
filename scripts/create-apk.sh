#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
ARTIFACTS_DIR="${REPO_ROOT}/artifacts"

cd "${REPO_ROOT}"

declare -a EXECUTED_COMMANDS=()

run_step() {
  local description="$1"
  local command="$2"

  printf '\n=== %s ===\n' "${description}"
  echo "Executing: ${command}"

  EXECUTED_COMMANDS+=("${description}: ${command}")

  if bash -lc "${command}"; then
    echo "Status: PASSED"
  else
    echo "Status: FAILED"
    return 1
  fi
}

run_step "Build debug APK" "./gradlew assembleDebug"

APK_PATH="$(find "${REPO_ROOT}/app/build/outputs/apk" -type f -name "*.apk" | head -n 1)"

if [[ -z "${APK_PATH}" ]]; then
  echo "Status: FAILED"
  echo "No APK file found under app/build/outputs/apk"
  exit 1
fi

echo "Status: PASSED"
echo "APK located at ${APK_PATH}"

run_step "Copy APK artifact" "mkdir -p '${ARTIFACTS_DIR}' && cp '${APK_PATH}' '${ARTIFACTS_DIR}/app-debug.apk'"
run_step "Run unit tests only (no Appium)" "./gradlew testDebugUnitTest"

echo "\nExecuted steps summary:"
for step in "${EXECUTED_COMMANDS[@]}"; do
  echo "- ${step}"
done

echo "\nFinal status: PASSED"
echo "APK copied to ${ARTIFACTS_DIR}/app-debug.apk"
