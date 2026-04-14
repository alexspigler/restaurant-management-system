-- =============================================
-- TRIGGERS
-- =============================================

-- =============================================
-- Trigger 1 (INSERT): Prevent Delivery for Dine-In
-- Business rule: only Online/Phone orders get deliveries.
-- =============================================
CREATE OR REPLACE FUNCTION fn_prevent_dinein_delivery()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM Orders O
        WHERE O.OrderID = NEW.OrderID
        AND O.OrderType = 'Dine-In'
    )
    THEN
        RAISE EXCEPTION 'Cannot create a delivery for a Dine-In order.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_PreventDineInDelivery
BEFORE INSERT ON Delivery
FOR EACH ROW
EXECUTE FUNCTION fn_prevent_dinein_delivery();

-- =============================================
-- Trigger 2 (UPDATE): Auto-Update Premium Status
-- Promotes customer to premium when total spending >= $200.
-- =============================================
CREATE OR REPLACE FUNCTION fn_update_premium_status()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Customer
    SET IsPremium = 'Yes'
    WHERE CustomerID IN (
        SELECT O.CustomerID
        FROM Orders O
        GROUP BY O.CustomerID
        HAVING SUM(O.TotalAmount) >= 200
    )
    AND IsPremium = 'No';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_UpdatePremiumStatus
AFTER UPDATE ON Orders
FOR EACH ROW
EXECUTE FUNCTION fn_update_premium_status();

-- =============================================
-- Trigger 3 (DELETE): Prevent Customer Deletion
-- Blocks deletion of customers who have orders.
-- =============================================
CREATE OR REPLACE FUNCTION fn_prevent_customer_delete()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM Orders O
        WHERE O.CustomerID = OLD.CustomerID
    )
    THEN
        RAISE EXCEPTION 'Cannot delete a customer who has existing orders.';
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_PreventCustomerDelete
BEFORE DELETE ON Customer
FOR EACH ROW
EXECUTE FUNCTION fn_prevent_customer_delete();
