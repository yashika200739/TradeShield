# TradeShield — Java Console Shopping and Inventory Project

TradeShield is a Java 17 JDBC console application. It stores application records in MySQL; SQLite is no longer used by the application.

## Configure MySQL

1. Install and start MySQL Server, then create a database named `tradeshield_db` in MySQL Workbench.
2. In the project root, copy `.env.example` to `.env` and enter your local MySQL username and password. Keep `.env` private; it is excluded by `.gitignore`.
3. In MySQL Workbench, select `tradeshield_db`, open `database/schema.sql`, and execute it. The script creates the seven tables if they are missing. It does not create or drop a database, replace tables, or insert demo data.

The project also creates any missing tables at startup using the same MySQL schema. Existing tables and rows are not dropped or overwritten. For an existing table with a different structure, update it deliberately in Workbench before running the application.

## Test the connection and storage

From a terminal opened at the project root, run:

```shell
mvn compile exec:java -Dtradeshield.mainClass=com.tradeshield.db.DatabaseConnectionTest
```

This explicitly selects `com.tradeshield.db.DatabaseConnectionTest` from `src/main/java/com/tradeshield/db/DatabaseConnectionTest.java`. It checks `SELECT 1` and the actual database name, runs `Schema.create()`, confirms all seven tables, then inserts and reads a temporary product inside a transaction and rolls it back.

## Run in VS Code

1. Install JDK 17 or later, Apache Maven, and the VS Code Extension Pack for Java.
2. Open the TradeShield folder (the folder containing `pom.xml`) and its integrated terminal.
3. Run:

   ```shell
   mvn clean compile exec:java
   ```

On Windows, `run-windows.bat` runs this same application command. The app starts with empty tables and adds no demo administrator, products, or batches. Register a user, then promote that account to the administrator role in Workbench if you need the admin menu: `UPDATE users SET role = 'ADMIN' WHERE email = 'the_registered_email';`. The admin menu can then add products and batches.

## Run in IntelliJ IDEA

Open the folder as a Maven project with JDK 17+, then run `com.tradeshield.app.Main` or use Maven's `exec:java` goal.

## Database tables

The MySQL schema contains seven linked tables: `users`, `products`, `batches`, `orders`, `order_items`, `claims`, and `recalls`. See `database/schema.sql` for the complete definitions.

The database connection settings are read from `.env` (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD`). Environment variables with those names can also provide the settings.

## Project structure

- `app` — entry point
- `db` — MySQL connection and schema setup
- `dao` — prepared JDBC queries and updates
- `model`, `service`, `ui`, `util`, `exception` — application data and console workflows
