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
