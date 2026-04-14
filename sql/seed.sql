-- =============================================
-- Restaurant Management Database System
-- Seed Data
-- =============================================
--
-- DATA SOURCES:
--   Menu items (IDs 101–132) are real data from:
--     zainhaidar16/Restaurant-Order-Analysis (GitHub)
--     https://github.com/zainhaidar16/Restaurant-Order-Analysis
--     Original source: Maven Analytics Restaurant Orders dataset
--   Beverage items (IDs 133–138) and all other table data
--   were generated to exercise the schema.
-- =============================================

-- =============================================
-- UserAccount (for GUI login)
-- =============================================
INSERT INTO UserAccount (UserID, Username, Password, Role) VALUES
(1, 'admin', 'admin123', 'Admin'),
(2, 'staff1', 'pass456', 'Staff'),
(3, 'manager', 'mgr789', 'Manager');

-- =============================================
-- Area
-- =============================================
INSERT INTO Area (AreaCode, AreaName, City) VALUES
('10001', 'Downtown', 'New York'),
('10002', 'Midtown', 'New York'),
('10003', 'Uptown', 'New York'),
('10004', 'Westside', 'New York'),
('10005', 'Eastside', 'New York'),
('10006', 'Little Italy', 'New York'),
('10007', 'Chinatown', 'New York'),
('10008', 'SoHo', 'New York');

-- =============================================
-- Customer
-- =============================================
INSERT INTO Customer (CustomerID, FirstName, LastName, Phone, Email, Street, AreaCode, JoinDate, IsPremium) VALUES
(1, 'John', 'Smith', '212-555-0101', 'john.smith@email.com', '123 Main St', '10001', '2023-01-15', 'Yes'),
(2, 'Maria', 'Garcia', '212-555-0102', 'maria.garcia@email.com', '456 Oak Ave', '10002', '2023-02-20', 'No'),
(3, 'David', 'Johnson', '212-555-0103', 'david.j@email.com', '789 Pine Rd', '10003', '2023-01-10', 'Yes'),
(4, 'Sarah', 'Williams', '212-555-0104', 'sarah.w@email.com', '321 Elm St', '10004', '2023-03-05', 'No'),
(5, 'Michael', 'Brown', '212-555-0105', 'michael.b@email.com', '654 Cedar Ln', '10001', '2023-01-25', 'Yes'),
(6, 'Emily', 'Davis', '212-555-0106', 'emily.d@email.com', '987 Birch Dr', '10005', '2023-02-14', 'No'),
(7, 'James', 'Wilson', '212-555-0107', 'james.w@email.com', '147 Maple Ave', '10002', '2023-03-01', 'No'),
(8, 'Lisa', 'Anderson', '212-555-0108', 'lisa.a@email.com', '258 Walnut St', '10006', '2023-01-30', 'Yes'),
(9, 'Robert', 'Taylor', '212-555-0109', 'robert.t@email.com', '369 Cherry Ln', '10003', '2023-02-28', 'No'),
(10, 'Jennifer', 'Thomas', '212-555-0110', 'jennifer.t@email.com', '741 Spruce Rd', '10007', '2023-03-15', 'Yes'),
(11, 'William', 'Martinez', '212-555-0111', 'william.m@email.com', '852 Ash St', '10004', '2023-01-05', 'No'),
(12, 'Amanda', 'Jackson', '212-555-0112', 'amanda.j@email.com', '963 Poplar Ave', '10008', '2023-02-10', 'No'),
(13, 'Daniel', 'White', '212-555-0113', 'daniel.w@email.com', '174 Cypress Dr', '10005', '2023-03-20', 'No'),
(14, 'Jessica', 'Harris', '212-555-0114', 'jessica.h@email.com', '285 Willow Ln', '10006', '2023-01-20', 'Yes'),
(15, 'Chris', 'Clark', '212-555-0115', 'chris.c@email.com', '396 Hickory St', '10001', '2023-02-05', 'No');

