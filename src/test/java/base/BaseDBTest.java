package base;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDBTest {
	
	protected Connection connection;

    // Setup DB connection
    public void connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/databse_testing";
        String user = "root";
        String password = "Password";
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("✅ Database connected successfully!");
    }

    // Close DB connection
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("✅ Database disconnected.");
        }
    }

    public static void main(String[] args) {
    	try {
            BaseDBTest db = new BaseDBTest();
            db.connect();
            db.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
}
