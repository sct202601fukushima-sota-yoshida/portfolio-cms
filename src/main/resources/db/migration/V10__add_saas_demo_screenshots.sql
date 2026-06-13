-- =============================================================
-- V10: V8 で追加した SaaS 作品スライドにスクリーンショットを添付する。
--
--   各プロダクトについて 2 枚:
--     0) LP ヒーロー（ポートフォリオバナー入り）
--     1) 実際に動いている画面（動作の証拠）
--
--   画像は uploads/portfolio-samples/ 配下に配置済み（Docker イメージに
--   COPY されるため Render の再デプロイでも消えない）。home.html は .jpg
--   から .webp の <source> を自動生成するため、ここでは .jpg を指定する。
-- =============================================================

INSERT INTO slide_images (slide_id, file_name, sort_order) VALUES
  ((SELECT id FROM slides WHERE title='VoxReply — AI レビュー返信 SaaS（AI 協働で開発・公開）'),
   'portfolio-samples/voxreply-lp.jpg', 0),
  ((SELECT id FROM slides WHERE title='VoxReply — AI レビュー返信 SaaS（AI 協働で開発・公開）'),
   'portfolio-samples/voxreply-generate.jpg', 1),

  ((SELECT id FROM slides WHERE title='Patchlog — AI changelog 生成 SaaS'),
   'portfolio-samples/patchlog-lp.jpg', 0),
  ((SELECT id FROM slides WHERE title='Patchlog — AI changelog 生成 SaaS'),
   'portfolio-samples/patchlog-dashboard.jpg', 1);
