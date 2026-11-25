# TradeShield — Java Console Shopping and Inventory Project

**College Java OOP + JDBC console project for VS Code / IntelliJ.** No React, web app, Spring, frameworks, or analytics dashboards.

## Start in VS Code
1. Install **JDK 17 or later** and **Apache Maven**, then VS Code **Extension Pack for Java**.
2. Open the **TradeShield** folder (the folder containing `pom.xml`).
3. Open Terminal > New Terminal and run:

   ```shell
   mvn clean compile exec:java
   ```

   Alternatively on Windows double-click `run-windows.bat` (Maven must be on PATH).
4. On first run `data/tradeshield.db` is created with sample products, batches, and one demo administrator.

## Start in IntelliJ IDEA
1. Install **JDK 17 or later** and open the **TradeShield** folder in IntelliJ IDEA.
2. If prompted, load the project as a Maven project and select a JDK 17+ Project SDK.
3. In the Maven tool window, run **Plugins > exec > exec:java**, or open `src/main/java/com/tracecart/app/Main.java` and run `Main.main()`.
4. On first run `data/tradeshield.db` is created with sample products, batches, and one demo administrator.

### Demo admin login
- Email: `admin@tradeshield.com`
- Password: `admin123`

**Demo only:** Credentials are stored as plain text for classroom simplicity. Do not use real passwords or expose this application publicly. This is not production-ready authentication.

### Simple demo for viva
1. Register a customer, login, browse products, view batches, buy 2 units from batch 1.
2. View My Orders, then View Bill. Write down the order item ID under My Orders.
3. Request a return or replacement using that order item ID.
4. Logout, login as admin. View claims and approve/reject one.
5. View batches, recall batch 1 as defective.
6. Show affected customer orders and remaining stock for batch 1.
7. Logout, login as customer, try to buy from recalled batch 1. The purchase must be refused.

### Simple explanation of folder structure
- `app` – entry-point `Main` class.
- `db` – JDBC connection and table creation.
- `model` – simple Java objects with fields, constructors and `toString`.
- `dao` – SQL queries with JDBC `PreparedStatement` and `ResultSet`.
- `service` – checks and business rules.
- `ui` – console menus (`switch`, loops and Scanner).
- `util` – input, validation and simple printing.
- `exception` – custom stock exception.

**35 `.java` files, each containing one public class.**

### Database (7 linked tables)
`users`, `products`, `batches`, `orders`, `order_items`, `claims`, `recalls`. Foreign keys and a batch ID connect orders to recalls.

### Scope and limitations
- One batch per order; customers may place several orders.
- Invoice is displayed in the console, not exported as PDF.
- Return/replacement workflow records and reviews requests; it does not send refunds or ship replacements.
- Only one demo administrator is created.
- A recall marks a batch as defective without deleting historical orders or changing remaining stock. New purchases from recalled batches are blocked.
- SQLite uses JDBC through the `sqlite-jdbc` Maven dependency. Internet is required *once* to download this dependency unless it is cached already.

### Check the Java class count
PowerShell:

```powershell
(Get-ChildItem -Recurse -Filter *.java).Count
```

Expected: **35**.

### Database and build verification
The project targets Java 17 and uses SQLite through the `sqlite-jdbc` Maven dependency. It creates `data/tradeshield.db`, its tables, and sample data on first run. No external MySQL server is used.
