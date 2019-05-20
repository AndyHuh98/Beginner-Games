
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Timer;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

class Controller implements MouseListener, KeyListener 
{
    Model model;
    //BuriedTreasureModel BTModel;
    View view;
    String instructions = 
    		String.format("====INSTRUCTIONS TO PLAY THIS GAME====\n"
    				+ "PRESS R: Start and/or Resume Gameplay\n"
    				+ "PRESS E: Exit Game\n"
    				+ "PRESS P: Pause Game\n"
    				+ "PRESS S: Save Game\n"
    				+ "PRESS L: Load Game\n"
    				+ "PRESS Q: View Hero HP and Dragon HP\n"
    				+ "PRESS SPACE BAR: Launch Arrows\n"
    				+ "PRESS ARROW KEYS: Move Hero\n"
    				+ "==============================================\n"
    				+ "||Upon exiting the game, when you re-enter, you will start a new game.||\n"
    				+ "||You can get back to the last level saved, by simply loading the game  ||\n"
    				+ "||after starting a new game.                                                                     ||\n"        
    				+ "==============================================\n"
    				+ "TREASURE HUNTING MODE:\n"
    				+ "You have 10 attempts to find the buried treasure!\n"
    				+ "Press the SPACE BAR to dig.\n");

    Controller() throws IOException, Exception {
        model = new Model();
        view = new View(this);
        JOptionPane.showMessageDialog(null, instructions);
    }
    
    public Model getModel() { return model; }
    public String getInstructions() { return instructions; }

    public synchronized void update(Graphics g) {
        model.update(g);
    }

