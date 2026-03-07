# AGENTS.md

Context for Claude Code sessions working in this repository.

---

## Project Overview

**itch-n-docs** is an interactive backend engineering study guide published as a static site via MkDocs. The audience is software engineers studying for interviews or deepening systems/algorithms knowledge. Content is structured as fill-in exercises — learners implement first, then explain.

- Site name: "Software Engineering Study Guide"
- Author: Richard
- Deployed to GitHub Pages via GitHub Actions on push to `rpn` branch (see `.github/workflows/deploy.yml`)

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

- `docs/systems/` — 20 topics (files `01`–`20`):
  - `01-storage-engines.md` — B+Tree, LSM Tree *(canonical styling example)*
  - `02-row-vs-column-storage.md` — OLTP vs OLAP, columnar formats
  - `03-networking-fundamentals.md` — TCP/UDP, HTTP, WebSockets, DNS, TLS
  - `04-search-and-indexing.md` — Inverted indexes, full-text search
  - `05-caching-patterns.md` — LRU, LFU, cache-aside, write-through
  - `06-api-design.md` — REST, versioning, pagination
  - `07-security-patterns.md` — JWT, RBAC, API keys
  - `08-rate-limiting.md` — Token bucket, sliding window
  - `09-load-balancing.md` — Consistent hashing, health checks
  - `10-concurrency-patterns.md` — Locks, producer-consumer, thread safety
  - `11-database-scaling.md` — Replication, sharding, partitioning
  - `12-message-queues.md` — Queue vs pub/sub, delivery guarantees
  - `13-event-sourcing-cqrs.md` — Append-only event log, projections
  - `14-stream-processing.md` — Windowing patterns, real-time aggregation
  - `15-observability.md` — Metrics, logging, tracing, SLOs
  - `16-resilience-patterns.md` — Circuit breaker, bulkhead, retry/backoff
  - `17-distributed-transactions.md` — Saga pattern, idempotency
  - `18-consensus-patterns.md` — Raft, leader election, distributed locks
  - `19-microservices-patterns.md` — API gateway, service discovery
  - `20-multi-region.md` — Active-active/passive, conflict resolution

- `docs/dsa/` — 15 topics (files `01`–`15`):
  - `01-two-pointers.md` — Opposite directions, same direction, fast/slow
  - `02-sliding-window.md` — Fixed/variable window subarray problems
  - `03-hash-tables.md` — Lookups, grouping, frequency counting
  - `04-linked-lists.md` — Reversal, cycle detection
  - `05-stacks--queues.md` — LIFO/FIFO, monotonic stacks, deque
  - `06-trees.md` — Inorder/preorder/postorder/BFS, recursion
  - `07-binary-search.md` — Classic, rotated arrays, 2D matrices
  - `08-heaps.md` — Priority queues, top-K, Dijkstra prep
  - `09-union-find.md` — Disjoint sets, dynamic connectivity, Kruskal's MST
  - `10-graphs.md` — DFS/BFS, cycle detection, connected components
  - `11-advanced-graphs.md` — Topological sort, Dijkstra, MST
  - `12-backtracking.md` — Subsets, combinations, permutations
  - `13-dynamic-programming.md` — Fibonacci, house robber, coin change
  - `14-prefix-sums.md` — Range queries, subarray sum, 2D prefix sum
  - `15-intervals.md` — Merge, insert, meeting rooms, greedy removal

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
| `.benchmark-table .blank` | Individual blank cells in benchmark tables | Bold orange text, light bg |
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

## Java Source Structure

All Java lives under `src/main/java/com/study/`. No test files exist yet (JUnit 5 is configured but unused). Base package: `com.study`.

```
src/main/java/com/study/
├── dsa/
│   └── twopointers/
│       ├── DifferentSpeedPointers.java   # Cycle detection, meeting point
│       ├── OppositeDirectionPointers.java # Two-sum, palindrome, max water
│       └── SameDirectionPointers.java    # Remove duplicates, rotate, move zeros
└── systems/
    ├── columnstorage/
    │   ├── ColumnStore.java              # Column-oriented (OLAP) implementation
    │   ├── RowStore.java                 # Row-oriented (OLTP) implementation
    │   └── StorageLayoutBenchmark.java   # Row vs column performance comparison
    └── storage/
        ├── BPlusTree.java                # Self-balancing tree, range queries
        ├── DiskSimulator.java            # Simulates disk I/O latency
        ├── LSMTree.java                  # Log-Structured Merge tree
        └── StorageBenchmark.java         # B+Tree vs LSM performance comparison
```

When adding a new systems topic with Java examples, create a new package under `com.study.systems.<topicname>` matching the docs file number (e.g. `com.study.systems.ratelimiting`).

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

### High-value sections — always preserve

Two section types carry significant editorial value and must never be removed or shortened, even when trimming a file for length:

**`## Case Studies: … in the Wild`** (bottom of systems files, `## Case Studies` in DSA files) — real-world examples grounding the theory. Pattern: `## Case Studies: X in the Wild`.

**`!!! warning "Operational reality"`** (admonition block near top of file, after front matter) — production gotchas and failure modes not covered in interview prep. These appear in ~10 systems files and are uniquely hard to reconstruct.

### After "file has been modified since read" error — re-read before editing

If the Edit tool returns "file has been modified since read," re-read the file first, then retry the edit. This happens after Python-based deletions that change line offsets.

