
import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class RestaurantForm extends JFrame {

    
    private JTextField txtCustID;
    private JTextField txtCustFirst;
    private JTextField txtCustLast;
    private JTextField txtCustPhone;
    private JTextField txtCustEmail;
    private JTextField txtCustArea;
    private JComboBox<String> cmbCustPremium;
    private JTextField txtCustSearch;
    private JTable     tblCustomers;

    
    private int selectedCustomerID = -1;

    private JTextField txtMenuID;
    private JTextField txtMenuName;
    private JComboBox<String> cmbMenuCategory;
    private JTextField txtMenuPrice;
    private JComboBox<String> cmbMenuAvailable;
    private JTextField txtMenuSearch;
    private JTable     tblMenu;

    
    private int selectedMenuID = -1;

    
    private JTable tblOrders;

    
    public RestaurantForm() {
        setTitle("Restaurant Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //https://docs.oracle.com/javase/8/docs/api/javax/swing/JTabbedPane.html
        ////^documentation for tabbedPane
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Customers",  buildCustomerPanel());
        tabs.addTab("Menu Items", buildMenuPanel());
        tabs.addTab("Orders",     buildOrderPanel());
        add(tabs);
    }
    //returns JPanel because without the JPanel being retuned I would not be able to addtab() which adds complexity to the app. 
    //read through documentation for more information 
    private JPanel buildCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCustID     = new JTextField(5);
        txtCustFirst  = new JTextField(10);
        txtCustLast   = new JTextField(10);
        txtCustPhone  = new JTextField(12);
        txtCustEmail  = new JTextField(15);
        txtCustArea   = new JTextField(6);
        cmbCustPremium = new JComboBox<>(new String[]{"Yes", "No"});
        txtCustSearch = new JTextField(15);

        JButton btnAdd         = new JButton("Add");
        JButton btnUpdate      = new JButton("Update");
        JButton btnDelete      = new JButton("Delete");
        JButton btnRefresh     = new JButton("Refresh");
        JButton btnSearch      = new JButton("Search");
        JButton btnClearSearch = new JButton("Clear Search");

        btnDelete.setBackground(new Color(255, 200, 200));
        btnDelete.setOpaque(true);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0; inputPanel.add(new JLabel("ID"),        gbc);
        gbc.gridx = 1; inputPanel.add(new JLabel("First Name"), gbc);
        gbc.gridx = 2; inputPanel.add(new JLabel("Last Name"),  gbc);
        gbc.gridx = 3; inputPanel.add(new JLabel("Phone"),      gbc);
        gbc.gridx = 4; inputPanel.add(new JLabel("Email"),      gbc);
        gbc.gridx = 5; inputPanel.add(new JLabel("Area Code"),  gbc);
        gbc.gridx = 6; inputPanel.add(new JLabel("Premium"),    gbc);
        gbc.gridy = 1;
        gbc.gridx = 0; inputPanel.add(txtCustID,      gbc);
        gbc.gridx = 1; inputPanel.add(txtCustFirst,   gbc);
        gbc.gridx = 2; inputPanel.add(txtCustLast,    gbc);
        gbc.gridx = 3; inputPanel.add(txtCustPhone,   gbc);
        gbc.gridx = 4; inputPanel.add(txtCustEmail,   gbc);
        gbc.gridx = 5; inputPanel.add(txtCustArea,    gbc);
        gbc.gridx = 6; inputPanel.add(cmbCustPremium, gbc);

        
        gbc.gridy = 2;
        gbc.gridx = 0; inputPanel.add(btnAdd,           gbc);
        gbc.gridx = 1; inputPanel.add(btnUpdate,         gbc);
        gbc.gridx = 2; inputPanel.add(btnDelete,         gbc);
        gbc.gridx = 3; inputPanel.add(btnRefresh,        gbc);
        gbc.gridx = 4; inputPanel.add(new JLabel("Keyword"), gbc);
        gbc.gridx = 5; inputPanel.add(txtCustSearch,     gbc);
        gbc.gridx = 6; inputPanel.add(btnSearch,         gbc);
        gbc.gridx = 7; inputPanel.add(btnClearSearch,    gbc);
        //from build table () code 
        tblCustomers = new JTable();
        tblCustomers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCustomers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()){
                onCustomerRowSelected();
            } 
                
        });

        btnAdd.addActionListener(e        -> onAddCustomer());
        btnUpdate.addActionListener(e     -> onUpdateCustomer());
        btnDelete.addActionListener(e     -> onDeleteCustomer());
        btnRefresh.addActionListener(e    -> loadCustomers());
        btnSearch.addActionListener(e     -> onSearchCustomers());
        btnClearSearch.addActionListener(e -> {
            txtCustSearch.setText("");
            loadCustomers();
        });

        panel.add(inputPanel,                   BorderLayout.NORTH);
        panel.add(new JScrollPane(tblCustomers), BorderLayout.CENTER);
        loadCustomers();
        return panel;
    }
    private void loadCustomers() {
        try {
            tblCustomers.setModel(DatabaseHelper.getAllCustomers());
            clearCustomerFields();
        } catch (Exception ex) {
            showError("Error loading customers: " + ex.getMessage());
        }
    }

    private void onCustomerRowSelected() {
        int viewRow = tblCustomers.getSelectedRow();
        if (viewRow == -1) return;

        int modelRow = tblCustomers.convertRowIndexToModel(viewRow);

        selectedCustomerID = (int) tblCustomers.getModel().getValueAt(modelRow, 0);
        txtCustID.setText(String.valueOf(selectedCustomerID));
        txtCustFirst.setText((String) tblCustomers.getModel().getValueAt(modelRow, 1));
        txtCustLast.setText( (String) tblCustomers.getModel().getValueAt(modelRow, 2));
        txtCustPhone.setText((String) tblCustomers.getModel().getValueAt(modelRow, 3));

        Object email = tblCustomers.getModel().getValueAt(modelRow, 4);
        txtCustEmail.setText(email != null ? email.toString() : "");

        txtCustArea.setText((String) tblCustomers.getModel().getValueAt(modelRow, 5));
        cmbCustPremium.setSelectedItem((String) tblCustomers.getModel().getValueAt(modelRow, 7));
    }
    private void onAddCustomer() {
        try {
            DatabaseHelper.addCustomer(
                    Integer.parseInt(txtCustID.getText().trim()),
                    txtCustFirst.getText().trim(),
                    txtCustLast.getText().trim(),
                    txtCustPhone.getText().trim(),
                    txtCustEmail.getText().trim(),
                    "",
                    txtCustArea.getText().trim(),
                    new Date(System.currentTimeMillis()),
                    cmbCustPremium.getSelectedItem().toString()
            );
            loadCustomers();
            JOptionPane.showMessageDialog(this, "Customer added.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error adding customer: " + ex.getMessage());
        }
    }

    //exact code from the onUpdate
    private void onUpdateCustomer() {
        if (selectedCustomerID == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DatabaseHelper.updateCustomer(
                    selectedCustomerID,
                    txtCustFirst.getText().trim(),
                    txtCustLast.getText().trim(),
                    txtCustPhone.getText().trim(),
                    txtCustEmail.getText().trim(),
                    cmbCustPremium.getSelectedItem().toString()
            );
            loadCustomers();
            JOptionPane.showMessageDialog(this, "Customer updated.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error updating customer: " + ex.getMessage());
        }
    }
