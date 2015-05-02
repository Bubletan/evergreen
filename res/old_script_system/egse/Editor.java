package egse;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

final class Editor extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 2587396492187836004L;

	private ArrayList<Character> queue;
	private ArrayList<Character> content;
	
	private Graphics2D graphics;
	private int width;
	private int height;
	private BufferedImage image;
	
	Editor() {
		Dimension size = new Dimension(500, 400);
		setMinimumSize(size);
		setPreferredSize(size);
		width = 500;
		height = 400;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
		queue = new ArrayList<Character>();
		content = new ArrayList<Character>();
		addKeyListener(this);
		Thread thread = new Thread(this);
		thread.setPriority(2);
		thread.start();
	}
	
	@Override
	public void paint(Graphics g) {
	}
	
	@Override
	public void update(Graphics g) {
	}

	@Override
	public void run() {
		while (true) {
			long time = System.currentTimeMillis();
			if (getGraphics() != null) {
				width = getWidth();
				height = getHeight();
				if (width > image.getWidth() || height > image.getHeight()) {
					graphics.dispose();
					image = new BufferedImage(image.getWidth() << 1, image.getHeight() << 1,
							BufferedImage.TYPE_INT_RGB);
					graphics = image.createGraphics();
				}
				for (char c : queue) {
					if (c == '\b') {
						if (content.size() >= 1) {
							content.remove(content.size() - 1);
						}
						continue;
					}
					content.add(c);
				}
				queue.clear();
				graphics = image.createGraphics();
				draw();
				graphics.dispose();
				getGraphics().drawImage(image, 0, 0, null);
			}
			time = 20 - (System.currentTimeMillis() - time);
			if (time > 0) {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	public void draw() {
		graphics.setColor(new Color(0x222222));
		graphics.fillRect(0, 0, 25, height);
		graphics.setColor(new Color(0x333333));
		graphics.fillRect(25, 0, width - 25, height);
		graphics.setColor(new Color(0xffffff));
		FontMetrics fm = graphics.getFontMetrics();
		int dy = fm.getHeight();
		int x = 30, y = dy;
		graphics.setColor(Color.GRAY);
		graphics.drawString(Integer.toString((y / dy)), 5, y);
		graphics.setColor(new Color(0xcccccc));
		for (char c : content) {
			if (c == '\n' || c == '\r') {
				y += dy;
				Color save = graphics.getColor();
				graphics.setColor(Color.GRAY);
				graphics.drawString(Integer.toString((y / dy)), 5, y);
				graphics.setColor(save);
				x = 30;
				continue;
			}
			if (c == '@') {
				graphics.setColor(new Color(0xff8040));
			}
			if (c != ' ') {
				graphics.drawString(Character.toString(c), x, y);
			}
			if (c == '@') {
				graphics.setColor(new Color(0xcccccc));
			}
			x += fm.charWidth(c);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
		queue.add(event.getKeyChar());
	}
}
