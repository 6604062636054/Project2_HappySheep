package game_happysheep;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Home extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Home() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        URL imgBgURL = getClass().getResource("pic/bgHome.jpg");
        Image imgBg = new ImageIcon(imgBgURL).getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imgBg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(80, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 4;
        ImageIcon level1Icon = new ImageIcon(getClass().getResource("pic/level1new.png"));
        Image level1Image = level1Icon.getImage().getScaledInstance(150, 70, Image.SCALE_SMOOTH);
        JButton level1Button = new JButton(new ImageIcon(level1Image));
        level1Button.setPreferredSize(new Dimension(150, 50));
        level1Button.setBorderPainted(false);
        level1Button.setContentAreaFilled(false);
        level1Button.setFocusPainted(false);
        level1Button.addActionListener(e -> {
            Level1Game game = new Level1Game(this);
            mainPanel.add(game, "Level1Game");
            cardLayout.show(mainPanel, "Level1Game");
        });
        panel.add(level1Button, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        ImageIcon level2Icon = new ImageIcon(getClass().getResource("pic/level2new.png"));
        Image level2Image = level2Icon.getImage().getScaledInstance(150, 70, Image.SCALE_SMOOTH);
        JButton level2Button = new JButton(new ImageIcon(level2Image));
        level2Button.setPreferredSize(new Dimension(150, 50));
        level2Button.setBorderPainted(false);
        level2Button.setContentAreaFilled(false);
        
        level2Button.setFocusPainted(false);
        level2Button.addActionListener(e -> {
            Level2Game game = new Level2Game(this);
            mainPanel.add(game, "Level2Game");
            cardLayout.show(mainPanel, "Level2Game");
        });
        panel.add(level2Button, gbc);

        mainPanel.add(panel, "Home");
        add(mainPanel);
        cardLayout.show(mainPanel, "Home");
    }

    public void showHomeScreen() {
        cardLayout.show(mainPanel, "Home");
    }

    public static void main(String[] args) {
        Home frame = new Home();
        frame.setTitle("Happy Sheep :)");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
