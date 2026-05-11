# Week 5 Project Feedback by PM/TA

**Dedicated PM/TA**: Jiahao Yu

## How to Read This Feedback
> [!NOTE]
> **Purpose.** This feedback focuses on your team's progress and collaboration. It is meant as guidance, not judgement.

> [!IMPORTANT]
> **Scope.** For the BVA and TDD items, the PM/TA evaluates only the `main` branch. Ongoing work in feature branches will be evaluated after it is merged. If you'd like early feedback on work in progress, please reach out to your PM/TA directly.

> [!TIP]
> **Mistakes are expected :).** As the instructor mentioned in class, early mistakes are part of the learning process. As long as your team addresses the issues after you get the feedback, your grade will not suffer from them.

## Checklist
Status:
- ✅: All done/Good job!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: No basis to evaluate

### Past Feedback
| # | Item                                                                                                 | Status | Reviewer Notes | Source Instructions or Resources |
|---|------------------------------------------------------------------------------------------------------|:------:|----------------|----------------------------------|
| 0 | The team has closed and merged the past Feedback PR(s), indicating that they have read the feedback. |   ❌   | Week 4 feedback PR #1 is still open and unmerged. It was created on 2026-04-28 and has no reviews, comments, or updates. Please read, discuss, and merge feedback PRs promptly. | |

### Software Process Quality
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1 | Each active feature branch has an open draft PR against main.                                                                                                |     ➖     | I do not see any active feature branches or open implementation PRs. If any setup work is happening locally, please push it and open a draft PR so the team can track and review it. | Week 4 Wednesday Lecture (Lecture 08)                                             |
| 2 | The team has a "definition of done" (BVA) fully documented for the part of the system that is done. (needed for Letter Grade D)                              |     ❌     | I do not see any concrete BVA documentation beyond the template README. Please document BVA for the first Game Setup Phase behavior before or alongside implementation. | Project grading rubrics                                                           |
| 3 | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code. (needed for Letter Grade C)                                       |     ➖     | There is no non-UI implementation or test code merged into `main`, so there is no TDD history to evaluate. Once coding starts, please use small test-first commits. | Project grading rubrics                                                           |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 4 | The team documents every week's planning and progress evaluation professionally. (needed for Letter Grade B)                                                 |     ❌     | `docs/weekly-reports/report.md` still contains placeholder/template entries and does not document Week 4 or Week 5 planning/progress. Please update this immediately with actual tasks, owners, statuses, and PR/issue links. | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| # | Item                                                                                                                                                                                   |  Status   | Reviewer Notes      | Source Instructions or Resources                 |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|--------------------------------------------------|
| 5 | Overall development progress (recall the recommended order is: Game Setup Phase -> One turn of the game -> Multiple turns -> One win condition -> Other win conditions (if applicable) |     ❌     | I do not see visible Game Setup Phase progress. The last student commit on `main` was on 2026-04-19, and the repo currently has no setup requirements/design, BVA, tests, implementation, issues, or feature PRs. This is a significant progress concern. | Canvas assignment Project: Week 4 and 5 Guidance |
| 6 | Collaboration: Quality of discussion in PR reviews and work item comments on the board.                                                                                                |     ❌     | I found no issues, no project-work comments visible through repository issues, no feature PR discussions, and the Week 4 feedback PR has no team response. Please make collaboration visible through issue updates, draft PRs, reviews, and comments. | |

### The following items are not checked by the reviewer as they were checked in the previous weeks
But if your team wants the reviewer to check any of these for any reasons, please contact them or the instructor via either email or tagging them in the feedback PR.

| #   | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1   | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval. (needed for Letter Grade C) |     ➖     | Not rechecked this week. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 2   | Continuous Integration (CI) is fully set up from the beginning. (needed for Letter Grade B)                                                                  |     ➖     | Not rechecked this week. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 3   | The team uses the project management board steadily and frequently, and the description of each task is detailed. (needed for Letter Grade B)                | See below | See breakdown below | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.1 | Every functionality-related work item on the management board includes a user story, and optionally one or more use cases.                                   |     ❌     | I found no repository issues for setup-phase work, and no user stories/use cases beyond the placeholder docs. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.2 | The design is documented somewhere, either in the work item description, or in a separate design document.                                                   |     ❌     | I do not see setup-phase design documentation under `docs/design/` or in repository issues. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.3 | Task assignments are documented clearly in the management board.                                                                                             |     ❌     | I found no visible setup-phase task assignments in repository issues or the weekly report. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |

## Additional Comments
This needs immediate attention. The repository infrastructure was set up in Week 3, but I do not see visible project progress after 2026-04-19, and last week's feedback PR is still open. Please regroup as a team, read and merge the feedback PR, update the weekly report, create setup-phase work items with owners, document the first BVA/design slice, and open draft PRs for implementation. If the team is blocked or work is happening somewhere outside GitHub, please make that visible and reach out for help.

## Review Snapshot (Just used for tracking purposes, not for feedback)
- Reviewed latest `main` commit: `98ea2a5`
- Commit summary: Update README to include Gradle Build badge
- Review date: 2026-05-05
- Verification: `bash ./gradlew test` passed with Java 11 on `main`; there are currently no test sources on `main`
