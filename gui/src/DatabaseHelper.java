import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseHelper {

    // =============================================
    // Database connection settings
    // Update these to match your local PostgreSQL setup before running
    // =============================================
    private static final String DB_HOST = "your_host";          // e.g. "localhost" or your DB server hostname
    private static final String DB_PORT = "5432";               // default PostgreSQL port
    private static final String DB_NAME = "restaurant_db";      // matches the `createdb` command in README
    private static final String DB_USER = "your_username";      // TODO: replace with your Postgres username
    private static final String DB_PASS = "your_password";      // TODO: replace with your Postgres password

    private static final String JDBC_URL =
            "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }

    public static boolean validateLogin(String username, String password) throws SQLException {
        String sql = "SELECT 1 FROM UserAccount WHERE Username = ? AND Password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

 
    public static DefaultTableModel getAllCustomers() throws SQLException {
        String[] columns = {"CustomerID", "FirstName", "LastName", "Phone", "Email", "AreaCode", "JoinDate", "IsPremium"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        String sql = "SELECT CustomerID, FirstName, LastName, Phone, Email, AreaCode, JoinDate, IsPremium " +
                "FROM Customer ORDER BY CustomerID";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("CustomerID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("AreaCode"),
                        rs.getDate("JoinDate"),
                        rs.getString("IsPremium")
                });
            }
        }

        return model;
    }

    public static DefaultTableModel searchCustomers(String keyword) throws SQLException {
        String[] columns = {"CustomerID", "FirstName", "LastName", "Phone", "Email", "AreaCode", "JoinDate", "IsPremium"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        String sql = "SELECT CustomerID, FirstName, LastName, Phone, Email, AreaCode, JoinDate, IsPremium " +
                "FROM Customer " +
                "WHERE CAST(CustomerID AS TEXT) ILIKE ? " +
                "   OR FirstName ILIKE ? " +
                "   OR LastName ILIKE ? " +
                "   OR Phone ILIKE ? " +
                "   OR Email ILIKE ? " +
                "   OR AreaCode ILIKE ? " +
                "ORDER BY CustomerID";

        String searchText = "%" + keyword + "%";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchText);
            pstmt.setString(2, searchText);
            pstmt.setString(3, searchText);
            pstmt.setString(4, searchText);
            pstmt.setString(5, searchText);
            pstmt.setString(6, searchText);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("CustomerID"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getString("AreaCode"),
                            rs.getDate("JoinDate"),
                            rs.getString("IsPremium")
                    });
                }
            }
        }

        return model;
    }

    public static void addCustomer(int id, String first, String last, String phone,
                                   String email, String street, String areaCode,
                                   Date joinDate, String isPremium) throws SQLException {

        String sql = "INSERT INTO Customer (CustomerID, FirstName, LastName, Phone, Email, Street, AreaCode, JoinDate, IsPremium) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, first);
            pstmt.setString(3, last);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setString(6, street);
            pstmt.setString(7, areaCode);
            pstmt.setDate(8, joinDate);
            pstmt.setString(9, isPremium);

            pstmt.executeUpdate();
        }
    }

    public static void updateCustomer(int id, String first, String last, String phone,
                                      String email, String isPremium) throws SQLException {

        String sql = "UPDATE Customer " +
                "SET FirstName=?, LastName=?, Phone=?, Email=?, IsPremium=? " +
                "WHERE CustomerID=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, first);
            pstmt.setString(2, last);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, isPremium);
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
        }
    }

    public static void deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE CustomerID=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }


    public static DefaultTableModel getAllMenuItems() throws SQLException {
        String[] columns = {"ItemID", "ItemName", "Category", "Price", "IsAvailable"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        String sql = "SELECT ItemID, ItemName, Category, Price, IsAvailable " +
                "FROM MenuItem ORDER BY ItemID";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ItemID"),
                        rs.getString("ItemName"),
                        rs.getString("Category"),
                        rs.getDouble("Price"),
                        rs.getString("IsAvailable")
                });
            }
        }

        return model;
    }

    public static DefaultTableModel searchMenuItems(String keyword) throws SQLException {
        String[] columns = {"ItemID", "ItemName", "Category", "Price", "IsAvailable"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        String sql = "SELECT ItemID, ItemName, Category, Price, IsAvailable " +
                "FROM MenuItem " +
                "WHERE CAST(ItemID AS TEXT) ILIKE ? " +
                "   OR ItemName ILIKE ? " +
                "   OR Category ILIKE ? " +
                "   OR IsAvailable ILIKE ? " +
                "ORDER BY ItemID";

        String searchText = "%" + keyword + "%";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchText);
            pstmt.setString(2, searchText);
            pstmt.setString(3, searchText);
            pstmt.setString(4, searchText);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("ItemID"),
                            rs.getString("ItemName"),
                            rs.getString("Category"),
                            rs.getDouble("Price"),
                            rs.getString("IsAvailable")
                    });
                }
            }
        }

        return model;
    }

    public static void addMenuItem(int id, String name, String category,
                                   double price, String isAvailable) throws SQLException {

        String sql = "INSERT INTO MenuItem (ItemID, ItemName, Category, Price, IsAvailable) " +
                "VALUES (?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, category);
            pstmt.setDouble(4, price);
            pstmt.setString(5, isAvailable);

            pstmt.executeUpdate();
        }
    }

    public static void updateMenuItem(int id, String name, String category,
                                      double price, String isAvailable) throws SQLException {

        String sql = "UPDATE MenuItem " +
                "SET ItemName=?, Category=?, Price=?, IsAvailable=? " +
                "WHERE ItemID=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setDouble(3, price);
            pstmt.setString(4, isAvailable);
            pstmt.setInt(5, id);

            pstmt.executeUpdate();
        }
    }

    public static void deleteMenuItem(int id) throws SQLException {
        String sql = "DELETE FROM MenuItem WHERE ItemID=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static String getMenuItemPriceStats() throws SQLException {
        String sql = "SELECT MAX(Price) AS max_price, MIN(Price) AS min_price, AVG(Price) AS avg_price " +
                "FROM MenuItem";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                double max = rs.getDouble("max_price");
                double min = rs.getDouble("min_price");
                double avg = rs.getDouble("avg_price");

                return "Max Price: $" + String.format("%.2f", max) +
                        "\nMin Price: $" + String.format("%.2f", min) +
                        "\nAverage Price: $" + String.format("%.2f", avg);
            }
        }

        return "No menu item data found.";
    }

   
    public static DefaultTableModel getAllOrders() throws SQLException {
        String[] columns = {"OrderID", "CustomerName", "OrderDate", "OrderType", "TotalAmount", "Discount"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        String sql = "SELECT O.OrderID, C.FirstName || ' ' || C.LastName AS CustomerName, " +
                "O.OrderDate, O.OrderType, O.TotalAmount, O.Discount " +
                "FROM Orders O " +
                "JOIN Customer C ON C.CustomerID = O.CustomerID " +
                "ORDER BY O.OrderID";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("OrderID"),
                        rs.getString("CustomerName"),
                        rs.getDate("OrderDate"),
                        rs.getString("OrderType"),
                        rs.getDouble("TotalAmount"),
                        rs.getDouble("Discount")
                });
            }
        }

        return model;
    }

    public static String getOrderAmountStats() throws SQLException {
        String sql = "SELECT MAX(TotalAmount) AS max_total, MIN(TotalAmount) AS min_total, AVG(TotalAmount) AS avg_total " +
                "FROM Orders";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                double max = rs.getDouble("max_total");
                double min = rs.getDouble("min_total");
                double avg = rs.getDouble("avg_total");

                return "Max Order Total: $" + String.format("%.2f", max) +
                        "\nMin Order Total: $" + String.format("%.2f", min) +
                        "\nAverage Order Total: $" + String.format("%.2f", avg);
            }
        }

        return "No order data found.";
    }
}