# Restaurant Management Database System

A full-stack restaurant management application backed by a normalized PostgreSQL database, with a Java Swing GUI for browsing, searching, and managing customers, menu items, orders, deliveries, payments, and reservations.

## Tech Stack

- **Database:** PostgreSQL 14+
- **Language:** Java (Swing for GUI, JDBC for DB access)
- **Driver:** `postgresql-42.7.10.jar` (shipped in `gui/lib/`)

## Highlights

- **11-table relational schema** normalized to BCNF, with per-table functional-dependency analysis
- **Real restaurant order data** from the Maven Analytics dataset (32 menu items) augmented with synthetic customers, orders, and deliveries
- **10 SQL queries** spanning multi-table joins, GROUP BY aggregation, set operations (`INTERSECT`, `EXCEPT`), and relational division via `NOT EXISTS`
- **2 views** (`CustomerOrderSummary`, `OrderDetails`) encapsulating common join + aggregation patterns
- **3 triggers** enforcing cross-table business rules:
  - Block delivery creation for Dine-In orders
  - Auto-promote customers to *premium* status once lifetime spend reaches $200
  - Block deletion of customers with existing orders
- **Java Swing GUI** with role-based login, case-insensitive keyword search (`ILIKE`), CRUD for Customers and Menu Items, and live MAX/MIN/AVG analytics over the Orders and MenuItem tables

## Entity-Relationship Diagram

![ERD](docs/erd.png)

## Repository Layout

```
├── docs/
│   ├── DESIGN.md               Full design and implementation walkthrough
│   ├── erd.png                 Entity-relationship diagram
│   └── screenshots/
│       ├── gui/                GUI screens — login, tabs, CRUD form, stats
│       └── views/              View result screenshots
├── sql/
│   ├── schema.sql              CREATE TABLE with PK / FK / CHECK / UNIQUE / NOT NULL
│   ├── seed.sql                Real + synthetic data (~200 rows across 11 tables)
│   ├── views.sql               CustomerOrderSummary, OrderDetails
│   ├── triggers.sql            Three business-rule triggers
│   └── queries.sql             Ten sample queries
└── gui/
    ├── src/
    │   ├── RestaurantApp.java      Main entry point
    │   ├── LoginForm.java          Login window
    │   ├── RestaurantForm.java     Main window — tabs, CRUD, stats
    │   └── DatabaseHelper.java     All JDBC / SQL logic
    └── lib/
        └── postgresql-42.7.10.jar  JDBC driver
```

## Running It Locally

### 1. Load the database

```bash
createdb restaurant_db
psql -d restaurant_db -f sql/schema.sql
psql -d restaurant_db -f sql/seed.sql
psql -d restaurant_db -f sql/views.sql
psql -d restaurant_db -f sql/triggers.sql
```

### 2. Configure DB credentials

Edit the constants near the top of `gui/src/DatabaseHelper.java` to match your local PostgreSQL setup:

```java
private static final String DB_HOST = "your_host";       // e.g. "localhost"
private static final String DB_PORT = "5432";            // default PostgreSQL port
private static final String DB_NAME = "restaurant_db";   // matches the createdb above
private static final String DB_USER = "your_username";
private static final String DB_PASS = "your_password";
```

### 3. Compile and run

```bash
cd gui
javac -cp "lib/postgresql-42.7.10.jar" -d out src/*.java
java  -cp "out:lib/postgresql-42.7.10.jar" RestaurantApp
```

### Default login credentials (from `seed.sql`)

| Username | Password | Role    |
|----------|----------|---------|
| admin    | admin123 | Admin   |
| staff1   | pass456  | Staff   |
| manager  | mgr789   | Manager |

## Screenshots

**Customer management tab — browse, search, add, update, delete**

![Customer Tab](docs/screenshots/gui/customer-tab.png)

**Live aggregate statistics over the Orders table**

![Order Stats](docs/screenshots/gui/order-stats.png)

More screenshots live under [`docs/screenshots/`](docs/screenshots/).

## Design Doc

For the full walkthrough — domain model, schema design, BCNF analysis, views, triggers, sample queries, and GUI behavior — see **[docs/DESIGN.md](docs/DESIGN.md)**.

## Data Source

Menu items (IDs 101–132) come from the [Maven Analytics Restaurant Orders dataset](https://github.com/zainhaidar16/Restaurant-Order-Analysis). Beverage items (IDs 133–138) and all other table data are synthetic and generated to exercise the schema.

## License

Released under the [MIT License](LICENSE).
