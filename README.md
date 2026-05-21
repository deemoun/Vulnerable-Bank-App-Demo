# VulnerableBankAppOOP

Short guide for how this project is organized and how to run tests.

## 1) App structure
- `app/src/main/java/com/training/vulnerablebank/` — Android app code (activities, utils, UI).
- `app/src/main/res/` — resources (strings, themes, drawables).
- `app/build.gradle.kts` — module build config, dependencies, test setup.
- Root `build.gradle.kts` and `settings.gradle.kts` — project-level Gradle config.

## 2) Where tests are located
- **Local/unit tests**: `app/src/test/java/...`
  - Includes JUnit 5 + Appium-based UI flow tests (`MainAppTest`, `TestBase`, page objects).
- **Instrumented Android tests**: `app/src/androidTest/java/...`
  - Espresso/instrumented tests running on device/emulator.

## 3) QA quickstart: run key tests from Gradle CLI

### 3.1 MainAppActionsEspressoTest (instrumented)
Runs Espresso test on a connected device/emulator:
- `./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.training.vulnerablebank.MainAppActionsEspressoTest`

Run on Gradle Managed Device (headless, no manual emulator start):
- `./gradlew :app:headlessApi36DebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.training.vulnerablebank.MainAppActionsEspressoTest`

### 3.2 PreferencesManagerInstrumentedTest (instrumented)
Connected device/emulator:
- `./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.training.vulnerablebank.PreferencesManagerInstrumentedTest`

Managed device:
- `./gradlew :app:headlessApi36DebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.training.vulnerablebank.PreferencesManagerInstrumentedTest`

Compatibility wrapper task already in project:
- `./gradlew :app:headlessPreferencesManagerInstrumentedTest`

### 3.3 MainAppTest (local/unit test in `src/test`)
Run only `MainAppTest`:
- `./gradlew testDebugUnitTest --tests com.training.vulnerablebank.MainAppTest`

Run single method example:
- `./gradlew testDebugUnitTest --tests 'com.training.vulnerablebank.MainAppTest.testValidLoginAndTransferFlow'`

## 4) Gradle parameters QA should know
- `connectedDebugAndroidTest`
  - Runs instrumented tests on **already connected** device/emulator.
- `:app:headlessApi36DebugAndroidTest`
  - Runs instrumented tests using the configured Gradle Managed Device (`Pixel 6`, API 36, `aosp`).
- `-Pandroid.testInstrumentationRunnerArguments.class=<fqcn>`
  - Filters instrumented run to one specific test class (FQCN = fully-qualified class name).
- `--tests <pattern>`
  - Filters **unit tests** (`src/test`) by class or method.
- `clean`
  - Optional first task to wipe previous build/test artifacts before run.
- `--info` / `--stacktrace`
  - Helpful diagnostics flags for flaky CI/local failures.

Examples:
- `./gradlew clean connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.training.vulnerablebank.MainAppActionsEspressoTest --info`
- `./gradlew clean testDebugUnitTest --tests com.training.vulnerablebank.MainAppTest --stacktrace`

## 5) App routes / entry points for QA exploration (adb)

Package:
- `com.training.vulnerablebank`

### 5.1 Launch activities directly
```bash
adb shell am start -n com.training.vulnerablebank/.LoginActivity
adb shell am start -n com.training.vulnerablebank/.DashboardActivity
adb shell am start -n com.training.vulnerablebank/.TransactionsActivity
adb shell am start -n com.training.vulnerablebank/.TransferActivity
adb shell am start -n com.training.vulnerablebank/.SettingsActivity
adb shell am start -n com.training.vulnerablebank/.SecurityInfoActivity
adb shell am start -n com.training.vulnerablebank/.HiddenFeatureActivity
```

### 5.2 Deep link route (interesting path)
`TransferActivity` is browsable via deep link:
- URI: `vuln://transfer`

Command:
```bash
adb shell am start -a android.intent.action.VIEW -d 'vuln://transfer'
```

