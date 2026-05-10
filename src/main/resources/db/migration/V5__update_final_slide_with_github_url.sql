-- V5: Add the actual GitHub URL to the "最後に" slide.
-- Until this point the slide said "（リンクは後日追加）" because the repo URL
-- was not yet known when V3 was authored.

UPDATE slides
SET description = E'35歳・未経験という立場ではございますが、\n**「動くだけではなく、壊れないように育てられるコードを書ける」ことを、本サイトをもってお示しできれば**と考えております。\nソースコードは [GitHub にて公開しております](https://github.com/sct202601fukushima-sota-yoshida/portfolio-cms)。\nご覧いただきありがとうございました。面接にてお話しできる機会を、心よりお待ちしております。\n\n**吉田 颯汰**'
WHERE title = '最後に'
  AND category_id = (SELECT id FROM categories WHERE name = '志望動機・今後');
