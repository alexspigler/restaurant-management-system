-- =============================================
-- VIEWS
-- =============================================
DROP VIEW IF EXISTS OrderDetails;
DROP VIEW IF EXISTS CustomerOrderSummary;

-- =============================================
-- View 1: CustomerOrderSummary
-- Shows each customer's order count, total spending,
-- and total discounts. LEFT JOIN includes customers
-- with zero orders.
-- =============================================
CREATE VIEW CustomerOrderSummary AS
SELECT
    C.CustomerID,
    C.FirstName,
    C.LastName,
    C.IsPremium,
    COUNT(O.OrderID) AS TotalOrders,
    COALESCE(SUM(O.TotalAmount), 0) AS TotalSpent,
    COALESCE(SUM(O.Discount), 0) AS TotalDiscount
FROM Customer C
LEFT JOIN Orders O ON C.CustomerID = O.CustomerID
GROUP BY C.CustomerID, C.FirstName, C.LastName, C.IsPremium;


-- =============================================
-- View 2: OrderDetails
-- Full line-item detail for each order with item
-- names, categories, and computed line totals.
-- =============================================
CREATE VIEW OrderDetails AS
SELECT
    O.OrderID,
    O.CustomerID,
    O.OrderDate,
    O.OrderType,
    M.ItemName,
    M.Category,
    OI.Quantity,
    OI.UnitPrice,
    (OI.Quantity * OI.UnitPrice) AS LineTotal
FROM Orders O
JOIN OrderItem OI ON O.OrderID = OI.OrderID
JOIN MenuItem M ON OI.ItemID = M.ItemID;
