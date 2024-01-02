CREATE TABLE IF NOT EXISTS content_versions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content_id BIGINT NOT NULL,
    version VARCHAR(255) NOT NULL UNIQUE,
    uploaded_at DATETIME NOT NULL,
    FOREIGN KEY (content_id) REFERENCES content(id)
);
