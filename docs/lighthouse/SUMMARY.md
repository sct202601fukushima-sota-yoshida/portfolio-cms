# Lighthouse Audit Summary

- URL: https://portfolio-cms-cjr4.onrender.com/
- Audit date: 2026-05-10
- Lighthouse: 12.8.2

| Category | Mobile | Desktop | Note |
|:---|:---:|:---:|:---|
| Performance | 97 | 99 | Render 無料枠 TTFB の変動で 97〜100 を行き来する |
| Accessibility | 100 | 100 | WCAG AA 色コントラスト準拠 |
| Best Practices | 100 | 100 | |
| SEO | 66 | 66 | **意図的に低い** — 求職活動用に noindex/nofollow を設定 |

## SEO 66 の内訳

失点項目はちょうど 1 件のみ:

- \`is-crawlable: Page is blocked from indexing\` — meta robots と /robots.txt で意図的に検索エンジンへのインデックスを拒否しているため

求職活動用ポートフォリオとして「URL を渡した相手だけが見られる」状態を維持することを優先した設計判断。SEO 観点で言えばこれは設計上の "正" であり、減点ではない。

## Mobile Web Vitals

| Metric | Value |
|:---|:---|
| Largest Contentful Paint | 1.2 s |
| Total Blocking Time | 0 ms |
| Cumulative Layout Shift | 0 |
