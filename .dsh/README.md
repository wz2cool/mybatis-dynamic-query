# `.dsh/` — DeepSeek Harness integration

This directory wires the [OpenSpec](https://github.com/Fission-AI/OpenSpec) workflow
into DeepSeek Harness, the same way `.claude/`, `.codex/` and `.zcode/` wire it into
their tools.

DSH discovers project skills from `.dsh/skills/<name>/SKILL.md` (see
`@deepseek-ai/dsh-skill-filesystem`). Each bundle appears in the session skill
catalog and can be invoked as `/openspec-<action>`:

| Skill | Trigger | OpenSpec workflow |
|---|---|---|
| `openspec-propose` | `/openspec-propose` | create a change and generate all its artifacts |
| `openspec-explore` | `/openspec-explore` | explore a problem before or during a change |
| `openspec-apply-change` | `/openspec-apply-change` | implement the tasks of a change |
| `openspec-archive-change` | `/openspec-archive-change` | archive a completed change |

The `openspec` CLI itself is unchanged and still required — these skills drive it
(`openspec new change`, `openspec status --json`, `openspec instructions … --json`,
`openspec validate --all --strict`). Project context and per-artifact rules live in
`openspec/config.yaml`, which the CLI injects into every artifact's instructions.

## Where these files come from

`openspec init` / `openspec update` generate skills only for the tools in the
OpenSpec CLI's own `AI_TOOLS` registry (`.claude`, `.codex`, `.zcode`, …); DeepSeek
Harness is not in that list, so the CLI cannot generate this directory. Instead,
`.dsh/skills/` is derived from the `.zcode/skills/` bundle the CLI *does* maintain:

```bash
openspec update            # refresh .claude / .codex / .zcode from the CLI
python3 scripts/sync-dsh-skills.py   # re-derive .dsh/skills (requires python3)
```

`python3 scripts/sync-dsh-skills.py --check` writes nothing and exits non-zero when
the generated files are missing, stale, or when the upstream templates no longer
match the adaptation rules — use it in review or CI to catch a forgotten re-sync.

The generated `SKILL.md` files are committed (as the `.claude`/`.codex`/`.zcode`
copies are) so the workflow works without running the script first. Do not hand-edit
them: edit `.zcode/skills/*/SKILL.md` or `scripts/sync-dsh-skills.py` and re-run it.
Each file carries a one-line marker saying so.

## Adaptations the script applies

Upstream skill bodies are copied verbatim except for tool and command names, so that
the instructions name facilities this harness actually has:

| Upstream | Here | Why |
|---|---|---|
| `AskUserQuestion tool` | `ask_user_question` tool | the DSH tool for the same interaction |
| `TodoWrite tool` | `todo_write` tool | idem |
| `/opsx:apply`, `/opsx:explore` | `/openspec-apply-change`, `/openspec-explore` | DSH loads a user-invocable skill as `/skill-name`; its composer rejects the `:` in the `/opsx:<id>` form, which would arrive as plain text instead of running the workflow |
| Agent/Task tool with `subagent_type` | `subagent` tool | DSH's subagent tool takes a self-contained prompt rather than a named subagent type |

The script asserts an exact occurrence count for every rule, so an upstream template
change fails loudly instead of producing a half-adapted skill.

## Known gap inherited from the workflow profile

The archive skill's spec-sync step refers to `openspec-sync-specs`, which this
project's workflow profile does not generate (see `workflows` in the global OpenSpec
config: `propose`, `explore`, `apply`, `archive`). That gap is not DSH-specific — no
tool directory here ships that skill. `openspec archive` syncing delta specs into
`openspec/specs/` itself (it does so unless `--skip-specs` is passed) is the
alternative if the agent-driven sync is unavailable.
