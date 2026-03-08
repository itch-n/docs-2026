# itch-n-docs

Backend engineering study guide with interactive documentation and fill-in exercises.

## Getting Started

This site is designed to be used **locally with live reload** — open it in your browser while you work through topics, and changes save instantly.

```bash
# 1. Install UV (if not already installed)
curl -LsSf https://astral.sh/uv/install.sh | sh

# 2. Install dependencies
uv sync

# 3. Start the site
uv run mkdocs serve --dirtyreload
```

Open http://127.0.0.1:8000. The `--dirtyreload` flag rebuilds only changed files, so saves are reflected in the browser in under a second.

## Other Commands

```bash
# Full rebuild (slower, use if something looks stale)
uv run mkdocs serve

# Build static site
uv run mkdocs build

# Build with strict mode (fails on warnings)
uv run mkdocs build --strict
```

## Project Structure

```
.
├── docs/                           # Markdown content
│   ├── index.md                    # Landing page
│   ├── css/
│   │   └── custom.css              # Custom styling
│   ├── systems/                    # 20 systems design topics
│   ├── dsa/                        # 15 DSA topics
│   └── reference/                  # Quick reference pages
│       ├── when-it-breaks.md
│       ├── back-of-envelope.md
│       └── symptom-pattern.md
├── src/
│   ├── main/java/com/study/
│   │   ├── systems/                # Java implementation stubs
│   │   └── dsa/                    # Java implementation stubs
│   └── test/java/com/study/
│       ├── systems/                # JUnit 5 tests (exercise spec)
│       └── dsa/                    # JUnit 5 tests (exercise spec)
├── mkdocs.yml                      # MkDocs configuration
├── pyproject.toml                  # Python dependencies
└── uv.lock                         # Locked dependencies
```

## Running Java Examples

All Java code requires Java 25.

### Tests

Every implementation class has a corresponding JUnit 5 test in `src/test/java/`. Tests are the exercise specification — they fail until you implement the TODOs.

```bash
# Run all tests
./gradlew test

# Run tests for a single class
./gradlew test --tests "com.study.dsa.twopointers.OppositeDirectionPointersTest"

# Run tests for a whole package
./gradlew test --tests "com.study.dsa.binarysearch.*"
```

Tests use [Awaitility](https://github.com/awaitility/awaitility) for concurrent and async assertions. Time-dependent classes (e.g. `TokenBucketRateLimiter`) accept a `java.time.Clock` constructor argument; tests pass a `MutableClock` from `com.study.util` to advance time without sleeping.

### Standalone benchmarks

These two classes produce timing output and are intentionally runnable; they have no unit tests:

| Class | Description |
|---|---|
| `com.study.systems.storage.StorageBenchmark` | B+Tree vs LSM Tree read/write throughput |
| `com.study.systems.columnstorage.StorageLayoutBenchmark` | Row vs column store scan performance |
| `com.study.systems.searchindexing.SearchIndexBenchmark` | Linear scan vs inverted index query latency |

```bash
./gradlew run -PmainClass=com.study.systems.storage.StorageBenchmark
./gradlew run -PmainClass=com.study.systems.columnstorage.StorageLayoutBenchmark
./gradlew run -PmainClass=com.study.systems.searchindexing.SearchIndexBenchmark
```

## Content Maintenance

### Markdown Formatting

**Common issue:** Lists need blank lines before them to render correctly.

**Fix with regex find/replace:**

```
Find:    (^[^-+\s].+\n)([-+]|1\.)
Replace: $1\n$2
```

This adds a blank line before list items that are missing proper spacing.

**Example:**

```markdown
# Before
Your task: Answer these questions.
- Question 1
- Question 2

# After
Your task: Answer these questions.

- Question 1
- Question 2
```

### Applying CSS Classes

To mark sections as learner-fillable:

```markdown
<div class="learner-section" markdown>

**Your task:** Fill in the blanks.

- Your answer: <span class="fill-in">[Fill in]</span>

</div>
```

### Adding New Topics

1. Create markdown file in `docs/systems/` or `docs/dsa/`
2. Follow existing structure (ELI5, Quiz, Implementation, etc.)
3. Add to `mkdocs.yml` navigation
4. Apply CSS classes for learner sections

## Configuration

### MkDocs (`mkdocs.yml`)

- **Theme:** Cinder
- **Plugins:** Search
- **Extensions:** Code highlighting, tables, task lists, details/summary

### Custom CSS

Located at `docs/css/custom.css`. Defines:

- `.learner-section` - Learner fill-in areas
- `.fill-in` - Individual blanks
- `.benchmark-table` - Results tables
- Checklist styling
- Details/summary collapsible sections

## Deployment

### GitHub Actions (Automatic)

Automatically deploy to GitHub Pages on every push to `main`.

**Workflow:** See [`.github/workflows/deploy.yml`](.github/workflows/deploy.yml)

**Setup:**

**1. Enable GitHub Pages:**

- Go to repository Settings → Pages
- Source: Deploy from a branch
- Branch: `gh-pages` / `root`
- Save

**2. Push to trigger deployment:**

```bash
git add .
git commit -m "Enable GitHub Actions deployment"
git push origin main
```

The site will be available at `https://<username>.github.io/<repository>/`

### GitHub Pages (Manual)

```bash
# Build and deploy to gh-pages branch
uv run mkdocs gh-deploy
```