package brick.breaker;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Creating a frame
        JFrame obj = new JFrame();
        GamePlay gamePlay = new GamePlay(); // creates game object
        obj.setBounds(10,10,700,600);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay); // adds game to the frame
    }
}
