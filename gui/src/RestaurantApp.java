
import javax.swing.*;

public class RestaurantApp {

    public static void main(String[] args) {


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found. Add postgresql-42.x.x.jar to your classpath.");
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}
