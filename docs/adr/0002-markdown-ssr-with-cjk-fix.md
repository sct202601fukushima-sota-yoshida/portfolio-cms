# 0002 - Markdown を SSR でレンダリングし、CJK 句読点問題は後処理で解決

## Status

Accepted (2026-05-09)

## Context

スライドの説明文を Markdown で書きたい要件があった（`**強調**`、リスト、リンク、改行など）。
当初は `th:text` で素のテキストを出していたため、`**` などの記号がそのまま表示されていた。

選択肢:
1. クライアントサイド Markdown レンダラ（marked.js 等）
2. サーバーサイド Markdown レンダラ（commonmark-java 等）
3. WYSIWYG エディタを管理画面に搭載

## Decision

**サーバーサイド (SSR) で `commonmark-java` を使ってレンダリング** する。理由:

- 既存の Thymeleaf SSR アーキテクチャに自然に乗る
- クライアントに JS バンドルを増やさない（Lighthouse スコア維持）
- 検索エンジン / SNS スクレイパーが見る HTML が完成形になる（OGP との相性も良い）
- `escapeHtml(true)` で生 `<script>` を遮断し、防御的に XSS 対策

```java
@Service
public class MarkdownService {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder()
            .escapeHtml(true)
            .softbreak("<br />\n")
            .build();
    public String toHtml(String md) { ... }
}
```

Thymeleaf からは `${@markdownService.toHtml(slide.description)}` で直接呼出し、`th:utext` で出力。

### CJK 句読点問題への対処

CommonMark の **flanking rule** は英文向けに設計されており、`**` の直前直後に CJK 括弧（「」『』）が来ると、太字として認識されない既知の挙動がある。

例: `のではなく**「監督する」**習慣` → `**「監督する」**` がリテラル文字として残る。

`MarkdownService` 内で **commonmark の出力 HTML をポストプロセス** することで対処:

```java
private static final Pattern RESIDUAL_BOLD =
        Pattern.compile("\\*\\*([^*\\n]{1,200}?)\\*\\*");

public String toHtml(String md) {
    String html = renderer.render(parser.parse(md));
    return RESIDUAL_BOLD.matcher(html).replaceAll("<strong>$1</strong>");
}
```

## Consequences

### Pros

- スライド本文を直感的に Markdown で書ける
- XSS が構造的に閉じる（`escapeHtml=true` + ホワイトリストな後処理）
- ブラウザ依存なくサーバー側で確定形が決まる
- CJK エッジケースを 5 行のコードで吸収

### Cons

- **本文を書く側に「インラインコード内に `**` を書くと strong に変換される」** 制約が残る（実用上は許容範囲）
- commonmark のバージョン更新時に flanking ロジックが変わると、後処理が冗長化する可能性

### テスト

`MarkdownServiceTest` で以下を検証（11 ケース）:

- 太字 / 斜体 / リンク / 番号付きリスト / 箇条書き / インラインコード / softbreak
- CJK 句読点隣接 (`**「監督する」**`) も `<strong>` 化される
- `<script>` / `<img onerror>` などの生 HTML がエスケープされる
- `null` / 空白のみ入力で空文字を返す

## Alternatives Considered

1. **クライアント側 marked.js** — 却下: バンドルサイズ増、SSR と非対応、SEO/OGP に不利
2. **WYSIWYG (TinyMCE 等)** — 却下: 管理画面の実装コスト過大、Markdown 直書きの方が高速で間違いない
3. **CJK 対応の Markdown ライブラリ (markdown-it + cjk plugin)** — 却下: Java では選択肢が乏しい、commonmark + 数行の後処理で十分
