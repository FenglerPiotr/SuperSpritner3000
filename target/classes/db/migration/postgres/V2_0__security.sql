CREATE TABLE users
(
    id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email    VARCHAR(200)  NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL
);

CREATE TABLE role
(
    id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
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

INSERT INTO users(id, email, password)
VALUES ('160a6c2b-b07e-4422-b0e9-c0d6ac0b6fa6', 'user@mail.com', '$2a$10$0t3pb1qlwKc4uyN2cCNLmej63F5XFvZMGW12i7IjWixy29shJ6s6O'), -- usersecret
       ('9400a854-1fea-4262-b2fb-a4e0b30d2faf', 'admin@mail.com', '$2a$10$6nIzvs8lTPZSScCzPjWzYu61N9Sz1BnOL9AOAt37AdtAtrYd1aOte'); -- adminsecret

INSERT INTO role(id, name)
VALUES ('7014b753-b963-4f50-81d8-def00f550386', 'DEVELOPER_READ'),
       ('4c6a158f-b360-49c4-b6f2-c26b3d1ed7df', 'DEVELOPER_WRITE');

INSERT INTO user_role(user_id, role_id)
VALUES ('160a6c2b-b07e-4422-b0e9-c0d6ac0b6fa6', '7014b753-b963-4f50-81d8-def00f550386'),
       ('9400a854-1fea-4262-b2fb-a4e0b30d2faf', '7014b753-b963-4f50-81d8-def00f550386'),
       ('9400a854-1fea-4262-b2fb-a4e0b30d2faf', '4c6a158f-b360-49c4-b6f2-c26b3d1ed7df');
