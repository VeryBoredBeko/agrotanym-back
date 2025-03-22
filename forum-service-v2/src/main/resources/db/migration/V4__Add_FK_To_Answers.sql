ALTER TABLE answers
ADD COLUMN question_id BIGINT NOT NULL;

ALTER TABLE answers
ADD CONSTRAINT fk_answers_question
FOREIGN KEY (question_id)
REFERENCES questions(id)
ON DELETE CASCADE;