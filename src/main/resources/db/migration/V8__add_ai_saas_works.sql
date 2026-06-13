-- =============================================================
-- V8: 「ポートフォリオ」カテゴリに、AI と協働して開発・公開した
--     実 SaaS（マイクロ SaaS）2 本を作品として追加する。
--
--   - 自作 CMS（本サイト）= バックエンド設計力の実証
--   - 実 SaaS 2 本        = 「AI と協働すれば、慣れないスタックでも
--                            動いて公開できるプロダクトを作れる」実証
--
--   既存「ポートフォリオ」スライドは sort_order 0..2。
--   本マイグレーションで 3（VoxReply）・4（Patchlog）を追加する。
--   就職活動向けに、両 SaaS は課金を停止し全機能を無料開放した
--   「作品版」として公開している。
-- =============================================================

INSERT INTO slides (category_id, title, description, sort_order, is_active) VALUES
  ((SELECT id FROM categories WHERE name='ポートフォリオ'),
   'VoxReply — AI レビュー返信 SaaS（AI 協働で開発・公開）',
   E'本サイト（自作 CMS）に加えて、**AI コーディングエージェント（Claude Code）と協働し、英語圏 B2B 向けの実 SaaS をゼロから設計・実装・公開**しました。\n慣れていない言語・スタックであっても、AI と役割分担すれば**「動いて、公開できるプロダクト」**まで到達できる——その実例です。\n\n**VoxReply** は、Google や Yelp などのカスタマーレビューに対し、店舗のブランドボイスに合わせた返信文を AI が生成するツールです。\n\n- **スタック**：Next.js 16 / React 19 / TypeScript / Cloudflare Workers / Supabase（PostgreSQL + 認証）/ Stripe / Claude API\n- **実装範囲**：認証・サブスクリプション課金・AI 生成・利用履歴・フィードバックまで一通り\n- 私の役割は **「何を・なぜ・どの順で作るか」の企画と設計判断・検証**。Claude Code が実装を担い、人間（私）が監督する分担です\n\n▶ **実際に触れるデモ**： https://voxreply.gladdot90s.workers.dev\n（就職活動用に**課金を停止し全機能を無料開放**した作品版です。ページ上部の「Try the live demo」から、サインアップ不要で試せます）',
   3, true),

  ((SELECT id FROM categories WHERE name='ポートフォリオ'),
   'Patchlog — AI changelog 生成 SaaS',
   E'**Patchlog** は、GitHub のリリースから、利用者向けの分かりやすいリリースノート（changelog）を AI が自動生成し、メール通知や埋め込みウィジェットで配信する B2B ツールです。VoxReply と同じく Claude Code との協働で構築しました。\n\n- **スタック**：Next.js 16 / TypeScript / Cloudflare Workers / Supabase / Stripe / Claude API / GitHub App 連携 / Resend（メール配信）\n- **実装範囲**：GitHub Webhook 連携・AI 生成・公開 changelog ページ・埋め込みウィジェット（widget.js）・メール購読・課金\n- **デザイン → フロントエンド → バックエンド → 外部サービス連携 → デプロイ運用**までを、AI と協働して地続きに扱えることを示す題材です\n\n▶ **実際に触れるデモ**： https://patchlog.gladdot90s.workers.dev\n（こちらも**課金停止・全機能無料**の作品版です。「Try the live demo」からサインアップ不要で、サンプルのリリースノートを AI が changelog に変換するところまで試せます）',
   4, true);
