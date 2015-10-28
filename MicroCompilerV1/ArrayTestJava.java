public class ArrayTestJava {
	public static int[] x = new int[10];
	public static void proc() {
		double y[] = new double[10];
		double z;
		int i;
		x[5] = 9;
		y[6] = 12;
		z = x[5];
		i = (int)y[6];
		x[6] = (int)y[6];
		y[5] = x[5];
	}
	public static void main(String[] args) {
	}
}
