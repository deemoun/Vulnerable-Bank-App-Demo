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

## 3) Run tests locally with Gradle
From project root:
- Run unit tests:
  - `./gradlew testDebugUnitTest`
- Run lint + unit tests:
  - `./gradlew clean lint testDebugUnitTest`
- Run instrumented tests on connected emulator/device:
  - `./gradlew connectedDebugAndroidTest`

## 4) Allure: full setup and usage

### 4.1 What is already configured in this repo
- Allure Java adapters are already added for unit tests:
  - `io.qameta.allure:allure-junit5:2.29.1`
  - `io.qameta.allure:allure-java-commons:2.29.1`
- JUnit 5 platform is enabled for `Test` tasks (`useJUnitPlatform()`), so Allure JUnit 5 integration can collect test lifecycle data.

### 4.2 What is **not** configured automatically
- There is no dedicated Gradle task like `allureReport`/`allureServe` in this repo.
- There is no committed `allure.properties` with custom output directory.
- CI workflows currently publish JUnit/Android reports, but do not generate/publish Allure reports.

### 4.3 Install Allure CLI per-repository (npm, not global)
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

### 4.4 Generate results and HTML report locally
From project root:
1. Run tests:
   - `./gradlew clean testDebugUnitTest`
2. Generate report from JUnit XML folder:
   - `npx allure generate app/build/test-results/testDebugUnitTest --clean -o build/allure-report`
3. Open report:
   - `npx allure open build/allure-report`

Alternative one-command preview:
- `npx allure serve app/build/test-results/testDebugUnitTest`

### 4.5 Optional: cleaner Allure results directory
If you want classic `allure-results` flow (instead of pointing directly to JUnit XML path), add `allure.properties` and set:
- `allure.results.directory=build/allure-results`

Then run tests and build report from that folder:
- `npx allure generate build/allure-results --clean -o build/allure-report`

### 4.6 Troubleshooting
- If `npx: command not found` or npm errors occur:
  - Install Node.js (which includes npm) and rerun the repo-local install command.
- If report is empty:
  - Ensure tests actually executed (`./gradlew testDebugUnitTest`) and files exist under `app/build/test-results/testDebugUnitTest`.
- If some Appium tests are skipped:
  - This is expected when Appium server at `http://127.0.0.1:4723` is not running (assumption-based skip in tests).

## 5) How CI runs
CI workflows are in `.github/workflows/`:
- `android-ci.yml` — no-emulator pipeline: runs lint + `testDebugUnitTest`, uploads reports and debug APK.
- `android-ci-emulator.yml` — emulator pipeline: runs `PreferencesManagerInstrumentedTest`.
- `android-ci-espresso-emulator.yml` — emulator pipeline: runs `MainAppActionsEspressoTest`.

All workflows are currently triggered manually (`workflow_dispatch`).

## 6) How unit tests work
- Unit tests use **JUnit 5** (`useJUnitPlatform()` in Gradle).
- Standard unit tests run with `testDebugUnitTest`.
- UI-like tests in `src/test` use **Appium** and `TestBase`:
  - Before each test, it checks if Appium is available at `http://127.0.0.1:4723`.
  - If Appium is not running, those tests are skipped via JUnit assumptions.
  - If running, driver/session is created and page objects are used for flows.
