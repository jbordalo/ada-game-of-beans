public class GameOfBeans {

    private int gameDepth;
    private boolean isJabaFirst;
    private int[][] scores;
    private int BEGIN;
    private int END;

    public GameOfBeans(int[] pile, int gameDepth, String firstPlayer) {
        this.gameDepth = gameDepth;
        this.isJabaFirst = firstPlayer.equals("Jaba");
        this.BEGIN = 0;
        this.END = pile.length;
        populateScores(pile);
    }

    private int[] Pieton(int i, int j) {
        int max = Integer.MIN_VALUE;
        int[] indices = new int[2];

        // Left-side
        for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
            int choice = this.scores[i][i + d];
            if (choice > max) {
                indices[0] = i;
                indices[1] = i + d;
                max = choice;
            }
        }

        // Right-side
        for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
            int choice = this.scores[j - d][j];
            if (choice > max) {
                indices[0] = j - d;
                indices[1] = j;
                max = choice;
            }
        }

        return indices;
    }

    private void populatePieton(int[][][] pieton) {
        for (int i = 0; i < this.END; i++) {
            for (int j = i; j < this.END; j++) {
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

    private void populateScores(int[] pile) {
        this.scores = new int[pile.length][pile.length];

        for (int i = this.BEGIN; i < this.END; i++) {
            int score = 0;
            for (int j = i; j < this.END; j++) {
                score += pile[j];
                this.scores[i][j] = score;
            }
        }
    }

    public int computeScore() {

        int[][][] pieton = new int[this.END][this.END][2];
        int[][] jaba = new int[this.END][this.END];

        // Populate Pieton's matrix
        this.populatePieton(pieton);

        // If Pieton moves first we need to find his move
        if (!this.isJabaFirst) {
            int[] pietonChoice = pieton[0][this.END - 1];

            if (pietonChoice[0] == 0) {
                this.BEGIN = pietonChoice[1] + 1;
            } else {
                this.END = pietonChoice[0];
            }

            if (this.BEGIN >= this.END) {
                return 0;
            }
        }

        // Fill first diagonal with the pile values
        for (int i = this.BEGIN; i < this.END; i++) {
            jaba[i][i] = this.scores[i][i];
        }

        int max;
        int score;

        // Run through the matrix diagonally
        for (int difference = 1; difference < this.END; difference++) {
            for (int i = this.BEGIN; i < this.END - difference; i++) {
                int j = i + difference;

                max = Integer.MIN_VALUE;

                // Left-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
                    score = this.scores[i][i + d];

                    // remainingPile is null if it's empty
                    int[] remainingPile = null;
                    if (i + d != j) remainingPile = this.computeRemainingPile(i + d + 1, j, pieton);
                    score += (remainingPile == null) ? 0 : jaba[remainingPile[0]][remainingPile[1]];

                    max = Math.max(max, score);
                }

                // Right-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
                    score = this.scores[j - d][j];

                    // remainingPile is null if it's empty
                    int[] remainingPile = null;
                    if (i != j - d) remainingPile = this.computeRemainingPile(i, j - d - 1, pieton);

                    score += (remainingPile == null) ? 0 : jaba[remainingPile[0]][remainingPile[1]];

                    max = Math.max(max, score);
                }

                jaba[i][j] = max;
            }
        }

        return jaba[this.BEGIN][this.END - 1];
    }
}
