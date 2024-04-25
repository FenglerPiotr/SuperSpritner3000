CREATE TABLE developer
(
    id         UUID DEFAULT random_uuid() PRIMARY KEY,
    email      VARCHAR(200) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL
);

CREATE TABLE user_story
(
    id                  UUID DEFAULT random_uuid() PRIMARY KEY,
    title               VARCHAR(100)     NOT NULL,
    description         VARCHAR(100)     NOT NULL,
    acceptance_criteria VARCHAR(100)     NOT NULL,
    estimation          DOUBLE PRECISION NOT NULL,
    business_value      INT              NOT NULL,
    status              VARCHAR(50)      NOT NULL,
    version             INT              NOT NULL
);

CREATE TABLE developer_user_story
(
    developer_id UUID,
    user_story_id UUID,
    FOREIGN KEY (developer_id) REFERENCES developer(id),
    FOREIGN KEY (user_story_id) REFERENCES user_story(id),
    UNIQUE (developer_id, user_story_id)
);

ALTER TABLE user_story
    ADD CONSTRAINT estimation_constraint
        CHECK (
                    estimation >= 0.5
                AND estimation <= 40.0
            );

ALTER TABLE user_story
    ADD CONSTRAINT business_value_constraint
        CHECK (
                    business_value >= 100
                AND business_value <= 1500
            );

ALTER TABLE user_story
    ADD CONSTRAINT title_constraint
        CHECK (
                    length(title) >= 5
                AND length(title) <= 50
            );

CREATE TABLE users
(
    id       UUID DEFAULT random_uuid() PRIMARY KEY,
    email    VARCHAR(200)  NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL
);

CREATE TABLE role
(
    id   UUID DEFAULT random_uuid() PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_role
(
    user_id UUID,
    role_id UUID,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES role (id),
    UNIQUE (user_id, role_id)
);
