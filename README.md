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

## 4) Run Allure and view results
Allure is **not wired as a Gradle task** in this repo right now, but you can still view test results with Allure CLI.

From project root:
1. Run tests to produce JUnit XML:
   - `./gradlew clean testDebugUnitTest`
2. Generate Allure report from test results:
   - `allure generate app/build/test-results/testDebugUnitTest --clean -o build/allure-report`
3. Open the report:
   - `allure open build/allure-report`

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
