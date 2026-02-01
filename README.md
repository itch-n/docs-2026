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