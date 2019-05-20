import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class View extends JFrame implements ActionListener {

    private class MyPanel extends JPanel {
        Controller controller;

        MyPanel(Controller c) {
            controller = c;
            addMouseListener(c);
        }

        public synchronized void paintComponent(Graphics g) {
            controller.update(g);
            revalidate();
        }
    }


    public View(Controller c) throws Exception{
    	addKeyListener(c);
        setTitle("The Dragon's Treasure");
        setSize(1000, 1000);
        MyPanel p = new MyPanel(c);
        
        JMenuBar menubar = new JMenuBar();
        //Instruction MenuItem
        JMenuItem instruction = new JMenuItem("Instructions");
        instruction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            	c.model.pauseGame();
                JOptionPane.showMessageDialog(null, c.instructions);
                c.model.resumeGame();
                c.model.createNewFireball();
                c.model.moveDragon();
                Thread thread = new Thread(new ProjectileLauncher(c.model, c.view));
        		thread.start();
            }
        });
        menubar.add(instruction);
        //Pause Game MenuItem
        JMenuItem pause = new JMenuItem("Pause");
        pause.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		c.model.pauseGame();
        	}
        });
        menubar.add(pause);
        JMenuItem resume = new JMenuItem("Resume");
        resume.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		c.model.resumeGame();
        		c.model.createNewFireball();
        		c.model.moveDragon();
        		Thread thread = new Thread(new ProjectileLauncher(c.model, c.view));
        		thread.start();
        	}
        });
        menubar.add(resume);
        //Exit Game MenuItem
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		c.model.pauseGame();
        		int reply = JOptionPane.showConfirmDialog(null, "Would you like to exit the game?", "Exit", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                  JOptionPane.showMessageDialog(null, "Thank you for playing.");
                  System.exit(0);
                }
                else {
                   JOptionPane.showMessageDialog(null, "Continue the good fight and resume the game!");
                   c.model.resumeGame();
                   c.model.createNewFireball();
                   c.model.moveDragon();
                   Thread thread = new Thread(new ProjectileLauncher(c.model, c.view));
           		   thread.start();
                }
        	}
        });
        //Save Game MenuItem
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		int reply = JOptionPane.showConfirmDialog(null, "Would you like to save the game?", "Save", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                	try {
    					c.model.saveGame();
    					JOptionPane.showMessageDialog(null, "Game Saved.");
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
                }
                else {
                   JOptionPane.showMessageDialog(null, "No Problem!");   
                }
        	}
        });
        menubar.add(save);
        //Load Game MenuItem
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		int reply = JOptionPane.showConfirmDialog(null, "Would you like to load the last saved game?", "Load", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                	try {
                		File file = new File("DragonTreasure.txt");
    					c.model.loadGame(file);
    					JOptionPane.showMessageDialog(null, "Game Loaded.");
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
                }
                else {
                   JOptionPane.showMessageDialog(null, "No Problem!");   
                }
        	}
        });
        menubar.add(load);
        
        JMenuItem reset = new JMenuItem("Reset");
        reset.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		int reply = JOptionPane.showConfirmDialog(null, "Would you like to reset your game?", "Reset", JOptionPane.YES_NO_OPTION);
        		if (reply == JOptionPane.YES_OPTION) {
        			try {
        				File file = new File("DragonTreasure.txt");
        				c.model.resetSave(file);
        				c.model.loadGame(file);
        				JOptionPane.showMessageDialog(null, "Game Reset");
        				c.model = new Model();
        				/*
        				c.model.clearScene();
        				c.model.getBackground().setImage("Background.png");
        				c.model.getSprites().add(c.model.getDragon());
        				*/
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		} else {
        			JOptionPane.showMessageDialog(null, "No Problem!");
        		}
        	}
        });
        menubar.add(reset);
        
        menubar.add(exit);
        
        p.setLayout(new BorderLayout());
        p.add(menubar, BorderLayout.NORTH);
        getContentPane().add(p);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt) {
        repaint();
    }
}
