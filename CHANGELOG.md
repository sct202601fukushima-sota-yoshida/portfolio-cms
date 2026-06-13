# Changelog

このファイルは Portfolio CMS の主な変更履歴を [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) 形式でまとめたものです。Semantic Versioning は採用していないため、日付ベースで区切っています。

---

## [Unreleased]

検討中:

- E2E テスト (Playwright)
- 画像ストレージを外部サービスへ移行（現在は Docker イメージ同梱）
- 管理画面の Markdown リアルタイムプレビュー

---

## [2026-06-13] — 実 SaaS 作品の掲載とコンテンツ改訂

「自作 CMS（バックエンド設計の実証）」に加えて、**AI と協働して開発・本番公開した実 SaaS 2 本**を作品として統合し、就職活動向けにコンテンツを整えた日。

### Added
- **実 SaaS 2 本をポートフォリオに追加（V8）** — VoxReply（AI レビュー返信）/ Patchlog（AI changelog 生成）を、Claude Code と協働で開発・公開した作品として掲載。「慣れないスタックでも動いて公開できる」実証 ([ebf9c96](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/ebf9c96))
- **SaaS 作品スクリーンショット（V10）** — 最適化済み jpg + webp を各 SaaS スライドに添付 ([636c8c1](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/636c8c1))
- **事業ストーリー + AI 運用設計の 2 スライド（V11）** — 「軽い興味から事業設計まで一気に」「AI で運用まで回す設計」をポートフォリオ末尾に追加 ([ea806a6](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/ea806a6))

### Changed
- **LP 内 Markdown リンクを別タブで開く** — `target=_blank` を付与し、外部デモ等への遷移で LP を離脱させない ([f529984](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/f529984))
- **SaaS デモ URL をクリック可能化（V9）** — 生 URL を Markdown リンクに修正 ([d5a392b](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/d5a392b))
- **就活配慮の表現調整（V12）** — LP から「祖母の介護」「フルリモート必須」の明記を外し、リモートは「一人完結できた強み」として提示。訓練校の指摘に対応 ([e6f041c](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/e6f041c))

### Fixed
- **Farbe チラシの事実訂正（V13）** — 「Photoshop で版下作成」を実態に合わせ「デザイン科最初期に Word で作った最初の制作物」に訂正 ([fff83c1](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/fff83c1))

---

## [2026-05-11] — UX polish & operational maturity

採用担当・エンジニア審査者の双方に対する完成度を引き上げた日。

### Added
- **目次サイドバー (sticky TOC)** — デスクトップ右側に 8 カテゴリのジャンプリンク、IntersectionObserver で現在地ハイライト ([2d5b12d](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/2d5b12d))
- **画像 lightbox** — ネイティブ `<dialog>` 要素でフォーカストラップ・ESC 閉じる・`::backdrop` 自動 ([d0a17bb](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/d0a17bb))
- **読了進捗バー + 戻るトップボタン** — `animation-timeline: scroll(root)` で pure CSS ([b28060b](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/b28060b))
- **ADR (Architecture Decision Records) 5 件** — `docs/adr/` に主要設計判断を文書化 ([882ca51](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/882ca51))
- **JaCoCo テストカバレッジ** — pom.xml に追加、CI artifact、README にバッジ。`CustomUserDetailsServiceTest` 追加でテスト 29 → 31 件 ([31eb549](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/31eb549))
- **Spring Boot Actuator** — `/actuator/health` `/actuator/info` を公開、Render の healthCheck パスを変更 ([9b06b0a](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/9b06b0a))
- **JSON-LD 構造化データ** — Person + WebSite schema を `<head>` に、SNS シェア時のリッチプレビュー対応 ([5a8fbfe](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/5a8fbfe))
- **`:focus-visible` 統一ゴールドリング** — 全インタラクティブ要素のキーボードフォーカス見た目を一致 ([cd67633](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/cd67633))
- **ヒーロー入場アニメ + スクロール連動カード** — 黄金螺旋の手描き描画、テキスト段差登場、`animation-timeline: view()` ([8aef922](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/8aef922))

