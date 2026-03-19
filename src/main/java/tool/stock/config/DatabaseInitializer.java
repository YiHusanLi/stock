package tool.stock.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {

        // ===== 1. 建立資料庫 =====
        jdbcTemplate.execute("""
            CREATE DATABASE IF NOT EXISTS stock
            CHARACTER SET utf8mb4
            COLLATE utf8mb4_unicode_ci
        """);

        // ===== 2. 切換 DB =====
        jdbcTemplate.execute("USE stock");

        // ===== 3. import_batch =====
        createTableIfNotExists("import_batch", """
            CREATE TABLE import_batch (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                batch_date DATE NOT NULL,
                source_type VARCHAR(50),
                source_file_name VARCHAR(255),
                source_file_path VARCHAR(500),
                imported_table_name VARCHAR(100),
                import_status VARCHAR(20),
                total_rows INT,
                success_rows INT,
                failed_rows INT,
                error_message TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                KEY idx_import_batch_file (source_file_name),
                KEY idx_import_batch_date (batch_date),
                KEY idx_import_batch_status (import_status)
            )
        """);

        // ===== 4. 修正舊的 UNIQUE KEY（如果存在）=====
        dropIndexIfExists("import_batch", "uk_import_batch_file");

        // ===== 5. security_master =====
        createTableIfNotExists("security_master", """
            CREATE TABLE security_master (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                stock_code VARCHAR(10) NOT NULL,
                stock_name VARCHAR(100),
                market_type VARCHAR(20),
                industry_category VARCHAR(50),
                is_active TINYINT DEFAULT 1,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                UNIQUE KEY uk_stock_code (stock_code)
            )
        """);

        // ===== 6. daily_price =====
        createTableIfNotExists("daily_price", """
            CREATE TABLE daily_price (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                stock_code VARCHAR(10) NOT NULL,
                trade_date DATE NOT NULL,
                open_price DECIMAL(10,2),
                high_price DECIMAL(10,2),
                low_price DECIMAL(10,2),
                close_price DECIMAL(10,2),
                volume BIGINT,
                turnover_amount DECIMAL(18,2),
                source_file_name VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                UNIQUE KEY uk_stock_date (stock_code, trade_date),
                KEY idx_trade_date (trade_date)
            )
        """);

        // ===== 7. daily_chip =====
        createTableIfNotExists("daily_chip", """
            CREATE TABLE daily_chip (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                stock_code VARCHAR(10) NOT NULL,
                trade_date DATE NOT NULL,
                foreign_net_buy BIGINT,
                investment_trust_net_buy BIGINT,
                dealer_net_buy BIGINT,
                source_file_name VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                UNIQUE KEY uk_chip (stock_code, trade_date)
            )
        """);

        // ===== 8. daily_valuation =====
        createTableIfNotExists("daily_valuation", """
            CREATE TABLE daily_valuation (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                stock_code VARCHAR(10) NOT NULL,
                trade_date DATE NOT NULL,
                pe_ratio DECIMAL(10,2),
                eps DECIMAL(10,2),
                pb_ratio DECIMAL(10,2),
                dividend_yield DECIMAL(10,2),
                source_file_name VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                UNIQUE KEY uk_val (stock_code, trade_date)
            )
        """);

        // ===== 9. stock_basic（給篩選/查詢系統使用）=====
        createTableIfNotExists("stock_basic", """
            CREATE TABLE stock_basic (
                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                stock_code VARCHAR(20) NOT NULL,
                stock_name VARCHAR(100) NOT NULL,
                market VARCHAR(20),
                industry VARCHAR(100),
                isin_code VARCHAR(20),
                listing_date DATE,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY uk_stock_basic_code (stock_code)
            )
        """);

        // ===== 10. stock_quote_daily（給權證篩選 SQL 使用）=====
        createTableIfNotExists("stock_quote_daily", """
            CREATE TABLE stock_quote_daily (
                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                stock_code VARCHAR(20) NOT NULL,
                trade_date DATE NOT NULL,
                open_price DECIMAL(12,4),
                high_price DECIMAL(12,4),
                low_price DECIMAL(12,4),
                close_price DECIMAL(12,4),
                price_change DECIMAL(12,4),
                change_percent DECIMAL(8,4),
                trade_volume BIGINT,
                trade_value BIGINT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY uk_stock_quote_daily (stock_code, trade_date),
                KEY idx_stock_quote_trade_date (trade_date),
                KEY idx_stock_quote_code_date (stock_code, trade_date)
            )
        """);

        // ===== 11. warrant_basic =====
        createTableIfNotExists("warrant_basic", """
            CREATE TABLE warrant_basic (
                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                warrant_code VARCHAR(20) NOT NULL,
                warrant_name VARCHAR(200) NOT NULL,
                underlying_code VARCHAR(20) NOT NULL,
                underlying_name VARCHAR(100),
                warrant_type VARCHAR(10) NOT NULL COMMENT 'CALL/PUT',
                issuer_name VARCHAR(100),
                strike_price DECIMAL(12,4),
                issue_price DECIMAL(12,4),
                exercise_ratio DECIMAL(12,6),
                issue_date DATE,
                maturity_date DATE,
                last_trade_date DATE,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY uk_warrant_basic_code (warrant_code),
                KEY idx_warrant_underlying (underlying_code),
                KEY idx_warrant_maturity (maturity_date)
            )
        """);

        // ===== 12. warrant_quote_daily =====
        createTableIfNotExists("warrant_quote_daily", """
            CREATE TABLE warrant_quote_daily (
                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                warrant_code VARCHAR(20) NOT NULL,
                trade_date DATE NOT NULL,
                close_price DECIMAL(12,4),
                price_change DECIMAL(12,4),
                change_percent DECIMAL(8,4),
                trade_volume BIGINT,
                trade_value BIGINT,
                bid_price DECIMAL(12,4),
                ask_price DECIMAL(12,4),
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE KEY uk_warrant_quote_daily (warrant_code, trade_date),
                KEY idx_warrant_quote_trade_date (trade_date),
                KEY idx_warrant_code_trade_date (warrant_code, trade_date)
            )
        """);

        System.out.println(">>> Database initialized SUCCESS");
    }

    /**
     * 如果資料表不存在就建立
     */
    private void createTableIfNotExists(String tableName, String createSql) {
        if (!tableExists(tableName)) {
            jdbcTemplate.execute(createSql);
            System.out.println(">>> Created table: " + tableName);
        } else {
            System.out.println(">>> Table already exists: " + tableName);
        }
    }

    /**
     * 檢查資料表是否存在
     */
    private boolean tableExists(String tableName) {
        Integer count = jdbcTemplate.queryForObject("""
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = DATABASE()
              AND table_name = ?
        """, Integer.class, tableName);

        return count != null && count > 0;
    }

    /**
     * 如果 index 存在就刪除
     */
    private void dropIndexIfExists(String tableName, String indexName) {
        try {
            Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.statistics
                WHERE table_schema = DATABASE()
                  AND table_name = ?
                  AND index_name = ?
            """, Integer.class, tableName, indexName);

            if (count != null && count > 0) {
                jdbcTemplate.execute("ALTER TABLE " + tableName + " DROP INDEX " + indexName);
                System.out.println(">>> Dropped index: " + indexName + " on table: " + tableName);
            }
        } catch (Exception e) {
            System.out.println(">>> Skip drop index " + indexName + ", reason: " + e.getMessage());
        }
    }
}