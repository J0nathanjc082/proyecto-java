import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ConexionBD {

    public static Connection conectarBD(String user, String pass) {
        String url = "jdbc:mysql://db4free.net:3306/festival_flores_?useSSL=false&serverTimezone=UTC";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            registrarIngreso(con, user);
            return con;
        } catch (Exception e) {
            System.out.println("Error conexión: " + e.getMessage());
            return null;
        }
    }

    private static void registrarIngreso(Connection con, String usuario) {
        String sql = "INSERT INTO log_ingresos (usuario, hora_ingreso) VALUES (?, NOW())";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.executeUpdate();
        } catch (Exception ignored) {
        }
    }
}
