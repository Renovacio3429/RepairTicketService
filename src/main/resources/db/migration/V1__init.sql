CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(20)         NOT NULL
);

COMMENT ON TABLE users IS 'Пользователи';
COMMENT ON COLUMN users.id IS 'Id';
COMMENT ON COLUMN users.username IS 'Логин';
COMMENT ON COLUMN users.password IS 'Пароль';
COMMENT ON COLUMN users.role IS 'Роль';

INSERT INTO users (username, password, role)
VALUES
    ('admin', '$2a$12$GU1AEg3BfF49H331nDq5FuxbAo.piu5DrlyBneCaNbBLzteUGbxsu', 'ADMIN'),
    ('manager', '$2a$12$4jORwEcK6ArpZiOv3JaArufOnh.1VMZwOdgre9.u3iEQKyS7SQhZm', 'MANAGER'),
    ('technician', '$2a$12$KJtDlOZlbx2Xg1dhsh0EzuhzLrTsvqil42k/20LoeV1tulIcQxv7S', 'TECHNICIAN');

CREATE TABLE repair_request
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description TEXT NOT NULL,
    status      VARCHAR(20)                  NOT NULL,
    created_at  TIMESTAMP                    NOT NULL,
    updated_at  TIMESTAMP,
    created_by  BIGINT REFERENCES users (id) NOT NULL,
    assigned_to BIGINT REFERENCES users (id)
);

COMMENT ON TABLE repair_request IS 'Заявки на ремонт';
COMMENT ON COLUMN repair_request.id IS 'Id';
COMMENT ON COLUMN repair_request.title IS 'Название заявки';
COMMENT ON COLUMN repair_request.description IS 'Описание заявки';
COMMENT ON COLUMN repair_request.status IS 'Статус';
COMMENT ON COLUMN repair_request.created_at IS 'Дата создания';
COMMENT ON COLUMN repair_request.updated_at IS 'Дата обновления';
COMMENT ON COLUMN repair_request.created_by IS 'Инциатор заявки';
COMMENT ON COLUMN repair_request.assigned_to IS 'Исполнитель заявки';

CREATE TABLE comment
(
    id             SERIAL PRIMARY KEY,
    text           TEXT                                  NOT NULL,
    created_at     TIMESTAMP                             NOT NULL,
    author         BIGINT REFERENCES users (id)          NOT NULL,
    repair_request BIGINT REFERENCES repair_request (id) NOT NULL
);

COMMENT ON TABLE comment IS 'Комментарии';
COMMENT ON COLUMN comment.id IS 'Id';
COMMENT ON COLUMN comment.text IS 'Комментарий';
COMMENT ON COLUMN comment.created_at IS 'Дата создания';
COMMENT ON COLUMN comment.author IS 'Автор';
COMMENT ON COLUMN comment.repair_request IS 'Id заявки на ремонт';

CREATE TABLE outbox_event (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    processed BOOLEAN NOT NULL
);

COMMENT ON TABLE outbox_event IS 'Outbox-таблица событий';
COMMENT ON COLUMN outbox_event.id IS 'Id события';
COMMENT ON COLUMN outbox_event.aggregate_type IS 'Тип агрегата';
COMMENT ON COLUMN outbox_event.aggregate_id IS 'Id агрегата';
COMMENT ON COLUMN outbox_event.type IS 'Тип события';
COMMENT ON COLUMN outbox_event.payload IS 'JSON-представление события';
COMMENT ON COLUMN outbox_event.created_at IS 'Дата создания события';
COMMENT ON COLUMN outbox_event.processed IS 'Флаг обработки';

