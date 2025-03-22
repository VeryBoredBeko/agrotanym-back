-- Удаляем все зависимости перед удалением таблиц и типов
DROP TABLE IF EXISTS user_badges CASCADE;
DROP TABLE IF EXISTS votes CASCADE;
DROP TABLE IF EXISTS question_tags CASCADE;
DROP TABLE IF EXISTS tags CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS badges CASCADE;
DROP TABLE IF EXISTS users CASCADE; -- если осталась таблица

-- Удаляем последовательности
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
DROP SEQUENCE IF EXISTS questions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS answers_id_seq CASCADE;
DROP SEQUENCE IF EXISTS comments_id_seq CASCADE;
DROP SEQUENCE IF EXISTS tags_id_seq CASCADE;
DROP SEQUENCE IF EXISTS votes_id_seq CASCADE;
DROP SEQUENCE IF EXISTS badges_id_seq CASCADE;

-- Удаляем пользовательские ENUM-типы
DROP TYPE IF EXISTS post_type_enum CASCADE;
DROP TYPE IF EXISTS vote_type_enum CASCADE;
DROP TYPE IF EXISTS badge_type_enum CASCADE;
