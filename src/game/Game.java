package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import game.graphics.Color;
import game.graphics.Screen;
import game.graphics.SpriteSheet;

public class Game extends Canvas implements Runnable {

	// 10:11:26 LEFT OFF - making font

	private static final long serialVersionUID = 1L;

	public static final String NAME = "Untitled Game";
	public static final int HEIGHT = 120;
	public static final int WIDTH = 160;
	public static final int BORDER = 8;
	public static final int SCALE = 4;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private boolean running = false;
	private int tickCount;
	private Screen screen;
	private InputHandler input = new InputHandler(this);
	private int dir = 0;
	private int walkDist = 0;
	private boolean walked;
	private int[] colors = new int[256];

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void init() {
		int pp = 0;
		Random random = new Random();
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;
					rr = ((rr + mid) / 2) * 200 / 255 + 35;
					gg = ((gg + mid) / 2) * 200 / 255 + 35;
					bb = ((bb + mid) / 2) * 200 / 255 + 35;

					colors[pp++] = rr << 16 | gg << 8 | bb;
					// colors[pp++] = random.nextInt(0x1000000);
				}
			}
		}
		try {
			screen = new Screen(WIDTH, HEIGHT,
					new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/textureMap.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60.0;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		init();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}
			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				System.out.println(ticks + "ticks " + frames + "fps");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {
		if(!hasFocus()) {
			input.releaseAll();
		}
		
		walked = false;
		
		if (input.up) {
			screen.yScroll--;
			walked = true;
			dir = 1;
		}
		;
		if (input.down) {
			screen.yScroll++;
			walked = true;
			dir = 0;
		}
		if (input.left) {
			screen.xScroll--;
			walked = true;
			dir = 2;
		}
		if (input.right) {
			screen.xScroll++;
			walked = true;
			dir = 3;
		}
		
		
		tickCount++;
		if(walked){
			walkDist++;
		}
	}

	public void render() {

		BufferStrategy bs = getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		screen.render();

		{
			int x0 = WIDTH / 2 - 8;
			int y0 = HEIGHT / 2 - 8;
			int xt = 0;
			int yt = 5;
			
			int flip1 = 0;
			int flip2 = 0;
			
			
			if(dir < 2) {
				xt += 2 * ((walkDist >> 3) & 1);
			}
			if(dir == 1) {
				yt = 7;
			}
			if(dir > 1) {
				xt = 4;
				xt += 2 * ((walkDist >> 3) & 1);
			}
			if(dir == 3) {
				flip1 = 0;
				flip2 = 0;
			}
			if(dir == 2) {
				flip1 = 1;
				flip2 = 1;
			}
			
			if(!walked) {
				if (dir == 0) {
					xt = 4;
					yt = 7;
					flip1 = 0;
					flip2 = 0;
				}
				if (dir == 1) {
					xt = 6;
					yt = 7;
					flip1 = 0;
					flip2 = 0;
				}
				if (dir == 2) {
					xt = 4;
					yt = 5;
					flip1 = 1;
					flip2 = 1;
				}
				if (dir == 3) {
					xt = 4;
					yt = 5;
					flip1 = 0;
					flip2 = 0;
				}
			}
			
			
			screen.render(x0 + 8 * flip1, y0, xt + yt * 32, Color.get(-1, 5, 440, 556), flip1);
			screen.render(x0 + 8 - 8 * flip1, y0, xt + 1 + (yt * 32), Color.get(-1, 5, 440, 556), flip1);
			screen.render(x0 + 8 * flip2, y0 + 8, xt + ((yt + 1) * 32), Color.get(-1, 5, 440, 556), flip2);
			screen.render(x0 + 8 - 8 * flip2, y0 + 8, xt + 1 + ((yt + 1) * 32), Color.get(-1, 556, 440, 555), flip2);
		}

		for (int y = 0; y < screen.h; y++) {
			for (int x = 0; x < screen.w; x++) {
				pixels[x + y * WIDTH] = colors[screen.pixels[x + y * screen.w]];
			}
		}

		Graphics g = bs.getDrawGraphics();
		// g.setColor(Color.GRAY );
		g.fillRect(0, 0, getWidth(), getHeight());

		int ww = WIDTH * SCALE - 8;
		int hh = HEIGHT * SCALE - 8;
		int xo = (getWidth() - ww) / 2;
		int yo = (getHeight() - hh) / 2;
		// g.drawImage(image, xo, yo, ww, hh); DOESNT WORK CUTS PIXELS IN HALF
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		JFrame frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		game.start();

	}

}
