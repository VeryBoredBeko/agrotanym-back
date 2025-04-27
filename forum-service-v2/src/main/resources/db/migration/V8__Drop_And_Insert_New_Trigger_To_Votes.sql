DROP TRIGGER IF EXISTS trigger_update_votes_count ON votes;
DROP FUNCTION IF EXISTS update_votes_count();

CREATE OR REPLACE FUNCTION update_votes_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF NEW.post_type = 'question' THEN
            UPDATE questions
            SET votes_count = COALESCE(votes_count, 0) +
                CASE WHEN NEW.vote_type = 'upvote' THEN 1 ELSE -1 END
            WHERE id = NEW.post_id;
        ELSIF NEW.post_type = 'answer' THEN
            UPDATE answers
            SET votes_count = COALESCE(votes_count, 0) +
                CASE WHEN NEW.vote_type = 'upvote' THEN 1 ELSE -1 END
            WHERE id = NEW.post_id;
        END IF;

    ELSIF TG_OP = 'UPDATE' THEN
        IF NEW.post_type = 'question' THEN
            UPDATE questions
            SET votes_count = COALESCE(votes_count, 0)
                + CASE WHEN NEW.vote_type = 'upvote' THEN 1 ELSE -1 END
                - CASE WHEN OLD.vote_type = 'upvote' THEN 1 ELSE -1 END
            WHERE id = NEW.post_id;
        ELSIF NEW.post_type = 'answer' THEN
            UPDATE answers
            SET votes_count = COALESCE(votes_count, 0)
                + CASE WHEN NEW.vote_type = 'upvote' THEN 1 ELSE -1 END
                - CASE WHEN OLD.vote_type = 'upvote' THEN 1 ELSE -1 END
            WHERE id = NEW.post_id;
        END IF;

    ELSIF TG_OP = 'DELETE' THEN
        IF OLD.post_type = 'question' THEN
            UPDATE questions
            SET votes_count = COALESCE(votes_count, 0) -
                CASE WHEN OLD.vote_type = 'upvote' THEN 1 ELSE -1 END
            WHERE id = OLD.post_id;
        ELSIF OLD.post_type = 'answer' THEN
            UPDATE answers
            SET votes_count = COALESCE(votes_count, 0) -
                CASE WHEN OLD.vote_type = 'upvote' THEN 1 ELSE -1 END
            WHERE id = OLD.post_id;
        END IF;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
