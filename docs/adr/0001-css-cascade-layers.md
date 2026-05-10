# 0001 - CSS Cascade Layers でスタイルアーキテクチャを構築

## Status

Accepted (2026-05-10)

## Context

`style.css` が 660 行を超えた段階で、上書き関係が偶発的に決まる場面が増えてきた。具体的には:

- ダークモード `@media (prefers-color-scheme: dark)` の上書きが、後から追加されたコンポーネントスタイルに負けることがある
- 印刷用 `@media print` が他のメディアクエリと衝突した
- `!important` を使いたくなる箇所が出始めていた

この種の問題は CSS の specificity（詳細度）と宣言順序の組み合わせで起きるが、コードの量が増えるほど **「なぜこのスタイルが効いている／効いていないのか」を追跡するコストが増大** する。

## Decision

[CSS Cascade Layers (`@layer`)](https://developer.mozilla.org/en-US/docs/Web/CSS/@layer) を導入し、5 つのレイヤーで宣言順序を明示する:

```css
@layer reset, tokens, base, components, overrides;
```

| レイヤー | 中身 | 優先度 |
|:---|:---|:---:|
| reset | ブラウザ既定値の正規化 | 低 |
| tokens | `:root` の CSS 変数（φ ベースのデザイントークン） | 低 |
| base | 素のタグ（`html`, `body`, 見出し, リンク, `img`） | |
| components | 名前付きコンポーネント（`.hero`, `.slide-card`, ...） | |
| overrides | `@media`（responsive / dark / print / reduced-motion） | 高 |

レイヤー間の優先度は **宣言順序** で決まり、specificity は無視される（後発レイヤーが必ず勝つ）。レイヤー内では通常通り specificity が働く。

## Consequences

### Pros

- **`!important` を撲滅できた** — 上書き関係を「宣言順序」で説明できる
- **新規コンポーネントの追加で specificity 戦争が起きない** — 必ず components レイヤーに置くだけ
- **メディアクエリの集約** — overrides レイヤーに responsive / dark / print / reduced-motion を全部入れる規律が生まれた
- **可読性向上** — `@layer reset, tokens, base, components, overrides;` の宣言を見るだけで全体像が掴める

### Cons

- **学習コスト** — `@layer` は 2022 年標準化の比較的新しい機能で、初見の人には説明が必要
- **デバッグ時の認知負荷** — DevTools で specificity を見る時、レイヤーも含めて理解する必要がある（ただし Chrome DevTools は @layer 対応済み）

### Browser Support

- Chrome 99+, Firefox 97+, Safari 15.4+ で対応（2022年以降）
- 本ポートフォリオの想定ユーザー（採用担当者）の標準ブラウザは全て対応済み

## Alternatives Considered

1. **BEM (Block Element Modifier) 命名規則** — specificity を全 1 クラスにフラット化することで戦争回避。
   却下理由: クラス名が冗長になる (`.slide-card__title--featured`)。φ ベースの token とも噛み合わない。
2. **CSS Modules / CSS-in-JS** — フレームワーク（React 等）と組み合わせて使うのが本筋で、Thymeleaf SSR には過剰。
3. **`!important` を許容する** — 短期的には楽だが、追加のたびに連鎖して負債が膨らむ。
