#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
ARTIFACTS_DIR="${ARTIFACTS_DIR:-${REPO_ROOT}/artifacts}"
APK_SOURCE="${REPO_ROOT}/app/build/outputs/apk/debug/app-debug.apk"
APK_TARGET="${ARTIFACTS_DIR}/app-debug.apk"

cd "${REPO_ROOT}"

chmod +x ./gradlew
./gradlew --no-daemon --console=plain clean assembleDebug

if [[ ! -f "${APK_SOURCE}" ]]; then
  echo "APK was not created at ${APK_SOURCE}" >&2
  exit 1
fi

mkdir -p "${ARTIFACTS_DIR}"
cp "${APK_SOURCE}" "${APK_TARGET}"

echo "APK created: ${APK_SOURCE}"
echo "APK copied to: ${APK_TARGET}"
