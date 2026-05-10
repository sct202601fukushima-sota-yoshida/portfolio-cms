-- =============================================================
-- V7: スライドの流れを PASONA の法則（新版）に沿って再構成
--
--   P  Problem        — AI 時代のエンジニア像（NEW）
--   A  Affinity       — 自己紹介
--   S  Solution (核)  — 強み
--   S  Solution (方法) — 学習・スキル
--   O  Offer (本体)   — ポートフォリオ
--   O  Offer (裏付)   — 経歴
--   O  Offer (幅)     — デザイン制作物
--   N+A Narrow + Action — 志望動機・今後 / 最後に
-- =============================================================

-- ---- カテゴリの並び替え ----
-- 新カテゴリ「AI 時代のエンジニア像」を sort_order 0 に追加し、
-- 既存カテゴリを PASONA 順に再配置する
INSERT INTO categories (name, sort_order) VALUES
  ('AI 時代のエンジニア像', 0);

UPDATE categories SET sort_order = 1 WHERE name = '自己紹介';
UPDATE categories SET sort_order = 2 WHERE name = '強み';
UPDATE categories SET sort_order = 3 WHERE name = '学習・スキル';
UPDATE categories SET sort_order = 4 WHERE name = 'ポートフォリオ';
UPDATE categories SET sort_order = 5 WHERE name = '経歴';
UPDATE categories SET sort_order = 6 WHERE name = 'デザイン制作物';
UPDATE categories SET sort_order = 7 WHERE name = '志望動機・今後';

-- ---- 冒頭の Problem 提示スライドを追加 ----
INSERT INTO slides (category_id, title, description, sort_order, is_active) VALUES
  ((SELECT id FROM categories WHERE name='AI 時代のエンジニア像'),
   '「書く」から「設計・監督」へ',
   E'生成AIの普及により、エンジニアの仕事の重心は **「動くコードを書ける」から「動くシステムを設計・監督できる」** へと移りつつあります。\nこれからのエンジニアに必要なのは、次の組み合わせだと考えています:\n\n- **全体の企画・計画力** — やりたいことを言語化し、要素に分解し、実行順を決められる\n- **AI ツールを使いこなす実装感覚** — 道具を使い分け、生成物を評価できる\n- **非エンジニアとの対話力** — 要件・制約を翻訳して合意できる\n- **複数領域を横断する視野** — デザイン、フロント、バック、運用を地続きで捉えられる\n\n本サイトは、その人材像に向けて自分を準備した経過を、**実物（このサイト自体が CMS）として**提示するものです。次以降のスライドで、その軸となる強みと成果物をご覧ください。',
   0, true);
