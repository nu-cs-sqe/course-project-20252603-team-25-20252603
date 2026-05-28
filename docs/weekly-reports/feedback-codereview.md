# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.

Please note that this feedback does not include evaluation of your progress, the proper use of linters, the quality of your test cases, or your compliance of TDD/BDD workflow.  
You can find the weekly feedback from your dedicated PM/TA for that.

## Week 7 Code Review
I have read every line of production code currently in the main branch. 
Overall, really good job! Two minor things:
1. Your code is full of null checks. However, Uncle Bob argues in Clean Code that we should not pollute our code with null checks. Instead, we should focus on never passing null around. 
2. There is inconsistent use of the Java doc comment for the method as only the LocaleManager class uses it. You would want the use of it to be consistent.

Also, I am very sorry that I only realized today (5/20) that I didn't clone your team's repository last week, so you didn't get any code review feedback from me. I am sorry :(!

Please approve and merge the PR once the team has read the feedback. Thanks!
