import java.util.Arrays;

public class GameOfBeans {

    private final int LEFT = 0;
    private final int LEFT_MAX = 1;
    private final int RIGHT = 2;
    private final int RIGHT_MAX = 3;

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

    private int[] Pieton(int i, int j) {
        int max = Integer.MIN_VALUE;
        int[] indexes = new int[2];

        // Left-side
        for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
            int choice = score(i, i + d);
            if (choice > max) {
                indexes[0] = i + d + 1;
                indexes[1] = j;
                max = choice;
            }
        }

        // Right-side
        for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
            int choice = score(j - d, j);
            if (choice > max) {
                indexes[0] = i;
                indexes[1] = j - d - 1;
                max = choice;
            }
        }

        if (indexes[1] - indexes[0] < 0)
            return null;

        return indexes;
    }


    private void populatePieton(int[][] pieton) {

            int max;
            int index;
            for (int i = 0; i < pile.length; i++) {
                    // Left-side
                max = Integer.MIN_VALUE;
                index = 0;
                for (int d = 0; d < this.gameDepth && (i+d < pile.length); d++) {
                    int choice = score(i, i + d);
                    if (choice > max) {
                        max = choice;
                        index = d;
                    }
                }
                pieton[i][LEFT] = i+index;
                pieton[i][LEFT_MAX] = max;

                max = Integer.MIN_VALUE;
                for (int d = 0; d < this.gameDepth && !(i-d<0); d++) {
                    int choice = score(i - d, i);
                    if (choice > max) {
                        max = choice;
                        index = d;
                    }
                }
                pieton[i][RIGHT] = i-index;
                pieton[i][RIGHT_MAX] = max;

            }
    }

    private int[] pietonChoice(int left_bound, int right_bound, int[][] pieton) {

        int left_value = pieton[left_bound][LEFT_MAX];
        int right_value = pieton[right_bound][RIGHT_MAX];

        if (left_value >= right_value) {
            int index = pieton[left_bound][LEFT];
            if (index + 1 > right_bound) return null;
            return new int[]{index+1, right_bound};
        } else {
            int index = pieton[right_bound][RIGHT];
            if (left_bound > index-1) return null;
            return new int[]{left_bound, index - 1};
        }
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

        int[][] pieton = new int[pile.length][4];

        // Populate Pieton's matrix
        populatePieton(pieton);

        // Fill first 'line' with the pile values
        for (int i = 0; i < pile.length; i++) {
            scores[i][i] = pile[i];
        }

        int max;
        int score;

        for (int difference = 1; difference < pile.length; difference++) {
            for (int i = 0; i < pile.length - difference; i++) {
                int j = i + difference;

                max = Integer.MIN_VALUE;

                // Left-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
                    score = score(i, i + d);

                    int[] pietonPos = null;
                    if (i + d != j) pietonPos = pietonChoice(i + d + 1, j, pieton);
                    score += (pietonPos == null) ? 0 : scores[pietonPos[0]][pietonPos[1]];

                    max = Math.max(max, score);
                }

                // Right-side
                for (int d = 0; d < this.gameDepth && i + d <= j; d++) {
                    score = score(j - d, j);

                    int[] pietonPos = null;
                    if (i != j - d) pietonPos = pietonChoice(i, j - d - 1, pieton);

                    score += (pietonPos == null) ? 0 : scores[pietonPos[0]][pietonPos[1]];

                    max = Math.max(max, score);
                }

                scores[i][j] = max;

            }
        }

        return scores[0][pile.length - 1];
    }
}
