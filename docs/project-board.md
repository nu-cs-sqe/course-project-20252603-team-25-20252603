# Project Board and Process Audit

**Canonical board:** [GitHub Issues](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/issues)

This file is a local snapshot of process evidence for the project rubric. The
live board remains GitHub Issues; this document records what can be verified
from repository history and checked-in workflow/docs.

## Current rubric-critical work

| PR | Branch / area | Rubric purpose | Status |
|----|---------------|----------------|--------|
| [#33](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/pull/33) | `feature/playable-turn-slice` | playable game loop for D/C | merged |
| [#34](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/pull/34) | `feat/win-condition` | 10-point win condition | merged |
| [#35](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/pull/35) | `feat/development-card-effects` | all development-card effects used by current model | merged |
| [#36](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/pull/36) | `test/domain-mutation-coverage` | domain-only Jacoco/PIT scope and first mutation cleanup | merged |
| [#37](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/pull/37) | `test/playable-game-integration` | integration test for playable game flow | merged |
| [#38](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/pull/38) | `test/mutation-cleanup-to-100` | effective 100% non-equivalent mutation score | merged |
| [#39](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/pull/39) | `test/cyclomatic-coverage` | 0 missed Jacoco complexity for filtered domain scope | merged |

## Earlier process items

| Evidence | Status | Notes |
|----------|--------|-------|
| Branch protection | verified earlier | Instructor Week 4 feedback records that `main` required PR review and Gradle status checks. Re-check the live GitHub settings before final submission because this cannot be proven from local files. |
| Continuous integration | present | `.github/workflows/main.yml` runs on pushes and pull requests to `main`; it executes `./gradlew check` and uploads Checkstyle/SpotBugs reports. |
| Project board usage | documented | Issues/PR links are recorded above and in weekly reports. Some old issue statuses in earlier snapshots were stale, so this section is the current repo-history audit. |
| Weekly planning/progress | documented | `docs/weekly-reports/report.md` records weekly planning and the final A-rubric push. |
| BVA/TDD evidence | documented | `docs/bva/` maps test cases to boundary values; commits for PRs #33-#39 separate BVA, tests, implementation, and evidence where practical. |

## Remaining manual checks

Before final submission, verify these directly in GitHub:

- `main` still blocks direct pushes.
- Pull requests still require at least one approval.
- Gradle Build is still a required status check.
- GitHub Issues/Project board cards match the merged PR status above.

## Team

| Name | GitHub |
|------|--------|
| Daniel Wu | [@danieljwu](https://github.com/danieljwu) |
| Daniel Kim | [@daniel-kimm](https://github.com/daniel-kimm) |
| Julian Tang | [@JulianTang2027](https://github.com/JulianTang2027) |
| Shun Fujita | [@sshunf](https://github.com/sshunf) |

Weekly history: [`docs/weekly-reports/report.md`](weekly-reports/report.md).
