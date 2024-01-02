CREATE TABLE IF NOT EXISTS content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    content_type BIGINT NOT NULL,
    platform BIGINT NOT NULL,
    game BIGINT NOT NULL,
    uploader_id BIGINT NOT NULL,
    uploaded_at DATETIME NOT NULL,
    is_active BOOLEAN DEFAULT true,
    FOREIGN KEY (content_type) REFERENCES content_types(id),
    FOREIGN KEY (platform) REFERENCES platforms(id),
    FOREIGN KEY (game) REFERENCES games(id)
);