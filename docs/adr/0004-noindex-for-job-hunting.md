# 0004 - 求職活動用に検索エンジンインデックスを拒否

## Status

Accepted (2026-05-10)

## Context

本サイトは **求職活動用のポートフォリオ** であり、応募書類に URL を貼って採用担当に直接アクセスしてもらう用途を想定している。

検索エンジンに載ることのリスク:

- **個人情報（氏名・地域・連絡先）が公開検索に上がる**
- 現職や次の応募活動への影響を考慮できない第三者がたどり着く
- ふとした SEO 操作で「未経験 35歳」等のキーワードでヒットしてしまう可能性

採用担当への共有用途と、検索結果での露出は **本質的に異なる目的** であり、後者は避けるべき。

## Decision

**3 層の防御で検索エンジンへのインデックスを拒否する**:

### 1. HTML meta タグ

```html
<meta name="robots" content="noindex, nofollow">
<meta name="googlebot" content="noindex, nofollow">
```

`fragments.html` の共通 `<head>` に配置し、全ページに自動適用。

### 2. /robots.txt

```
User-agent: *
Disallow: /
```

`src/main/resources/static/robots.txt` として静的配信。

### 3. URL を関係者間でのみ共有

応募書類・LinkedIn・職務経歴書・面接連絡時の自己紹介などに直接 URL を記載することで、検索を経由せず到達できる経路を確保。

## Consequences

### Pros

- **検索結果に表示されない** — 関係者しかアクセスしない状態を維持
- **OGP は維持** — Slack / メールで URL を共有した時のリッチプレビューは正しく機能（`og:image` 等は noindex の対象外）
- **個人情報の露出を抑える** — メールアドレス、住所の一部、勤務歴などが検索クロールされない

### Cons

- **Lighthouse の SEO スコアが 66 になる** — `is-crawlable: Page is blocked from indexing` という単一項目で減点
- 応募活動が終わって「広く見せたい」フェーズになった場合、meta + robots.txt を撤去する手間がある
- 既にクロール済みのページは Google Search Console で URL 削除リクエストを送る必要がある（現状クロール対象になる前に noindex を入れたので問題なし）

### Lighthouse SEO 66 の解釈

これは **採点上の減点ではなく設計上の正** である。
`docs/lighthouse/SUMMARY.md` で「失点項目はちょうど 1 件のみ（is-crawlable）であり、求職活動用に noindex / nofollow を設定しているため」と明記しており、応募書類で URL と一緒に渡しても誤解されない設計にしてある。

## Alternatives Considered

1. **Spring Security で BASIC 認証を全パスに掛ける** — 「URL を渡した相手だけ」の要件を厳格に満たすが、応募活動用に毎回パスワードを伝えるのは煩雑。却下。
2. **noindex のみ（robots.txt なし）** — 多くの bot は meta で十分だが、robots.txt があると更にクロール前の段階で弾けるため二重化を採用。
3. **Cloudflare 等の WAF で IP 制限** — 採用担当の IP を事前に知れないため不可能。
4. **何もしない（インデックスされる）** — 個人情報露出の懸念から却下。
