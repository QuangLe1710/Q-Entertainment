CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    refresh_token_hash VARCHAR(255) NOT NULL,
    device_info VARCHAR(500),
    ip_address VARCHAR(45),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

-- Index để tìm kiếm nhanh chóng khi gọi API /refresh
CREATE UNIQUE INDEX idx_refresh_tokens_hash ON refresh_tokens (refresh_token_hash);

-- Index để tìm kiếm và thu hồi tất cả token của 1 user (khi đổi mật khẩu / logout all)
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);