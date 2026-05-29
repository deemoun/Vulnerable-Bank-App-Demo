# VulnerableBankAppOOP

Android/Kotlin training bank app used for engineering, QA, and security testing. The app intentionally includes vulnerable patterns for demo and education.

## Project layout

- `app/` — Android application module.
- `app/src/main/java/com/training/vulnerablebank/` — activities, app logic, and utilities.
- `app/src/main/res/` — Android resources.
- `app/src/test/java/com/training/vulnerablebank/` — local JUnit 5 tests, including Appium-style flows.
- `app/src/androidTest/java/com/training/vulnerablebank/` — instrumented/Espresso tests.
- `.github/workflows/` — CI workflows.
- `scripts/` — local/CI helper scripts.

## Requirements

- JDK 17.
- Android SDK with API 36 available.
- Use the Gradle wrapper (`./gradlew`) from the repository root.
- Optional for device tests: Android emulator/device and `adb`.
- Optional for Appium-style local tests: Appium server on `http://127.0.0.1:4723`.

## Engineer quickstart

```bash
chmod +x ./gradlew
./gradlew clean assembleDebug
```

Debug APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Common commands:

```bash
./gradlew lint
./gradlew testDebugUnitTest
./gradlew clean lint testDebugUnitTest
```

Build and copy the APK to `artifacts/app-debug.apk`:

```bash
./scripts/ci-build-apk.sh
```

## Test engineer quickstart

Run local/unit tests:

```bash
./gradlew testDebugUnitTest
```

Run a single local test class:

```bash
./gradlew testDebugUnitTest --tests com.training.vulnerablebank.MainAppTest
```

Run instrumented tests on a connected emulator/device:

```bash
./gradlew connectedDebugAndroidTest
```

Run a single instrumented test class:

```bash
./gradlew connectedDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.training.vulnerablebank.MainAppActionsEspressoTest
```

Run the configured Gradle managed device test:

```bash
./gradlew :app:headlessApi36DebugAndroidTest
```

## CI

- `.github/workflows/build-apk.yml` builds the debug APK and uploads it as a workflow artifact.
- `.github/workflows/android-ci.yml` runs lint/unit tests and uploads reports plus a debug APK.
- Emulator workflows under `.github/workflows/` run selected instrumented test classes manually.

CI build script used by the APK workflow:

```bash
./scripts/ci-build-apk.sh
```

## QA app entry points

Package name:

```text
com.training.vulnerablebank
```

Launch examples:

```bash
adb shell am start -n com.training.vulnerablebank/.LoginActivity
adb shell am start -n com.training.vulnerablebank/.DashboardActivity
adb shell am start -n com.training.vulnerablebank/.TransferActivity
```

Deep link example:

```bash
adb shell am start -a android.intent.action.VIEW -d 'vuln://transfer'
```
