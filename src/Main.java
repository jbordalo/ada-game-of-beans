import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        int testCases = Integer.parseInt(input.readLine());

//        long time = 0;

        for (int i = 0; i < testCases; i++) {
//            long init = System.nanoTime();
            solveTestCase(input);
//            long end = System.nanoTime();
//            time += (end-init);
        }

//        System.out.printf("Time:%f", (double) time / testCases / 1000000);
        input.close();
    }

    public static void solveTestCase(BufferedReader input) throws IOException {

        String[] tokens = input.readLine().split(" ");
        int nPiles = Integer.parseInt(tokens[0]);
        int gameDepth = Integer.parseInt(tokens[1]);

        String[] pileInput = input.readLine().split(" ");
        int[] pile = new int[nPiles];

        for (int i = 0; i < pileInput.length; i++) {
            pile[i] = Integer.parseInt(pileInput[i]);
        }

        String firstPlayerInput = input.readLine();
        boolean firstPlayer = firstPlayerInput.equals("Jaba");

        GameOfBeans game = new GameOfBeans(pile, gameDepth, firstPlayer);

        int answer = game.computeScore();

        System.out.println(answer);

    }
}
