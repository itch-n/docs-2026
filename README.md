# Staff Engineering Preparation Guide

A comprehensive technical study guide for staff engineer interviews, covering systems design, data structures & algorithms, and infrastructure & operations.

## Overview

This guide follows a **Three Pillars** approach to staff engineering preparation:

- **Pillar 1: Systems Design & Architecture** - From API design to distributed systems
- **Pillar 2: Data Structures & Algorithms** - From core patterns to system-level algorithms  
- **Pillar 3: Infrastructure & Operations** - From concurrency to observability at scale

Each pillar is structured with progressive difficulty: **Foundations** → **Advanced** → **Practice**

## Prerequisites

- **Python 3.13+** - Required for MkDocs
- **UV** - Modern Python package manager (recommended)

### Install UV

```bash
# macOS/Linux
curl -LsSf https://astral.sh/uv/install.sh | sh

# Windows
powershell -c "irm https://astral.sh/uv/install.ps1 | iex"

# Or via pip
pip install uv
```

## Quick Start

### 1. Clone and Setup

```bash
git clone <repository-url>
cd staff-eng-prep

# Install dependencies with UV
uv sync
```

### 2. Local Development

```bash
# Serve documentation locally with live reload
uv run mkdocs serve

# Open http://127.0.0.1:8000 in your browser
```

### 3. Build Static Site

```bash
# Build static site to site/ directory
uv run mkdocs build

# Clean previous build
uv run mkdocs build --clean
```

## Development Workflow

### Adding Content

1. **Create new content** in the appropriate pillar directory:
   ```
   docs/
   ├── pillar-1/          # Systems Design
   │   ├── foundations/   # Senior engineer level
   │   ├── advanced/      # Staff engineer level
   │   └── practice/      # Problems & case studies
   ├── pillar-2/          # DSA
   └── pillar-3/          # Infrastructure
   ```

2. **Update navigation** in [`mkdocs.yml`](mkdocs.yml) if adding new pages

3. **Live preview** your changes with `uv run mkdocs serve`

### Writing Guidelines

- Use **clear headings** with consistent structure
- Include **code examples** in Java for implementation details
- Add **learning objectives** at the start of each topic
- Focus on **trade-offs and decisions** rather than just implementation
- Link to **external resources** for deeper learning

### Content Template

```markdown
# Topic Name

## Learning Objectives
- Specific, actionable learning goals

## Core Concepts
- Key ideas and principles

## Advanced Topics  
- Staff-level depth and complexity

## Resources
- Links to further reading
```

## Deployment Options

### GitHub Pages (Recommended)

```bash
# Setup GitHub Pages deployment
uv run mkdocs gh-deploy

# This will:
# 1. Build the site
# 2. Push to gh-pages branch
# 3. Enable GitHub Pages automatically
```

### Manual Deployment

```bash
# Build static site
uv run mkdocs build

# Deploy the site/ directory to any static hosting:
# - Netlify: drag & drop the site/ folder
# - Vercel: connect to repository
# - AWS S3: sync with aws s3 sync site/ s3://bucket-name
# - Any web server: copy site/ contents to document root
```

### Continuous Deployment

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy MkDocs
on:
  push:
    branches: [main]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: astral-sh/setup-uv@v3
        with:
          enable-cache: true
      - run: uv sync
      - run: uv run mkdocs gh-deploy --force
```

## Project Structure

```
staff-eng-prep/
├── docs/                    # Documentation source
│   ├── index.md            # Homepage
│   ├── pillar-1/           # Systems Design
│   ├── pillar-2/           # Data Structures & Algorithms
│   └── pillar-3/           # Infrastructure & Operations
├── mkdocs.yml              # MkDocs configuration
├── pyproject.toml          # Python project metadata
├── uv.lock                 # Locked dependencies
├── main.py                 # Optional Python entry point
└── README.md               # This file
```

## Configuration

### MkDocs Features

- **Material Theme**: Modern, responsive design with dark/light mode
- **Navigation**: Hierarchical sidebar with tabs
- **Search**: Full-text search with suggestions
- **Code Highlighting**: Syntax highlighting with copy buttons
- **Roam Links**: Wiki-style linking between pages
- **Math Support**: LaTeX formula rendering

### Customization

Edit [`mkdocs.yml`](mkdocs.yml) to:
- Modify theme colors and fonts
- Add/remove navigation sections
- Configure plugins and extensions
- Update site metadata

## Development Commands

```bash
# Install dependencies
uv sync

# Serve locally with live reload
uv run mkdocs serve

# Serve on custom port
uv run mkdocs serve --dev-addr localhost:8080

# Build static site
uv run mkdocs build

# Clean and rebuild
uv run mkdocs build --clean

# Deploy to GitHub Pages
uv run mkdocs gh-deploy

# Check for broken links (if plugin installed)
uv run mkdocs build --strict

# Run with verbose output
uv run mkdocs serve --verbose
```

## Dependencies

The project uses these key dependencies:

- **[MkDocs](https://www.mkdocs.org/)**: Static site generator
- **[MkDocs Cinder](https://github.com/chrissimpkins/cinder)**: Clean, responsive theme
- **[MkDocs Roam Links](https://github.com/Jackiexiao/mkdocs-roamlinks-plugin)**: Wiki-style linking

All dependencies are managed with UV for fast, reliable builds.

### Content Priorities

- **Real-world examples** from production systems
- **Trade-off analysis** for different approaches
- **Hands-on exercises** and practice problems
- **Case studies** from major tech companies
- **Interview-focused** scenarios and questions

## Resources

### Essential Reading
- **"Designing Data-Intensive Applications"** - Martin Kleppmann
- **"Staff Engineer"** - Will Larson  
- **"Building Microservices"** - Sam Newman

### Tech Blogs
- [Netflix Tech Blog](https://netflixtechblog.com/) - Resilience and scalability
- [Uber Engineering](https://eng.uber.com/) - Distributed systems
- [High Scalability](http://highscalability.com/) - Architecture case studies