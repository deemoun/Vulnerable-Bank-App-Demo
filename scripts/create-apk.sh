#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
ARTIFACTS_DIR="${REPO_ROOT}/artifacts"

cd "${REPO_ROOT}"

./gradlew assembleDebug

APK_PATH="$(find "${REPO_ROOT}/app/build/outputs/apk" -type f -name "*.apk" | head -n 1)"

if [[ -z "${APK_PATH}" ]]; then
  echo "No APK file found under app/build/outputs/apk"
  exit 1
fi

mkdir -p "${ARTIFACTS_DIR}"
cp "${APK_PATH}" "${ARTIFACTS_DIR}/app-debug.apk"

echo "APK copied to ${ARTIFACTS_DIR}/app-debug.apk"
