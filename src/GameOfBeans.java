
public class GameOfBeans {

	private int[] pile;
	private int gameDepth;
	private boolean firstPlayer;

	public GameOfBeans(int[] pile, int gameDepth, boolean firstPlayer) {
		this.pile = pile;
		this.gameDepth = gameDepth;
		this.firstPlayer = firstPlayer;
	}

	private int score(int i, int j) {
		int sum = 0;
		for (int k = i; k <= j; k++) {
			sum += pile[k];
		}
		return sum;
	}
	
	public int computeScore() {

		int[][] scores = new int[pile.length+1][pile.length+1];

		for (int i = 0; i <= pile.length; i++) {
			scores[i][i] = pile[i];
		}
		
		for (int i = 1; i <= pile.length; i++) {
			for (int j = i; j <= pile.length; j++) {
				
				int max = -1;
				// POSSIBLE MOVES
				for(int n = 0; n < this.gameDepth; n++) {
					int posChoice = score(i+n, j);
					max = Math.max(max, posChoice + scores[i+n][j]);
					
					int negChoice = score(i, j-n);
					max = Math.max(max, negChoice + scores[i][j-1]);
					
				}
				
			}
		}

		return scores[pile.length][pile.length];
	}
}
