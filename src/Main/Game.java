package Main;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {
		System.setProperty("sun.java2d.opengl", "true");
		JFrame window = new JFrame("Dusk");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
}
