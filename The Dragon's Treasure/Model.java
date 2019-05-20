import java.awt.Graphics;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.util.Random;
import java.util.Scanner;

public class Model {
	
	private static boolean gameNotPaused = true;
	private static boolean levelOnePassed = true;
	private static boolean walkedThruPortal = false;
	private static boolean treasureFound = false;
	private static int dragonLevel = 0;
	
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private ArrayList<Sprite> fireballs = new ArrayList<Sprite>();
	private ArrayList<Sprite> arrows = new ArrayList<Sprite>();
	
	private Dragon dragon = new Dragon(dragonLevel);
	private Background background = new Background();
	private Portal portal = new Portal();
	private Hero hero = new Hero();
	private Fireball fireball = new Fireball(dragon.getX(), dragon.getY());
	private Arrow arrow = new Arrow(hero.getX(), hero.getY());
	
	//private Treasure treasure = new Treasure();
	private int digsRemaining = 10;
	private ArrayList<Integer> treasureCoordinates;
	private static int buriedTreasureCount;
	
	
	Model() throws IOException {
		if (buriedTreasureCount > 0) {
			spendTreasure();
			saveGame();
		}
		
		walkedThruPortal = false;
		levelOnePassed = true;
		gameNotPaused = true;
		treasureFound = false;
		
		File file = new File("DragonTreasure.txt");
		startNewGame(file);
		
		//dragon = new Dragon(dragonLevel);
		sprites.add(background);
		sprites.add(dragon);
		sprites.add(hero);
		dragon.alive();
		hero.alive();
	}
	
	public Dragon getDragon() { return dragon; }
	public int getDragonLevel() { return dragonLevel; }
	public void nextDragon() { dragonLevel++; }
	public Arrow getArrow() { return arrow; }
	public ArrayList<Sprite> getSprites() { return sprites; }
	public Hero getHero() { return hero; }
	public Background getBackground() { return background; }
	
	public int getDigsRemaining() { return digsRemaining; }
	public void dig() { digsRemaining -= 1; }
	public boolean getWalkedThruPortal() { return walkedThruPortal; }
	public int getburiedTreasureCount() { return buriedTreasureCount; }
	public boolean getTreasureFound() { return treasureFound; }
	public void TreasureFound() { 
		buriedTreasureCount++;
		treasureFound = true; 
	}
	public void spendTreasure() {
		String message = String.format("You have %d buried treasures, would you like to use one to improve your damage?", getburiedTreasureCount());
		int reply = JOptionPane.showConfirmDialog(null, message, "Spend Buried Treasure", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
          JOptionPane.showMessageDialog(null, "Your arrows have been tipped with an even more potent poison.");
          arrow.setDamage(arrow.getDamage() + 1);
          buriedTreasureCount--;
        }
	}
	
	public synchronized void update(Graphics g) {
		if (!(dragon.getDragonIsNotDead()) && levelOnePassed) {
			sprites.add(portal);
			if (hero.overlaps(portal, 150, 150)) {
				walkedThruPortal = true;
				clearScene();
				background.setImage("TreasureRoom.png");
				treasureCoordinates = generateTreasureCoordinates();
				
				levelOnePassed = false;
			}
		}
		
		Iterator<Sprite> FireballIter = fireballs.iterator();
		
		Iterator<Sprite> iterator = sprites.iterator(); 
		while (iterator.hasNext()) {
			iterator.next().updateImage(g);
		}
		
		while (FireballIter.hasNext()) {
			FireballIter.next().updateImage(g);
		}
		
		Iterator<Sprite> arrowIter = arrows.iterator();
		while (arrowIter.hasNext()) {
			arrowIter.next().updateImage(g);
		}
		
    }
	
	public void pauseGame() {
		gameNotPaused = false;
	}
	
	public void resumeGame() {
		gameNotPaused = true;
	}
	
