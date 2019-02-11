import java.util.Scanner;

public class main {

	public static void clear() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void main(String args[]) {
		clear();

		game g = new game();
		g.loop();
		// g.PrintBoard();

	}
}