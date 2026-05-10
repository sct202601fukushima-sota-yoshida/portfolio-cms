-- Add a new category "デザイン制作物" between "ポートフォリオ" (5) and "志望動機・今後".
-- Push 志望動機・今後 to sort_order 6 and insert デザイン制作物 at sort_order 5.

UPDATE categories SET sort_order = 6 WHERE name = '志望動機・今後';

INSERT INTO categories (name, sort_order) VALUES ('デザイン制作物', 5);

INSERT INTO slides (category_id, title, description, sort_order, is_active) VALUES
  ((SELECT id FROM categories WHERE name='デザイン制作物'),
   'ひいらぎ不動産（架空案件）— 不動産会社サイト',
   E'架空クライアント「ひいらぎ不動産」のコーポレートサイト。\n**デザイン → HTML / CSS 実装まで一人で完遂**した職業訓練の制作物です。\nヒーロー画像のフルブリード構成、物件カードの3カラムグリッド、フォーム導線などを設計しました。\n本サイト内（[/works/hiiragi/](/works/hiiragi/index.html)）に**実物のサイトをそのままホスティング**しております。スクリーンショットだけでなく、実装そのものをご覧いただけます。',
   0, true),

  ((SELECT id FROM categories WHERE name='デザイン制作物'),
   'Grand Inquisitor — 書籍紹介LP',
   E'ドストエフスキー『カラマーゾフの兄弟』の作中編「大審問官」をモチーフにした書籍紹介LP。\n色味・タイポグラフィを抑えた**重厚な世界観の構築**を意図し、ファーストビューで主題が伝わる構成を組みました。\n同じく [/works/grand-inquisitor/](/works/grand-inquisitor/index.html) に実物を掲載しております。',
   1, true),

  ((SELECT id FROM categories WHERE name='デザイン制作物'),
   'Farbe チラシ — 印刷物デザイン',
   E'美容サロン「Farbe」（架空）の集客チラシ。Photoshop で版下まで作成しております。\n**読み手の視線誘導 → CTA への着地**を最優先に、写真選定・余白・行間を調整しました。\nWeb と印刷では制約が異なる（CMYK、塗り足し、可読フォントサイズ等）ことを学んだ案件です。',
   2, true),

  ((SELECT id FROM categories WHERE name='デザイン制作物'),
   'Works Corporation — ロゴ & 名刺',
   E'架空企業「Works Corporation」のブランディング一式。Illustrator でロゴから名刺まで作成。\n**ロゴの最小単位（シンボル）から一貫したトーン**を派生させる設計を意識しました。\n色違いバリエーションを用意し、媒体ごとの最適解を提示できる構成にしております。',
   3, true),

  ((SELECT id FROM categories WHERE name='デザイン制作物'),
   '広告バナー集 — Web クリエイティブ',
   E'PC・モバイル各サイズの広告バナー制作物です。\n**限られた領域でメッセージを伝える要約力**を鍛える題材として、コピー・配色・優先順位の付け方を反復練習しました。\nバックエンドの仕事においても、API レスポンスや管理画面の情報設計に**「何を残し何を削るか」**の判断軸として活きていると感じております。',
   4, true);

INSERT INTO slide_images (slide_id, file_name, sort_order) VALUES
  ((SELECT id FROM slides WHERE title='ひいらぎ不動産（架空案件）— 不動産会社サイト'),
   'portfolio-samples/hiiragi-screenshot.jpg', 0),

  ((SELECT id FROM slides WHERE title='Grand Inquisitor — 書籍紹介LP'),
   'portfolio-samples/grand-inquisitor-screenshot.jpg', 0),

  ((SELECT id FROM slides WHERE title='Farbe チラシ — 印刷物デザイン'),
   'portfolio-samples/farbe-1.jpg', 0),
  ((SELECT id FROM slides WHERE title='Farbe チラシ — 印刷物デザイン'),
   'portfolio-samples/farbe-2.jpg', 1),

  ((SELECT id FROM slides WHERE title='Works Corporation — ロゴ & 名刺'),
   'portfolio-samples/works-logo.jpg', 0),
  ((SELECT id FROM slides WHERE title='Works Corporation — ロゴ & 名刺'),
   'portfolio-samples/works-meishi-1.jpg', 1),
  ((SELECT id FROM slides WHERE title='Works Corporation — ロゴ & 名刺'),
   'portfolio-samples/works-meishi-2.jpg', 2),

  ((SELECT id FROM slides WHERE title='広告バナー集 — Web クリエイティブ'),
   'portfolio-samples/banner-overview.jpg', 0),
  ((SELECT id FROM slides WHERE title='広告バナー集 — Web クリエイティブ'),
   'portfolio-samples/banner-detail-1.jpg', 1),
  ((SELECT id FROM slides WHERE title='広告バナー集 — Web クリエイティブ'),
   'portfolio-samples/banner-detail-2.jpg', 2);