### 5.3 Discover exported surfaces from installed APK
```bash
adb shell cmd package resolve-activity --brief com.training.vulnerablebank
adb shell dumpsys package com.training.vulnerablebank | sed -n '/Activity Resolver Table:/,/Receiver Resolver Table:/p'
```

## 6) Run tests locally with Gradle
From project root:
- Run unit tests:
  - `./gradlew testDebugUnitTest`
- Run lint + unit tests:
  - `./gradlew clean lint testDebugUnitTest`
- Run instrumented tests on connected emulator/device:
  - `./gradlew connectedDebugAndroidTest`

## 7) Allure: full setup and usage

### 7.1 What is already configured in this repo
- Allure Java adapters are already added for unit tests:
  - `io.qameta.allure:allure-junit5:2.29.1`
  - `io.qameta.allure:allure-java-commons:2.29.1`
- JUnit 5 platform is enabled for `Test` tasks (`useJUnitPlatform()`), so Allure JUnit 5 integration can collect test lifecycle data.

### 7.2 What is **not** configured automatically
- A compatibility Gradle task `allureReport` is available at the root project and expects results in `app/build/allure-results`.
- There is no committed `allure.properties` with custom output directory.
- CI workflows currently publish JUnit/Android reports, but do not generate/publish Allure reports.

### 7.3 Install Allure CLI per-repository (npm, not global)
Use a local dev dependency so Allure is installed only for this repository.

From project root (Linux/macOS):
- `npm install --save-dev allure-commandline`
- `npx allure --version`

From project root (Windows PowerShell):
- `npm install --save-dev allure-commandline`
- `npx allure --version`

Notes:
- Do **not** use `npm install -g` for this repo setup.
- `npx allure ...` uses `./node_modules/.bin/allure`, so it is repo-scoped.

### 7.4 Generate results and HTML report locally
From project root:
1. Run tests:
   - `./gradlew clean testDebugUnitTest`
2. Generate report from JUnit XML folder:
   - `npx allure generate app/build/test-results/testDebugUnitTest --clean -o build/allure-report`
3. Open report:
   - `npx allure open build/allure-report`

Alternative one-command preview:
- `npx allure serve app/build/test-results/testDebugUnitTest`

### 7.5 Optional: cleaner Allure results directory
If you want classic `allure-results` flow (instead of pointing directly to JUnit XML path), add `allure.properties` and set:
- `allure.results.directory=build/allure-results`

Then run tests and build report from that folder:
- `npx allure generate build/allure-results --clean -o build/allure-report`

### 7.6 Troubleshooting
- If `npx: command not found` or npm errors occur:
  - Install Node.js (which includes npm) and rerun the repo-local install command.
- If report is empty:
  - Ensure tests actually executed (`./gradlew testDebugUnitTest`) and files exist under `app/build/test-results/testDebugUnitTest`.
- If some Appium tests are skipped:
  - This is expected when Appium server at `http://127.0.0.1:4723` is not running (assumption-based skip in tests).

## 8) How CI runs
CI workflows are in `.github/workflows/`:
- `android-ci.yml` — no-emulator pipeline: runs lint + `testDebugUnitTest`, uploads reports and debug APK.
- `android-ci-emulator.yml` — emulator pipeline: runs `PreferencesManagerInstrumentedTest`.
- `android-ci-espresso-emulator.yml` — emulator pipeline: runs `MainAppActionsEspressoTest`.

All workflows are currently triggered manually (`workflow_dispatch`).

## 9) How unit tests work
- Unit tests use **JUnit 5** (`useJUnitPlatform()` in Gradle).
- Standard unit tests run with `testDebugUnitTest`.
- UI-like tests in `src/test` use **Appium** and `TestBase`:
  - Before each test, it checks if Appium is available at `http://127.0.0.1:4723`.
  - If Appium is not running, those tests are skipped via JUnit assumptions.
  - If running, driver/session is created and page objects are used for flows.