	public boolean getGameNotPaused() {
		return gameNotPaused;
	}
	
	
	public  void createNewFireball() {
		new Thread(new Runnable() {
	        public void run() {
	            while (gameNotPaused && hero.isNotDead() && dragon.getDragonIsNotDead()) {
	            	fireball = new Fireball(dragon.getX(), dragon.getY());
	        		synchronized(Model.this) {
	        			fireballs.add(fireball);
	        		}
	            try
	                {
	            	int sleepTime = 175 - 20 * dragonLevel;
	            	if (sleepTime > 5) {
	            		Thread.sleep(sleepTime); 
	            	} else {
	            		Thread.sleep(5);
	            	}
	                }
	            catch(InterruptedException e)
	                {
	                e.printStackTrace();
	                Thread.currentThread().interrupt();
	                }
	            }
	        }
	    }).start();
	}
	
	public synchronized void moveDragon() {
		new Thread(new Runnable() {
			public void run() {
				while(gameNotPaused && dragon.getDragonIsNotDead()) {
					dragon.setMoveDirection(new Random().nextInt(2));
	            	if (dragon.getMoveDirection() == 1) {
	            		dragon.moveLeft();
	            	} else {
	            		dragon.moveRight();
	            	}
	            try
	                {
	                Thread.sleep(200); //Waits for .1 second
	                }
	            catch(InterruptedException e)
	                {
	                e.printStackTrace();
	                Thread.currentThread().interrupt();
	                }
	            }
			}
		}).start();
	}
	
	public synchronized void shootArrow() {
		if (gameNotPaused && hero.isNotDead() && dragon.getDragonIsNotDead()) {
			arrow = new Arrow(hero.getX(), hero.getY());
			arrows.add(arrow);
		}
	}
	
	public void moveHero(String direction) {
		if (gameNotPaused && hero.isNotDead() && dragon.getDragonIsNotDead())
			switch (direction) {
				case "left": 
					hero.moveLeft(65, -30);
					break;
				case "right":
					hero.moveRight(65, 1000);
					break;
				case "up":
					hero.moveUp(65, 330);
					break;
				case "down":
					hero.moveDown(65, 820);
					break;	
			} else if (!(dragon.getDragonIsNotDead())) {
				switch (direction) {
				case "left": 
					hero.moveLeft(65, -30);
					break;
				case "right":
					hero.moveRight(65, 1000);
					break;
				case "up":
					hero.moveUp(65, 0);
					break;
				case "down":
					hero.moveDown(65, 820);
					break;
				}
			}
	}
	
	public ArrayList<Integer> generateTreasureCoordinates() {
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		
		Random xRandom = new Random();
		int xCoordinate = xRandom.nextInt(800) + 100;
		
		Random yRandom = new Random();
		int yCoordinate = yRandom.nextInt(600) + 135;
		
		coordinates.add(xCoordinate);
		coordinates.add(yCoordinate);
		
		return coordinates;
	}
	
	public ArrayList<Integer> getTreasureCoordinates() {
		return treasureCoordinates;
	}
	
	public synchronized void updateScene(int width, int height) {
		Iterator<Sprite> arrowIter = arrows.iterator();
		while(arrowIter.hasNext()) {
			Sprite spriteObject = arrowIter.next(); 
			((Arrow) spriteObject).updateState();	
			if (spriteObject.overlaps(dragon, 350, 350)) {
				dragon.arrowHit();
				arrowIter.remove();
			}
		}
		
		Iterator<Sprite> fireballIter = fireballs.iterator();
		while(fireballIter.hasNext()) {
			Sprite spriteObject = fireballIter.next(); 
			((Fireball) spriteObject).updateState();	
			if (spriteObject.overlaps(hero, 100, 135)) {
				hero.fireballHit();
				fireballIter.remove();
			}
		}
	}
	
	public void clearScene() {
		Iterator<Sprite> iterator = sprites.iterator();
		
		while (iterator.hasNext()) {
			Sprite spriteObject = iterator.next();
			if (spriteObject instanceof Hero || spriteObject instanceof Background) {
				
			} else {
				iterator.remove();
			}
		}
	}
	
