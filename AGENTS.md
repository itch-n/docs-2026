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

1. **Learning Objectives** — bulleted outcomes
2. **`!!! warning "Operational reality"`** — theory-vs-production gap, named systems approach
3. **ELI5** — simplified explanation (learner fills in)
4. **Core Implementation / Core Concepts** — code stubs with TODOs and debugging challenges
5. **Before/After** — motivation for the pattern (some files)
6. **Case Studies** — real-world examples (always preserve, never cut)
7. **Common Misconceptions** — `!!! warning` blocks, one per misconception
8. **`!!! warning "When it breaks"`** — scale thresholds and failure conditions
9. **Decision Framework** — when/how to use the concept (learner fills in)
10. **Practice** — scenario questions
11. **Test Your Understanding** — `??? success "Rubric"` self-assessment
12. **Connected Topics** — `!!! info "Where this topic connects"`

Topics live in two sections:

- `docs/systems/` — 20 topics: storage engines, row vs column, networking, search & indexing, caching, API design, security, rate limiting, load balancing, concurrency, database scaling, message queues, event sourcing/CQRS, stream processing, observability, resilience, distributed transactions, consensus, microservices, multi-region
- `docs/dsa/` — 15 topics: two pointers, sliding window, hash tables, linked lists, stacks & queues, trees, binary search, heaps, union-find, graphs, advanced graphs, backtracking, dynamic programming, prefix sums, intervals

Quick reference pages live directly in `docs/`:

- `docs/when-it-breaks.md` — compiled scale thresholds and failure conditions from all 35 topic files
- `docs/back-of-envelope.md` — hardware primitives, worked derivations, QPS/storage/write scale context

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

### Standard admonitions

Four admonition types have established placement and purpose:

| Admonition | Type | Placement | Purpose |
|------------|------|-----------|---------|
| `!!! warning "Operational reality"` | warning | After Learning Objectives `---`, before `## ELI5` | Theory-vs-production gap; named tools and systems |
| `!!! warning "When it breaks"` | warning | Before `## Decision Framework` (or after Case Studies if present) | Scale thresholds and failure conditions |
| `!!! warning "Misconception N: ..."` | warning | In Common Misconceptions section | One block per misconception |
| `!!! info "Where this topic connects"` | info | End of file | Cross-topic links |

Collapsible blocks (`??? success "Answer"`, `??? success "Rubric"`) are used for debugging challenge answers and self-assessment rubrics respectively.

For reference pages (`back-of-envelope.md` style), use `??? note "Derivation"` to collapse the arithmetic and keep results scannable at the top level.

### Operational reality — content rules

- Anchor to **named systems and tools** (Redis, PostgreSQL, Kafka, Linux kernel, git), not language-specific APIs
- The voice is direct, slightly contrarian, and names real failure modes
- For DSA files: focus on "where this appears in systems you already work with" — non-obvious production appearances
- For systems files: focus on the gap between the textbook pattern and what actually ships

### When it breaks — content rules

- Lead with the breaking condition, then give the concrete number or threshold
- Format: `**Concept:** description. **Concept:** description.` within the single admonition block
- For DSA: breaking conditions are algorithmic (wrong precondition, wrong operation, state space too large)
- For systems: breaking conditions are scale thresholds (ops/sec, node count, data volume, latency floor)

### Quick Reference pages — maintenance note

`docs/when-it-breaks.md` and `docs/back-of-envelope.md` are intentionally curated digests, not auto-generated mirrors. Minor drift from individual topic files is acceptable. Update them when making significant changes to a topic's breaking conditions or scale thresholds — not for every small edit.

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

