CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(255) NOT NULL UNIQUE,
                       firstname VARCHAR(255),
                       lastname VARCHAR(255),
                       password VARCHAR(255),
                       country VARCHAR(255),
                       city VARCHAR(255),
                       address VARCHAR(255),
                       postal_code VARCHAR(20),
                       avatar_color VARCHAR(20),
                       receive_news_letters BOOLEAN DEFAULT FALSE,
                       is_locked BOOLEAN DEFAULT FALSE,
                       is_enabled BOOLEAN DEFAULT TRUE,
                       role VARCHAR(20) NOT NULL
);

CREATE TABLE accounts (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name VARCHAR(255) NOT NULL,
                          currency VARCHAR(20) NOT NULL,
                          current_balance DOUBLE PRECISION NOT NULL,
                          type VARCHAR(255) NOT NULL,
                          avatar_color VARCHAR(20),
                          user_id UUID NOT NULL,
                          CONSTRAINT fk_accounts_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE user_accounts (
                               user_id UUID NOT NULL,
                               account_id UUID NOT NULL,
                               PRIMARY KEY (user_id, account_id),
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE account_addition_requests (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           owner_user_id UUID NOT NULL,
                                           requested_user_id UUID NOT NULL,
                                           account_name VARCHAR(255) NOT NULL,
                                           is_request_approved BOOLEAN DEFAULT FALSE,
                                           is_request_checked BOOLEAN DEFAULT FALSE,
                                           CONSTRAINT fk_owner_user FOREIGN KEY(owner_user_id) REFERENCES users(id),
                                           CONSTRAINT fk_requested_user FOREIGN KEY(requested_user_id) REFERENCES users(id)
);

CREATE TABLE record_categories (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   name VARCHAR(255) NOT NULL,
                                   description TEXT
);

CREATE TABLE budgets (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(255) NOT NULL,
                         amount DOUBLE PRECISION NOT NULL CHECK (amount >= 1),
                         user_id UUID NOT NULL,
                         CONSTRAINT fk_budgets_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE budget_record_categories (
                                          budget_id UUID NOT NULL,
                                          record_category_id UUID NOT NULL,
                                          PRIMARY KEY (budget_id, record_category_id),
                                          FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
                                          FOREIGN KEY (record_category_id) REFERENCES record_categories(id) ON DELETE CASCADE
);

CREATE TABLE email_auth_tokens (
                                   token UUID PRIMARY KEY,
                                   user_id UUID NOT NULL,
                                   created_at TIMESTAMP NOT NULL,
                                   is_used BOOLEAN DEFAULT FALSE,
                                   CONSTRAINT fk_eat_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE records (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         amount DOUBLE PRECISION NOT NULL,
                         user_id UUID NOT NULL,
                         payment_time TIMESTAMP,
                         category_id UUID,
                         type VARCHAR(255) NOT NULL,
                         note TEXT,
                         currency VARCHAR(20) NOT NULL,
                         withdrawal_account_id UUID,
                         reciving_account_id UUID,
                         CONSTRAINT fk_records_user FOREIGN KEY(user_id) REFERENCES users(id),
                         CONSTRAINT fk_records_category FOREIGN KEY(category_id) REFERENCES record_categories(id),
                         CONSTRAINT fk_records_withdrawal_account FOREIGN KEY(withdrawal_account_id) REFERENCES accounts(id),
                         CONSTRAINT fk_records_reciving_account FOREIGN KEY(reciving_account_id) REFERENCES accounts(id)
);
