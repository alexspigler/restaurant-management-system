# Design & Implementation

This document covers the data model, schema, SQL, and GUI behind the Restaurant Management Database System.

---

## 1. Domain Overview

The system models a restaurant that supports both in-house dining and remote ordering (online and phone). It tracks:

- A menu catalog of food and beverage items across multiple cuisine categories
- Customer records with a premium-status flag used for discount eligibility
- Orders across three channels — Dine-In, Online, and Phone — along with the individual line items in each
- Deliveries, assigned to delivery personnel responsible for specific area codes
- Dine-in table reservations
- Payment transactions across cash, credit card, debit card, and online methods

---

## 2. Entities and Relationships

The system is built around ten business entities plus a separate table for GUI authentication:

| Entity | Purpose |
|---|---|
| **Area** | Geographic delivery zones, identified by area code |
| **Customer** | Patrons with contact info, join date, and premium flag |
| **MenuItem** | Menu catalog with name, cuisine category, price, and availability |
| **DineInTable** | Physical tables with seating capacity and location |
| **DeliveryBoy** | Delivery personnel, each assigned to exactly one area |
| **Orders** | A customer's order, recording date, type, total, and discount |
| **OrderItem** | Line items linking orders to menu items (weak entity) |
| **Delivery** | Delivery record for online/phone orders |
| **Payment** | Payment transactions, one per order |
| **Reservation** | Dine-in table reservations with date, time, and party size |
| **UserAccount** | Login accounts for the Swing application (stores a role per user) |

Key design decisions:

- **One-to-one between DeliveryBoy and Area.** A `UNIQUE` constraint on `DeliveryBoy.AreaCode` enforces the rule that each area has at most one delivery driver, making driver assignment deterministic.
- **OrderItem as a weak entity.** Orders and menu items have a many-to-many relationship, so `OrderItem` resolves it with a composite primary key `(OrderID, ItemID)` plus `Quantity` and `UnitPrice` attributes. Deletion of an order cascades to its line items so no orphans can exist.
- **`UnitPrice` as a historical snapshot.** `OrderItem.UnitPrice` is captured at order time, not looked up from `MenuItem.Price`. When the restaurant raises a price, historical orders preserve what the customer was actually charged.
- **Participation constraints.** Every order must belong to a registered customer, every payment must belong to an order, and every delivery must be assigned to a delivery driver — enforced by `NOT NULL` foreign keys.
- **`Orders` is named in the plural** to avoid colliding with the SQL reserved word `ORDER` (as in `ORDER BY`), which is invalid as a bare table name in most engines.
- **`UserAccount` is an application-layer table.** It exists only to authenticate the Swing client (storing a role per user) and has no foreign-key relationships to the operational entities — it is intentionally outside the restaurant business domain.
- **`MenuItem.Category` uses cuisine types** (`American`, `Asian`, `Mexican`, `Italian`, `Beverage`) rather than a coarse food/beverage split, enabling category-level filtering, per-cuisine reporting, and keyword search.

### ERD

![ERD](erd.png)

---

## 3. Relational Schema

The eleven tables use `INT` primary keys, `VARCHAR` for free-text fields, `DECIMAL(10,2)` for monetary values, and `DATE` / `TIMESTAMP` / `TIME` for temporal fields. Every constrained text column (order type, payment method, delivery status, menu category) uses a `CHECK` constraint against an explicit domain to prevent bad data at the storage layer.

```
Area(AreaCode PK, AreaName, City)
Customer(CustomerID PK, FirstName, LastName, Phone, Email UNIQUE, Street,
         AreaCode FK -> Area, JoinDate, IsPremium)
MenuItem(ItemID PK, ItemName,
         Category ∈ {American, Asian, Mexican, Italian, Beverage},
         Description, Price > 0, IsAvailable)
DineInTable(TableID PK, Capacity > 0, Location)
DeliveryBoy(DeliveryBoyID PK, Name, Phone, AreaCode FK UNIQUE)
Orders(OrderID PK, CustomerID FK, OrderDate,
       OrderType ∈ {Dine-In, Online, Phone}, TotalAmount, Discount)
OrderItem(OrderID, ItemID, Quantity > 0, UnitPrice > 0)  PK(OrderID, ItemID)
Delivery(DeliveryID PK, OrderID FK UNIQUE, DeliveryBoyID FK, DeliveryTime,
         Status ∈ {Pending, In Transit, Delivered, Cancelled})
Payment(PaymentID PK, OrderID FK UNIQUE, Amount > 0,
        PaymentMethod ∈ {Cash, Credit Card, Debit Card, Online}, PaymentDate)
Reservation(ReservationID PK, CustomerID FK, TableID FK,
            ReservationDate, ReservationTime, NumGuests > 0,
            Status ∈ {Confirmed, Cancelled, Completed})
UserAccount(UserID PK, Username UNIQUE, Password, Role)
```

