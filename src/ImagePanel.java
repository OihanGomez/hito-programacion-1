import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private JComboBox<String> imageComboBox;
    private JLabel imageLabel;
    private JCheckBox commentCheckBox;
    private JTextField commentTextField;

    // Ruta de la carpeta donde se encuentran las imágenes
    private static final String FOLDER_PATH = "src/images";

    public ImagePanel() {
        setLayout(new BorderLayout());

        // Panel para los controles de selección de imágenes
        JPanel controlPanel = new JPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Combo box para las imágenes
        imageComboBox = new JComboBox<>();
        loadCombo();
        imageComboBox.addActionListener(new ComboListener());
        controlPanel.add(imageComboBox);

        // Etiqueta para mostrar la imagen seleccionada
        imageLabel = new JLabel();
        add(imageLabel, BorderLayout.CENTER);

        // Cargar la primera imagen por defecto
        if (imageComboBox.getItemCount() > 0) {
            String firstImage = (String) imageComboBox.getItemAt(0);
            ImageIcon imageIcon = new ImageIcon(FOLDER_PATH + "\\" + firstImage);
            imageLabel.setIcon(imageIcon);
        }

        // Panel para los controles de guardar comentarios
        JPanel savePanel = new JPanel();
        add(savePanel, BorderLayout.SOUTH);

        // Casilla de verificación y campo de texto para el comentario
        commentCheckBox = new JCheckBox("Agregar comentario");
        savePanel.add(commentCheckBox);

        commentTextField = new JTextField(15);
        savePanel.add(commentTextField);

        // Botón para guardar comentarios
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(new SaveButtonListener());
        savePanel.add(saveButton);
    }

    // Método para cargar las imágenes en el combo box
    private void loadCombo() {
        File folder = new File(FOLDER_PATH);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    imageComboBox.addItem(file.getName());
                }
            }
        }
    }

    // Listener para el combo box que muestra la imagen seleccionada
    private class ComboListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedImage = (String) imageComboBox.getSelectedItem();
            ImageIcon imageIcon = new ImageIcon(FOLDER_PATH + "\\" + selectedImage);
            imageLabel.setIcon(imageIcon);
        }
    }

    // Listener para el botón de guardar comentarios
    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedImage = (String) imageComboBox.getSelectedItem();
            String comment = commentTextField.getText();

            // Si la casilla de verificación está seleccionada, guarda el comentario
            if (commentCheckBox.isSelected()) {
                saveComment(selectedImage, comment);
            }
        }

        // Método para guardar el comentario en un archivo de texto
        private void saveComment(String imageName, String comment) {
            String fileName = imageName + ".txt";
            try (FileWriter writer = new FileWriter(FOLDER_PATH + "\\" + fileName, true)) {
                writer.write(comment + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}