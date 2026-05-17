INSERT INTO students (student_name,
                      furigana,
                      nickname,
                      phone_number,
                      mailaddress,
                      region,
                      age,
                      gender,
                      remark,
                      is_deleted)
VALUES ('山田太郎', 'ヤマダタロウ', 'タロー', '09012345678', 'yamada@example.com', '東京都', 20,
        '男性', 'Java学習中', FALSE),

       ('佐藤花子', 'サトウハナコ', 'ハナ', '08011112222', 'sato@example.com', '大阪府', 22, '女性',
        'AWS勉強中', FALSE),

       ('鈴木一郎', 'スズキイチロウ', 'イッチ', '07033334444', 'suzuki@example.com', '北海道', 25,
        '男性', '転職活動中', FALSE),

       ('高橋美咲', 'タカハシミサキ', 'ミサ', '09055556666', 'takahashi@example.com', '福岡県', 19,
        '女性', 'Spring Boot学習中', FALSE),

       ('田中健太', 'タナカケンタ', 'ケン', '08077778888', 'tanaka@example.com', '愛知県', 28,
        '男性', 'MyBatis勉強中', TRUE);

INSERT INTO students_courses (course_id,
                              student_pk,
                              course_name,
                              start_date,
                              end_date)
VALUES ('1', 1, 'Javaコース', '2026-01-01', '2026-07-01'),

       ('2', 2, 'AWSコース', '2026-02-01', '2026-08-01'),

       ('3', 3, 'Spring Bootコース', '2026-03-01', '2026-09-01'),

       ('4', 4, 'Web開発コース', '2026-04-01', '2026-10-01'),

       ('5', 5, 'MyBatisコース', '2026-05-01', '2026-11-01');