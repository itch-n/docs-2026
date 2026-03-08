# itch-n-docs

Backend engineering study guide with interactive documentation and fill-in exercises.

## Setup

### Prerequisites

- Python 3.13+
- [UV](https://github.com/astral-sh/uv) package manager

### Installation

```bash
# Install UV (if not already installed)
curl -LsSf https://astral.sh/uv/install.sh | sh

# Install dependencies
uv sync
```

## Development

### Local Preview

```bash
# Start development server with live reload
uv run mkdocs serve
```

Open http://127.0.0.1:8000 in your browser.

### Build Static Site

```bash
# Generate static HTML in site/ directory
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
│   ├── systems/                    # 15 systems design topics
│   └── dsa/                        # 16 DSA topics
├── mkdocs.yml                      # MkDocs configuration
├── pyproject.toml                  # Python dependencies
└── uv.lock                         # Locked dependencies
```

## Running Java Examples

### Tests

Most implementation classes have a corresponding JUnit 5 test in `src/test/java/`. Tests are the exercise specification — they fail until you implement the TODOs.

```bash
# Run all tests
./gradlew test

# Run tests for a single class
./gradlew test --tests "com.study.dsa.twopointers.OppositeDirectionPointersTest"

# Run tests for a whole package
./gradlew test --tests "com.study.dsa.binarysearch.*"
```

### Running individual classes

Each topic's Java classes can be run individually via Gradle:

```bash
./gradlew run -PmainClass=com.study.systems.storage.BPlusTree
./gradlew run -PmainClass=com.study.dsa.twopointers.OppositeDirectionPointers
```

The fully qualified class name follows the package structure under `src/main/java/`.

### Standalone runners (no unit tests)

These classes produce observable output (benchmarks, concurrency demos, timing) but are not covered by unit tests. Run them directly:

**Benchmarks**

| Class | Description |
|---|---|
| `com.study.systems.storage.StorageBenchmark` | B+Tree vs LSM Tree read/write throughput |
| `com.study.systems.columnstorage.StorageLayoutBenchmark` | Row vs column store scan performance |

**Concurrency demos**

| Class | Description |
|---|---|
| `com.study.systems.concurrency.LockBasedSync` | Thread-safe counter, read-write cache, bank transfers |
| `com.study.systems.concurrency.ThreadPoolPatterns` | Fixed, cached, scheduled, work-stealing pools |
| `com.study.systems.concurrency.ThreadSafeDataStructures` | ConcurrentHashMap, CopyOnWriteArrayList, BlockingQueue |
| `com.study.systems.messagequeues.ProducerConsumer` | Producer-consumer with blocking queue |

**Timing-dependent demos**

| Class | Description |
|---|---|
| `com.study.systems.ratelimiting.TokenBucketRateLimiter` | Token bucket, leaky bucket, sliding window |
| `com.study.systems.caching.WriteBackCache` | Write-back with background flush |
| `com.study.systems.observability.DistributedTracer` | Span creation and trace propagation |
| `com.study.systems.observability.SLOManager` | SLO tracking with time-based windows |
| `com.study.systems.streamprocessing.StreamWindow` | Tumbling, sliding, session windows |

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