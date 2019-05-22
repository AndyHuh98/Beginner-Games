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

/*
The controller class is created along with Model and View in the Model-View-Controller design pattern,
which I felt would suit the game best. This controller class essentially contains the "controls"
for the game, such as character movement, pausing/resuming/loading/saving/etc. Controller also contains the main 
method, where the game is called and run.
*/

class Controller implements MouseListener, KeyListener 
{
    /*
    Here, a model and view object are going to be created for each call.
    */
    Model model;
    View view;
    //Instructions are listed as a global variable. This allows for easy modification of instructions above,
    //so in various calls to pull up the instructions, you don't have to change long strings of text.
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
    /* Constructor for the Controller class. Creates a new model (logic for the game) and view (display)
    as well as brings up the instruction manual upon opening the JFrame. */
    Controller() throws IOException, Exception {
        model = new Model();
        view = new View(this);
        JOptionPane.showMessageDialog(null, instructions);
    }
    
    //Getters for the view method to use, basically.
    public Model getModel() { return model; }
    public String getInstructions() { return instructions; }

    //Main method in controller that calls the model's update method, which is used to update the 
    //location of the sprite and allow for movement animations. Go to model.java and view update method
    //for more information.
    public synchronized void update(Graphics g) {
        model.update(g);
    }
    
    /* Mouse click events. These are not used aside from the "right click" which was used for testing purposes.*/
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
    
    /* Key press events - when a user presses a certain key, an action is done in game. */
    public void keyPressed(KeyEvent e) {
    	/* The first four utilize the arrow keys to move the hero sprite left, right, up, and down. */
    	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
    		model.moveHero("left");
		//after each movement, manually repaint the view (updating the sprite's location)
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
	//If the dragon is not dead, the hero can shoot arrows at the dragon to deal damage
	if (model.getDragon().getDragonIsNotDead()) {
	    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	    	model.shootArrow();
	    	view.repaint();
	    }
	//If the dragon IS dead, and the hero has walked thru the portal that spawns upon its death,
	//begin the treasure hunting phase of the game
    	} else if (!(model.getDragon().getDragonIsNotDead()) && model.getWalkedThruPortal()) {
    		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			//hero gets x digs (default is 10. can change in Model.java)
    			if (model.getDigsRemaining() > 0) {
    				model.dig();
	    			int x = model.getHero().getX();
	    			int y = model.getHero().getY();
	    			
	    			int x2 = model.getTreasureCoordinates().get(0);
	    			int y2 = model.getTreasureCoordinates().get(1);
	    			
				/* Two rectangles are created. One using the hidden treasure's location,
				the other using the hero's location upon digging (sprite boundaries). */
	    			Rectangle rect1 = new Rectangle(x, y, 75, 100);
	    			Rectangle rect2 = new Rectangle(x2, y2, 100, 100);
	    		
	    			String digMessage = String.format("Hero dug at: %d, %d%n", x, y);
	    			JOptionPane.showMessageDialog(null, digMessage);
	    			
				/* If the hero digs at the right spot (intersects treasure boundaries),
				   the treasure appears and the hero gains a treasure which can be used
				   to increase damage (optionally) */
	    			if (rect1.intersects(rect2)) {
	    				Treasure treasure = new Treasure();
	    				treasure.setX(x2);
	    				treasure.setY(y2);
	    				model.TreasureFound();
	    				model.getSprites().add(treasure);
	    				model.spendTreasure();
					/* UNCOMMENT IF GAME DOESNT PROPERLY WORK
	    				try {
						model.saveGame();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					*/
	    				
	    				int reply = JOptionPane.showConfirmDialog(null, "Would you like to face the next dragon?", "Exit", JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						JOptionPane.showMessageDialog(null, "Good luck, adventurer.");
					      	try {
							model.nextDragon();
							//model.saveGame();
							model = new Model();
					      	} catch (IOException e1) {
							e1.printStackTrace();
					      	}
					} else {
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
                    				//model.saveGame();
						model = new Model();
                      			} catch (IOException e1) {
						e1.printStackTrace();
                      			}
                    		} else {
                      			JOptionPane.showMessageDialog(null, "Try your luck next time!");
                      			System.exit(0);
                    		}
    			}
    		}
    	}	
    }
    
    /* Key Typed Events -- Pretty much the same as Key Press; just more convenient to use Key Press for
    things like space, arrow keys which don't necessarily create a character value */
    public void keyReleased(KeyEvent e) { }
    public synchronized void keyTyped(KeyEvent e) { 
    	// If the dragon is not dead, allow for pausing, saving, loading, resuming, etc.
	// The things below should be fairly straightforward. Game is paused after most menu actions, such
	// as bringing up hero health, etc. At some point, a health bar will/should be added.
	// After exiting the JOptionPanes, game is normally resumed. Thread will need to be restarted,
	// and the fireball creation/dragon movement along with it.
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
	            	} else {
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
	            } else {
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
					/* when loading, create new model so that the dragon can be updated
					with the correct dragon level (and other attributes) */
					model = new Model();
					view.repaint();
					JOptionPane.showMessageDialog(null, "Game Loaded.");
				} catch (IOException evt) {
					evt.printStackTrace();
				}
	        	} else {
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
