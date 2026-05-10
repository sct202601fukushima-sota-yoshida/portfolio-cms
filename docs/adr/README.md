# Architecture Decision Records

本プロジェクトで採用した主要な設計判断と、その背景・代替案・影響を記録する。

[ADR (Architecture Decision Record)](https://adr.github.io/) は **「いつ・なぜ・何を選んだか」** を残すための軽量な形式。後から読む人（および未来の自分）が、コードだけでは追えない判断の文脈を理解できるようにする。

## 一覧

| # | タイトル | 状態 |
|:---:|:---|:---:|
| [0001](0001-css-cascade-layers.md) | CSS Cascade Layers でスタイルアーキテクチャを構築 | Accepted |
| [0002](0002-markdown-ssr-with-cjk-fix.md) | Markdown を SSR でレンダリングし、CJK 句読点問題は後処理で解決 | Accepted |
| [0003](0003-render-supabase-deploy.md) | Render 無料枠 + Supabase Session Pooler でデプロイ | Accepted |
| [0004](0004-noindex-for-job-hunting.md) | 求職活動用に検索エンジンインデックスを拒否 | Accepted |
| [0005](0005-no-opacity-in-entrance-animations.md) | 入場アニメーションから opacity 遷移を排除し a11y を優先 | Accepted |

## フォーマット

各 ADR は MADR ベースの簡易形式:

```
# NNNN - タイトル
## Status — Accepted / Superseded / Deprecated
## Context — なぜこの判断が必要だったか
## Decision — 何を選んだか
## Consequences — 採用したことで起きること（pros / cons）
## Alternatives Considered — 検討して却下した選択肢
```
