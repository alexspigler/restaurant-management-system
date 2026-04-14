

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JButton        btnLogin;

    public LoginForm() {
        setTitle("Login");
        setSize(300, 200);

        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;


        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0;
        panel.add(new JLabel("UserName"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtUsername = new JTextField(15);
        panel.add(txtUsername, gbc);

        
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0;
        panel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPassword = new JPasswordField(15);
        panel.add(txtPassword, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill  = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        btnLogin = new JButton("Login");
        panel.add(btnLogin, gbc);

        txtPassword.addActionListener(e -> onLogin());
        btnLogin.addActionListener(e -> onLogin());

        add(panel);
    }

    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean valid = DatabaseHelper.validateLogin(username, password);
            if (valid) {
                RestaurantForm mainForm = new RestaurantForm();
                mainForm.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                //https://learn.microsoft.com/en-us/dotnet/api/android.views.view.requestfocus?view=net-android-35.0
                txtPassword.setText("");
                txtUsername.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
