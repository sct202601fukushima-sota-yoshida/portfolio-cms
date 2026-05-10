package com.portfolio.cms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link MarkdownService} のユニットテスト。
 * 太字 / リンク / リスト / コード / 改行 / null 入力 / XSS（生 HTML）について検証する。
 */
class MarkdownServiceTest {

    private MarkdownService markdown;

    @BeforeEach
    void setUp() {
        markdown = new MarkdownService();
    }

    @Test
    @DisplayName("太字: **text** → <strong>text</strong>")
    void bold_isRenderedAsStrongTag() {
        String html = markdown.toHtml("**実務水準**");

        assertThat(html).contains("<strong>実務水準</strong>");
    }

    @Test
    @DisplayName("斜体: *text* → <em>text</em>")
    void italic_isRenderedAsEmTag() {
        String html = markdown.toHtml("*強調*");

        assertThat(html).contains("<em>強調</em>");
    }

    @Test
    @DisplayName("リンク: [label](url) → <a href=\"url\">label</a>")
    void link_isRenderedAsAnchorTag() {
        String html = markdown.toHtml("[GitHub](https://github.com/)");

        assertThat(html)
                .contains("<a href=\"https://github.com/\">GitHub</a>");
    }

    @Test
    @DisplayName("箇条書き: - item → <ul><li>item</li></ul>")
    void unorderedList_isRenderedAsUlLi() {
        String input = "- 一つ目\n- 二つ目\n- 三つ目";

        String html = markdown.toHtml(input);

        assertThat(html)
                .contains("<ul>")
                .contains("<li>一つ目</li>")
                .contains("<li>二つ目</li>")
                .contains("<li>三つ目</li>")
                .contains("</ul>");
    }

    @Test
    @DisplayName("インラインコード: `code` → <code>code</code>")
    void inlineCode_isRenderedAsCodeTag() {
        String html = markdown.toHtml("`SELECT *`");

        assertThat(html).contains("<code>SELECT *</code>");
    }

    @Test
    @DisplayName("単一改行は <br /> に変換される（softbreak の設定）")
    void softBreak_isRenderedAsBreakTag() {
        String input = "一行目\n二行目";

        String html = markdown.toHtml(input);

        assertThat(html).contains("<br />");
    }

    @Test
    @DisplayName("生 HTML はエスケープされ、<script> がそのまま出力されない（XSS 対策）")
    void rawHtml_isEscaped() {
        String malicious = "<script>alert('xss')</script>";

        String html = markdown.toHtml(malicious);

        assertThat(html)
                .doesNotContain("<script>")
                .contains("&lt;script&gt;");
    }

    @Test
    @DisplayName("マークダウン中の <img onerror=...> もエスケープされる")
    void rawImgTag_isEscaped() {
        String malicious = "Hello <img src=x onerror=\"alert(1)\">";

        String html = markdown.toHtml(malicious);

        assertThat(html)
                .doesNotContain("<img")
                .contains("&lt;img");
    }

    @Test
    @DisplayName("null 入力は空文字を返す（NPE を起こさない）")
    void nullInput_returnsEmptyString() {
        assertThat(markdown.toHtml(null)).isEmpty();
    }

    @Test
    @DisplayName("空白のみの入力は空文字を返す")
    void blankInput_returnsEmptyString() {
        assertThat(markdown.toHtml("   \n\t  ")).isEmpty();
    }
}
