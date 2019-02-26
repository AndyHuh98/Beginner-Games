import java.security.SecureRandom;
import javax.swing.JOptionPane;

//PROBLEM WITH INPUT: when inputting (???)
//need to add come and dont come bets
//add dice roll animation
/*need to perhaps add better way for status (DP LOST etc.), bc when pass wins, dp
  loses. One solution is to create if statements with mirroring code*/
//storage to see how much they win and lose?
//implement "betting" final part

public class Craps {
	private static SecureRandom randomNumber = new SecureRandom();
	private int point = 0;
	static enum Status {
		PASS_WON, PASS_LOST, KEEP_ROLLING, DP_WON, DP_LOST;
	}; 
	
	
	//Status variable for each single game
	private static Status GameStatus;
	
	//Getter for the single game status
	public Status getGameStatus() {
		return GameStatus;
	}
	
	//setter for single game status
	public void setGameStatus(Status status) {
		GameStatus = status;
	}
	
	//Method that simulates Rolling the Dice
	public static int RollDice() {
		int die1 = 0;
		int die2 = 0;
		
		die1 = randomNumber.nextInt(6) + 1;
		die2 = randomNumber.nextInt(6) + 1;
		
		int sum = 0;
		sum = die1 + die2;
		
		return sum;
	}
	
	//Asks for confirmation to roll dice or not
	public static boolean RollOrNot() {
		String rollOrNot = JOptionPane.showInputDialog("Are you ready to roll? Y/N");
		char rollChar;
		boolean roll = true;
		rollChar = rollOrNot.charAt(0);
		rollChar = Character.toLowerCase(rollChar);
		
		do {
			rollChar = rollOrNot.charAt(0);
			rollChar = Character.toLowerCase(rollChar);
			if (rollChar == 'y') {
				roll = true;
			} else if (rollChar == 'n') {
				roll = false;
			} else {
				rollOrNot = JOptionPane.showInputDialog("Invalid Input. Please input Y/N.");
			}
		} while (rollChar != 'y' && rollChar != 'n');
		
		return roll;
	}
	
	//Ask whether a pass or don't pass bet will be made.
	public static boolean PassOrNot() {
		String message = "Would you like to make a Pass Line Bet? Y/N";
		String yOrN = JOptionPane.showInputDialog(message);
		char yOrNChar;
		yOrNChar = yOrN.charAt(0);
	    yOrNChar = Character.toLowerCase(yOrNChar);
		
		boolean pass = true;
		do {
			yOrNChar = yOrN.charAt(0);
		    yOrNChar = Character.toLowerCase(yOrNChar);
			if (yOrNChar == 'y') {
				pass = true;
			} else if (yOrNChar == 'n') {
				pass = false;
			} else {
				yOrN = JOptionPane.showInputDialog("Invalid input, please input the character Y or N.");
			}
		} while (yOrNChar != 'y' && yOrNChar != 'n');
		
		return pass;
	}
	
	//Getter for the original craps point
	public int getPoint() {
		return point;
	}
	
	//Setter for the original craps point
	public int setPoint(int pointVal) {
		point = pointVal;
		return point;
	}
	
