-- Seeds only the shared demo catalog. Run after database/schema.sql.
-- Existing products with these names and existing batch codes are left untouched.
-- No users, passwords, orders, or other personal data are inserted.
USE tradeshield_db;

START TRANSACTION;

-- Add a product only when its name is not already present.
INSERT INTO products (name, category, price)
SELECT seed.name, seed.category, seed.price
FROM (
    SELECT 'Laptop' AS name, 'Electronics' AS category, 45000.00 AS price
    UNION ALL SELECT 'Headphones', 'Electronics', 1500.00
    UNION ALL SELECT 'Notebook', 'Stationery', 80.00
) AS seed
LEFT JOIN products AS existing ON existing.name = seed.name
WHERE existing.id IS NULL;

-- Resolve each batch to the lowest-ID matching product row. A batch code that
-- already exists is left as-is, including any differing quantity or product.
INSERT INTO batches (product_id, code, quantity)
SELECT product_rows.id, seed.code, seed.quantity
FROM (
    SELECT 'Laptop' AS name, 'Electronics' AS category, 45000.00 AS price, 'LAP-A' AS code, 10 AS quantity
    UNION ALL SELECT 'Laptop', 'Electronics', 45000.00, 'LAP-B', 8
    UNION ALL SELECT 'Headphones', 'Electronics', 1500.00, 'HEAD-A', 20
    UNION ALL SELECT 'Notebook', 'Stationery', 80.00, 'NOTE-A', 50
) AS seed
JOIN (
    SELECT name, category, price, MIN(id) AS id
    FROM products
    GROUP BY name, category, price
) AS product_rows
    ON product_rows.name = seed.name
    AND product_rows.category = seed.category
    AND product_rows.price = seed.price
LEFT JOIN batches AS existing ON existing.code = seed.code
WHERE existing.id IS NULL;

COMMIT;
