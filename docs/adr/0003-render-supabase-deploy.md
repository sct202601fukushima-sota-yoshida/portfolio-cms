# 0003 - Render 無料枠 + Supabase Session Pooler でデプロイ

## Status

Accepted (2026-05-10)

## Context

求職活動用のポートフォリオを **完全無料** で公開したい。前提:

- Spring Boot 3.5 / Java 17 / PostgreSQL 15 構成
- 個人プロジェクトのため月額課金は避けたい
- 採用担当者がアクセスする頻度は低～中（初回オープン → 数分閲覧 → 離脱が多い）
- 福島県在住、東京・大阪のリモート採用が主ターゲット → 日本リージョンに近い場所が理想

## Decision

**アプリ層: Render Web Service (Free) — Singapore リージョン**
**DB 層:  Supabase PostgreSQL (Free) — `Session Pooler` 経由**

### Render を選んだ理由

| 候補 | Pros | Cons |
|:---|:---|:---|
| **Render** | GitHub 連携で自動デプロイ、Docker 対応、無料 SSL、Singapore リージョン | 15 分アイドルでスリープ → コールドスタート 30〜60 秒 |
| OCI Always Free | 真の永久無料、ARM 4 vCPU / 24GB RAM、スリープなし | 構築に半日以上 (VCN, security list, ARM 用 JVM)、Postgres は別途 |
| Fly.io | 無料枠あり、Postgres 内蔵 | クレジットカード登録必須、料金体系が変わりやすい |
| Railway | UX 良好 | 完全無料枠が縮小傾向 |

学習コストと完成までの距離を最小化したかったため、**Render を選択**。スリープ問題は **GitHub Actions の cron で `0,10,20,30,40,50` の 10 分間隔で /  を ping することで緩和** している。

### Supabase を選んだ理由

Render の無料 PostgreSQL は 90 日で消滅する制限がある。永続的な DB が必要だったため:

- Supabase の無料 Postgres は **500 MB まで永久無料**
- リージョンは ap-northeast-1 (東京) を選択し、Render Singapore からのレイテンシは ~80ms
- GitHub アカウントで即サインアップ

### Session Pooler を選んだ理由

Supabase は 3 種の接続経路を提供する:

| 経路 | URL 形式 | プロトコル | 用途 |
|:---|:---|:---|:---|
| Direct | `db.xxx.supabase.co:5432` | IPv6 のみ | 開発機からの直接接続 |
| **Session Pooler** | `aws-N-region.pooler.supabase.com:5432` | **IPv4 対応** | 通常の Web アプリ |
| Transaction Pooler | `aws-N-region.pooler.supabase.com:6543` | IPv4 対応、prepare 文非対応 | サーバレス向け |

**Render の無料枠は IPv6 outbound 不可** のため、Direct 接続は使えない。Spring Boot + HikariCP は prepare 文を多用するため Transaction Pooler も不適。**Session Pooler が唯一の選択肢**となる。

## Consequences

### Pros

- **完全無料** で本番デプロイが回る（OCI ほどの構築コストなく）
- `git push` → 自動再デプロイのループが確立
- Supabase ダッシュボードで DB の状態を GUI で確認可能（Flyway 適用後の検証が楽）
- `application-prod.yml` の `${DB_URL} ${DB_USER} ${DB_PASSWORD}` で環境別に切り替え可能

### Cons

- **コールドスタート** — 15 分アイドル後の初回アクセスは 30〜60 秒待ち（keep-warm cron で軽減中）
- **Render の DC が Singapore** — 日本のユーザーから ~50ms の遅延（実用上問題ないレベル）
- **Supabase の 500 MB 制限** — 数百枚の画像をアップロードすると枯渇する（現状 portfolio-samples のみで余裕）

### 監視・運用上の注意

- Supabase Free は 7 日間アクセスがないと一時停止される → keep-warm cron が DB セッションも温める副次効果あり
- Render の Free 750 hr/月制限 → 1 サービスのみなら超えない

## Alternatives Considered

1. **OCI Always Free + Supabase** — VCN セットアップ等の構築コストが大きく、時間効率で却下
2. **Render Free + Render Postgres (90 日)** — DB が消えるリスク回避のため却下
3. **GitHub Pages + 静的化** — CMS の動的部分が死ぬので却下（このサイトは「動く CMS」自体が成果物）
