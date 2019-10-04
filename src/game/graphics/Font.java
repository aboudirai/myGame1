package game.graphics;

public class Font {
	private String chars = "" + //
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + //
			"0123456789.,!?'\"-+=/\\%()<>		" + //
			"";

	public void draw(String msg, Screen screen, int x, int y) {
		msg = msg.toUpperCase();
		for (int i = 0; i < msg.length(); i++) {
			int ix = chars.indexOf(msg.charAt(i));
			if (ix >= 0) {
				screen.setTile(x + i, y, ix + 32 * 30, (5 + 5 * 6 + 5 * 36) * 256 * 256 * 256, 0);
			}
		}
	}

}
