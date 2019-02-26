import javax.swing.JOptionPane;

import java.util.ArrayList;

public class BetMoney extends Craps {
	private double initialMoney;
	private double remainingMoney;
	private double bet;
	private int wins;
	private int losses;
	
	
	public BetMoney(double firstMoney) {
		initialMoney = firstMoney;
		remainingMoney = initialMoney;
	}

	public void addWin() {
		int win = 0;
		win = wins + 1;
		wins = win;
	}
	
	public void addLoss() {
		int loss = 0;
		loss = losses + 1;
		losses = loss;
	}
	
	
	//Method for Betting Money
	public void bet() {
		boolean goThruLoop = true;
		while(goThruLoop) {
			try {
				bet = Double.parseDouble(JOptionPane.showInputDialog("How much money would you like to bet? (Round to the nearest decimal, e.g. \"111.1.\")"));
				while (bet > remainingMoney || bet < 0) {
					if (bet < 0) {
						bet = Double.parseDouble(JOptionPane.showInputDialog("Input a number greater than zero. (Round to the nearest decimal, e.g. \"111.1.\")"));
					} 
					if (bet > remainingMoney) {
						String message = String.format("You don't have that much money! You only have %.2f remaining. (Round to the nearest decimal, e.g. \"111.1.\")", remainingMoney);
						bet = Double.parseDouble(JOptionPane.showInputDialog(message));
					}
				} 
				goThruLoop = false;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid Input. Please input a decimal value (EXAMPLE: 303.3)");
			}
		}
	}
	
	//Getter for RemainingMoney
	public double getRemainingMoney() {
		return remainingMoney;
	}
	
	//Getter for the bet amount
	public double getBet() {
		return bet;
	}
	
	//Subtractor for RemainingMoney
	public void subtractMoney(double amount) {
		remainingMoney = remainingMoney - amount;
	}
	
	//Adder for RemainingMoney
	public void addMoney(double amount) {
		remainingMoney = remainingMoney + amount;
	}
	
	public static boolean PlayOrNot() {
		String playOrNot = JOptionPane.showInputDialog("Are you ready to play? Y/N");
		char playChar;
		boolean play = true;
		playChar = playOrNot.charAt(0);
		playChar = Character.toLowerCase(playChar);
		
		do {
			playChar = playOrNot.charAt(0);
			playChar = Character.toLowerCase(playChar);
			if (playChar == 'y') {
				play = true;
			} else if (playChar == 'n') {
				play = false;
			} else {
				playOrNot = JOptionPane.showInputDialog("Invalid Input. Please input Y/N.");
			}
		} while (playChar != 'y' && playChar != 'n');
		
		return play;
	}
	
	public static void main(String[] args) {
		//Java Dialog Pane Welcome Message
		JOptionPane.showMessageDialog(null, "Welcome to Andrew's Craps Simulator!");
		
		//IDEA: Implement a Menu
		
		boolean goThruLoop = true;
		double moolah = 0.0;
		while (goThruLoop) {
			try {
				moolah = Double.parseDouble(JOptionPane.showInputDialog("How much money did you bring with you? (Round to the nearest decimal, e.g. \"111.1.\")"));
				if (moolah > 0) {
					goThruLoop = false;
				} else { 
					JOptionPane.showMessageDialog(null, "Please input a positive number.");
				}
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid Input. Please input a decimal value (EXAMPLE: 303.3)");
			}
		}
		
		//Initializes new Craps and BetMoney objects
		BetMoney newBets = new BetMoney(moolah);
		
		//newBets.bet();
		Craps newGame = new Craps();
		
		while (newBets.getRemainingMoney() > 0) {
			if (PlayOrNot()) {
				newBets.bet();
				Craps.playCraps();
			} else {
				JOptionPane.showMessageDialog(null, "Thanks for trying. See ya!");
				System.exit(0);
			}

			//System.out.println("I am here. 1");
			if (newGame.getGameStatus() == Craps.Status.PASS_WON) {
				//System.out.println("I am here. 2");
				newBets.addWin(); 
				newBets.addMoney(newBets.getBet());
				String message = String.format("You just won $%.2f! You have $%.2f left.", newBets.getBet(), newBets.getRemainingMoney());
				JOptionPane.showMessageDialog(null, message);
			} else if (newGame.getGameStatus() == Craps.Status.PASS_LOST) {
				newBets.addLoss();
				newBets.subtractMoney(newBets.getBet());
				String message = String.format("You just lost $%.2f. You have $%.2f left.", newBets.getBet(), newBets.getRemainingMoney());
				JOptionPane.showMessageDialog(null, message);
				//System.out.println("I am here. 3");
			}
		}
	}
}
