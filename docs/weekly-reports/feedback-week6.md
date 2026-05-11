# Week 6 Project Feedback by PM/TA

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
| 0 | The team has closed and merged the past Feedback PR(s), indicating that they have read the feedback. |   ⚠️   | The Week 5 feedback PR #2 is still open, so the feedback loop is not fully closed yet. That said, many of the Week 5 process requests were addressed on `main` this week through merged requirements/design docs, BVA, implementation, and linters. | |

### Software Process Quality
| # | Item                                                                                                                                         | Status | Reviewer Notes | Source Instructions or Resources                                                                              |
|---|----------------------------------------------------------------------------------------------------------------------------------------------|:------:|----------------|---------------------------------------------------------------------------------------------------------------|
| 1 | Checkstyle: Checkstyle is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B) |   ✅   | Checkstyle is configured on `main` in `build.gradle.kts`, with a repository config file under `config/checkstyle/checkstyle.xml`. | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |
| 2 | SpotBugs: SpotBugs is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B)     |   ✅   | SpotBugs is also configured on `main`, including an explicit exclude filter under `config/spotbugs/excludeFilter.xml`. | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 4 | The team documents every week's planning and progress evaluation professionally. (needed for Letter Grade B)                                                 |     ✅     | `docs/weekly-reports/report.md` now includes a detailed Week 6 entry on `main`, with scope, decisions, owners, and links to merged PRs. | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| # | Item                                                                                                                                                                                   |  Status   | Reviewer Notes      | Source Instructions or Resources                 |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|--------------------------------------------------|
| 5 | Overall development progress (recall the recommended order is: Game Setup Phase -> One turn of the game -> Multiple turns -> One win condition -> Other win conditions (if applicable) |     ⚠️     | This was a strong start to the Game Setup Phase. However, the core Game Setup classes listed in the Week 6 plan, such as `Board`, `GameSetup`, `TurnOrder`, and development-card/deck logic, are still not on `main` yet. Please keep the focus on finishing and merging those core setup pieces next. | Canvas assignment Project: Week 4 and 5 Guidance |

### The following items are not checked by the reviewer this week as they were checked in the previous weeks
If your team wants the reviewer to check any of these for any reasons, please contact them or the instructor via either email or tag/mention them in the feedback PR.

| #   | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1   | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval. (needed for Letter Grade C) |     ➖     | Not rechecked this week. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 2   | Continuous Integration (CI) is fully set up from the beginning. (needed for Letter Grade B)                                                                  |     ➖     | Not rechecked this week. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 3   | The team uses the project management board steadily and frequently, and the description of each task is detailed. (needed for Letter Grade B)                | See below | See breakdown below | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.1 | Every functionality-related work item on the management board includes a user story, and optionally one or more use cases.                                   |     ➖     | Not rechecked in full this week. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.2 | The design is documented somewhere, either in the work item description, or in a separate design document.                                                   |     ➖     | Not rechecked in full this week. `docs/design/README.md` has been substantially expanded on `main`. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.3 | Task assignments are documented clearly in the management board.                                                                                             |     ➖     | Not rechecked in full this week. The Week 6 report now shows concrete ownership more clearly than before. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 4   | Each active feature branch has an open draft PR against main.                                                                                                |     ➖     | Not rechecked this week. Current visible work is being merged in smaller completed PRs rather than left only in hidden branches, which is good. | Week 4 Wednesday Lecture (Lecture 08)                                             |
| 5   | The team has a “definition of done” (BVA) fully documented for the part of the system that is done. (needed for Letter Grade D)                              |     ➖     | Not rechecked this week. The `Player` and `LocaleManager` BVA docs are now present on `main`. | Project grading rubrics                                                           |
| 6   | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code. (needed for Letter Grade C)                                       |     ➖     | Not rechecked this week. The merged `Player` and `LocaleManager` work shows a visible docs/test/green sequence. | Project grading rubrics                                                           |
| 7   | Collaboration: Quality of discussion in PR reviews and work item comments on the board.                                                                      |     ➖     | Not rechecked this week. |                                                                                   |

## Additional Comments
This is a meaningful improvement over the repository state from last week. That is a much healthier direction than letting setup work remain vague or invisible.

The two main next steps are straightforward. First, close the loop by merging the still-open Week 5 feedback PR so the feedback trail is complete. Second, keep pushing the core Game Setup domain work onto `main`, since the current merged work is a good foundation but not yet the complete setup phase described in your Week 6 plan. Review approvals are present, but most written review comments are still very brief; when possible, add a little more detail about what was checked.

## Review Snapshot (Just used for tracking purposes, not for feedback)
- Reviewed latest `main` commit: `790d92a`
- Commit summary: Merge pull request #9 from `nu-cs-sqe/feature/locale-followup`
- Review date: 2026-05-11
- Verification: `bash ./gradlew test` passed with Java 11
