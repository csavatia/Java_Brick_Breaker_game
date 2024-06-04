package brick.breaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// extend class to panel to add it to the frame object in main class
public class GamePlay extends JPanel implements KeyListener, ActionListener {
    // define properties
    private boolean play= false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8; // speed of ball

    private int playerX = 310; // start position of player
    private int ballPosX = 120; // start position of ball x axis
    private int ballPosY = 350; // start position of ball y axis
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map; // variable for mapgen class

    // create constructor
     public GamePlay(){
         map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer= new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){
         // add background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        // call draw function from map generator class
        map.draw((Graphics2D)g);

        // create borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score,590,30);

        //create paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        //create the ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // detect if game is finished i.e. all bricks are broken
        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU WON", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press ENTER to Restart", 230, 350);

            repaint();
        }

        if(ballPosY > 570){
            play=false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over", 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press ENTER to Restart", 230, 350);
        }

        g.dispose();
    }

    // add unimplemented abstract methods to rid the errors
    @Override
    public void keyTyped(KeyEvent keyEvent) {}
    @Override
    public void keyReleased(KeyEvent keyEvent){}

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
         timer.start();

         //move the ball conditions
        if(play){
            //detect intersection of ball and paddle with rect obj
            if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100,8))){
                ballYdir = -ballYdir;
            }

            //condition for bricks to detect intersection
            // access 2d array using map obj
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length ; j++) {
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        //create rectangle around brick
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir; //  move ball oppposite direction
                            }else{
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }

                    }
                }
            }

            ballPosX += ballXdir;
            ballPosY += ballYdir;
            //left border
            if(ballPosX < 0 ){
                ballXdir = -ballXdir;
            }
            //top border
            if(ballPosY < 0 ){
                ballYdir = -ballYdir;
            }
            //right border
            if(ballPosX > 670 ){
                ballXdir = -ballXdir;
            }
        }
         repaint();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
         if(keyEvent.getKeyCode() == keyEvent.VK_RIGHT){
             if(playerX >= 600){
                 playerX = 600;
             }else{
                 moveRight();
             }
         }
        if(keyEvent.getKeyCode() == keyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }

        // when press enter restarts game
        if(keyEvent.getKeyCode() == keyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight(){
         play = true;
         playerX += 20; // if pressed right then move 20pixels to the right
    }
    public void moveLeft(){
        play = true;
        playerX -= 20; // if pressed left then move 20pixels to the left
    }

}
