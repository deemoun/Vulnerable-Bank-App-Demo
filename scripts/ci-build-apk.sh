#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
ARTIFACTS_DIR="${ARTIFACTS_DIR:-${REPO_ROOT}/artifacts}"
BUILD_TYPE="${BUILD_TYPE:-debug}"

case "${BUILD_TYPE}" in
  debug)
    GRADLE_TASK="assembleDebug"
    APK_SOURCE="${REPO_ROOT}/app/build/outputs/apk/debug/app-debug.apk"
    APK_TARGET="${ARTIFACTS_DIR}/app-debug.apk"
    ;;
  production)
    GRADLE_TASK="assembleRelease"
    APK_SOURCE="${REPO_ROOT}/app/build/outputs/apk/release/app-release.apk"
    APK_TARGET="${ARTIFACTS_DIR}/app-production.apk"
    ;;
  *)
    echo "Unsupported BUILD_TYPE '${BUILD_TYPE}'. Expected 'debug' or 'production'." >&2
    exit 1
    ;;
esac

cd "${REPO_ROOT}"

chmod +x ./gradlew
./gradlew --no-daemon --console=plain clean "${GRADLE_TASK}"

if [[ ! -f "${APK_SOURCE}" ]]; then
  echo "APK was not created at ${APK_SOURCE}" >&2
  exit 1
fi

mkdir -p "${ARTIFACTS_DIR}"
cp "${APK_SOURCE}" "${APK_TARGET}"

echo "APK created: ${APK_SOURCE}"
echo "APK copied to: ${APK_TARGET}"
