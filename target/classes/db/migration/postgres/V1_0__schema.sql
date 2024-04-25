CREATE TABLE developer
(
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email      VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL
);

CREATE TABLE user_story
(
    id                  UUID DEFAULT gen_random_uuid() PRIMARY KEY,
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
