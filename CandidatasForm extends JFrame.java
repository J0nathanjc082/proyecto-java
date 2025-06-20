import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CandidatasForm extends JFrame {

    private Connection con;
    private JTextField txtApellidos, txtNombres, txtEdad, txtSeccion, txtEspecialidad, txtVotos, txtDinero;
    private JTable tabla;
    private DefaultTableModel modelo;

    public CandidatasForm(Connection conexion) {
        this.con = conexion;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Registro de Candidatas");
        cargarDatosTabla();
    }

    private void initComponents() {
        txtApellidos = new JTextField(15);
        txtNombres = new JTextField(15);
        txtEdad = new JTextField(3);
        txtSeccion = new JTextField(5);
        txtEspecialidad = new JTextField(15);
        txtVotos = new JTextField(5);
        txtDinero = new JTextField(8);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnGuardar = new JButton("Guardar y Salir");

        modelo = new DefaultTableModel(new String[]{"ID","Apellidos","Nombres","Edad","Sección","Especialidad","Votos","Dinero"},0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelCampos = new JPanel(new GridLayout(7,2));
        panelCampos.add(new JLabel("Apellidos:")); panelCampos.add(txtApellidos);
        panelCampos.add(new JLabel("Nombres:")); panelCampos.add(txtNombres);
        panelCampos.add(new JLabel("Edad:")); panelCampos.add(txtEdad);
        panelCampos.add(new JLabel("Sección:")); panelCampos.add(txtSeccion);
        panelCampos.add(new JLabel("Especialidad:")); panelCampos.add(txtEspecialidad);
        panelCampos.add(new JLabel("Votos:")); panelCampos.add(txtVotos);
        panelCampos.add(new JLabel("Dinero:")); panelCampos.add(txtDinero);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGuardar);

        setLayout(new BorderLayout());
        add(panelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        btnAgregar.addActionListener(e -> agregarCandidata());
        btnModificar.addActionListener(e -> modificarCandidata());
        btnEliminar.addActionListener(e -> eliminarCandidata());
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,"Guardado y cerrando...");
            System.exit(0);
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                int fila = tabla.getSelectedRow();
                txtApellidos.setText(modelo.getValueAt(fila, 1).toString());
                txtNombres.setText(modelo.getValueAt(fila, 2).toString());
                txtEdad.setText(modelo.getValueAt(fila, 3).toString());
                txtSeccion.setText(modelo.getValueAt(fila, 4).toString());
                txtEspecialidad.setText(modelo.getValueAt(fila, 5).toString());
                txtVotos.setText(modelo.getValueAt(fila, 6).toString());
                txtDinero.setText(modelo.getValueAt(fila, 7).toString());
            }
        });
    }

    private void cargarDatosTabla() {
        modelo.setRowCount(0);
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM candidatas");
            while(rs.next()) {
                modelo.addRow(new Object[] {
                    rs.getInt("ID"),
                    rs.getString("Apellidos"),
                    rs.getString("Nombres"),
                    rs.getInt("Edad"),
                    rs.getString("Seccion"),
                    rs.getString("Especialidad"),
                    rs.getInt("Votos"),
                    rs.getDouble("Dinero_recaudado")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: "+e.getMessage());
        }
    }

    private void agregarCandidata() {
        try {
            String sql = "INSERT INTO candidatas (Apellidos, Nombres, Edad, Seccion, Especialidad, Votos, Dinero_recaudado) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtApellidos.getText());
            ps.setString(2, txtNombres.getText());
            ps.setInt(3, Integer.parseInt(txtEdad.getText()));
            ps.setString(4, txtSeccion.getText());
            ps.setString(5, txtEspecialidad.getText());
            ps.setInt(6, Integer.parseInt(txtVotos.getText()));
            ps.setDouble(7, Double.parseDouble(txtDinero.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Candidata agregada.");
            cargarDatosTabla();
            limpiarCampos();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage());
        }
    }

    private void modificarCandidata() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila primero.");
            return;
        }
        try {
            int id = (int) modelo.getValueAt(fila, 0);
            String sql = "UPDATE candidatas SET Apellidos=?, Nombres=?, Edad=?, Seccion=?, Especialidad=?, Votos=?, Dinero_recaudado=? WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtApellidos.getText());
            ps.setString(2, txtNombres.getText());
            ps.setInt(3, Integer.parseInt(txtEdad.getText()));
            ps.setString(4, txtSeccion.getText());
            ps.setString(5, txtEspecialidad.getText());
            ps.setInt(6, Integer.parseInt(txtVotos.getText()));
            ps.setDouble(7, Double.parseDouble(txtDinero.getText()));
            ps.setInt(8, id);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Candidata modificada.");
            cargarDatosTabla();
            limpiarCampos();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }

    private void eliminarCandidata() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila primero.");
            return;
        }
        try {
            int id = (int) modelo.getValueAt(fila, 0);
            String sql = "DELETE FROM candidatas WHERE ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Candidata eliminada.");
            cargarDatosTabla();
            limpiarCampos();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtApellidos.setText("");
        txtNombres.setText("");
        txtEdad.setText("");
        txtSeccion.setText("");
        txtEspecialidad.setText("");
        txtVotos.setText("");
        txtDinero.setText("");
    }
}
