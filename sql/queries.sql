-- =============================================
-- Restaurant Management Database System
-- Sample SQL Queries
-- =============================================
-- Ten representative queries against the schema,
-- ranging from simple filters to relational division.
-- =============================================


-- =============================================
-- Query 1: Find all premium customers
-- Simple SELECT with WHERE filter.
-- =============================================
SELECT C.CustomerID, C.FirstName, C.LastName, C.Phone
FROM Customer C
WHERE C.IsPremium = 'Yes';


-- =============================================
-- Query 2: Find customer name and order date
--          for all dine-in orders
-- JOIN between Customer and Orders
-- =============================================
SELECT C.FirstName, C.LastName, O.OrderDate
FROM Customer C
JOIN Orders O ON C.CustomerID = O.CustomerID
WHERE O.OrderType = 'Dine-In';


-- =============================================
-- Query 3: Find item names, quantities, and
--          unit prices for order 1001
-- JOIN between MenuItem and OrderItem
-- =============================================
SELECT M.ItemName, OI.Quantity, OI.UnitPrice
FROM MenuItem M
JOIN OrderItem OI ON M.ItemID = OI.ItemID
WHERE OI.OrderID = 1001;


-- =============================================
-- Query 4: Find customers who have never
--          placed an order
-- NOT EXISTS subquery
-- =============================================
SELECT C.FirstName, C.LastName
FROM Customer C
WHERE NOT EXISTS (
    SELECT *
    FROM Orders O
    WHERE O.CustomerID = C.CustomerID
);


-- =============================================
-- Query 5: Find customers and their total
--          discount received
-- GROUP BY with SUM and COUNT aggregation
-- =============================================
SELECT C.FirstName, C.LastName,
    COUNT(O.OrderID) AS DiscountedOrders,
    SUM(O.Discount) AS TotalDiscount
FROM Customer C
JOIN Orders O ON C.CustomerID = O.CustomerID
WHERE O.Discount > 0
GROUP BY C.CustomerID, C.FirstName, C.LastName
ORDER BY TotalDiscount DESC;


-- =============================================
-- Query 6: Find customer name and delivery boy
--          name for each delivery
-- Multi-table JOIN across 4 tables
-- =============================================
SELECT C.FirstName, C.LastName,
    DB.Name AS DeliveryBoyName,
    D.DeliveryTime, D.Status
FROM Customer C
JOIN Orders O ON C.CustomerID = O.CustomerID
JOIN Delivery D ON O.OrderID = D.OrderID
JOIN DeliveryBoy DB ON D.DeliveryBoyID = DB.DeliveryBoyID;


-- =============================================
-- Query 7: Find customers who placed both
--          online and phone orders
-- INTERSECT of two queries
-- =============================================
SELECT C.FirstName, C.LastName
FROM Customer C
JOIN Orders O ON C.CustomerID = O.CustomerID
WHERE O.OrderType = 'Online'
INTERSECT
SELECT C.FirstName, C.LastName
FROM Customer C
JOIN Orders O ON C.CustomerID = O.CustomerID
WHERE O.OrderType = 'Phone';


-- =============================================
-- Query 8: Find all reservations for tables
--          with capacity of at least 4
-- JOIN with WHERE filter
-- =============================================
SELECT R.ReservationID, R.ReservationDate,
    T.Capacity, T.Location
FROM Reservation R
JOIN DineInTable T ON R.TableID = T.TableID
WHERE T.Capacity >= 4;


-- =============================================
-- Query 9: Find distinct menu items ordered
--          by premium customers
-- Multi-table JOIN with DISTINCT
-- =============================================
SELECT DISTINCT M.ItemName, M.Category, M.Price
FROM MenuItem M
JOIN OrderItem OI ON M.ItemID = OI.ItemID
JOIN Orders O ON OI.OrderID = O.OrderID
JOIN Customer C ON O.CustomerID = C.CustomerID
WHERE C.IsPremium = 'Yes';


-- =============================================
-- Query 10: Find customers who have ordered
--           every beverage on the menu
-- Division using NOT EXISTS with EXCEPT
-- =============================================
SELECT C.FirstName, C.LastName
FROM Customer C
WHERE NOT EXISTS (
    SELECT M.ItemID
    FROM MenuItem M
    WHERE M.Category = 'Beverage'
    EXCEPT
    SELECT OI.ItemID
    FROM Orders O
    JOIN OrderItem OI ON O.OrderID = OI.OrderID
    WHERE O.CustomerID = C.CustomerID
);

-- =============================================
-- View 1: CustomerOrderSummary
-- Each customer's order count, total spending,
-- and total discounts received
-- =============================================
SELECT * FROM CustomerOrderSummary;


-- =============================================
-- View 2: OrderDetails
-- Full line-item detail for each order with
-- item names, categories, and line totals
-- =============================================
SELECT * FROM OrderDetails;
