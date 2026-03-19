# stock-v8-sniper

可在 IntelliJ IDEA 直接執行的 Java 17 / Spring Boot 2.7 / MariaDB 台股規則型選股系統。

## 啟動
1. 先建立 MariaDB 資料庫 `stock_v8_sniper`。
2. 執行 `src/main/resources/schema.sql`。
3. 調整 `src/main/resources/application.properties` 的資料庫帳密。
4. 在 IntelliJ IDEA 開啟專案後執行 `tool.stock.StockApplication`。

## 主要流程
1. `/api/import/twse/daily?date=2026-03-19`
2. `/api/strategy/run?v=8&date=2026-03-19`
3. `/api/strategy/results?date=2026-03-19`
4. `/api/strategy/history?stockCode=2330`

## 資料夾
- `data/raw/YYYYMMDD`：TWSE 原始 JSON
- `data/export/YYYYMMDD`：選股 CSV
- `logs`：保留給外部 logging appender 使用
