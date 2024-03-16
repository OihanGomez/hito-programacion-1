import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main(){
        super("Try yourself");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));

        // Container principal
        Container mainContainer = getContentPane();
        mainContainer.setLayout(new BorderLayout());

        // Panel norte
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        // Anadir checkbox al panel norte
        JCheckBox checkBox1 = new JCheckBox("Katniss");
        JCheckBox checkBox2 = new JCheckBox("Peeta");
        northPanel.add(checkBox1);
        northPanel.add(checkBox2);

        //Agregar northPanel al main Container
        mainContainer.add(northPanel, BorderLayout.NORTH);


        //Panel este
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(null);
        // Ancho constante de 250 pixeles
        eastPanel.setPreferredSize(new Dimension(250,600));

        // Radio buttons
        JRadioButton[] radioButtons = new JRadioButton[3];
        ButtonGroup buttonGroup = new ButtonGroup();


        // Coordenadas iniciales para los botones
        int x = 100;
        int y = 100;
        // Anadir botones a el Array, grupo y panel
        for (int i = 0;i < 3 ; i++){
            radioButtons[i] = new JRadioButton("OPT"+(i+1));
            buttonGroup.add(radioButtons[i]);
            radioButtons[i].setBounds(x, y, 100, 30);
            y+=30;
            eastPanel.add(radioButtons[i]);
        }
        mainContainer.add(eastPanel, BorderLayout.EAST);


        // Panel sur
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel,BoxLayout.X_AXIS));
        southPanel.setPreferredSize(new Dimension(0, 50));

        // Crear y anadir botones al panel
        JButton button1 = new JButton("But 1");
        JButton button2 = new JButton("But 2");
        southPanel.add(button1);
        southPanel.add(button2);

        mainContainer.add(southPanel, BorderLayout.SOUTH);


        // Panel Centro
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2,2));

        // Crear imagen
        ImageIcon image = new ImageIcon("Images/nerd.jpg");

        // Anadir imagen a cada celda
        for (int i = 0; i < 4; i++) {
            centerPanel.add(new JLabel(image));
        }
        mainContainer.add(centerPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        Main main = new Main();
    }
}