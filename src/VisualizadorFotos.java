import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisualizadorFotos extends JFrame {
    private JComboBox<String> comboBoxFotografos;
    private JXDatePicker datePicker;
    private JList<String> listaFotografias;
    private DefaultListModel<String> listModel;
    private JLabel imagenLabel;
    private Connection connection;

    public VisualizadorFotos() {
        super("Photography");
        setLayout(new GridLayout(2, 2));
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        comboBoxFotografos = new JComboBox<>();
        panel1.add(comboBoxFotografos);
        datePicker = new JXDatePicker();
        panel2.add(datePicker);
        listModel = new DefaultListModel<>();
        listaFotografias = new JList<>(listModel);
        panel3.add(new JScrollPane(listaFotografias));
        imagenLabel = new JLabel();
        panel4.add(imagenLabel);
        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        ConexionBD conexionBD = new ConexionBD();
        connection = conexionBD.getConexion();
        cargarFotografos();
        comboBoxFotografos.addActionListener(e -> cargarFotografias());
        datePicker.addActionListener(e -> cargarFotografias());
    }

    private void cargarFotografos() {
        List<String> nombresFotografos = new ArrayList<>();
        String sql = "SELECT Nombre FROM Fotografos";
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    nombresFotografos.add(rs.getString("Nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBoxFotografos.removeAllItems();
        for (String nombre : nombresFotografos) {
            comboBoxFotografos.addItem(nombre);
        }
    }

    private void cargarFotografias() {
        listModel.clear();
        String fotografoSeleccionado = (String) comboBoxFotografos.getSelectedItem();
        java.util.Date fechaSeleccionada = datePicker.getDate();
        String sql = "SELECT Titulo, Archivo FROM Fotografias f " +
                "JOIN Fotografos ft ON f.IdFotografo = ft.IdFotografo " +
                "WHERE ft.Nombre = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, fotografoSeleccionado);
            if (fechaSeleccionada != null) {
                sql += " AND Fecha >= ?";
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    listModel.addElement(rs.getString("Titulo"));
                    incrementarVisitas(rs.getString("Titulo"));
                    String rutaImagen = rs.getString("Archivo");
                    ImageIcon imagenIcon = new ImageIcon(rutaImagen);
                    Image imagen = imagenIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imagenIcon = new ImageIcon(imagen);
                    imagenLabel.setIcon(imagenIcon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void incrementarVisitas(String tituloFotografia) {
        String sql = "UPDATE Fotografias SET Visitas = Visitas + 1 WHERE Titulo = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, tituloFotografia);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VisualizadorFotos();
        });
    }
}
