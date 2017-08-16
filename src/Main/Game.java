package Main;

import javax.swing.*;
import java.awt.*;

/** PTP 2017
 * Main runnable class. Creates the window and the active buffering strategy.
 *
 * @author Ali Popa, Dewin Bagci
 * @version 14.08.
 * @since 31.05.
 */
public class Game
{
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        JFrame window = new JFrame("Dusk");
        GamePanel panel = new GamePanel();
        Canvas canvas = new Canvas();
        panel.setLayout(null);
        canvas.addKeyListener(panel);
        canvas.setIgnoreRepaint(true);
        canvas.setBounds(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        panel.add(canvas);
        window.setContentPane(panel);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        canvas.createBufferStrategy(2);
        panel.strategy = canvas.getBufferStrategy();
    }
}
