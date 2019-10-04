package game.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Screen {
	private List<Sprite> sprites = new ArrayList<Sprite>();

	private static final int MAP_WIDTH = 64;
	private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	private static final int SPRITE_SHEET_SIZE = 256;

	public int[] tiles = new int[MAP_WIDTH * MAP_WIDTH];
	public int[] colors = new int[MAP_WIDTH * MAP_WIDTH];
	public int[] databits = new int[MAP_WIDTH * MAP_WIDTH];
	public int xScroll;
	public int yScroll;

	public static final int BIT_MIRROR_X = 0x01;
	public static final int BIT_MIRROR_Y = 0x02;

	public final int w, h;
	public int[] pixels;

	private SpriteSheet sheet;

	public Screen(int w, int h, SpriteSheet sheet) {
		this.sheet = sheet;
		this.w = w;
		this.h = h;

		pixels = new int[w * h];

		Random random = new Random();

		for (int i = 0; i < MAP_WIDTH * MAP_WIDTH; i++) {
			//colors[i] = Color.get(0, 0, 40, 30);
			
			if (random.nextInt(40) == 0) {
				tiles[i] = 32;
				colors[i] = Color.get(111, 333, 40, 2221);
				databits[i] = random.nextInt(2);
			}
			else if (random.nextInt(40) == 0) {
				tiles[i] = 33;
				colors[i] = Color.get(20, 30, 40, 550);
			}
			else {
				tiles[i] = random.nextInt(4);
				colors[i] = Color.get(0, 0, 40, 30);
				databits[i] = random.nextInt(4);
				
			}
		}

		new Font().draw("   M A N - H U N T    ", this, 0, 0);

	}

	public void render() {
		for (int yt = yScroll >> 3; yt <= (yScroll + h) >> 3; yt++) {
			int yp = yt * 8 - yScroll;
			for (int xt = xScroll >> 3; xt <= (xScroll + w) >> 3; xt++) {
				int xp = xt * 8 - xScroll;

				int ti = (xt & (MAP_WIDTH_MASK)) + (yt & (MAP_WIDTH_MASK)) * MAP_WIDTH;

				render(xp, yp, tiles[ti], colors[ti], databits[ti]);
			}
		}

		for (int i = 0; i < sprites.size(); i++) {
			Sprite s = sprites.get(i);
			render(s.x, s.y, s.img, s.col, s.bits);
		}
		sprites.clear();
	}

	public void render(int xp, int yp, int tile, int colors, int bits) {
		boolean mirrorX = (bits == 1);
		boolean mirrorY = (bits == 2);
		
		int xTile = tile % 32;
		int yTile = tile / 32;
		int toffs = xTile * 8 + yTile * 8 * sheet.width;

		for (int y = 0; y < 8; y++) {
			int ys = y;
			if (mirrorY)
				ys = 7 - y;
			if (y + yp < 0 || y + yp >= h)
				continue;

			for (int x = 0; x < 8; x++) {
				if (x + xp < 0 || x + xp >= w)
					continue;

				int xs = x;
				if (mirrorX)
					xs = 7 - x;
				int col = (colors >> sheet.pixels[xs + ys * sheet.width + toffs] * 8) & 255;
				if (col < 255)
					pixels[(x + xp) + (y + yp) * w] = col;

			}
		}

	}

	public void setTile(int x, int y, int tile, int color, int bits) {
		int tp = (x & MAP_WIDTH_MASK) + (y & MAP_WIDTH_MASK) * MAP_WIDTH;
		tiles[tp] = tile;
		colors[tp] = color;
		databits[tp] = bits;
	}

	public void addSprite(Sprite sprite) {
		// TODO Auto-generated method stub
		
	}

}