The full DDL with all foreign keys, `NOT NULL`, and `CHECK` constraints lives in [`sql/schema.sql`](../sql/schema.sql).

### Normalization (BCNF)

All eleven tables are in **BCNF**: for every non-trivial functional dependency `X -> Y`, the left-hand side `X` is a superkey. FDs are derived from the application semantics, not just the current data. The per-table analysis follows.

**Area** — `AreaCode -> AreaName, City`. Candidate key `{AreaCode}`.

**Customer** — `CustomerID -> {all other attributes}`. Candidate key `{CustomerID}`. (`Email` is `UNIQUE` but nullable, so it is not treated as a candidate key.) A suspected transitive `AreaCode -> City` does not arise: Customer stores only `AreaCode` (a foreign key), and `City` lives solely in Area, so the dependency is correctly isolated.

**MenuItem** — `ItemID -> ItemName, Category, Description, Price, IsAvailable`. Candidate key `{ItemID}`.

**DineInTable** — `TableID -> Capacity, Location`. Candidate key `{TableID}`.

**DeliveryBoy** — `DeliveryBoyID -> Name, Phone, AreaCode` and `AreaCode -> DeliveryBoyID, Name, Phone`. Two candidate keys, `{DeliveryBoyID}` and `{AreaCode}` (the `UNIQUE` on `AreaCode` enforces the one-to-one with Area). Both left-hand sides are superkeys.

