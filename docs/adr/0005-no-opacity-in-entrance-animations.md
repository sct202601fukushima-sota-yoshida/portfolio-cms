# 0005 - 入場アニメーションから opacity 遷移を排除し a11y を優先

## Status

Accepted (2026-05-11)

## Context

採用担当に「直感的に洗練されている」印象を与えるため、3 種の入場アニメーションを実装した:

1. ヒーローの黄金螺旋を `stroke-dashoffset` で手描き風に描画
2. ヒーロー h1 → lead → meta を段差つきで登場させる
3. スクロールでスライドカードを順次フェードイン (`animation-timeline: view()`)

当初の実装では、すべてに `opacity: 0 → 1` の遷移を含めていた:

```css
@keyframes card-reveal {
    from { opacity: 0; transform: translateY(24px); }
    to   { opacity: 1; transform: translateY(0); }
}
```

その結果、Lighthouse の Accessibility スコアが **100 → 95 に降下** した。

## Context (more detail)

### 何が起きたか

Lighthouse の `color-contrast` audit がアニメーション途中の画面状態をサンプリングしていた。
具体的には、`animation-timeline: view()` で entry range 中のスライドカードは `opacity` が `0 < x < 1` の中間値となり、ブラウザは **ハーフ透明状態のテキストを背景と合成した結果のピクセル色** を最終的にレンダリングする。

色のサンプル例:

| サンプル要素 | 期待値 | 実測値 (Lighthouse) | 計算上の合成 (opacity 0.53) |
|:---|:---|:---|:---|
| テキスト (`var(--color-text)`) | `#c9cfd6` | `#797e84` | `0.53 × #c9cfd6 + 0.47 × #1f242c ≈ #797e84` ✓ |
| 背景 (`var(--color-surface)`) | `#1f242c` | `#181c23` | `0.53 × #1f242c + 0.47 × body bg #0e1117 ≈ #181c23` ✓ |

**コントラスト比: 4.17** （WCAG AA 要件の 4.5 を下回る）。

### 本質的な問題

- フル不透明時のコントラストは ~10:1 で問題ない
- **しかしアニメーション中の中間状態がテキストの可読性を一時的に損なう**
- Lighthouse はその一瞬を検出するし、**実利用中のユーザーにも数百ms～1秒程度の「読みづらい瞬間」が存在する**

つまりこれは Lighthouse 対策というより、**実際にアクセシビリティを損なっていた** ということ。

## Decision

**入場アニメーションから `opacity` 遷移を完全に排除し、`transform: translateY(...)` のみで「現れる」感覚を表現する**。

```css
@keyframes card-reveal {
    from { transform: translateY(24px); }
    to   { transform: translateY(0); }
}

@keyframes hero-fade-up {
    from { transform: translateY(12px); }
    /* opacity 遷移を意図的に省く */
}
```

## Consequences

### Pros

- **コントラスト比が常に 10:1 以上を維持** → Lighthouse Accessibility 100 復活
- **実利用中の可読性が向上** — 中間状態でも文字が読める
- 演出としても遜色ない —  「位置が動いて止まる」だけで「現れた」感は十分作れる
- `prefers-reduced-motion: reduce` 対応のロジックも単純化（`opacity` を戻す必要なし）

### Cons

- 「フェードイン」感は減る → 厳密な比較ではやや控えめな演出に
- ただし transform だけでも十分にスムーズで、視覚的な"完成度"はほぼ等価

### 学び（ライブラリ / フレームワーク選びの観点でも）

- **アニメーション中のコントラスト** は a11y チェックの盲点になりやすい
- `opacity: 0 → 1` パターンは多くのライブラリ・チュートリアルでデフォルトだが、**コントラスト要件のあるテキストには本来不適切**
- スタイリッシュな入場演出を作る時の原則: **位置（transform）だけで動かし、可視性は常に保つ**

## Alternatives Considered

1. **`opacity: 0.5 → 1` の短い遷移** — 半透明化が短時間でも、Lighthouse の audit に引っかかるタイミングは残る。却下。
2. **アニメーションを削除して静的表示** — 採用担当への「洗練感」演出効果が消える。却下。
3. **`prefers-reduced-motion: reduce` だけを設定して放置** — Lighthouse は `prefers-reduced-motion` を設定せずに audit するため救えない。却下。
4. **`opacity` を `clip-path` の reveal アニメに置き換え** — テキストでは読めない瞬間が一部発生し、CJK の縦書き要素では破綻する可能性。却下。
