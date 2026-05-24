# AGENTS.md

Guidance for AI coding agents working in this repository.

## Project Overview
- **Type**: Android app (Kotlin + Gradle Kotlin DSL)
- **Module**: `app/`
- **Package**: `com.training.vulnerablebank`
- **Primary purpose**: Training/demo banking app with intentionally insecure patterns for security education and testing.

## Repository Layout
- `app/src/main/java/com/training/vulnerablebank/` — app logic (activities, utilities, app class).
- `app/src/main/java/com/training/vulnerablebank/ui/` — UI components/theme utilities.
- `app/src/main/res/` — resources (`values`, drawables, launcher assets, manifest-linked XML).
- `app/src/test/java/com/training/vulnerablebank/` — local tests (JUnit 5 + Appium-style flows and page objects).
- `app/src/androidTest/java/com/training/vulnerablebank/` — instrumented/Espresso tests.
- `app/build.gradle.kts` — Android module config and dependencies.
- Root `build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml` — project-level build and version catalog.
- `scripts/create-apk.sh` — helper script for APK-related workflow.

## Build & Test Commands
Run from repo root:

### Fast validation
- `./gradlew testDebugUnitTest`
- `./gradlew lint`

### Full local validation (if environment allows)
- `./gradlew clean lint testDebugUnitTest`

### Instrumented tests
- Connected device/emulator:
  - `./gradlew connectedDebugAndroidTest`
- Managed emulator (headless):
  - `./gradlew :app:headlessApi36DebugAndroidTest`

### Targeted tests
- Unit class:
  - `./gradlew testDebugUnitTest --tests com.training.vulnerablebank.MainAppTest`
- Instrumented class:
  - `./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.training.vulnerablebank.MainAppActionsEspressoTest`

## Coding Conventions
- Prefer **small, focused changes** over broad refactors.
- Keep package structure consistent under `com.training.vulnerablebank`.
- Match existing Kotlin style in neighboring files.
- Avoid introducing new libraries unless clearly justified.
- Do not add `try/catch` around imports.
- When modifying tests, keep existing JUnit 5 conventions.

## Security/Domain Context
This app intentionally contains vulnerable patterns for training. When changing security-related behavior:
- Preserve educational intent unless task explicitly requests remediation.
- If remediating, document what changed and why in commit/PR notes.
- Avoid silently removing demo attack surfaces that tests or lessons may rely on.

## Agent Workflow Expectations
1. Read this file and relevant module files before editing.
2. Make minimal, task-scoped edits.
3. Run the most relevant verification commands.
4. Summarize changes with file paths and key impacts.
5. If tests cannot run due to environment limits (e.g., missing emulator/Appium), state that explicitly.

## PR/Commit Guidance
- Use clear conventional-style commit messages, e.g.:
  - `docs: add AGENTS.md guidance for repository automation`
  - `fix: adjust transfer validation in TransferActivity`
- PR descriptions should include:
  - What changed
  - Why it changed
  - How it was validated
  - Any environment limitations encountered

## Notes for AI Tools
- Prefer `rg` for code search.
- Avoid expensive recursive shell patterns that can hang CI environments.
- For Android behavior changes, mention impacted activities/tests explicitly.
