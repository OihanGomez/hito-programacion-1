import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class VisorArchivos extends JFrame {
    private JComboBox<String> comboBoxArchivos;
    private JTextArea areaTextoContenido;

    public VisorArchivos() {
        setTitle("Test Event Files");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 500));
        setVisible(true);

        comboBoxArchivos = new JComboBox<>(new String[]{"python.txt", "c.txt", "java.txt"});
        areaTextoContenido = new JTextArea();
        JButton botonLimpiar = new JButton("Clear");
        botonLimpiar.setPreferredSize(new Dimension(50, 50)); // Establecer el tamaño del botón

        JPanel panel = new JPanel(new GridLayout(1, 2));

        JPanel panelIzquierda = new JPanel(new BorderLayout());
        panelIzquierda.add(comboBoxArchivos, BorderLayout.NORTH);
        panelIzquierda.add(botonLimpiar, BorderLayout.SOUTH);

        panel.add(panelIzquierda);

        panel.add(new JScrollPane(areaTextoContenido));

        add(panel);

        comboBoxArchivos.addActionListener(e -> cargarContenidoArchivo());

        botonLimpiar.addActionListener(e -> areaTextoContenido.setText(""));
        pack();

    }

    private void cargarContenidoArchivo() {
        String archivoSeleccionado = (String) comboBoxArchivos.getSelectedItem();
        try {
            FileInputStream entradaArchivo = new FileInputStream("src/textos/" + archivoSeleccionado);
            BufferedReader lectorBuffer = new BufferedReader(new InputStreamReader(entradaArchivo));
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = lectorBuffer.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            lectorBuffer.close();
            areaTextoContenido.setText(contenido.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        VisorArchivos visor = new VisorArchivos();
    }
}
