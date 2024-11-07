package game_happysheep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Level2Game extends JPanel implements ActionListener {
    private Timer timer;
    private int playerX = 100;
    private int playerY = 400;
    private int health = 5;
    private int timeElapsed = 0;
    private boolean jumping = false;
    private boolean gameOver = false;
    private int obstacleSpeed = 5;
    private int[] obstaclesX = {800, 1200};
    private Image backgroundImage;
    private Image gameOverBackground;
    private Image characterImage;
    private Image obstacleImage1;
    private Image obstacleImage2;
    private Image healthIcon;
    private Image gameOverButton;
    private Image restartButton;
    private boolean showGameOverButton = false;
    private Home home;
    private boolean moveLeftPressed = false;
    private boolean moveRightPressed = false;

    public Level2Game(Home home) {
        this.home = home;
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    jump();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveLeftPressed = true;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveRightPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveLeftPressed = false;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveRightPressed = false;
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (gameOver && showGameOverButton) {
                    int buttonX = 300;
                    int buttonY = 250;
                    int buttonWidth = 200;
                    int buttonHeight = 100;
                    if (e.getX() >= buttonX && e.getX() <= buttonX + buttonWidth &&
                        e.getY() >= buttonY && e.getY() <= buttonY + buttonHeight) {
                        home.showHomeScreen();
                    }
                }
                if (gameOver && showRestartButton()) {
                    int restartButtonX = 300;
                    int restartButtonY = 400;
                    int buttonWidth = 200;
                    int buttonHeight = 100;
                    if (e.getX() >= restartButtonX && e.getX() <= restartButtonX + buttonWidth &&
                        e.getY() >= restartButtonY && e.getY() <= restartButtonY + buttonHeight) {
                        restartGame();
                    }
                }
            }
        });

        loadImages();
        timer = new Timer(1000 / 60, this);
        timer.start();

        new Thread(this::obstacleGenerator).start();
        new Thread(this::timerCountUp).start();
    }

    private void loadImages() {
        backgroundImage = new ImageIcon(getClass().getResource("pic/bgStart1.png")).getImage();
        gameOverBackground = new ImageIcon(getClass().getResource("pic/bgEnd1.png")).getImage();
        characterImage = new ImageIcon(getClass().getResource("pic/sheep.png")).getImage();
        obstacleImage1 = new ImageIcon(getClass().getResource("pic/bom.png")).getImage();
        obstacleImage2 = new ImageIcon(getClass().getResource("pic/fire.png")).getImage();
        healthIcon = new ImageIcon(getClass().getResource("pic/heartpixel.png")).getImage();
        gameOverButton = new ImageIcon(getClass().getResource("pic/menu.png")).getImage();
        restartButton = new ImageIcon(getClass().getResource("pic/regame.png")).getImage();
    }

    private void jump() {
        if (!jumping && !gameOver) {
            jumping = true;
            new Thread(() -> {
                for (int i = 0; i < 15; i++) {
                    playerY -= 15;
                    repaint();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < 15; i++) {
                    playerY += 15;
                    repaint();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                jumping = false;
            }).start();
        }
    }

    private void moveLeft() {
        if (!gameOver && playerX > 0) {
            playerX -= 5;
        }
        repaint();
    }

    private void moveRight() {
        if (!gameOver && playerX < getWidth() - 200) {
            playerX += 5;
        }
        repaint();
    }

    private void obstacleGenerator() {
        while (!gameOver) {
            for (int i = 0; i < obstaclesX.length; i++) {
                obstaclesX[i] -= obstacleSpeed;
                if (obstaclesX[i] < 0) {
                    obstaclesX[i] = 800 + (int)(Math.random() * 400);
                }
                if (checkCollision(obstaclesX[i])) {
                    if (i == 0) {
                        health -= 2;
                    } else {
                        health -= 1;
                    }
                    obstaclesX[i] = 800 + (int)(Math.random() * 400);
                    checkGameOver();
                }
            }
            repaint();
            try {
                Thread.sleep(29);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void timerCountUp() {
        while (!gameOver) {
            try {
                Thread.sleep(1000);
                timeElapsed++;
                if (timeElapsed % 10 == 0) {
                    obstacleSpeed += 4;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkCollision(int obstacleX) {
        return playerX + 50 > obstacleX && playerX < obstacleX + 50 && playerY >= 350;
    }

    private void checkGameOver() {
        if (health <= 0) {
            gameOver = true;
            showGameOverButton = true;
            timer.stop();
        }
    }

    private boolean showRestartButton() {
        return gameOver;
    }

    private void restartGame() {
        health = 5;
        timeElapsed = 0;
        obstacleSpeed = 5;
        playerX = 100;
        playerY = 400;
        gameOver = false;
        showGameOverButton = false;

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(1000 / 60, this);
        timer.start();

        new Thread(this::obstacleGenerator).start();
        new Thread(this::timerCountUp).start();
        
        requestFocusInWindow();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        requestFocusInWindow();

        if (gameOver) {
            g.drawImage(gameOverBackground, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.drawImage(characterImage, playerX, playerY, 200, 100, this);

        for (int i = 0; i < health; i++) {
            g.drawImage(healthIcon, 20 + i * 30, 20, 30, 40, this);
        }

        for (int i = 0; i < obstaclesX.length; i++) {
            if (i == 0) {
                g.drawImage(obstacleImage1, obstaclesX[i], 400, 200, 100, this);
            } else {
                g.drawImage(obstacleImage2, obstaclesX[i], 400, 300, 100, this);
            }
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 24));
        g.drawString("Time : " + timeElapsed + "s", 650, 50);

        if (showGameOverButton) {
            g.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 36));
            g.setColor(Color.BLACK);
            g.drawString("Time : " + timeElapsed + "s", 320, 230);
            g.drawImage(gameOverButton, 300, 250, 200, 100, this);
            g.drawImage(restartButton, 300, 350, 200, 100, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (moveLeftPressed) {
            moveLeft();
        } else if (moveRightPressed) {
            moveRight();
        }
    }
}