//exact same code from ondelete()
    private void onDeleteCustomer() {
        if (selectedCustomerID == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this customer permanently?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            DatabaseHelper.deleteCustomer(selectedCustomerID);
            loadCustomers();
            JOptionPane.showMessageDialog(this, "Customer deleted.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error deleting customer: " + ex.getMessage());
        }
    }
    private void onSearchCustomers() {
        try {
            tblCustomers.setModel(DatabaseHelper.searchCustomers(txtCustSearch.getText().trim()));
        } catch (Exception ex) {
            showError("Error searching customers: " + ex.getMessage());
        }
    }


    private void clearCustomerFields() {
        txtCustID.setText("");
        txtCustFirst.setText("");
        txtCustLast.setText("");
        txtCustPhone.setText("");
        txtCustEmail.setText("");
        txtCustArea.setText("");
        cmbCustPremium.setSelectedIndex(0);
        selectedCustomerID = -1;
    }

  
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     
        txtMenuID       = new JTextField(5);
        txtMenuName     = new JTextField(12);
        cmbMenuCategory = new JComboBox<>(new String[]{"American", "Asian", "Mexican", "Italian", "Beverage"});
        txtMenuPrice    = new JTextField(8);
        cmbMenuAvailable = new JComboBox<>(new String[]{"Yes", "No"});
        txtMenuSearch   = new JTextField(15);


        JButton btnAdd    = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnSearch = new JButton("Search");
        JButton btnClear  = new JButton("Clear Search");
        JButton btnStats  = new JButton("Price Stats");

     
        btnDelete.setBackground(new Color(255, 200, 200));
        btnDelete.setOpaque(true);

      
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        String[]     labels = {"Item ID", "Item Name", "Category", "Price", "Available"};
        JComponent[] fields = {txtMenuID, txtMenuName, cmbMenuCategory, txtMenuPrice, cmbMenuAvailable};

       
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = 0; gbc.gridx = i; inputPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridy = 1; gbc.gridx = i; inputPanel.add(fields[i],             gbc);
        }

       
        gbc.gridy = 2;
        gbc.gridx = 0; inputPanel.add(btnAdd,    gbc);
        gbc.gridx = 1; inputPanel.add(btnUpdate, gbc);
        gbc.gridx = 2; inputPanel.add(btnDelete, gbc);
        gbc.gridx = 3; inputPanel.add(btnRefresh, gbc);
        gbc.gridx = 4; inputPanel.add(btnStats,  gbc);

        
        gbc.gridy   = 3;
        gbc.gridx   = 0; gbc.weightx = 0;
        inputPanel.add(new JLabel("Keyword Search"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 4; gbc.weightx = 1.0;
        txtMenuSearch.setPreferredSize(new Dimension(300, 28));
        inputPanel.add(txtMenuSearch, gbc);

        gbc.gridx = 5; gbc.gridwidth = 1; gbc.weightx = 0;
        inputPanel.add(btnSearch, gbc);

        gbc.gridx = 6;
        inputPanel.add(btnClear, gbc);

        tblMenu = new JTable();
        tblMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // When the user clicks a row, populate the text fields.
        tblMenu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onMenuRowSelected();
        });

        btnAdd.addActionListener(e    -> onAddMenuItem());
        btnUpdate.addActionListener(e -> onUpdateMenuItem());
        btnDelete.addActionListener(e -> onDeleteMenuItem());
        btnRefresh.addActionListener(e -> loadMenuItems());
        btnSearch.addActionListener(e -> onSearchMenuItems());
        btnStats.addActionListener(e  -> onShowMenuStats());
        btnClear.addActionListener(e  -> {
            txtMenuSearch.setText("");
            loadMenuItems();
        });

        panel.add(inputPanel,               BorderLayout.NORTH);
        panel.add(new JScrollPane(tblMenu), BorderLayout.CENTER);

        loadMenuItems();
        return panel;
    }


    private void loadMenuItems() {
        try {
            tblMenu.setModel(DatabaseHelper.getAllMenuItems());
            clearMenuFields();
        } catch (Exception ex) {
            showError("Error loading menu items: " + ex.getMessage());
        }
    }


    private void onMenuRowSelected() {
        int viewRow = tblMenu.getSelectedRow();
        if (viewRow == -1) return;

        int modelRow = tblMenu.convertRowIndexToModel(viewRow);

        selectedMenuID = (int) tblMenu.getModel().getValueAt(modelRow, 0);
        txtMenuID.setText(String.valueOf(selectedMenuID));
        txtMenuName.setText((String) tblMenu.getModel().getValueAt(modelRow, 1));
        cmbMenuCategory.setSelectedItem((String) tblMenu.getModel().getValueAt(modelRow, 2));
        txtMenuPrice.setText(String.valueOf(tblMenu.getModel().getValueAt(modelRow, 3)));
        cmbMenuAvailable.setSelectedItem((String) tblMenu.getModel().getValueAt(modelRow, 4));
    }

    private void onAddMenuItem() {
        try {
            DatabaseHelper.addMenuItem(
                    Integer.parseInt(txtMenuID.getText().trim()),
                    txtMenuName.getText().trim(),
                    cmbMenuCategory.getSelectedItem().toString(),
                    Double.parseDouble(txtMenuPrice.getText().trim()),
                    cmbMenuAvailable.getSelectedItem().toString()
            );
            loadMenuItems();
            JOptionPane.showMessageDialog(this, "Menu item added.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error adding menu item: " + ex.getMessage());
        }
    }

   
    private void onUpdateMenuItem() {
        if (selectedMenuID == -1) {
            JOptionPane.showMessageDialog(this, "Select a menu item first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DatabaseHelper.updateMenuItem(
                    selectedMenuID,
                    txtMenuName.getText().trim(),
                    cmbMenuCategory.getSelectedItem().toString(),
                    Double.parseDouble(txtMenuPrice.getText().trim()),
                    cmbMenuAvailable.getSelectedItem().toString()
            );
            loadMenuItems();
            JOptionPane.showMessageDialog(this, "Menu item updated.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error updating menu item: " + ex.getMessage());
        }
    }

   
    private void onDeleteMenuItem() {
        if (selectedMenuID == -1) {
            JOptionPane.showMessageDialog(this, "Select a menu item first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this menu item permanently?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            DatabaseHelper.deleteMenuItem(selectedMenuID);
            loadMenuItems();
            JOptionPane.showMessageDialog(this, "Menu item deleted.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error deleting menu item: " + ex.getMessage());
        }
    }

    private void onSearchMenuItems() {
        try {
            tblMenu.setModel(DatabaseHelper.searchMenuItems(txtMenuSearch.getText().trim()));
        } catch (Exception ex) {
            showError("Error searching menu items: " + ex.getMessage());
        }
    }

    private void onShowMenuStats() {
        try {
            JOptionPane.showMessageDialog(this,
                    DatabaseHelper.getMenuItemPriceStats(),
                    "Menu Item Statistics",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error loading menu stats: " + ex.getMessage());
        }
    }

    private void clearMenuFields() {
        txtMenuID.setText("");
        txtMenuName.setText("");
        cmbMenuCategory.setSelectedIndex(0);
        txtMenuPrice.setText("");
        cmbMenuAvailable.setSelectedIndex(0);
        selectedMenuID = -1;
    }

   
    private JPanel buildOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tblOrders = new JTable();
        tblOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton btnRefresh = new JButton("Refresh");
        JButton btnStats   = new JButton("Order Stats");

        btnRefresh.addActionListener(e -> loadOrders());
        btnStats.addActionListener(e   -> onShowOrderStats());
        //https://docs.oracle.com/javase/8/docs/api/java/awt/FlowLayout.html
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(btnRefresh);
        top.add(btnStats);

        panel.add(top,                       BorderLayout.NORTH);
        panel.add(new JScrollPane(tblOrders), BorderLayout.CENTER);

        loadOrders();
        return panel;
    }


    private void loadOrders() {
        try {
            tblOrders.setModel(DatabaseHelper.getAllOrders());
        } catch (Exception ex) {
            showError("Error loading orders: " + ex.getMessage());
        }
    }

   
    private void onShowOrderStats() {
        try {
            JOptionPane.showMessageDialog(this,
                    DatabaseHelper.getOrderAmountStats(),
                    "Order Statistics",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Error loading order stats: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