-- =============================================
-- MenuItem
-- Items 101–132: REAL DATA from Maven Analytics /
--   zainhaidar16/Restaurant-Order-Analysis (GitHub)
-- Items 133–138: Beverages added for schema coverage
-- =============================================
INSERT INTO MenuItem (ItemID, ItemName, Category, Description, Price, IsAvailable) VALUES
-- American (real data)
(101, 'Hamburger', 'American', NULL, 12.95, 'Yes'),
(102, 'Cheeseburger', 'American', NULL, 13.95, 'Yes'),
(103, 'Hot Dog', 'American', NULL, 9.00, 'Yes'),
(104, 'Veggie Burger', 'American', NULL, 10.50, 'Yes'),
(105, 'Mac & Cheese', 'American', NULL, 7.00, 'Yes'),
(106, 'French Fries', 'American', NULL, 7.00, 'Yes'),
-- Asian (real data)
(107, 'Orange Chicken', 'Asian', NULL, 16.50, 'Yes'),
(108, 'Tofu Pad Thai', 'Asian', NULL, 14.50, 'Yes'),
(109, 'Korean Beef Bowl', 'Asian', NULL, 17.95, 'Yes'),
(110, 'Pork Ramen', 'Asian', NULL, 17.95, 'Yes'),
(111, 'California Roll', 'Asian', NULL, 11.95, 'Yes'),
(112, 'Salmon Roll', 'Asian', NULL, 14.95, 'Yes'),
(113, 'Edamame', 'Asian', NULL, 5.00, 'Yes'),
(114, 'Potstickers', 'Asian', NULL, 9.00, 'Yes'),
-- Mexican (real data)
(115, 'Chicken Tacos', 'Mexican', NULL, 11.95, 'Yes'),
(116, 'Steak Tacos', 'Mexican', NULL, 13.95, 'Yes'),
(117, 'Chicken Burrito', 'Mexican', NULL, 12.95, 'Yes'),
(118, 'Steak Burrito', 'Mexican', NULL, 14.95, 'Yes'),
(119, 'Chicken Torta', 'Mexican', NULL, 11.95, 'Yes'),
(120, 'Steak Torta', 'Mexican', NULL, 13.95, 'No'),
(121, 'Cheese Quesadillas', 'Mexican', NULL, 10.50, 'Yes'),
(122, 'Chips & Salsa', 'Mexican', NULL, 7.00, 'Yes'),
(123, 'Chips & Guacamole', 'Mexican', NULL, 9.00, 'Yes'),
-- Italian (real data)
(124, 'Spaghetti', 'Italian', NULL, 14.50, 'Yes'),
(125, 'Spaghetti & Meatballs', 'Italian', NULL, 17.95, 'Yes'),
(126, 'Fettuccine Alfredo', 'Italian', NULL, 14.50, 'Yes'),
(127, 'Meat Lasagna', 'Italian', NULL, 17.95, 'Yes'),
(128, 'Cheese Lasagna', 'Italian', NULL, 15.50, 'Yes'),
(129, 'Mushroom Ravioli', 'Italian', NULL, 15.50, 'Yes'),
(130, 'Shrimp Scampi', 'Italian', NULL, 19.95, 'Yes'),
(131, 'Chicken Parmesan', 'Italian', NULL, 17.95, 'Yes'),
(132, 'Eggplant Parmesan', 'Italian', NULL, 16.95, 'No'),
-- Beverages (added for schema coverage)
(133, 'Coca-Cola', 'Beverage', NULL, 2.50, 'Yes'),
(134, 'Lemonade', 'Beverage', NULL, 3.00, 'Yes'),
(135, 'Iced Tea', 'Beverage', NULL, 2.75, 'Yes'),
(136, 'Coffee', 'Beverage', NULL, 3.50, 'Yes'),
(137, 'Orange Juice', 'Beverage', NULL, 3.25, 'Yes'),
(138, 'Sparkling Water', 'Beverage', NULL, 2.00, 'Yes');

