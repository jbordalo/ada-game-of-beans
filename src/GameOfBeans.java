import java.util.Arrays;

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

	private int[] Pieton(int i, int f) {
		int max = Integer.MIN_VALUE;
		int[] indexes = new int[2];
		
		// Left-side
		for (int n = 0; n < this.gameDepth && i+n <= f; n++) {
			int choice = score(i, i + n);
			if (choice > max) {
				indexes[0] = i + n + 1;
				indexes[1] = f;
				max = choice;
			}
		}

		// Right-side
		for (int n = 0; n < this.gameDepth && i+n <= f; n++) {
			int choice = score(f - n, f);
			if (choice > max) {
				indexes[0] = i;
				indexes[1] = f - n - 1;
				max = choice;
			}
		}

		if (indexes[1] - indexes[0] < 0)
			return null;
		return indexes;
	}

	public int computeScore() {

		// If Pieton moves first we need to find his move
		if (!firstPlayer) {
			int[] pietonChoice = Pieton(0, pile.length - 1);
			if (pietonChoice == null) {
				return 0;
			}
			// TODO
			this.pile = Arrays.copyOfRange(pile, pietonChoice[0], pietonChoice[1] + 1);
		}

		int[][] scores = new int[pile.length][pile.length];
		// Fill first line with the pile values
		for (int i = 0; i < pile.length; i++) {
			scores[0][i] = pile[i];
		}

		for (int f = 1; f < pile.length; f++) {
			for (int i = 0; i < pile.length - f; i++) {

				int max = Integer.MIN_VALUE;
				// POSSIBLE MOVES
				for (int n = 0; n < this.gameDepth && n <= f; n++) {
					int posChoice = score(i, i + n);
					int s = 0;

					int[] pietonPos = (f - n - 1 < 1) ? null : Pieton(i + n + 1, f + i);
					s = (pietonPos == null) ? 0 : scores[pietonPos[1] - pietonPos[0]][pietonPos[0]];

					max = Math.max(max, posChoice + s);
					int negChoice = score(f - n + i, f + i);
					s = 0;
					int[] pietonNeg = (f - n - 1 < 1) ? null : Pieton(i, f + i - n - 1);
					s = (pietonNeg == null) ? 0 : scores[pietonNeg[1] - pietonNeg[0]][pietonNeg[0]];

					max = Math.max(max, negChoice + s);
				}
				scores[f][i] = max;
			}
		}

		return scores[pile.length - 1][0];
	}
}
