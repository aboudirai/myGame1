package game.graphics;


public class Color{
	
	public static int get(int a, int b, int c, int d) {
		return (getCol(d) << 24) + (getCol(c) << 16) + (getCol(b) << 8) + (getCol(a));
	}

	public static int getCol(int d) {
		if (d < 0)
			return 255;

		int r = d / 100 % 10;
		int g = d / 10 % 10;
		int b = d % 10;
		
		System.out.println(r);
		System.out.println(g);
		System.out.println(b);
		return r * 36 + g * 6 + b;
	}

}
