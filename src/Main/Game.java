package Main;

import javax.swing.JFrame;
import java.awt.*;

public class Game
{
    
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.opengl", "true");
        JFrame window = new JFrame("Dusk");
        GamePanel panel = new GamePanel();
        Canvas canvas = new Canvas();
        canvas.addKeyListener(panel);
        canvas.setIgnoreRepaint(true);
        canvas.setBounds(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        panel.add(canvas);
        window.setContentPane(panel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        canvas.createBufferStrategy(2);
        panel.strategy = canvas.getBufferStrategy();
    }
}
