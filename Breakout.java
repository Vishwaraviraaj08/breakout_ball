import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Breakout extends JPanel {

    private int ballX = 250;
    private int ballY = 250;
    private final int ballSize = 20;
    private int ballSpeedX = -4;
    private int ballSpeedY = -4;

    private int paddleX = 200;
    private final int paddleY = 460;
    private final int paddleWidth = 120;
    private final int paddleHeight = 20;

    private final ArrayList<Rectangle> bricks = new ArrayList<>();
    private final int brickWidth = 60;
    private final int brickHeight = 20;
    private final int brickRows = 5;
    private final int brickColumns = 7;
    private final int brickPadding = 10;

    private boolean playing = false;
    private int score = 0;

    public Breakout() {
        setPreferredSize(new Dimension(500, 500));
        addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (paddleX - 10 >= 0) {
                        paddleX -= 10;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (paddleX + paddleWidth + 10 <= getWidth()) {
                        paddleX += 10;
                    }
                }
            }
        });
        setFocusable(true);
        initializeBricks();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, ballSize, ballSize);

        g.setColor(Color.GREEN);
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

        g.setColor(Color.YELLOW);
        for (Rectangle brick : bricks) {
            g.fillRect(brick.x, brick.y, brick.width, brick.height);
        }

        g.setColor(Color.WHITE);

        if (!playing) {
            g.drawString("Try Again !!", getWidth() / 2 - 50, getHeight() / 2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);

        }
    }

    public void play() {
        playing = true;
        while (playing) {
            moveBall();
            checkCollisions();
            repaint();

            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeBricks() {
        int brickX = brickPadding;
        int brickY = brickPadding;
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickColumns; j++) {
                bricks.add(new Rectangle(brickX, brickY, brickWidth, brickHeight));
                brickX += brickWidth + brickPadding;
            }
            brickY += brickHeight + brickPadding;
            brickX = brickPadding;
        }
    }

    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;
    }

    private void checkCollisions() {

        if (ballX < 0 || ballX + ballSize > getWidth()) {
            ballSpeedX *= -1;
        }
        if (ballY < 0) {
            ballSpeedY *= -1;
        }
        if (ballY + ballSize > getHeight()) {
            playing = false;
        }

        Rectangle ballRect = new Rectangle(ballX, ballY, ballSize, ballSize);
        for (int i = 0; i < bricks.size(); i++) {
            Rectangle brick = bricks.get(i);
            if (ballRect.intersects(brick)) {
                bricks.remove(i);
                ballSpeedY *= -1;
                score++;
                break;
            }
        }
        Rectangle paddleRect = new Rectangle(paddleX + 2, paddleY, paddleWidth - 2, paddleHeight);
        Rectangle leftEdge = new Rectangle(paddleX - 2, paddleY, 2, paddleHeight);
        Rectangle rightEdge = new Rectangle(paddleX + paddleWidth - 2, paddleY, 2, paddleHeight);
        if (paddleRect.intersects(ballRect)) {
            ballSpeedY *= -1;
        }
        if (ballRect.intersects(leftEdge) || ballRect.intersects(rightEdge)) {
            ballSpeedX *= -1;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout");
        Breakout game = new Breakout();
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                game.requestFocus();
            }
        });

        game.play();
    }
}