-- =============================================
-- DineInTable
-- =============================================
INSERT INTO DineInTable (TableID, Capacity, Location) VALUES
(1, 2, 'Window'),
(2, 2, 'Window'),
(3, 4, 'Main Floor'),
(4, 4, 'Main Floor'),
(5, 6, 'Main Floor'),
(6, 6, 'Patio'),
(7, 8, 'Private Room'),
(8, 4, 'Patio');

-- =============================================
-- DeliveryBoy
-- Areas 10006–10008 intentionally have no delivery boy
-- =============================================
INSERT INTO DeliveryBoy (DeliveryBoyID, Name, Phone, AreaCode) VALUES
(1, 'Carlos Reyes', '212-555-0201', '10001'),
(2, 'Kevin Park', '212-555-0202', '10002'),
(3, 'Ahmed Hassan', '212-555-0203', '10003'),
(4, 'Tyler Brooks', '212-555-0204', '10004'),
(5, 'Marcus Chen', '212-555-0205', '10005');

-- =============================================
-- Orders
-- Mix of Dine-In, Online, and Phone orders.
-- Discounts applied only to premium customers.
-- Customer 13 intentionally has no orders (for query testing).
-- =============================================
INSERT INTO Orders (OrderID, CustomerID, OrderDate, OrderType, TotalAmount, Discount) VALUES
(1001, 1, '2023-01-15', 'Dine-In', 45.95, 5.00),
(1002, 2, '2023-01-16', 'Online', 26.50, 0.00),
(1003, 3, '2023-01-18', 'Phone', 35.95, 3.00),
(1004, 4, '2023-01-20', 'Online', 22.25, 0.00),
(1005, 5, '2023-01-22', 'Dine-In', 43.65, 5.00),
(1006, 1, '2023-01-25', 'Online', 22.50, 2.00),
(1007, 6, '2023-01-28', 'Phone', 27.90, 0.00),
(1008, 7, '2023-02-01', 'Dine-In', 34.45, 0.00),
(1009, 8, '2023-02-03', 'Online', 48.35, 4.00),
(1010, 3, '2023-02-05', 'Online', 25.45, 3.00),
(1011, 9, '2023-02-08', 'Phone', 18.50, 0.00),
(1012, 10, '2023-02-10', 'Dine-In', 57.85, 5.00),
(1013, 2, '2023-02-12', 'Phone', 25.90, 0.00),
(1014, 11, '2023-02-15', 'Online', 33.90, 0.00),
(1015, 12, '2023-02-18', 'Dine-In', 20.00, 0.00),
(1016, 14, '2023-02-20', 'Phone', 42.15, 5.00),
(1017, 5, '2023-02-22', 'Phone', 34.25, 3.00),
(1018, 15, '2023-02-25', 'Online', 18.00, 0.00),
(1019, 4, '2023-02-28', 'Phone', 29.40, 0.00),
(1020, 8, '2023-03-02', 'Dine-In', 35.50, 4.00),
(1021, 1, '2023-03-05', 'Phone', 21.95, 2.00),
(1022, 10, '2023-03-08', 'Online', 41.65, 5.00),
(1023, 14, '2023-03-10', 'Online', 37.65, 4.00),
(1024, 3, '2023-03-12', 'Dine-In', 43.40, 5.00),
(1025, 6, '2023-03-15', 'Online', 16.75, 0.00);

