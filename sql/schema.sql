-- =============================================
-- Restaurant Management Database System
-- Schema Definition (PostgreSQL)
-- =============================================

-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS OrderItem;
DROP TABLE IF EXISTS Delivery;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS DeliveryBoy;
DROP TABLE IF EXISTS MenuItem;
DROP TABLE IF EXISTS DineInTable;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Area;
DROP TABLE IF EXISTS UserAccount;

-- =============================================
-- UserAccount: GUI login authentication
-- =============================================
CREATE TABLE UserAccount (
    UserID INT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(100) NOT NULL,
    Role VARCHAR(20) NOT NULL
);

-- =============================================
-- Area: geographic delivery regions
-- =============================================
CREATE TABLE Area (
    AreaCode VARCHAR(10) PRIMARY KEY,
    AreaName VARCHAR(100) NOT NULL,
    City VARCHAR(100) NOT NULL
);

-- =============================================
-- Customer
-- =============================================
CREATE TABLE Customer (
    CustomerID INT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Phone VARCHAR(20) NOT NULL,
    Email VARCHAR(200) UNIQUE,
    Street VARCHAR(200),
    AreaCode VARCHAR(10) NOT NULL,
    JoinDate DATE NOT NULL,
    IsPremium VARCHAR(3) NOT NULL CHECK (IsPremium IN ('Yes', 'No')),
    FOREIGN KEY (AreaCode) REFERENCES Area(AreaCode)
);

-- =============================================
-- MenuItem: restaurant catalog
-- Category matches real cuisine types from the
-- underlying dataset, plus 'Beverage' for drinks.
-- =============================================
CREATE TABLE MenuItem (
    ItemID INT PRIMARY KEY,
    ItemName VARCHAR(100) NOT NULL,
    Category VARCHAR(20) NOT NULL CHECK (Category IN ('American', 'Asian', 'Mexican', 'Italian', 'Beverage')),
    Description VARCHAR(500),
    Price DECIMAL(10, 2) NOT NULL CHECK (Price > 0),
    IsAvailable VARCHAR(3) NOT NULL CHECK (IsAvailable IN ('Yes', 'No'))
);

-- =============================================
-- DineInTable: physical restaurant tables
-- =============================================
CREATE TABLE DineInTable (
    TableID INT PRIMARY KEY,
    Capacity INT NOT NULL CHECK (Capacity > 0),
    Location VARCHAR(50) NOT NULL
);

-- =============================================
-- DeliveryBoy: delivery personnel
-- UNIQUE on AreaCode enforces one-to-one with Area
-- =============================================
CREATE TABLE DeliveryBoy (
    DeliveryBoyID INT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Phone VARCHAR(20) NOT NULL,
    AreaCode VARCHAR(10) NOT NULL UNIQUE,
    FOREIGN KEY (AreaCode) REFERENCES Area(AreaCode)
);

-- =============================================
-- Orders: customer orders
-- (named 'Orders' to avoid SQL reserved word 'Order')
-- =============================================
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY,
    CustomerID INT NOT NULL,
    OrderDate DATE NOT NULL,
    OrderType VARCHAR(10) NOT NULL CHECK (OrderType IN ('Dine-In', 'Online', 'Phone')),
    TotalAmount DECIMAL(10, 2) NOT NULL CHECK (TotalAmount >= 0),
    Discount DECIMAL(10, 2) CHECK (Discount >= 0),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- =============================================
-- OrderItem: line items within an order (weak entity)
-- Composite PK (OrderID, ItemID)
-- =============================================
CREATE TABLE OrderItem (
    OrderID INT NOT NULL,
    ItemID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    UnitPrice DECIMAL(10, 2) NOT NULL CHECK (UnitPrice > 0),
    PRIMARY KEY (OrderID, ItemID),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE,
    FOREIGN KEY (ItemID) REFERENCES MenuItem(ItemID)
);

-- =============================================
-- Delivery: tracks order deliveries
-- =============================================
CREATE TABLE Delivery (
    DeliveryID INT PRIMARY KEY,
    OrderID INT NOT NULL UNIQUE,
    DeliveryBoyID INT NOT NULL,
    DeliveryTime TIMESTAMP,
    Status VARCHAR(20) NOT NULL CHECK (Status IN ('Pending', 'In Transit', 'Delivered', 'Cancelled')),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE,
    FOREIGN KEY (DeliveryBoyID) REFERENCES DeliveryBoy(DeliveryBoyID)
);

-- =============================================
-- Payment: payment transactions
-- =============================================
CREATE TABLE Payment (
    PaymentID INT PRIMARY KEY,
    OrderID INT NOT NULL UNIQUE,
    Amount DECIMAL(10, 2) NOT NULL CHECK (Amount > 0),
    PaymentMethod VARCHAR(20) NOT NULL CHECK (PaymentMethod IN ('Cash', 'Credit Card', 'Debit Card', 'Online')),
    PaymentDate DATE NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE
);

-- =============================================
-- Reservation: dine-in table reservations
-- =============================================
CREATE TABLE Reservation (
    ReservationID INT PRIMARY KEY,
    CustomerID INT NOT NULL,
    TableID INT NOT NULL,
    ReservationDate DATE NOT NULL,
    ReservationTime TIME NOT NULL,
    NumGuests INT NOT NULL CHECK (NumGuests > 0),
    Status VARCHAR(20) NOT NULL CHECK (Status IN ('Confirmed', 'Cancelled', 'Completed')),
    UNIQUE (TableID, ReservationDate, ReservationTime),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (TableID) REFERENCES DineInTable(TableID)
);

