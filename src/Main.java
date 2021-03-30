import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		int testCases = Integer.parseInt(input.readLine());

		for (int i = 0; i < testCases; i++) {
			solveTestCase(input);
		}

		input.close();
	}

	public static void solveTestCase(BufferedReader input) throws IOException {

		String[] tokens = input.readLine().split(" ");
		int nPiles = Integer.parseInt(tokens[0]);
		int gameDepth = Integer.parseInt(tokens[1]);

		String pile = input.readLine();
		String firstPlayer = input.readLine();

		GameOfBeans game = new GameOfBeans();
		
		int answer = game.computeScore();

		System.out.println(answer);

	}
	
}