### Fixed
- **黄金分割 SVG の縦線位置** — 底部矩形の再帰分割を x=160.2 → x=145.6 に修正、螺旋が正しく収束 ([7aaade6](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/7aaade6))
- **入場アニメから opacity 撤去** — Lighthouse がアニメ中の半透明合成を a11y 失点と判定する問題を解消 ([0d3981f](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/0d3981f))

### Changed
- **README 技術スタック表を詳細化** — 4 セクション × 30 行超、バージョン番号と選定理由を併記 ([8f5f7a0](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/8f5f7a0))

---

## [2026-05-10] — Deploy + CSS architecture + narrative pivot

ローカル開発から本番公開、デザイン全体の品質向上、PASONA 構成への切替まで一気に進めた日。

### Added
- **GitHub リポジトリ** + git push の運用ループ ([9b8d7c1](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/9b8d7c1))
- **Render Web Service デプロイ** — Dockerfile + render.yaml + PORT 環境変数対応 ([8ee073b](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/8ee073b))
- **CI バッジ + Keep-warm workflow** — GitHub Actions で 14 分毎の自動 ping（後に 10 分固定スロットへ） ([1f2cff9](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/1f2cff9))
- **CSS Cascade Layers (`@layer`)** — reset / tokens / base / components / overrides の 5 層構造 ([dac5da0](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/dac5da0))
- **ダークモード対応** — `prefers-color-scheme: dark` で CSS 変数を反転、ヒーローは「持ち上げ」階層感 ([a10b7e6](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/a10b7e6))
- **プリント用スタイルシート** — `@media print` で A4 1 ページ 1 スライドの構成 ([a10b7e6](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/a10b7e6))
- **PASONA フロー** — 「AI 時代のエンジニア像」カテゴリを新設、8 カテゴリ × 22 スライド構成へ ([fd7c8b7](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/fd7c8b7))

### Changed
- **ポートフォリオのナラティブを再フレーミング** — Java 学習成果物 → 「企画・計画力 × バイブコーディング × 複合的技術者志望」 ([b55ed9f](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/b55ed9f))
- **WebP 配信 + lazy loading** — Lighthouse Performance Mobile 73 → 100、画像 70% 圧縮 ([c2b1095](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/c2b1095))
- **Lighthouse 100/100/100/100 達成** ([cc5358c](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/cc5358c))
- **検索エンジンインデックス拒否** — `noindex/nofollow` + `/robots.txt`（求職用、設計上の判断） ([34ed435](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/34ed435))

### Fixed
- **WCAG AA 色コントラスト** — 紙背景上のゴールドテキスト用に `--color-gold-text: #826a3a` を新設 ([ecf687d](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/ecf687d))
- **CommonMark の CJK 句読点問題** — `**「foo」**` 形式の bold が認識されない既知制約を、出力 HTML の後処理で解決 ([44c661d](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/44c661d))
- **Keep-warm cron の信頼性** — `*/14` の不均一スロットから `0,10,20,30,40,50` の 10 分固定スロットへ ([bf0e2c5](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/bf0e2c5))
- **Grand Inquisitor フッタの不要文言** — 「by -Affilithink.net-」を削除 ([c2952ca](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms/commit/c2952ca))

---

## Initial release — 2026-05-10

初期コミット時点で既に持っていた機能:

- Spring Boot 3.5 + Java 17 + PostgreSQL 15 のフルスタック CMS
- 4 テーブル正規化 (User / Category / Slide / SlideImage)
- Spring Security + BCrypt + CSRF 対策の管理画面
- Flyway V1〜V5 マイグレーション（スキーマ + シード + 設計制作物カテゴリ）
- JUnit 5 + Mockito + MockMvc によるテスト (18 件)
- 黄金比ベースの独自 CSS デザインシステム
- 2026 年デザイントレンド（Blueprint / Resonant Stark / Frosted Touch / Light Skeuomorphism）反映
- Markdown SSR (commonmark-java、XSS 対策込み)
- SEO / OGP / Favicon
- 過去のデザイン制作物 (5 案件) を `デザイン制作物` カテゴリとして統合
- ライブホスティング: `/works/hiiragi/`, `/works/grand-inquisitor/`
