import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

//Michael Dao
//Program description:
//Mar 15, 2024
//

public class SpaceInvaders extends JPanel implements KeyListener
{
   
   
   private Image playerImage, playerImage2, alienImage;
   private static final long serialVersionUID = 1L;
   private Sprite laser, ship, alienLaser;
   private ArrayList<Sprite> aliens;
   public static final int PANEL_WIDTH = 700;
   public static final int PANEL_HEIGHT = 500  ;
   private int TOTAL_LIVES = 3;
   private static int  speed = 1;
   private int score, livesLeft;
   private boolean playing, gameOver, laserIsFired, alienLaserIsFired, alienLaserOffScreen;
   private String message;
   private Clip hitSound;
   int level, alienMoveCounter;
  
   
   
   
  

   
   //class constructor... initializer function
   public SpaceInvaders()
   {

      this.addKeyListener(this);
      this.setFocusable(true);
      
      playing = false;
      gameOver = false;
      laserIsFired = false;
      
      message = "Press <Space> to Serve";
      livesLeft = TOTAL_LIVES;
      
      try {
         hitSound = AudioSystem.getClip(); //initialize a sound clip object
         hitSound.open(AudioSystem.getAudioInputStream(new File("src/Perfect-_Street-Fighter_-QuickSounds.com.wav"))); //use the filename for your audio
      } catch (Exception e) {e.printStackTrace();}
      
      
     playerImage = new ImageIcon(this.getClass().getResource("bird_background.png")).getImage();
     Image player2Image = new ImageIcon(this.getClass().getResource("Terence_bird_AB2.png")).getImage();
     Image GTR_Image = new ImageIcon(this.getClass().getResource("nissan_gtr.png")).getImage();
     Image Porsche_Image = new ImageIcon(this.getClass().getResource("porsche-model.png")).getImage();
    laser = new Sprite(100, 100, 6, 20, 0, -10, playerImage);
    alienLaser = new Sprite(100, 20, 6, 20, 0, -10, playerImage);
    alienLaserIsFired = false;
    alienLaserOffScreen = true;
    alienLaser = new Sprite(50, 50, 4, 13, 0, .75, playerImage);
    alienLaser.setBoundaries(0, PANEL_WIDTH, 0, PANEL_HEIGHT);
    alienLaser.setX(-50);
    alienLaser.setY(-50);
    
    alienImage = new ImageIcon(this.getClass().getResource("invader1.png")).getImage();
   
    laser.setBoundaries(0, PANEL_WIDTH, 0, PANEL_HEIGHT);


    ship = new Sprite(PANEL_WIDTH/2-50, PANEL_HEIGHT-40, 100, 25, 0.5, 0.5, 37, 39, 38, 40, GTR_Image);
    ship.isFacingRight(false);
    ship.setBoundaries(0, PANEL_WIDTH, 0, PANEL_HEIGHT);
    
    laser.setToShip(ship);
    aliens = new ArrayList<Sprite>();
    level = 5;
    resetAliens(level);

   
      
      Timer timer = new Timer(speed, new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e)
         {
    
            ship.update_paddle_brickbreaker();
           
             
            if(playing) {
               
                          message = "Playing...";
               //do the playing stuff... updates, collision, etc.
               //check if playing should stop and point is scored
                     //Does score value result in an end of the game
               
               if(laserIsFired)
                  laser.update_space_invaders();
               else
                  laser.setToShip(ship);

               if(laser.isOffPanel())
               {
                  laserIsFired = false;
                  
               }
               
               
               
               if (alienLaserIsFired == true)
               {
                  alienLaser.update_space_invaders();
               }
               else
               {
                  alienLaser.setX(-50);
                  alienLaser.setY(-50);
               }
               if (alienLaser.getBounds().intersects(ship.getBounds()))
               {
                  alienLaserIsFired = false;
                  livesLeft--;
            
                 
               }
               
               if (alienLaser.isOffPanel())
               {
                  alienLaserIsFired = false;
               }
      
               
               for(int i = aliens.size()-1; i >= 0; i--) 
               {
                  if(aliens.get(i).getY() > PANEL_HEIGHT - 60) {
                     gameOver = true;
                     playing = false;
                  }
                  
                  if(laser.getBounds().intersects(aliens.get(i).getBounds()))
                  {  
                     aliens.remove(i);
                     score++;
                     laserIsFired = false;
                  }
               }
                 alienMoveCounter++;
                 if(alienMoveCounter > 200)
                 {
                    moveAliens();
                    alienMoveCounter = 0;
                 }
                    
               
               //check for clearing level
               if(aliens.size() == 0)
               {
              
                  resetAliens(level);
               }
                  
               
     
                  
            }
          else if(!gameOver) {
               //Not playing but not over... prepare for the next "round"... put the ball in the middle of the panel
             message = "Press <Space> to Serve.";
        
          }
          else {
               //The game is over... do game over stuff
             message = "Game Over";
             
          }
            
            
           
            repaint();

         }
         
      });
      
      timer.start();
    //SPACE INVADERS METHODS
      


      
      
      
      //END OF SPACE INVADERS METHODS
      
    
   } //end of constructor
   public void moveAliens()
   {
      //should the aliens change directions
      boolean shouldChangeDirection = false;
      for(Sprite a : aliens)
         if(a.willHitEdge())
            shouldChangeDirection = true;
     
      //move aliens
      for(int i = 0; i<aliens.size(); i++)
      {
         if(shouldChangeDirection)
         {
            aliens.get(i).changeHorizontal();
            aliens.get(i).moveAlienDown();
         }
         else
            aliens.get(i).update_alien();
      }
   }
   
   public void resetAliens(int rows)
   {
      for(int r = 0; r < rows; r++)
         for(int i = 0; i < 8; i++)
         {
            Sprite nextAlien = new Sprite(100+i*43, 100+r*35,30,20,50,25, alienImage);
            nextAlien.setBoundaries(0, PANEL_WIDTH, 0, PANEL_HEIGHT);
            aliens.add(nextAlien);
         }
      
   }
  
   

   public void paintComponent(Graphics g)
   {
      
      Graphics2D g2 = (Graphics2D) g;
      
      
      alienLaser.drawImage(g2);
      laser.drawImage(g2);
      ship.drawImage(g2);
      
      //draw all bricks
      
     

      for(Sprite b : aliens)
         b.drawImage(g2);
      
      g2.setFont(new Font("Sunday Mango", Font.PLAIN, 46));
      ship.isFacingRight(false);
    
      g2.drawString(score + "", PANEL_WIDTH/4, 50);
//      g2.drawString(livesLeft + "", PANEL_WIDTH*3/4, 50);
      
 
      
      FontMetrics fm = g2.getFontMetrics();
      int messageWidth = fm.stringWidth(message);
      int startX = PANEL_WIDTH / 2 - messageWidth / 2;
      g2.drawString(message, startX, 170);
     
      g2.setColor(Color.green);
      g2.fillRect(150, 300, 50, 40);
      
      g2.setColor(Color.green);
      g2.fillRect(50, 300, 50, 40);
      
      g2.setColor(Color.green);
      g2.fillRect(250, 300, 50, 40);
      
      g2.setColor(Color.green);
      g2.fillRect(350, 300, 50, 40);
      
      g2.setColor(Color.green);
      g2.fillRect(450, 300, 50, 40);
      
      g2.setColor(Color.green);
      g2.fillRect(550, 300, 50, 40);
      
      
      
      
    
      


   }

   public Dimension getPreferredSize()
   {
     return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
   }
   
   
   public static void main(String[] args)
   {
      JFrame frame = new JFrame("Space Invaders");
      frame.add(new SpaceInvaders());
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   
      
   }





   @Override
   public void keyTyped(KeyEvent e)
   {
   }

   public void resetGame()
   {
      score = 0;
      gameOver=false;
      laser.resetPongBall();
      livesLeft = TOTAL_LIVES;
      level = 5;
      aliens = new ArrayList<Sprite>();
   }



   @Override
   public void keyPressed(KeyEvent e)
   {
      
      if(e.getKeyCode() == 32) {
         if(gameOver) 
            resetGame();
         else if(!playing) 
            playing = true;
         else
            laserIsFired = true;
    }


      
      
//      if(e.getKeyCode() == KeyEvent.VK_W) 
//         player.dy = -6;
//      if(e.getKeyCode() == KeyEvent.VK_A) 
//         player.dx = -6;
//      if(e.getKeyCode() == KeyEvent.VK_S) 
//         player.dy = 6;
//      if(e.getKeyCode() == KeyEvent.VK_D) 
//         player.dx = 6;

      System.out.println(e.getKeyCode());
      ship.checkForPress(e.getKeyCode());
     
      
  

      
   }

   @Override
   public void keyReleased(KeyEvent e)
   {
//      if(e.getKeyCode() == KeyEvent.VK_W) 
//         player.dy = 0;
//      if(e.getKeyCode() == KeyEvent.VK_A) 
//         player.dx = 0;
//      if(e.getKeyCode() == KeyEvent.VK_S) 
//         player.dy = 0;
//      if(e.getKeyCode() == KeyEvent.VK_D) 
//         player.dx = 0;
      ship.checkForRelease(e.getKeyCode());
     
      
   }

}
