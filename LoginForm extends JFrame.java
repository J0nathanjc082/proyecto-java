import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class LoginForm extends JFrame {

    private JTextField txtNombre, txtUsuario;
    private JPasswordField txtPassword;

    public LoginForm() {
        setTitle("Login al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JLabel lblNombre = new JLabel("Nombre completo:");
        txtNombre = new JTextField(20);

        JLabel lblUsuario = new JLabel("Usuario MySQL:");
        txtUsuario = new JTextField(20);

        JLabel lblPassword = new JLabel("Contraseña MySQL:");
        txtPassword = new JPasswordField(20);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(e -> intentarLogin());

        JPanel panel = new JPanel(new GridLayout(4,2,5,5));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblUsuario);
        panel.add(txtUsuario);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel);
    }

    private void intentarLogin() {
        String nombre = txtNombre.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        Connection con = ConexionBD.conectarBD(usuario, password);
        if (con != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + nombre + "!");
            this.dispose();
            CandidatasForm form = new CandidatasForm(con);
            form.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Error de conexión. Verifique credenciales.");
        }
    }
}
