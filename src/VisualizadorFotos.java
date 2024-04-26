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
    // Agrega un campo para la conexión a la base de datos
    private Connection connection;

    public VisualizadorFotos() {
        super("Photography");

        // Establecer el diseño de GridLayout con 3 filas y 1 columna
        setLayout(new GridLayout(2, 2));

        // Crear paneles para cada sección
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();


        // Inicializar el JComboBox para fotógrafos
        comboBoxFotografos = new JComboBox<>();
        panel1.add(comboBoxFotografos);

        // Panel 2: JXDatePicker
        datePicker = new JXDatePicker();
        panel2.add(datePicker);

        // Panel 3: JList
        listModel = new DefaultListModel<>();
        listaFotografias = new JList<>(listModel);
        panel3.add(new JScrollPane(listaFotografias));

        // Panel 4:
        imagenLabel = new JLabel();
        panel4.add(imagenLabel);


        // Agregar los paneles a la ventana
        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);

        // Configuración de la ventana
        setSize(400, 300); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true); // Hacer visible la ventana

        // Inicializar la conexión a la base de datos
        ConexionBD conexionBD = new ConexionBD();
        connection = conexionBD.getConexion();

        // Cargar los fotógrafos en el JComboBox al inicio
        cargarFotografos();

        // Agregar un listener al JComboBox para cargar las fotografías cuando se seleccione un fotógrafo
        comboBoxFotografos.addActionListener(e -> cargarFotografias());

        // Agregar un listener al JXDatePicker para cargar las fotografías cuando se seleccione una fecha
        datePicker.addActionListener(e -> cargarFotografias());
    }

    // Método para cargar los fotógrafos en el JComboBox
    private void cargarFotografos() {
        List<String> nombresFotografos = new ArrayList<>();

        // Consulta SQL para obtener todos los fotógrafos
        String sql = "SELECT Nombre FROM Fotografos";

        // Crear la declaración
        try (Statement stmt = connection.createStatement()) {
            // Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery(sql)) {
                // Iterar sobre los resultados y obtener los nombres de los fotógrafos
                while (rs.next()) {
                    nombresFotografos.add(rs.getString("Nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Limpiar el JComboBox antes de cargar los nombres de los fotógrafos
        comboBoxFotografos.removeAllItems();

        // Agregar cada nombre de fotógrafo al JComboBox
        for (String nombre : nombresFotografos) {
            comboBoxFotografos.addItem(nombre);
        }
    }

    // Método para cargar las fotografías en la lista
    private void cargarFotografias() {
        // Limpiar el modelo de la lista antes de cargar nuevas fotografías
        listModel.clear();

        // Obtener el fotógrafo seleccionado
        String fotografoSeleccionado = (String) comboBoxFotografos.getSelectedItem();

        // Obtener la fecha seleccionada
        java.util.Date fechaSeleccionada = datePicker.getDate();
        // Consulta SQL para obtener las fotografías del fotógrafo desde la fecha seleccionada
        String sql = "SELECT Titulo, Archivo FROM Fotografias f " +
                "JOIN Fotografos ft ON f.IdFotografo = ft.IdFotografo " +
                "WHERE ft.Nombre = ?";

        // Crear la declaración
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the first parameter which is the photographer's name
            pstmt.setString(1, fotografoSeleccionado);

            // If a date is selected, set the second parameter
            if (fechaSeleccionada != null) {
                // Increment the parameter index
                sql += " AND Fecha >= ?";
            }

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Iterar sobre los resultados y agregar los títulos de las fotografías al modelo de la lista
                while (rs.next()) {
                    listModel.addElement(rs.getString("Titulo"));
                    // Incrementar el contador de visitas para la fotografía actual
                    incrementarVisitas(rs.getString("Titulo"));
                    // Obtener la ruta de la imagen desde la base de datos
                    String rutaImagen = rs.getString("Archivo");
                    // Crear un ImageIcon desde la ruta de la imagen
                    ImageIcon imagenIcon = new ImageIcon(rutaImagen);
                    // Escalar la imagen para que se ajuste al tamaño del JLabel
                    Image imagen = imagenIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imagenIcon = new ImageIcon(imagen);
                    // Asignar la imagen al JLabel
                    imagenLabel.setIcon(imagenIcon);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para incrementar el contador de visitas para una fotografía
    private void incrementarVisitas(String tituloFotografia) {
        // Consulta SQL para incrementar el contador de visitas para la fotografía
        String sql = "UPDATE Fotografias SET Visitas = Visitas + 1 WHERE Titulo = ?";
        // Crear la declaración
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Establecer el parámetro
            pstmt.setString(1, tituloFotografia);
            // Ejecutar la actualización
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
