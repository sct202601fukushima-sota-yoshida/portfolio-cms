-- =============================================================
-- V9: V8 で追加した SaaS 作品スライドのデモ URL を、
--     クリック可能な Markdown リンク（[text](url)）に修正する。
--
--   MarkdownService は素の CommonMark（autolink 拡張なし）のため、
--   ベタ書きの URL はプレーンテキストになりクリックできない。
--   既存の作品スライドと同様、明示的な [表示テキスト](URL) 形式へ。
-- =============================================================

UPDATE slides SET description =
  E'本サイト（自作 CMS）に加えて、**AI コーディングエージェント（Claude Code）と協働し、英語圏 B2B 向けの実 SaaS をゼロから設計・実装・公開**しました。\n慣れていない言語・スタックであっても、AI と役割分担すれば**「動いて、公開できるプロダクト」**まで到達できる——その実例です。\n\n**VoxReply** は、Google や Yelp などのカスタマーレビューに対し、店舗のブランドボイスに合わせた返信文を AI が生成するツールです。\n\n- **スタック**：Next.js 16 / React 19 / TypeScript / Cloudflare Workers / Supabase（PostgreSQL + 認証）/ Stripe / Claude API\n- **実装範囲**：認証・サブスクリプション課金・AI 生成・利用履歴・フィードバックまで一通り\n- 私の役割は **「何を・なぜ・どの順で作るか」の企画と設計判断・検証**。Claude Code が実装を担い、人間（私）が監督する分担です\n\n▶ **[実際に触れるデモを開く →](https://voxreply.gladdot90s.workers.dev)**\n（就職活動用に**課金を停止し全機能を無料開放**した作品版です。ページ上部の「Try the live demo」から、サインアップ不要で試せます）'
WHERE title = 'VoxReply — AI レビュー返信 SaaS（AI 協働で開発・公開）';

UPDATE slides SET description =
  E'**Patchlog** は、GitHub のリリースから、利用者向けの分かりやすいリリースノート（changelog）を AI が自動生成し、メール通知や埋め込みウィジェットで配信する B2B ツールです。VoxReply と同じく Claude Code との協働で構築しました。\n\n- **スタック**：Next.js 16 / TypeScript / Cloudflare Workers / Supabase / Stripe / Claude API / GitHub App 連携 / Resend（メール配信）\n- **実装範囲**：GitHub Webhook 連携・AI 生成・公開 changelog ページ・埋め込みウィジェット（widget.js）・メール購読・課金\n- **デザイン → フロントエンド → バックエンド → 外部サービス連携 → デプロイ運用**までを、AI と協働して地続きに扱えることを示す題材です\n\n▶ **[実際に触れるデモを開く →](https://patchlog.gladdot90s.workers.dev)**\n（こちらも**課金停止・全機能無料**の作品版です。「Try the live demo」からサインアップ不要で、サンプルのリリースノートを AI が changelog に変換するところまで試せます）'
WHERE title = 'Patchlog — AI changelog 生成 SaaS';
