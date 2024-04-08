import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ImageApplication extends JFrame {
    // Contraseña requerida para acceder
    private static final String PASSWORD = "damocles";

    public ImageApplication() {
        super("Image Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setContentPane(new ImagePanel());

        // Mensaje al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(ImageApplication.this, "Adios");
            }
        });
    }

    public static void main(String[] args) {
        ImageApplication app = new ImageApplication();
        app.setVisible(true);
        // Solicitar contraseña al usuario
        String passwordInput = JOptionPane.showInputDialog(app, "Ingrese la contraseña:");
        // Verificar la contraseña
        if (passwordInput == null || !passwordInput.equals(PASSWORD)) {
            JOptionPane.showMessageDialog(app, "Contraseña incorrecta.");
            System.exit(0);
        }
    }
}
