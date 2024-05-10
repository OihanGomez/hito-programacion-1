import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VisualizadorFotos extends JFrame {
    private JComboBox<String> comboBoxFotografos;
    private JXDatePicker datePicker;
    private JList<String> listaFotografias;
    private DefaultListModel<String> listModel;
    private JLabel imagenLabel;
    private Connection connection;
    private JButton premioButton;
    private JButton eliminarButton;

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

        // Agregar botones PREMIO y ELIMINAR
        premioButton = new JButton("PREMIO");
        eliminarButton = new JButton("ELIMINAR");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(premioButton);
        buttonPanel.add(eliminarButton);

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        add(buttonPanel);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        ConexionBD conexionBD = new ConexionBD();
        connection = conexionBD.getConexion();
        cargarFotografos();
        comboBoxFotografos.addActionListener(e -> cargarFotografias());
        datePicker.addActionListener(e -> cargarFotografias());

// Agregar ActionListener al botón PREMIO
        premioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int minVisitas = Integer.parseInt(JOptionPane.showInputDialog("Introduce el mínimo de visitas para premiar:"));
                marcarPremiados(minVisitas);
            }
        });




        // Agregar ActionListener al botón ELIMINAR
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarFotografiasNoMostradas();
                eliminarFotografosSinFotos();
            }
        });
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
    private void marcarPremiados(int minVisitas) {
        HashMap<Integer, Integer> visitsMap = createVisitsMap();

        String sql = "UPDATE Fotografos SET Premiado = true WHERE IdFotografo = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Integer idFotografo : visitsMap.keySet()) {
                int visitas = visitsMap.get(idFotografo);
                if (visitas >= minVisitas) {
                    preparedStatement.setInt(1, idFotografo);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    private HashMap<Integer, Integer> createVisitsMap() {
        HashMap<Integer, Integer> visitsMap = new HashMap<>();
        String sql = "SELECT IdFotografo, COUNT(*) AS Visitas FROM Fotografias GROUP BY IdFotografo";

        try (Statement connectionStatement = connection.createStatement();
             ResultSet resultSet = connectionStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                int idFotografo = resultSet.getInt("IdFotografo");
                int visitas = resultSet.getInt("Visitas");
                visitsMap.put(idFotografo, visitas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visitsMap;
    }

    private void eliminarFotografiasNoMostradas() {
        HashMap<Integer, Integer> visitsMap = createVisitsMap();

        String sql = "SELECT Titulo, IdFotografo FROM Fotografias WHERE Visitas = 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String titulo = resultSet.getString("Titulo");
                int idFotografo = resultSet.getInt("IdFotografo");
                if (!visitsMap.containsKey(idFotografo)) {
                    int respuesta = JOptionPane.showConfirmDialog(VisualizadorFotos.this,
                            "¿Desea eliminar la fotografía '" + titulo + "'?");
                    if (respuesta == JOptionPane.YES_OPTION) {
                        eliminarFotografia(titulo);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void eliminarFotografosSinFotos() {
        String sql = "DELETE FROM Fotografos WHERE IdFotografo NOT IN (SELECT DISTINCT IdFotografo FROM Fotografias)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(VisualizadorFotos.this, "Fotógrafos sin fotografías eliminados correctamente.");
                cargarFotografos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarFotografia(String tituloFotografia) {
        String sql = "DELETE FROM Fotografias WHERE Titulo = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, tituloFotografia);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(VisualizadorFotos.this, "Fotografía eliminada correctamente.");
                listModel.removeElement(tituloFotografia);
            } else {
                JOptionPane.showMessageDialog(VisualizadorFotos.this, "No se pudo eliminar la fotografía.");
            }
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
