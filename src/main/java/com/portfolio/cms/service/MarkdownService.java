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
     * Markdown 文字列を HTML へ変換する。
     *
     * @param markdown 変換元の Markdown。{@code null} または空白のみの場合は空文字を返す。
     * @return レンダリング結果の HTML 文字列
     */
    public String toHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        return renderer.render(parser.parse(markdown));
    }
}