**Orders** — `OrderID -> CustomerID, OrderDate, OrderType, TotalAmount, Discount`. Candidate key `{OrderID}`. Note that `CustomerID -> Discount` does **not** hold (the same customer's orders can carry different discounts). `TotalAmount` is a stored aggregate that could be derived from `SUM(Quantity * UnitPrice) - Discount`; the FD `OrderID -> TotalAmount` still has a superkey on the left, so BCNF is not violated — keeping it consistent is an application-layer integrity concern, not a normalization one.

**OrderItem** — `{OrderID, ItemID} -> Quantity, UnitPrice`. Candidate key `{OrderID, ItemID}` (composite). `ItemID -> UnitPrice` does **not** hold: `UnitPrice` is a snapshot at order time, so the same item can carry different unit prices across orders. No BCNF violation.

**Delivery** — `DeliveryID -> OrderID, DeliveryBoyID, DeliveryTime, Status` and `OrderID -> {the rest}`. Candidate keys `{DeliveryID}` and `{OrderID}` (each order has at most one delivery).

**Payment** — `PaymentID -> OrderID, Amount, PaymentMethod, PaymentDate` and `OrderID -> {the rest}`. Candidate keys `{PaymentID}` and `{OrderID}` (one payment per order).

**Reservation** — `ReservationID -> {all other attributes}` and `{TableID, ReservationDate, ReservationTime} -> {the rest}`. Candidate keys `{ReservationID}` and `{TableID, ReservationDate, ReservationTime}` (a table cannot be double-booked at the same date and time).

**UserAccount** — `UserID -> Username, Password, Role` and `Username -> UserID, Password, Role`. Candidate keys `{UserID}` and `{Username}` (`Username` is `UNIQUE`).

Because every table is already in BCNF (which is stricter than 3NF), all tables are also in 3NF, 2NF, and 1NF, and no decomposition is required.

---

## 4. Views

Two views encapsulate frequently-needed joins. Full definitions live in [`sql/views.sql`](../sql/views.sql).

### CustomerOrderSummary

One row per customer with total orders, total spending, and total discounts received. Uses a `LEFT JOIN` so customers with zero orders still appear with counts of zero.

```sql
CREATE VIEW CustomerOrderSummary AS
SELECT
    C.CustomerID, C.FirstName, C.LastName, C.IsPremium,
    COUNT(O.OrderID)                  AS TotalOrders,
    COALESCE(SUM(O.TotalAmount), 0)   AS TotalSpent,
    COALESCE(SUM(O.Discount), 0)      AS TotalDiscount
FROM Customer C
LEFT JOIN Orders O ON C.CustomerID = O.CustomerID
GROUP BY C.CustomerID, C.FirstName, C.LastName, C.IsPremium;
```

Customer-level aggregates such as `MAX`/`MIN`/`AVG` of `TotalSpent` become one-liners on top of this view. (The GUI's own stats buttons aggregate the base `Orders` and `MenuItem` tables directly, not this view.)

![CustomerOrderSummary](screenshots/views/customer-order-summary.png)

### OrderDetails

Flattens the three-way join between `Orders`, `OrderItem`, and `MenuItem` into a single row per line item, with a computed `LineTotal` column.

```sql
CREATE VIEW OrderDetails AS
SELECT
    O.OrderID, O.CustomerID, O.OrderDate, O.OrderType,
    M.ItemName, M.Category,
    OI.Quantity, OI.UnitPrice,
    (OI.Quantity * OI.UnitPrice) AS LineTotal
FROM Orders O
JOIN OrderItem OI ON O.OrderID = OI.OrderID
JOIN MenuItem  M  ON OI.ItemID  = M.ItemID;
```

Simplifies item-level sales reporting.

![OrderDetails](screenshots/views/order-details.png)

---

## 5. Triggers

Three triggers enforce cross-table business rules that can't be expressed with simple `CHECK` constraints. Full definitions live in [`sql/triggers.sql`](../sql/triggers.sql).

### Block deliveries for Dine-In orders — `BEFORE INSERT ON Delivery`

```sql
CREATE OR REPLACE FUNCTION fn_prevent_dinein_delivery()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM Orders O
        WHERE O.OrderID = NEW.OrderID AND O.OrderType = 'Dine-In'
    ) THEN
        RAISE EXCEPTION 'Cannot create a delivery for a Dine-In order.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

### Auto-promote to premium at $200 lifetime spend — `AFTER INSERT OR UPDATE ON Orders`

```sql
CREATE OR REPLACE FUNCTION fn_update_premium_status()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Customer
    SET IsPremium = 'Yes'
    WHERE CustomerID = NEW.CustomerID
      AND IsPremium = 'No'
      AND (
          SELECT SUM(O.TotalAmount) FROM Orders O
          WHERE O.CustomerID = NEW.CustomerID
      ) >= 200;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

Firing on both `INSERT` and `UPDATE` means a new order that crosses $200 promotes the customer immediately. Keying the update on `NEW.CustomerID` re-checks only the customer whose order changed (rather than re-scanning every customer), and the `AND IsPremium = 'No'` guard avoids redundant writes when they are already premium. Promotion is one-way by design: cumulative lifetime spend does not decrease under normal use, so customers are never demoted.

`IsPremium` is deliberately dual-sourced. The trigger grants premium *automatically* once lifetime spend reaches $200, and staff can also set it *manually* on the Customer form — for example, to comp a VIP or honor a promotion that isn't spend-based. Because the trigger only ever promotes, a manually granted status is never overwritten by it, and an automatically earned one is never revoked. In other words, `$200` is the automatic floor for premium, not the only path to it.

### Block deletion of customers with orders — `BEFORE DELETE ON Customer`

```sql
CREATE OR REPLACE FUNCTION fn_prevent_customer_delete()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM Orders O WHERE O.CustomerID = OLD.CustomerID
    ) THEN
        RAISE EXCEPTION 'Cannot delete a customer who has existing orders.';
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;
```

Preserves order history and prevents accidental referential data loss.

---

## 6. Sample Queries

Ten representative queries against the schema, ranging from simple filters to relational division. All ten live in [`sql/queries.sql`](../sql/queries.sql).

### Customers who placed both online and phone orders — set intersection

```sql
SELECT C.FirstName, C.LastName
FROM Customer C
JOIN Orders O ON C.CustomerID = O.CustomerID
WHERE O.OrderType = 'Online'
INTERSECT
SELECT C.FirstName, C.LastName
FROM Customer C
JOIN Orders O ON C.CustomerID = O.CustomerID
WHERE O.OrderType = 'Phone';
```

### Customers who have ordered every beverage on the menu — relational division

```sql
SELECT C.FirstName, C.LastName
FROM Customer C
WHERE NOT EXISTS (
    SELECT M.ItemID FROM MenuItem M
    WHERE M.Category = 'Beverage'
    EXCEPT
    SELECT OI.ItemID
    FROM Orders O
    JOIN OrderItem OI ON O.OrderID = OI.OrderID
    WHERE O.CustomerID = C.CustomerID
);
```

Implements relational division via `NOT EXISTS` + `EXCEPT` — finds customers for whom the set of beverage items *not yet ordered* is empty.

### Customers and drivers per delivery — four-way join

```sql
SELECT C.FirstName, C.LastName,
       DB.Name AS DeliveryBoyName,
       D.DeliveryTime, D.Status
FROM Customer    C
JOIN Orders      O  ON C.CustomerID    = O.CustomerID
JOIN Delivery    D  ON O.OrderID       = D.OrderID
JOIN DeliveryBoy DB ON D.DeliveryBoyID = DB.DeliveryBoyID;
```

### Total discount by customer — aggregation with filter

```sql
SELECT C.FirstName, C.LastName,
       COUNT(O.OrderID) AS DiscountedOrders,
       SUM(O.Discount)  AS TotalDiscount
FROM Customer C
JOIN Orders   O ON C.CustomerID = O.CustomerID
WHERE O.Discount > 0
GROUP BY C.CustomerID, C.FirstName, C.LastName
ORDER BY TotalDiscount DESC;
```

### Customers who have never placed an order — anti-join via `NOT EXISTS`

```sql
SELECT C.FirstName, C.LastName
FROM Customer C
WHERE NOT EXISTS (
    SELECT *
    FROM Orders O
    WHERE O.CustomerID = C.CustomerID
);
```

The remaining five queries — premium customers, dine-in order details by customer, line items for a specific order, reservations by table capacity, and distinct items ordered by premium customers — are in `sql/queries.sql`.

---

## 7. GUI

The application is built with Java Swing and JDBC. Three classes in total:

- [`RestaurantApp`](../gui/src/RestaurantApp.java) — entry point; loads the JDBC driver and launches the login form
- [`LoginForm`](../gui/src/LoginForm.java) — authentication window
- [`RestaurantForm`](../gui/src/RestaurantForm.java) — main window with tabbed data views

All SQL access is routed through [`DatabaseHelper`](../gui/src/DatabaseHelper.java), which exposes a small set of parameterized methods backed by `PreparedStatement` to prevent injection.

### Login

Authenticates against the `UserAccount` table by username and password. Each account carries a role (Admin, Staff, Manager), though the GUI does not yet gate features by role — any valid login gets full access.

```sql
SELECT 1 FROM UserAccount WHERE Username = ? AND Password = ?;
```

`SELECT 1` is used because the query only needs to know whether a matching row exists — no column values are consumed.

![Login](screenshots/gui/login-page.png)

### Main Window

Tabbed interface across three data domains: Customers, Menu Items, and Orders.

**Keyword search.** Each searchable tab has a text field that runs a case-insensitive partial match across every displayed column using `ILIKE`:

```sql
SELECT CustomerID, FirstName, LastName, Phone, Email, AreaCode, JoinDate, IsPremium
FROM Customer
WHERE CAST(CustomerID AS TEXT) ILIKE '%' || ? || '%'
   OR FirstName ILIKE '%' || ? || '%'
   OR LastName  ILIKE '%' || ? || '%'
   OR Phone     ILIKE '%' || ? || '%'
   OR Email     ILIKE '%' || ? || '%'
   OR AreaCode  ILIKE '%' || ? || '%'
ORDER BY CustomerID;
```

**CRUD operations.** Add / Update / Delete dialogs for `Customer` and `MenuItem` records, with confirmation prompts and automatic `JTable` refresh after each operation. The "Auto-promote to premium" trigger and "Block deletion with existing orders" trigger both surface in the UI when they fire.

**Aggregate statistics.** "Price Stats" (Menu tab) and "Order Stats" (Orders tab) buttons run `MAX` / `MIN` / `AVG` against the respective tables and display the results in a popup dialog.

![Customer Tab](screenshots/gui/customer-tab.png)

![Menu Items Tab](screenshots/gui/menu-items-tab.png)

![Order Stats](screenshots/gui/order-stats.png)

Additional GUI screenshots live in [`screenshots/gui/`](screenshots/gui/).

---

## 8. Data Source

Menu items (IDs 101–132) come from the [Maven Analytics Restaurant Orders dataset](https://github.com/zainhaidar16/Restaurant-Order-Analysis). Beverage items (IDs 133–138) and all other seed data were generated to exercise the schema.

The bundled [`sql/seed.sql`](../sql/seed.sql) populates all eleven tables with a mix of real and synthetic data:

| Table | Rows | Notes |
|---|---|---|
| UserAccount | 3 | Admin, Staff, Manager logins for the GUI |
| Area | 8 | Area codes 10001–10008 |
| Customer | 15 | Mix of premium and non-premium |
| MenuItem | 38 | 32 real items (Maven Analytics) + 6 beverages |
| DineInTable | 8 | Various capacities and locations |
| DeliveryBoy | 5 | Areas 10006–10008 intentionally have no driver |
| Orders | 25 | Dine-In, Online, and Phone |
| OrderItem | 83 | Line items linking orders to menu items |
| Delivery | 10 | Online/Phone orders only |
| Payment | 25 | One per order |
| Reservation | 8 | Various statuses |

Several rows are constructed to exercise specific queries — for example, Customer 13 has no orders (for the anti-join), Customer 1 has ordered every beverage (for the relational-division query), and areas 10006–10008 have no delivery driver.