	//Main Method
	public static void playCraps() {
		//Creating new Instance of a Craps Game
		Craps CrapsGame = new Craps();
		//Initializing the Game Status (May need to change status enum later)
		//Status GameStatus = Status.KEEP_ROLLING;
		
		CrapsGame.setGameStatus(Status.KEEP_ROLLING);
		
		//Figures out if the player makes a "Pass" or "Don't Pass" Bet
		boolean passBet = PassOrNot();
		
		//If user decides to roll then roll, otherwise they can pull out at the last instant.
		if (RollOrNot()) {
			CrapsGame.setPoint(RollDice());
		} else {
			JOptionPane.showMessageDialog(null, "Changed your mind? See ya!");
			System.exit(0);
		}
		
		int newRoll = 0; //Used for following rolls
		
		//If user places a bet on the "Pass"
		if (passBet == true) {
			switch (CrapsGame.getPoint()) {
				case 7:
				case 11:
					CrapsGame.setGameStatus(Status.PASS_WON); 
					String PassWonMsg = String.format("A %d was rolled! You win!", CrapsGame.getPoint());
					JOptionPane.showMessageDialog(null, PassWonMsg);
					break;
				case 4:
				case 5:
				case 6:
				case 8:
				case 9:
				case 10:
					CrapsGame.setGameStatus(Status.KEEP_ROLLING);
					String KeepRolling = String.format("A %d was rolled. Keep rolling.", CrapsGame.getPoint());
					JOptionPane.showMessageDialog(null, KeepRolling);
					break;
				default:
					CrapsGame.setGameStatus(Status.PASS_LOST);
					String PassLostMsg = String.format("A %d was rolled. You lost.", CrapsGame.getPoint());
					JOptionPane.showMessageDialog(null, PassLostMsg);
			}
		} 
		
		//FILL THIS PART IN
		if (passBet == false) {
			switch (CrapsGame.getPoint()) {
				case 7:
				case 11:
					CrapsGame.setGameStatus(Status.DP_LOST);
					String DPLostMsg = String.format("A %d was rolled. You lost.", CrapsGame.getPoint());
					JOptionPane.showMessageDialog(null, DPLostMsg);
					break;
				case 4:
				case 5:
				case 6:
				case 8:
				case 9:
				case 10:
					CrapsGame.setGameStatus(Status.KEEP_ROLLING);
					String KeepRolling = String.format("A %d was rolled. Keep rolling.", CrapsGame.getPoint());
					JOptionPane.showMessageDialog(null, KeepRolling);
					break;
				default:
					CrapsGame.setGameStatus(Status.DP_WON);
					String DPWonMsg = String.format("A %d was rolled! You win!", CrapsGame.getPoint());
					JOptionPane.showMessageDialog(null, DPWonMsg);
			}
		}
		
		while(CrapsGame.getGameStatus().equals(Status.KEEP_ROLLING)) {
			if (passBet) {
				String PassBetDialog = String.format("You need a %d to win.", CrapsGame.getPoint());
				JOptionPane.showMessageDialog(null, PassBetDialog);
				
				if (RollOrNot()) {
					newRoll = RollDice();
				} else {
					JOptionPane.showMessageDialog(null, "Changed your mind? See ya!");
				}
				
				if (newRoll == CrapsGame.getPoint()) {
					CrapsGame.setGameStatus(Status.PASS_WON);
					String message = String.format("A %d was rolled! You win!", newRoll);
					JOptionPane.showMessageDialog(null, message);
				} else if (newRoll == 7 || newRoll == 11) {
					CrapsGame.setGameStatus(Status.PASS_LOST);
					String message = String.format("A %d was rolled. You lose.", newRoll);
					JOptionPane.showMessageDialog(null, message);
				} else {
					CrapsGame.setGameStatus(Status.KEEP_ROLLING);
					String message = String.format("A %d was rolled. Keep Rolling.", newRoll);
					JOptionPane.showMessageDialog(null, message);
				}
			} 
			
			if (!passBet) {
				String PassBetDialog = "You need a 7 or 11 to win.";
				JOptionPane.showMessageDialog(null, PassBetDialog);
				
				if (RollOrNot()) {
					newRoll = RollDice();
				} else {
					JOptionPane.showMessageDialog(null, "Changed your mind? See ya!");
					System.exit(0);
				}
				
				if (newRoll == CrapsGame.getPoint()) {
					CrapsGame.setGameStatus(Status.DP_LOST);
					String message = String.format("A %d was rolled. You lost.", newRoll);
					JOptionPane.showMessageDialog(null, message);
				} else if (newRoll == 7 || newRoll == 11) {
					CrapsGame.setGameStatus(Status.DP_WON);
					String message = String.format("A %d was rolled! You win!", newRoll);
					JOptionPane.showMessageDialog(null, message);
				} else {
					CrapsGame.setGameStatus(Status.KEEP_ROLLING);
					String message = String.format("A %d was rolled. Keep Rolling.", newRoll);
					JOptionPane.showMessageDialog(null, message);
				}
			}
		}
	}	
}
