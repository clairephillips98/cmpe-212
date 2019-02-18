import javax.swing.JOptionPane;

public class Assn1_15cvp2 { //Claire Phillips 20010910
	
	public static int round(int r1, int r2) {
		int points = 0;
		if(r1 == 1 && r2 == 1) points = 25;
		else if (r1 == 1 || r2 == 1) points = 0;
		else if(r1 == r2) points = r1*4;
		else points = r1 + r2;
		return points;
	} //calculates the score per roll
	
	public static boolean nextround(int turnscore, int score, int r1, int r2) {
		boolean  n;
		if (score == 0) {
			n = false;
			JOptionPane.showMessageDialog(null, "you rolled " + r1 + " and " + r2 + ", your turn score is set to " + score + " and your turn is done.");
		} //message if get 1 and 1
		else if (score >= 25) {
			n = true;
			JOptionPane.showMessageDialog(null, "you rolled " + r1 + " and " + r2 + ". You won " + score + "! Your turn score is " + turnscore + ". You must roll again.");
		} //message if u score doubles
		else {
			int c = JOptionPane.showConfirmDialog(null, "you rolled " + r1 + " and " + r2 + ". You won " + score + " and your turn score is " + turnscore + ". Do you wish to roll again?", null, JOptionPane.YES_NO_OPTION);
			if (c==1) n = false;
			else n = true;
		} //message if u don't score doubles, make choice
		return n; //true if round not over, true if over 
	}
	
	public static int computer(int ctotal) {
		boolean cnext = true;
		int cturnscore = 0;
		while(cnext) {
			int roll1 = (int)(Math.random()*6)+1;
			int roll2 = (int)(Math.random()*6)+1;
			int score = round(roll1, roll2); //calculates score for round
			cturnscore = cturnscore + score;
			if (score == 0) {
				cturnscore = 0;
				cnext = false; //if computer gets a 1
			} 
			else if (score <= 25 && (cturnscore >= 40 || (ctotal + cturnscore) >= 100))cnext = false;
			//if score is under 25, the turn score is over than 40 and computer has gotten over 100, don't go again
		}
		return cturnscore;
	} //computers round
	
	public static boolean winner( int ct, int t, int cs) {
		boolean nextgame;
		if (t >= 100) {
			JOptionPane.showMessageDialog(null, "Your total is " + t + "! YOU WIN!!!");
			nextgame = false;
		} //checking if player has won first because the player starts the game
		else if (ct >= 100) {
			JOptionPane.showMessageDialog(null, "Your total is " + t + " but the computer scored " + cs + " and won with" + ct + ". You lose.");
			nextgame = false;
		}
		else {
			JOptionPane.showMessageDialog(null, "Your total is " + t + "! the computer scored " + cs + " now its score is " + ct + ".");
			nextgame = true; //only continue if no one has won
		}
		
		return nextgame;
	} //either theres a winner or on to the next round
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int total = 0, comptotal = 0; 
		boolean nextround = true;
		while (nextround) {
			int turnscore = 0;
			boolean next = true;
			while(next) {
				int roll1 = (int)(Math.random()*6)+1; //int between 1 and 6
				int roll2 = (int)(Math.random()*6)+1; //int between 1 and 6
				int score = round(roll1, roll2); //calculate score for round
				if (score == 0) turnscore = 0;  
				else turnscore = turnscore + score;
				next = nextround(turnscore, score, roll1, roll2); //player decides for next round
			}
			total = total + turnscore;
			int compscore = computer(comptotal);
			comptotal = comptotal + compscore;
			nextround = winner(comptotal, total, compscore); //anounce winner
		} // if no winner play another round
		 System.out.println("end"); //game over
	}

}