    public void mousePressed(MouseEvent e) {     }
    public void mouseReleased(MouseEvent e) {    }
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e) {  
    	if (e.getButton() == MouseEvent.BUTTON3 || (e.isControlDown() && e.getButton() == MouseEvent.BUTTON1)) {
    		model.updateScene(view.getWidth(), view.getHeight());
    		view.repaint();
    	}
    }
    
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    		model.moveHero("left");
    		view.repaint();
    	} 
    	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
    		model.moveHero("right");
    		view.repaint();
    	}
    	if (e.getKeyCode() == KeyEvent.VK_UP) {
    		model.moveHero("up");
    		view.repaint();
    	}
    	if (e.getKeyCode() == KeyEvent.VK_DOWN) {
    		model.moveHero("down");
    		view.repaint();
    	}
	    if (model.getDragon().getDragonIsNotDead()) {
	    	if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	    		model.shootArrow();
	    		view.repaint();
	    	}
    	} else if (!(model.getDragon().getDragonIsNotDead()) && model.getWalkedThruPortal()) {
    		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
    			if (model.getDigsRemaining() > 0) {
    				model.dig();
	    			int x = model.getHero().getX();
	    			int y = model.getHero().getY();
	    			
	    			int x2 = model.getTreasureCoordinates().get(0);
	    			int y2 = model.getTreasureCoordinates().get(1);
	    			
	    			Rectangle rect1 = new Rectangle(x, y, 75, 100);
	    			Rectangle rect2 = new Rectangle(x2, y2, 100, 100);
	    		
	    			String digMessage = String.format("Hero dug at: %d, %d%n", x, y);
	    			JOptionPane.showMessageDialog(null, digMessage);
	    			
	    			if (rect1.intersects(rect2)) {
	    				Treasure treasure = new Treasure();
	    				treasure.setX(x2);
	    				treasure.setY(y2);
	    				model.TreasureFound();
	    				model.getSprites().add(treasure);
	    				model.spendTreasure();
	    				try {
							model.saveGame();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	    				
	    				int reply = JOptionPane.showConfirmDialog(null, "Would you like to face the next dragon?", "Exit", JOptionPane.YES_NO_OPTION);
	                    if (reply == JOptionPane.YES_OPTION) {
	                      JOptionPane.showMessageDialog(null, "Good luck, adventurer.");
	                      try {
	                    	model.nextDragon();
	                    	model.saveGame();
							model = new Model();
	                      } catch (IOException e1) {
							e1.printStackTrace();
	                      }
	                    }
	                    else {
	                      JOptionPane.showMessageDialog(null, "Try your luck next time!");
	                      System.exit(0);
	                    }
	    			}	
    			} else {
    				int reply = JOptionPane.showConfirmDialog(null, "Would you like to face the next dragon?", "Exit", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                      JOptionPane.showMessageDialog(null, "Good luck, adventurer.");
                      try {
                    	model.nextDragon();
                    	model.saveGame();
						model = new Model();
                      } catch (IOException e1) {
						e1.printStackTrace();
                      }
                    }
                    else {
                      JOptionPane.showMessageDialog(null, "Try your luck next time!");
                      System.exit(0);
                    }
    			}
    		}
    	}
    	
    }
    public void keyReleased(KeyEvent e) { }
    public synchronized void keyTyped(KeyEvent e) { 
    	if (model.getDragon().getDragonIsNotDead()) {
	    	if (e.getKeyChar() == 'r') {
	    		model.resumeGame();
	    		model.moveDragon();
	    		model.createNewFireball();
	    		Thread thread = new Thread(new ProjectileLauncher(model, view));
	    		thread.start();
	    	}
	    	if (e.getKeyChar() == 'p') {
	    		model.pauseGame();
	    	}
	    	if (e.getKeyChar() == 'q') {
	    		model.pauseGame();
	    		String healthMessage = String.format("HERO HEALTH: %d/%d%nDRAGON HEALTH: %d/%d%nDRAGON LEVEL: %d", model.getHero().getHeroCurrentHealth(), model.getHero().getHeroMaxHealth(), model.getDragon().getDragonCurrentHealth(), model.getDragon().getDragonMaxHealth(), model.getDragonLevel());
	    		JOptionPane.showMessageDialog(null, healthMessage);
	    		model.resumeGame();
	            model.moveDragon();
	            model.createNewFireball();
	            Thread thread = new Thread(new ProjectileLauncher(model, view));
	    		thread.start();
	    	}
	    	if (e.getKeyChar() == 'e') {
	    		model.pauseGame();
	    		int reply = JOptionPane.showConfirmDialog(null, "Would you like to exit the game?", "Exit", JOptionPane.YES_NO_OPTION);
	            if (reply == JOptionPane.YES_OPTION) {
	              JOptionPane.showMessageDialog(null, "Thank you for playing.");
	              System.exit(0);
	            }
	            else {
	               JOptionPane.showMessageDialog(null, "Continue the good fight and resume the game!");
	               model.resumeGame();
	               model.moveDragon();
	               model.createNewFireball();
	               Thread thread = new Thread(new ProjectileLauncher(model, view));
	       		   thread.start();
	            }
	    	}
	    	if (e.getKeyChar() == 'i') {
	    		model.pauseGame();
	    		JOptionPane.showMessageDialog(null, instructions);
	    		model.resumeGame();
	            model.moveDragon();
	            model.createNewFireball();
	            Thread thread = new Thread(new ProjectileLauncher(model, view));
	    		thread.start();
	    	} 
	    	if (e.getKeyChar() == 's') {
	    		model.pauseGame();
	    		int reply = JOptionPane.showConfirmDialog(null, "Would you like to save the game?", "Save", JOptionPane.YES_NO_OPTION);
	            if (reply == JOptionPane.YES_OPTION) {
	            	try {
						model.saveGame();
						JOptionPane.showMessageDialog(null, "Game Saved.");
						model.resumeGame();
						model.moveDragon();
						model.createNewFireball();
			            Thread thread = new Thread(new ProjectileLauncher(model, view));
			    		thread.start();
					} catch (IOException evt) {
						evt.printStackTrace();
					}
	            }
	            else {
	               JOptionPane.showMessageDialog(null, "No Problem!");  
	               model.resumeGame();
	               model.createNewFireball();
	               model.moveDragon();
	               Thread thread = new Thread(new ProjectileLauncher(model, view));
	       		   thread.start();
	            }
	    	}
	    	if (e.getKeyChar() == 'l') {
	    		model.pauseGame();
	    		int reply = JOptionPane.showConfirmDialog(null, "Would you like to load the last saved game?", "Load", JOptionPane.YES_NO_OPTION);
	            if (reply == JOptionPane.YES_OPTION) {
	            	try {
	            		File file = new File("DragonTreasure.txt");
						model.loadGame(file);
						model = new Model();
						view.repaint();
						JOptionPane.showMessageDialog(null, "Game Loaded.");
					} catch (IOException evt) {
						evt.printStackTrace();
					}
	            }
	            else {
	               JOptionPane.showMessageDialog(null, "No Problem!"); 
	               model.resumeGame();
	               model.moveDragon();
	               model.createNewFireball();
	               Thread thread = new Thread(new ProjectileLauncher(model, view));
	       		   thread.start();
	            }
	    	}
    	} else if (!(model.getDragon().getDragonIsNotDead())) {
    		if (e.getKeyChar() == 'i') {
    			JOptionPane.showMessageDialog(null, instructions);  
    		}
    	}
    }
   
    public static synchronized void main(String[] args) throws Exception {
        new Controller();
    }

	
}
