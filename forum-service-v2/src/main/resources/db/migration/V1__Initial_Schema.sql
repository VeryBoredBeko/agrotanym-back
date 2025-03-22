CREATE TYPE post_type_enum AS ENUM ('question', 'answer');
CREATE TYPE vote_type_enum AS ENUM ('upvote', 'downvote');
CREATE TYPE badge_type_enum AS ENUM ('bronze', 'silver', 'gold');

CREATE SEQUENCE users_id_seq START 1 INCREMENT 1;

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    keycloak_user_id UUID UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    bio TEXT,
    profile_image VARCHAR(255),
    reputation INTEGER DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE questions_id_seq START 1 INCREMENT 1;

CREATE TABLE questions (
    id BIGINT PRIMARY KEY DEFAULT nextval('questions_id_seq'),
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    views INTEGER DEFAULT 0,
    votes_count INTEGER DEFAULT 0,
    answers_count INTEGER DEFAULT 0,
    is_closed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

CREATE SEQUENCE answers_id_seq START 1 INCREMENT 1;

CREATE TABLE answers (
    id BIGINT PRIMARY KEY DEFAULT nextval('answers_id_seq'),
    question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    body TEXT NOT NULL,
    is_accepted BOOLEAN DEFAULT FALSE,
    votes_count INTEGER DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

CREATE SEQUENCE comments_id_seq START 1 INCREMENT 1;

CREATE TABLE comments (
    id BIGINT PRIMARY KEY DEFAULT nextval('comments_id_seq'),
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    post_type post_type_enum NOT NULL,
    post_id BIGINT NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE tags_id_seq START 1 INCREMENT 1;

CREATE TABLE tags (
    id BIGINT PRIMARY KEY DEFAULT nextval('tags_id_seq'),
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE question_tags (
    question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (question_id, tag_id)
);

CREATE SEQUENCE votes_id_seq START 1 INCREMENT 1;

CREATE TABLE votes (
    id BIGINT PRIMARY KEY DEFAULT nextval('votes_id_seq'),
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    post_type post_type_enum NOT NULL,
    post_id BIGINT NOT NULL,
    vote_type vote_type_enum NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE badges_id_seq START 1 INCREMENT 1;

CREATE TABLE badges (
    id BIGINT PRIMARY KEY DEFAULT nextval('badges_id_seq'),
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    badge_type badge_type_enum NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_badges (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    badge_id BIGINT NOT NULL REFERENCES badges(id) ON DELETE CASCADE,
    awarded_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, badge_id)
);