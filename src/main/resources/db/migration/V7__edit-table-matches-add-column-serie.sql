SET @col_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_NAME = 'matches'
      AND TABLE_SCHEMA = DATABASE()
      AND COLUMN_NAME = 'serie'
);

SET @sql := IF(@col_exists = 0,
               'ALTER TABLE matches ADD COLUMN serie VARCHAR(20) NOT NULL;',
               'SELECT "Column serie already exists."');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;