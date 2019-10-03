package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;
	
	public void releaseAll() {
		up = down = right = left = false;
	}
	
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		toggle(ke, true);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		toggle(ke, false);
	}
	
	private void toggle(KeyEvent ke, boolean pressed) {
		if(ke.getKeyCode() == KeyEvent.VK_UP) up = pressed;
		if(ke.getKeyCode() == KeyEvent.VK_DOWN) down = pressed;
		if(ke.getKeyCode() == KeyEvent.VK_RIGHT) right = pressed;
		if(ke.getKeyCode() == KeyEvent.VK_LEFT) left = pressed;
	}
	
	public void keyTyped(KeyEvent ke) {
		
	}

}
