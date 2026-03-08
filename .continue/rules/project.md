# Project Context

This is **itch-n-docs** — a MkDocs static site (backend engineering study guide). Java 25 implementations, Python 3.13 via `uv`. See `AGENTS.md` for full conventions and `AGENTS.local.md` for machine-specific config (git remotes, branch names, personal workflow).

## Critical Rules

**Admonitions** — Cinder theme supports only three types. Never use `!!! info`, `!!! tip`, `!!! success`:

- `!!! note` — blue
- `!!! warning` — yellow/orange
- `!!! danger` — red

**Blank line before lists** — Python-Markdown requires it; missing blank line is a silent failure (renders as plain text):

```markdown
# Wrong
Your task:
- Item 1

# Correct
Your task:

- Item 1
```

**`??? success`** — collapsible blocks use `success` type (pymdownx.details renders it correctly even though `!!! success` is unsupported).

**"Where this topic connects"** — uses a custom div, not an admonition:

```markdown
<div class="bs-callout bs-callout-info" markdown>

**Where this topic connects**

- **Topic** — explanation → [Link](file.md)

</div>
```

## Commands

```bash
uv run mkdocs serve --dirtyreload   # local dev
./gradlew test                       # run all Java tests
./gradlew test --tests "com.study.dsa.binarysearch.*"
```
