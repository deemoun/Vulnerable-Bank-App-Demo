# AGENTS.md

Scope: `app/src/main/java/com/training/vulnerablebank/`

## App structure guidance
- Keep activity-level logic in this package (`*Activity.kt`, app class, core helpers).
- Reuse existing utilities under `utils/` before adding new helper classes.
- Keep changes small and focused; avoid broad architecture refactors.
- Preserve intentionally vulnerable training behavior unless remediation is explicitly requested.