-- =============================================
-- OrderItem
-- Each row links an order to a menu item with quantity.
-- UnitPrice matches MenuItem.Price at time of order.
-- Customer 1 (orders 1001, 1006, 1021) orders all 6 beverages.
-- =============================================
INSERT INTO OrderItem (OrderID, ItemID, Quantity, UnitPrice) VALUES
-- Order 1001 (Customer 1, Dine-In)
(1001, 101, 1, 12.95),
(1001, 106, 1, 7.00),
(1001, 124, 1, 14.50),
(1001, 133, 2, 2.50),
(1001, 134, 1, 3.00),
(1001, 136, 1, 3.50),
-- Order 1002 (Customer 2, Online)
(1002, 108, 1, 14.50),
(1002, 113, 1, 5.00),
(1002, 122, 1, 7.00),
-- Order 1003 (Customer 3, Phone)
(1003, 109, 1, 17.95),
(1003, 114, 2, 9.00),
-- Order 1004 (Customer 4, Online)
(1004, 121, 1, 10.50),
(1004, 123, 1, 9.00),
(1004, 135, 1, 2.75),
-- Order 1005 (Customer 5, Dine-In)
(1005, 125, 1, 17.95),
(1005, 130, 1, 19.95),
(1005, 133, 1, 2.50),
(1005, 137, 1, 3.25),
-- Order 1006 (Customer 1, Online)
(1006, 107, 1, 16.50),
(1006, 135, 1, 2.75),
(1006, 137, 1, 3.25),
-- Order 1007 (Customer 6, Phone)
(1007, 115, 1, 11.95),
(1007, 117, 1, 12.95),
(1007, 134, 1, 3.00),
-- Order 1008 (Customer 7, Dine-In)
(1008, 126, 1, 14.50),
(1008, 131, 1, 17.95),
(1008, 138, 1, 2.00),
-- Order 1009 (Customer 8, Online)
(1009, 110, 1, 17.95),
(1009, 111, 1, 11.95),
(1009, 112, 1, 14.95),
(1009, 136, 1, 3.50),
-- Order 1010 (Customer 3, Online)
(1010, 127, 1, 17.95),
(1010, 113, 1, 5.00),
(1010, 133, 1, 2.50),
-- Order 1011 (Customer 9, Phone)
(1011, 107, 1, 16.50),
(1011, 138, 1, 2.00),
-- Order 1012 (Customer 10, Dine-In)
(1012, 125, 1, 17.95),
(1012, 130, 1, 19.95),
(1012, 132, 1, 16.95),
(1012, 134, 1, 3.00),
-- Order 1013 (Customer 2, Phone)
(1013, 116, 1, 13.95),
(1013, 119, 1, 11.95),
-- Order 1014 (Customer 11, Online)
(1014, 101, 1, 12.95),
(1014, 102, 1, 13.95),
(1014, 106, 1, 7.00),
-- Order 1015 (Customer 12, Dine-In)
(1015, 104, 1, 10.50),
(1015, 105, 1, 7.00),
(1015, 133, 1, 2.50),
-- Order 1016 (Customer 14, Phone)
(1016, 109, 1, 17.95),
(1016, 110, 1, 17.95),
(1016, 135, 1, 2.75),
(1016, 136, 1, 3.50),
-- Order 1017 (Customer 5, Phone)
(1017, 128, 1, 15.50),
(1017, 129, 1, 15.50),
(1017, 137, 1, 3.25),
-- Order 1018 (Customer 15, Online)
(1018, 103, 1, 9.00),
(1018, 106, 1, 7.00),
(1018, 138, 1, 2.00),
-- Order 1019 (Customer 4, Phone)
(1019, 117, 1, 12.95),
(1019, 120, 1, 13.95),
(1019, 133, 1, 2.50),
-- Order 1020 (Customer 8, Dine-In)
(1020, 124, 1, 14.50),
(1020, 126, 1, 14.50),
(1020, 134, 1, 3.00),
(1020, 136, 1, 3.50),
-- Order 1021 (Customer 1, Phone)
(1021, 118, 1, 14.95),
(1021, 113, 1, 5.00),
(1021, 138, 1, 2.00),
-- Order 1022 (Customer 10, Online)
(1022, 127, 1, 17.95),
(1022, 131, 1, 17.95),
(1022, 133, 1, 2.50),
(1022, 137, 1, 3.25),
-- Order 1023 (Customer 14, Online)
(1023, 112, 1, 14.95),
(1023, 132, 1, 16.95),
(1023, 134, 1, 3.00),
(1023, 135, 1, 2.75),
-- Order 1024 (Customer 3, Dine-In)
(1024, 125, 1, 17.95),
(1024, 130, 1, 19.95),
(1024, 136, 1, 3.50),
(1024, 138, 1, 2.00),
-- Order 1025 (Customer 6, Online)
(1025, 113, 1, 5.00),
(1025, 114, 1, 9.00),
(1025, 135, 1, 2.75);

