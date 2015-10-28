import java.util.Scanner;
public class Add {
	private static int x;
	private static int y;
	private static int z;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		x = in.nextInt();
		y = in.nextInt();
		z = x + y;
		System.out.println(z);
	}
}
