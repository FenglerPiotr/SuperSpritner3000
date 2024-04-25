INSERT INTO developer(id, first_name, last_name, email)
VALUES ('d9c21f6e-c541-4d34-9293-961976204294', 'Tomasz', 'Nowak', 'tn@test.com'),
       ('d063ff50-00a1-4f96-94f9-a4fb31d33e76', 'Jan', 'Nowak', 'jn@test.com'),
       ('04a4c4b8-c361-4681-bd4b-13c332f0ba38', '***', '*****', 'deleted-b8081184-c21c-40ce-b806-32cdf73a82db@test.com');

INSERT INTO user_story(id,  title, description, acceptance_criteria, estimation, business_value, status, version)
VALUES ('68466722-a5a7-49da-af86-b2dcbd6b8203', 'Test story', 'Implement tests', 'Cover all cases', 30.0, 100, 'TODO', 0),
       ('b8081184-c21c-40ce-b806-32cdf73a82db', 'Super Test story', 'Implement more tests', 'Cover all cases', 35.0, 150, 'TODO', 0);

INSERT INTO developer_user_story(developer_id, user_story_id)
VALUES ('d9c21f6e-c541-4d34-9293-961976204294', '68466722-a5a7-49da-af86-b2dcbd6b8203');