-- =============================================
-- Delivery
-- Only Online/Phone orders get deliveries.
-- Delivery boy area matches customer area.
-- =============================================
INSERT INTO Delivery (DeliveryID, OrderID, DeliveryBoyID, DeliveryTime, Status) VALUES
(1, 1002, 2, '2023-01-16 12:30:00', 'Delivered'),
(2, 1003, 3, '2023-01-18 13:15:00', 'Delivered'),
(3, 1004, 4, '2023-01-20 11:45:00', 'Delivered'),
(4, 1006, 1, '2023-01-25 14:00:00', 'Delivered'),
(5, 1007, 5, '2023-01-28 12:00:00', 'Delivered'),
(6, 1011, 3, '2023-02-08 13:30:00', 'In Transit'),
(7, 1014, 4, '2023-02-15 11:00:00', 'Delivered'),
(8, 1017, 1, '2023-02-22 12:45:00', 'Delivered'),
(9, 1018, 1, '2023-02-25 14:30:00', 'Pending'),
(10, 1025, 5, '2023-03-15 11:30:00', 'Delivered');

-- =============================================
-- Payment (one per order)
-- Amount = TotalAmount - Discount
-- =============================================
INSERT INTO Payment (PaymentID, OrderID, Amount, PaymentMethod, PaymentDate) VALUES
(1, 1001, 40.95, 'Credit Card', '2023-01-15'),
(2, 1002, 26.50, 'Online', '2023-01-16'),
(3, 1003, 32.95, 'Credit Card', '2023-01-18'),
(4, 1004, 22.25, 'Online', '2023-01-20'),
(5, 1005, 38.65, 'Cash', '2023-01-22'),
(6, 1006, 20.50, 'Online', '2023-01-25'),
(7, 1007, 27.90, 'Credit Card', '2023-01-28'),
(8, 1008, 34.45, 'Debit Card', '2023-02-01'),
(9, 1009, 44.35, 'Online', '2023-02-03'),
(10, 1010, 22.45, 'Credit Card', '2023-02-05'),
(11, 1011, 18.50, 'Cash', '2023-02-08'),
(12, 1012, 52.85, 'Credit Card', '2023-02-10'),
(13, 1013, 25.90, 'Debit Card', '2023-02-12'),
(14, 1014, 33.90, 'Online', '2023-02-15'),
(15, 1015, 20.00, 'Cash', '2023-02-18'),
(16, 1016, 37.15, 'Credit Card', '2023-02-20'),
(17, 1017, 31.25, 'Online', '2023-02-22'),
(18, 1018, 18.00, 'Debit Card', '2023-02-25'),
(19, 1019, 29.40, 'Cash', '2023-02-28'),
(20, 1020, 31.50, 'Credit Card', '2023-03-02'),
(21, 1021, 19.95, 'Credit Card', '2023-03-05'),
(22, 1022, 36.65, 'Online', '2023-03-08'),
(23, 1023, 33.65, 'Credit Card', '2023-03-10'),
(24, 1024, 38.40, 'Cash', '2023-03-12'),
(25, 1025, 16.75, 'Online', '2023-03-15');

-- =============================================
-- Reservation
-- =============================================
INSERT INTO Reservation (ReservationID, CustomerID, TableID, ReservationDate, ReservationTime, NumGuests, Status) VALUES
(1, 1, 3, '2023-01-15', '18:00:00', 3, 'Completed'),
(2, 3, 5, '2023-01-18', '19:00:00', 5, 'Completed'),
(3, 5, 7, '2023-01-22', '18:30:00', 7, 'Completed'),
(4, 7, 1, '2023-02-01', '19:30:00', 2, 'Completed'),
(5, 8, 4, '2023-02-03', '20:00:00', 3, 'Cancelled'),
(6, 10, 6, '2023-02-10', '18:00:00', 4, 'Completed'),
(7, 12, 2, '2023-02-18', '19:00:00', 2, 'Completed'),
(8, 14, 5, '2023-03-10', '20:30:00', 6, 'Confirmed');
