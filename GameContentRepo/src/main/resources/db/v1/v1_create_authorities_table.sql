CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR(255),
    authority VARCHAR(255),
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username)
);