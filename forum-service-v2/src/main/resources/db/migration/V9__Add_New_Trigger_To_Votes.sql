CREATE TRIGGER trigger_update_votes_count
AFTER INSERT OR UPDATE OR DELETE ON votes
FOR EACH ROW EXECUTE FUNCTION update_votes_count();