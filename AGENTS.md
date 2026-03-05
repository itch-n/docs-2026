# AGENTS.md

Context for Claude Code sessions working in this repository.

---

## Project Overview

**itch-n-docs** is an interactive backend engineering study guide published as a static site via MkDocs. The audience is software engineers studying for interviews or deepening systems/algorithms knowledge. Content is structured as fill-in exercises — learners implement first, then explain.

- Site name: "Software Engineering Study Guide"
- Author: Richard
- Deployed to GitHub Pages via GitHub Actions on push to `main`

---

## Tech Stack

| Layer | Tool |
|-------|------|
| Static site | MkDocs 1.6.1+ with Cinder theme |
| Python runtime | Python 3.13+, managed via `uv` |
| Code examples | Java 25, built with Gradle |
| Testing | JUnit 5 (Jupiter) |
| Diagrams | mkdocs-mermaid2-plugin |
| Markdown extensions | pymdownx (tasklist, details, superfences), tables, toc, md_in_html |
| CI/CD | GitHub Actions → GitHub Pages |

---

## Content Structure

Every topic file follows this section pattern:

1. **ELI5** — simplified explanation (learner fills in)
2. **Quick Quiz** — pre-implementation predictions (learner fills in)
3. **Implementation** — Java code stub with TODOs
4. **Decision Framework** — when/how to use the concept (learner fills in)
5. **Practice Scenarios** — real-world application questions
6. **Review Checklist** — self-assessment checkboxes

Topics live in two sections:

- `docs/systems/` — 16 topics: storage engines, networking, caching, API design, security, rate limiting, load balancing, concurrency, database scaling, message queues, stream processing, observability, distributed transactions, consensus
- `docs/dsa/` — 14 topics: two pointers, sliding window, hash tables, linked lists, stacks & queues, trees (traversals + recursion), binary search, heaps, graphs, union-find, advanced graphs, dynamic programming, prefix sums, intervals

---

## CSS Conventions

Learner-fillable content uses specific CSS classes defined in `docs/css/custom.css`:

```markdown
<div class="learner-section" markdown>

**Your task:** After implementing, explain the concept.

- Your answer: <span class="fill-in">[Fill in]</span>

</div>
```

| Class | Purpose | Visual |
|-------|---------|--------|
| `.learner-section` | Wraps entire fill-in sections | Yellow/orange background |
| `.fill-in` | Inline blank prompts | Bold orange text |
| `.benchmark-table` | Tables for recording results | Yellow-bordered |
| `.learner-prompt` | Individual list item prompts | Yellow background |
| `.code-reference` | Reference/example code | Gray background |

Reference material (code stubs, algorithm explanations, complexity tables) is left unstyled.

---

## Common Commands

```bash
# Install dependencies
uv sync

# Local dev server (live reload)
uv run mkdocs serve

# Build static site
uv run mkdocs build

# Build with strict mode (fails on warnings)
uv run mkdocs build --strict

# Deploy to GitHub Pages manually
uv run mkdocs gh-deploy
```

---

## Content Authoring Notes

- New topics: create file in `docs/systems/` or `docs/dsa/`, then register in `mkdocs.yml` nav
- `md_in_html` extension is enabled — `markdown` attribute on divs processes inner Markdown
- See `STYLING-GUIDE.md` for full CSS class usage and conversion tips
- See `docs/systems/01-storage-engines.md` as the canonical example of a fully-styled topic

---

## Markdown Rendering Gotchas

MkDocs uses Python-Markdown, which has stricter whitespace rules than CommonMark. These are **silent failures** — the build passes with no warnings but the output looks wrong in the browser.

### List Formatting — blank line required before lists

Python-Markdown requires a blank line before any list. A list that immediately follows a paragraph or heading (no blank line) renders as plain text, not bullet points.

**Wrong:**
```markdown
Your task: Answer these questions.
- Question 1
- Question 2
```

**Correct:**
```markdown
Your task: Answer these questions.

- Question 1
- Question 2
```

AI-generated markdown frequently omits this blank line. After generating any file with lists, check with:

```bash
grep -n "^- \|^\* " docs/path/to/file.md
# Inspect the lines above each match in your editor
```

### Hard Line Breaks — two trailing spaces required

A single newline between two lines of text is treated as a soft wrap — they render as one continuous paragraph. To force a hard line break (e.g. labelled field pairs like `**Repo:** ...` / `**Used in:** ...`), add **two trailing spaces** at the end of the line that should break.

**Wrong:**
```markdown
**Repo:** itch-n-docs
**Used in:** systems/01-storage-engines.md
```

**Correct:**
```markdown
**Repo:** itch-n-docs
**Used in:** systems/01-storage-engines.md
```

AI-generated markdown often omits trailing spaces on consecutive labelled lines, causing them to flow together. Check structured metadata blocks after generation and add `  ` (two spaces) where needed.

---

## Persisting Learnings

When working in this repo, track patterns worth adding here. A good repo-level learning:

- Would have saved time if known at the start of the session
- Applies to future contributors, not just this specific task
- Is a decision, convention, or gotcha — not a one-off fix
- Isn't already captured in this file

At natural pause points, offer: *"I noticed [X pattern]. Should I add this to AGENTS.md?"*

---

## Editing Conventions (Learned from Past Sessions)

### Large section deletions — use Python, not Edit

The Edit tool fails when `old_string` spans many lines or is very long. For cuts of 50+ lines, use an inline Python script:

```python
with open('docs/systems/file.md', 'r') as f:
    lines = f.readlines()
out = lines[:start_line] + lines[end_line:]  # 0-indexed
with open('docs/systems/file.md', 'w') as f:
    f.writelines(out)
```

Or with content-based markers:

```python
start_idx = content.find('\n### Section to cut')
end_idx = content.find('\n## Next section to keep')
new_content = content[:start_idx] + content[end_idx:]
```

### Runnable client code is trimmable boilerplate

Each implementation pattern may end with a `public class XClient { public static void main(String[] args) {...} }` test harness. These are **not learner content** — they just exercise the TODO stubs. When trimming a file for length, always remove these first (typical savings: 60–100 lines per pattern).

### Case Studies sections — always preserve

User explicitly wants all `## Case Studies:` sections kept in every file. Never remove or shorten them, even when cutting for length.

### After "file has been modified since read" error — re-read before editing

If the Edit tool returns "file has been modified since read," re-read the file first, then retry the edit. This happens after Python-based deletions that change line offsets.

