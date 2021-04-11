import java.util.Arrays;

public class GameOfBeans {

    private int[] pile;
    private final int gameDepth;
    private final boolean firstPlayer;
    private int[][] scores;
    private int BEGIN;
    private int END;

    public GameOfBeans(int[] pile, int gameDepth, boolean firstPlayer) {
        this.pile = pile;
        this.gameDepth = gameDepth;
        this.firstPlayer = firstPlayer;
        this.scores = new int[this.pile.length][this.pile.length];
        this.BEGIN = 0;
        this.END = this.pile.length;
        populateScores();
    }

    private int score(int i, int j) {
//        System.out.println(i + " " + j);

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
//            int choice = this.score(i, i + d);
            int choice = this.scores[i][i+d];
            if (choice > max) {
                indices[0] = i;
                indices[1] = i + d;
                max = choice;
            }
        }

        // Right-side
        for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
//            int choice = this.score(j - d, j);
            int choice = this.scores[j-d][j];
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

    private void populateScores() {
        //TODO
        for (int i = this.BEGIN; i < this.END; i++) {
            for (int j = i; j < this.END; j++) {
                this.scores[i][j] = this.score(i, j);
            }
        }
    }

    public int computeScore() {

        int[][][] pieton = new int[this.pile.length][this.pile.length][2];
        int[][] jaba = new int[this.pile.length][this.pile.length];

        // Populate Pieton's matrix
        this.populatePieton(pieton);

        //TODO If Pieton moves first we need to find his move
        if (!this.firstPlayer) {
            int[] pietonChoice = this.Pieton(0, this.pile.length - 1);

            if (pietonChoice[0] == 0) {
                this.BEGIN = pietonChoice[1]+1;
                // this.pile = Arrays.copyOfRange(this.pile, pietonChoice[1] + 1, this.pile.length);
            } else {
                this.END = pietonChoice[0];
                // this.pile = Arrays.copyOfRange(this.pile, 0, pietonChoice[0]);

            }

            if (this.BEGIN >= this.END) {
                return 0;
            }
        }

        //TODO populateScores();

        // Fill first 'line' with the pile values
        for (int i = this.BEGIN; i < this.END; i++) {
            jaba[i][i] = this.pile[i];
        }

        int max;
        int score;

        for (int difference = 1; difference < this.END; difference++) {
            for (int i = this.BEGIN; i < this.END - difference; i++) {
                int j = i + difference;

                max = Integer.MIN_VALUE;

                // Left-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
//                    score = this.score(i, i + d);
                    score = this.scores[i][i+d];

                    int[] pietonPos = null;
                    // TODO
                    if (i + d != j) pietonPos = this.computeRemainingPile(i + d + 1, j, pieton);
                    score += (pietonPos == null) ? 0 : jaba[pietonPos[0]][pietonPos[1]];

                    max = Math.max(max, score);
                }

                // Right-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
//                    score = score(j - d, j);
                    score = this.scores[j-d][j];

                    int[] pietonPos = null;
                    if (i != j - d) pietonPos = this.computeRemainingPile(i, j - d - 1, pieton);

                    score += (pietonPos == null) ? 0 : jaba[pietonPos[0]][pietonPos[1]];

                    max = Math.max(max, score);
                }

                jaba[i][j] = max;
            }
        }

        return jaba[this.BEGIN][this.END - 1];
    }
}
