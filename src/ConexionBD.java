
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // Atributos para la conexión
    static final String SERVER_IP = "127.0.0.1";
    static final String DB_NAME = "hito4";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://" + SERVER_IP + ":3306/" + DB_NAME;

    // Database credentials
    static final String USER = "root";
    static final String PASSWORD = "g0rkA1974";

    private Connection connection;

    // Constructor (establecer conexion)
    public ConexionBD() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para cerrar la conexión
    public void desconectar() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexión cerrada.");
        }
    }

    // Getter para obtener la conexión
    public Connection getConexion() {
        return connection;
    }
}