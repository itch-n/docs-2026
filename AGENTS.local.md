# AGENTS.local.md

Machine-specific configuration. Not synced to the upstream template.

## Deployment

Personal branch: `rpn` — this is the branch that triggers GitHub Actions deployment (see `.github/workflows/deploy.yml`).

---

## Git Workflow

**Two remotes, two concerns:**
- `origin` → `https://github.com/itch-n/docs-2026.git` (personal fork, deploys from `rpn`)
- `upstream` → `https://github.com/itch-n/docs.git` (learner template, stays clean)

**Branch roles:**
- `main` — template content only. Mirrored exactly to `upstream/main`. No personal work ever lands here.
- `rpn` — personal branch. Always `main` + one commit on top containing fill-ins, implementations, and personal config.

**Default: stay on `rpn`.** Switch to `main` only when doing template work. Switch back and rebase immediately after.

---

### What goes where

| Type of change | Branch | Pushed to |
|----------------|--------|-----------|
| New doc, content fix, diagram, renamed file | `main` | `origin main` + `upstream main` |
| Fill-in answers, Java implementations | `rpn` | `origin rpn` only |
| `.idea/`, `.continue/`, personal config | `rpn` | `origin rpn` only |
| `AGENTS.local.md` changes | `rpn` | `origin rpn` only |
| `AGENTS.md` changes (template instructions) | `main` | `origin main` + `upstream main` |

---

### Template work (new docs, fixes, content improvements)

```bash
git checkout main
git fetch upstream && git merge --ff-only upstream/main   # ensure current
# edit files...
git add <files> && git commit -m "..."
git push origin main && git push upstream main            # both remotes
git checkout rpn && git rebase main                       # bring personal forward
git push -f origin rpn                                    # rebase rewrites history
```

### Personal work (fill-ins, implementations)

```bash
# just stay on rpn — no switching needed
git add <files> && git commit --amend                     # keep personal work as one commit
git push -f origin rpn
```

Personal work lives in a single amended commit on top of `main`. Amending (not stacking) keeps the rebase clean — one personal commit means one thing to replay when `main` moves.

### Syncing upstream changes (template got new content)

```bash
git fetch upstream
git checkout main && git merge --ff-only upstream/main
git push origin main
git checkout rpn && git rebase main
git push -f origin rpn
```

---

### Known gotcha: `.gitignore` conflicts on cherry-pick / rebase

`.gitignore` is the one file both branches touch. Template adds entries like `*.drawio.bkp`; personal adds `.continue/config.json` and `CONTINUITY.md`. When rebasing `rpn` onto a new `main`, expect a conflict here. Resolution is always: **keep both sets of entries** (template entries + personal entries).

### If `--ff-only` fails on main

Personal work was accidentally committed to `main`. Do not force-merge. Identify which commits are personal, reset main to the last clean template commit, and cherry-pick the personal commits back onto `rpn`.

---

## Commit discipline

The personal commit (`rpn`) is intentionally amended rather than stacked. If it grows too large or covers distinct concerns (e.g., two-pointers fill-ins vs storage implementations), split it into multiple commits — but keep all of them clearly after the template tip so rebase stays mechanical.
