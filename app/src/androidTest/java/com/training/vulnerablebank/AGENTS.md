# AGENTS.md

Scope: `app/src/androidTest/java/com/training/vulnerablebank/`

## UI instrumented testing guidance
- Keep Espresso/instrumented tests deterministic and focused on user-visible behavior.
- Avoid flaky timing assumptions; use existing test patterns/utilities first.
- Update only relevant test cases for the changed screen/feature.
- Document if connected-device/emulator execution was not possible.
