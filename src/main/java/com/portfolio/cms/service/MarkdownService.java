package com.portfolio.cms.service;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

/**
 * Renders Markdown to HTML for slide descriptions.
 *
 * <p>セキュリティ:
 * <ul>
 *   <li>{@code escapeHtml(true)} で本文中の生 HTML（&lt;script&gt; 等）をエスケープ。
 *       管理画面でしか入力されない想定だが、防御的に有効化している。</li>
 *   <li>{@code softbreak("&lt;br /&gt;")} で改行（単一 \n）を視覚的な改行に変換し、
 *       既存シードデータの段組みを保つ。</li>
 * </ul>
 */
@Service
public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder()
                .escapeHtml(true)
                .softbreak("<br />\n")
                .build();
    }

    /**
     * 「**bold**」が CommonMark の flanking rule で太字認識されなかった残留パターンを
     * 後処理で {@code <strong>} へ昇格させる。CommonMark の flanking rule は
     * 英文向けに作られており、`**` の直前直後が CJK の括弧（「」『』 等）の場合に
     * 太字として認識されないことがある（既知の英文中心の制約）。
     *
     * <p>例: {@code **「監督する」**} は CommonMark では太字にならない（`**` の直後が `「`、
     * 直前が `」` で、いずれも CommonMark 上は punctuation 扱いとなり flanking 規則を満たさない）。
     */
    private static final java.util.regex.Pattern RESIDUAL_BOLD =
            java.util.regex.Pattern.compile("\\*\\*([^*\\n]{1,200}?)\\*\\*");

    /**
     * Markdown 文字列を HTML へ変換する。
     *
     * @param markdown 変換元の Markdown。{@code null} または空白のみの場合は空文字を返す。
     * @return レンダリング結果の HTML 文字列
     */
    public String toHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        String html = renderer.render(parser.parse(markdown));
        return RESIDUAL_BOLD.matcher(html).replaceAll("<strong>$1</strong>");
    }
}