	public void resetSave(File save) throws IOException {
		save = new File("DragonTreasure.txt");
		save.createNewFile();
		System.out.println("\n----------------------------------");
        System.out.println("The game has been reset.");
        System.out.println("------------------------------------\n");
        FileWriter fileWriter = new FileWriter("DragonTreasure.txt");
        fileWriter.write(Integer.toString(60));
        fileWriter.write('\n');
        fileWriter.write(Integer.toString(100));
        fileWriter.write('\n');
        fileWriter.write(Integer.toString(0));
        fileWriter.write('\n');
        fileWriter.write(Integer.toString(1));
        fileWriter.write('\n');
        fileWriter.write(Integer.toString(0));
        fileWriter.close();
	}
	
	public void startNewGame(File save) throws IOException {
		save = new File("DragonTreasure.txt");
		if (!save.exists()) {
			save.createNewFile();
			System.out.println("\n----------------------------------");
	        System.out.println("The file has been created.");
	        System.out.println("------------------------------------\n");
	        FileWriter fileWriter = new FileWriter("DragonTreasure.txt");
	        fileWriter.write(Integer.toString(60));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(dragon.getDragonMaxHealth()));
	        dragon.setDragonHealth(dragon.getDragonMaxHealth());
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(0));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(1));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(0));
	        fileWriter.close();
		} else {
			List<Integer> data = new ArrayList<Integer>();
			Scanner in = new Scanner(save);
			while(in.hasNextLine()) {
				data.add(Integer.valueOf(in.nextLine()));
			}
			
			FileWriter fileWriter = new FileWriter("DragonTreasure.txt");
			//dragon.setDragonHealth(dragon.getDragonMaxHealth());
			buriedTreasureCount = data.get(2);
			arrow.setDamage(data.get(3));
			dragonLevel = data.get(4);
			
			System.out.println("\n----------------------------------");
	        System.out.println("The file has been overwritten3.");
	        System.out.println("------------------------------------\n");
	        fileWriter.write(Integer.toString(60));
	        hero.setHeroHealth(60);
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(dragon.getDragonMaxHealth()));
	        dragon.setDragonHealth(dragon.getDragonMaxHealth());
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(buriedTreasureCount));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(arrow.getDamage())); 
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(dragonLevel));
	        fileWriter.close();
		}
	}

	
	public void saveGame() throws IOException {
		File save = new File("DragonTreasure.txt");
		if (!save.exists()) {
			save.createNewFile();
			System.out.println("\n----------------------------------");
	        System.out.println("The file has been created.");
	        System.out.println("------------------------------------\n");
	        FileWriter fileWriter = new FileWriter("DragonTreasure.txt");
	        fileWriter.write(Integer.toString(hero.getHeroCurrentHealth()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(dragon.getDragonCurrentHealth()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(getburiedTreasureCount()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(arrow.getDamage()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(dragonLevel));
	        fileWriter.close();
		} else {
			System.out.println("\n----------------------------------");
	        System.out.println("The file has been overwritten2.");
	        System.out.println("------------------------------------\n");
			FileWriter fileWriter = new FileWriter("DragonTreasure.txt");
	        fileWriter.write(Integer.toString(hero.getHeroCurrentHealth()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(dragon.getDragonCurrentHealth()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(getburiedTreasureCount()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(arrow.getDamage()));
	        fileWriter.write('\n');
	        fileWriter.write(Integer.toString(dragonLevel));
	        fileWriter.close();
		}
	}
	
	public void loadGame(File file) throws IOException {
		if (file.exists()) {
			System.out.println("\n----------------------------------");
	        System.out.println("Loading File.");
	        System.out.println("------------------------------------\n");
			List<Integer> data = new ArrayList<Integer>();
			Scanner in = new Scanner(file);
			while(in.hasNextLine()) {
				data.add(Integer.valueOf(in.nextLine()));
			}
			if (hero.getHeroCurrentHealth() <= 0) {
				if (data.get(0) > 0) {
					hero.alive();
				}
			}
 			hero.setHeroHealth(data.get(0));
			dragon.setDragonHealth(data.get(1));
			buriedTreasureCount = data.get(2);
			arrow.setDamage(data.get(3));
			dragonLevel = data.get(4);
		} else {
			System.out.println("File does not exist. Save game first!");
		} 
	}
}

