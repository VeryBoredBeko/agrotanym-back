CREATE OR REPLACE FUNCTION update_answers_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE questions
        SET answers_count = COALESCE(answers_count, 0) + 1
        WHERE id = NEW.question_id;

        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        UPDATE questions
        SET answers_count = answers_count - 1
        WHERE id = OLD.question_id;

        RETURN OLD;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_answers_count
AFTER INSERT OR DELETE ON answers
FOR EACH ROW EXECUTE FUNCTION update_answers_count();
