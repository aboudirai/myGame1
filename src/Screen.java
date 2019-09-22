import java.util.ArrayList;
import java.util.List;

public class Screen {
	private List<Sprite> sprites = new ArrayList<Sprite>();

	private static final int MAP_WIDTH = 64;
	private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	private static final int SPRITE_SHEET_SIZE = 256;

	private int[] tiles = new int[MAP_WIDTH * MAP_WIDTH * 2];
	private int[] colors = new int[MAP_WIDTH * MAP_WIDTH * 3];
	private int[] databits = new int[MAP_WIDTH * MAP_WIDTH];
	private int xScroll;
	private int yScroll;

	public final int w, h;

	public Screen(int w, int h) {
		this.w = w;
		this.h = h;
	}
	
	public void render(int[] pixels, int offs, int row) {
		
	}

}