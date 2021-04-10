import java.util.Arrays;

public class GameOfBeans {

    private int[] pile;
    private final int gameDepth;
    private final boolean firstPlayer;

    public GameOfBeans(int[] pile, int gameDepth, boolean firstPlayer) {
        this.pile = pile;
        this.gameDepth = gameDepth;
        this.firstPlayer = firstPlayer;
    }

    private int score(int i, int j) {
        int sum = 0;
        for (int k = i; k <= j; k++) {
            sum += this.pile[k];
        }
        return sum;
    }

    private int[] Pieton(int i, int j) {
        int max = Integer.MIN_VALUE;
        int[] indices = new int[2];

        // Left-side
        for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
            int choice = this.score(i, i + d);
            if (choice > max) {
                indices[0] = i;
                indices[1] = i + d;
                max = choice;
            }
        }

        // Right-side
        for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
            int choice = this.score(j - d, j);
            if (choice > max) {
                indices[0] = j - d;
                indices[1] = j;
                max = choice;
            }
        }

//        assert(indices[1]-indices[0] >= 0);
//        if (indices[1] - indices[0] < 0)
//            return null;

        return indices;
    }

    private void populatePieton(int[][][] pieton) {
        for (int i = 0; i < this.pile.length; i++) {
            for (int j = i; j < this.pile.length; j++) {
                pieton[i][j] = this.Pieton(i, j);
            }
        }
    }

    private int[] computeRemainingPile(int left_bound, int right_bound, int[][][] pieton) {

        int[] indices = pieton[left_bound][right_bound];

        // Pieton chose from the left
        if (left_bound == indices[0]) {
            if (indices[1] + 1 > right_bound) return null;
            return new int[]{indices[1] + 1, right_bound};
        }
        // Pieton chose from the right
        else {
            if (left_bound > indices[0] - 1) return null;
            return new int[]{left_bound, indices[0] - 1};
        }
    }

    public int computeScore() {

        int[][] scores = new int[this.pile.length][this.pile.length];

        int[][][] pieton = new int[this.pile.length][this.pile.length][2];

        // If Pieton moves first we need to find his move
        if (!this.firstPlayer) {
            int[] pietonChoice = this.Pieton(0, pile.length - 1);

            if (pietonChoice[0] == 0) {
                this.pile = Arrays.copyOfRange(this.pile, pietonChoice[1] + 1, this.pile.length);
            } else {
                this.pile = Arrays.copyOfRange(this.pile, 0, pietonChoice[0]);
            }

            if (this.pile.length == 0) {
                return 0;
            }
        }

        // Populate Pieton's matrix
        this.populatePieton(pieton);

        // Fill first 'line' with the pile values
        for (int i = 0; i < this.pile.length; i++) {
            scores[i][i] = this.pile[i];
        }

        int max;
        int score;

        for (int difference = 1; difference < this.pile.length; difference++) {
            for (int i = 0; i < this.pile.length - difference; i++) {
                int j = i + difference;

                max = Integer.MIN_VALUE;

                // Left-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
                    score = this.score(i, i + d);

                    int[] pietonPos = null;
                    // TODO
                    if (i + d != j) pietonPos = this.computeRemainingPile(i + d + 1, j, pieton);
                    score += (pietonPos == null) ? 0 : scores[pietonPos[0]][pietonPos[1]];

                    max = Math.max(max, score);
                }

                // Right-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
                    score = score(j - d, j);

                    int[] pietonPos = null;
                    if (i != j - d) pietonPos = this.computeRemainingPile(i, j - d - 1, pieton);

                    score += (pietonPos == null) ? 0 : scores[pietonPos[0]][pietonPos[1]];

                    max = Math.max(max, score);
                }

                scores[i][j] = max;

            }
        }

        return scores[0][this.pile.length - 1];
    }
}
