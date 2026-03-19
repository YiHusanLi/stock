CREATE TABLE IF NOT EXISTS stock_basic (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    stock_code VARCHAR(20) NOT NULL,
    stock_name VARCHAR(100) NOT NULL,
    market_type VARCHAR(30),
    industry VARCHAR(100),
    is_etf TINYINT(1) NOT NULL DEFAULT 0,
    listing_type VARCHAR(30),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_stock_basic_code (stock_code)
);

CREATE TABLE IF NOT EXISTS stock_daily_quote (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    trade_date DATE NOT NULL,
    stock_code VARCHAR(20) NOT NULL,
    open_price DECIMAL(12,4),
    high_price DECIMAL(12,4),
    low_price DECIMAL(12,4),
    close_price DECIMAL(12,4),
    price_change DECIMAL(12,4),
    pct_change DECIMAL(10,4),
    volume BIGINT,
    turnover DECIMAL(18,2),
    trades BIGINT,
    source_type VARCHAR(30),
    raw_batch_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_trade_date_stock_code (trade_date, stock_code),
    KEY idx_stock_daily_quote_code_date (stock_code, trade_date)
);

CREATE TABLE IF NOT EXISTS stock_valuation_daily (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    trade_date DATE NOT NULL,
    stock_code VARCHAR(20) NOT NULL,
    pe_ratio DECIMAL(12,4),
    dividend_yield DECIMAL(12,4),
    pb_ratio DECIMAL(12,4),
    eps DECIMAL(12,4),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_valuation_date_stock (trade_date, stock_code)
);

CREATE TABLE IF NOT EXISTS stock_chip_daily (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    trade_date DATE NOT NULL,
    stock_code VARCHAR(20) NOT NULL,
    foreign_net_buy BIGINT,
    investment_trust_net_buy BIGINT,
    dealer_net_buy BIGINT,
    margin_balance BIGINT,
    short_balance BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_chip_date_stock (trade_date, stock_code)
);

CREATE TABLE IF NOT EXISTS import_batch (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    batch_date DATE NOT NULL,
    source_type VARCHAR(30) NOT NULL,
    api_url VARCHAR(500),
    source_file_name VARCHAR(255),
    source_file_path VARCHAR(500),
    import_status VARCHAR(30) NOT NULL,
    total_rows INT NOT NULL DEFAULT 0,
    success_rows INT NOT NULL DEFAULT 0,
    fail_rows INT NOT NULL DEFAULT 0,
    error_message TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at DATETIME NULL
);

CREATE TABLE IF NOT EXISTS strategy_pick_result (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    trade_date DATE NOT NULL,
    strategy_name VARCHAR(100) NOT NULL,
    stock_code VARCHAR(20) NOT NULL,
    stock_name VARCHAR(100),
    industry VARCHAR(100),
    total_score INT NOT NULL,
    close_price DECIMAL(12,4),
    pct_change DECIMAL(10,4),
    volume BIGINT,
    volume_ratio DECIMAL(12,4),
    trend_slope DECIMAL(12,6),
    suggested_buy_price DECIMAL(12,4),
    pick_reason VARCHAR(500),
    rating VARCHAR(30),
    is_limit_up TINYINT(1) NOT NULL DEFAULT 0,
    is_previous_day_picked TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_trade_date_stock_code_strategy (trade_date, stock_code, strategy_name),
    KEY idx_strategy_result_date (trade_date),
    KEY idx_strategy_result_code (stock_code)
);

CREATE TABLE IF NOT EXISTS strategy_pick_detail (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    trade_date DATE NOT NULL,
    stock_code VARCHAR(20) NOT NULL,
    strategy_name VARCHAR(100) NOT NULL,
    rule_code VARCHAR(50) NOT NULL,
    rule_name VARCHAR(100) NOT NULL,
    score INT NOT NULL,
    passed TINYINT(1) NOT NULL,
    rule_value VARCHAR(255),
    rule_threshold VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_pick_detail_date_code (trade_date, stock_code)
);

CREATE TABLE IF NOT EXISTS stock_daily_quote_staging (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    batch_id BIGINT NOT NULL,
    trade_date DATE NOT NULL,
    stock_code VARCHAR(20) NOT NULL,
    stock_name VARCHAR(100),
    raw_json JSON,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_staging_batch (batch_id)
);

CREATE TABLE IF NOT EXISTS stock_indicator_daily (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    trade_date DATE NOT NULL,
    stock_code VARCHAR(20) NOT NULL,
    ma5 DECIMAL(12,4),
    ma10 DECIMAL(12,4),
    ma20 DECIMAL(12,4),
    ma60 DECIMAL(12,4),
    avg_volume5 DECIMAL(18,4),
    avg_volume10 DECIMAL(18,4),
    avg_volume20 DECIMAL(18,4),
    volume_ratio DECIMAL(12,4),
    trend_slope DECIMAL(12,6),
    trend_pressure DECIMAL(12,4),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_indicator_date_stock (trade_date, stock_code)
);
