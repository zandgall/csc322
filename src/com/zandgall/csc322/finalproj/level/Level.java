/* CSC 322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

 ## Level
 # Stores information about the world, and serves as a wrapper to update and render all things in a level

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.level;

import com.zandgall.csc322.finalproj.entity.EntityRegistry;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.entity.Cloud;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.Camera;
import com.zandgall.csc322.finalproj.Main;

import javafx.scene.transform.Affine;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.IOException;

public class Level {
	public Rectangle bounds = new Rectangle(0, 0, 0, 0);
	private HashMap<Integer, HashMap<Integer, Tile>> level = new HashMap<>();

	private static final int CHUNK_SIZE = 128;
	private static final boolean USE_TILES = false;

	private HashMap<Integer, HashMap<Integer, Image>> images_0 = new HashMap<>();
	private HashMap<Integer, HashMap<Integer, Image>> images_1 = new HashMap<>();
	private HashMap<Integer, HashMap<Integer, Image>> shadow_0 = new HashMap<>();
	private HashMap<Integer, HashMap<Integer, Image>> shadow_1 = new HashMap<>();

	private ArrayList<Entity> entities = new ArrayList<>(), removeQueue = new ArrayList<>(),
			addQueue = new ArrayList<>();
	private ArrayList<Cloud> clouds = new ArrayList<>();

	public Level() {
	}

	public void load(String path) throws IOException {

		// Clear level data
		level.clear();
		entities.clear();

		// Load resource
		ObjectInputStream s = new ObjectInputStream(Level.class.getResourceAsStream(path));

		// Check version number
		byte major = s.readByte();
		byte minor = s.readByte();

		if (major != 1 || (minor != 1 && minor != 2)) {
			System.err.println("Unknown level version!!");
			return;
		}

		// Read Y range
		for (int y = s.readInt(), end = y + s.readInt(); y <= end; y++) {
			// The left x position of this line
			int x = s.readInt();
			boolean reading = true;
			while (reading) {
				// Read tile for this position
				int tile = s.readInt();

				// If the tile is empty,
				if (tile == 0) {
					// Read next int
					int next = s.readInt();
					// If it is 0, it signifies the end of this line
					if (next == 0)
						reading = false;
					// If it's not 0, jump forward 'next' number of tiles
					// Functionally the same as adding 'next' number of empty tiles
					else {
						x += next;
						if(major == 1 && minor == 1)
							x ++; // 1.1 has off by one error
					}
				} else {
					// Place the tile
					put(x, y, Tile.get(tile));
					x++;
				}
			}
		}

		int numEntities = s.readInt();
		for (int i = 0; i < numEntities; i++) {
			// Read data and construct entity
			String entityName = s.readUTF();
			double x = s.readDouble(), y = s.readDouble();

			Class<?> entityClass = EntityRegistry.nameMap.get(entityName);
			addEntity(EntityRegistry.construct(entityClass, x, y));
		}

		// Populate clouds
		Random r = new Random();
		for (int i = 0; i < bounds.width * bounds.height / 200; i++)
			clouds.add(new Cloud(r.nextDouble(bounds.x, bounds.x + bounds.width),
					r.nextDouble(bounds.y, bounds.y + bounds.height)));

		s.close();

		// Load level graphics
		if(!USE_TILES)
			loadGraphics();
	}

	public void loadGraphics() {
		Canvas cropper = new Canvas(CHUNK_SIZE, CHUNK_SIZE);
		SnapshotParameters p = new SnapshotParameters();
		p.setFill(Color.TRANSPARENT);
		GraphicsContext g = cropper.getGraphicsContext2D();
		Image l0 = new Image("/level_0.png");
		Image l1 = new Image("/level_1.png");
		Image s0 = new Image("/shadow_0.png");
		Image s1 = new Image("/shadow_1.png");
		for(int i = 0; i < l0.getWidth() / CHUNK_SIZE; i++) {
			images_0.put(i, new HashMap<>());
			images_1.put(i, new HashMap<>());
			shadow_0.put(i, new HashMap<>());
			shadow_1.put(i, new HashMap<>());
			for(int j = 0; j < l0.getHeight() / CHUNK_SIZE; j++) {
				g.clearRect(0, 0, CHUNK_SIZE, CHUNK_SIZE);
				g.drawImage(l0, -i*CHUNK_SIZE, -j*CHUNK_SIZE);
				images_0.get(i).put(j, cropper.snapshot(p, null));
				g.clearRect(0, 0, CHUNK_SIZE, CHUNK_SIZE);
				g.drawImage(l1, -i*CHUNK_SIZE, -j*CHUNK_SIZE);
				images_1.get(i).put(j, cropper.snapshot(p, null));
				g.clearRect(0, 0, CHUNK_SIZE, CHUNK_SIZE);
				g.drawImage(s0, -i*CHUNK_SIZE, -j*CHUNK_SIZE);
				shadow_0.get(i).put(j, cropper.snapshot(p, null));
				g.clearRect(0, 0, CHUNK_SIZE, CHUNK_SIZE);
				g.drawImage(s1, -i*CHUNK_SIZE, -j*CHUNK_SIZE);
				shadow_1.get(i).put(j, cropper.snapshot(p, null));

			}
		}

	}

	public void tick() {
		Camera c = Main.getCamera();
		// TODO: Pull out to Camera.getScreenBounds()
		Hitbox screen = new Hitbox(c.getX() - 0.5 * Main.layer_0.getWidth() / c.getZoom(),
				c.getY() - 0.5 * Main.layer_0.getHeight() / c.getZoom(), Main.layer_0.getWidth() / c.getZoom(),
				Main.layer_0.getHeight() / c.getZoom());

		flushEntityQueues();

		for (Cloud cloud : clouds)
			cloud.tick();
	}

	public void flushEntityQueues() {
		Camera c = Main.getCamera();
		Hitbox screen = new Hitbox(c.getX() - 0.5 * Main.layer_0.getWidth() / c.getZoom(),
			c.getY() - 0.5 * Main.layer_0.getHeight() / c.getZoom(), Main.layer_0.getWidth() / c.getZoom(),
			Main.layer_0.getHeight() / c.getZoom());

		for (Entity e : addQueue)
			if (!entities.add(e))
				System.err.println("Could not add entity!");
		addQueue.clear();

		for (Entity e : entities)
			if (e.getUpdateBounds().intersects(screen))
				e.tick();

		for (Entity e : removeQueue)
			if (!entities.remove(e))
				System.err.println("Asked to remove entity that does not exist...");
		removeQueue.clear();
	}

	/**
	 * A level render method provided with several graphical layers
	 * 
	 * @param context_0 A context for layer 0 - usually reserved for tiles
	 * @param context_1 A context for layer 1
	 * @param shadow_0  A context specifically for shadows (applies to layers 1
	 *                  and 0)
	 * @param context_2 A context for layer 2
	 * @param shadow_1  A context specifically for shadows (applies to all layers
	 *                  below)
	 */
	public void render(GraphicsContext context_0, GraphicsContext context_1, GraphicsContext shadow_0,
			GraphicsContext context_2, GraphicsContext shadow_1) {
		// All contexts should use the same transform at this time
		Affine af = context_0.getTransform();
		int xMin = (int) Math.floor(-af.getTx() / af.getMxx());
		int xMax = (int) (-af.getTx() / af.getMxx() + (1 / af.getMxx()) * Main.layer_0.getWidth());
		int yMin = (int) Math.floor(-af.getTy() / af.getMyy());
		int yMax = (int) (-af.getTy() / af.getMyy() + (1 / af.getMyy()) * Main.layer_0.getHeight());
		// System.out.printf("(%d, %d) to (%d, %d) - (%.2f, %.2f, %.2f, %.2f)%n", xMin,
		// yMin, xMax, yMax, af.getMxx(), af.getMyy(), af.getTx(), af.getTy());
		if(USE_TILES)
			for (int x = xMin; x <= xMax; x++)
				for (int y = yMin; y <= yMax; y++) {
					if (level.get(x) == null || level.get(x).get(y) == null)
						continue;
					context_0.save();
					context_0.translate(x, y);
					level.get(x).get(y).render(context_0);
					context_0.restore();
				}
		else {
			xMin = (xMin - bounds.x) / (CHUNK_SIZE / 16);
			yMin = (yMin - bounds.y) / (CHUNK_SIZE / 16);
			xMax = (xMax - bounds.x) / (CHUNK_SIZE / 16);
			yMax = (yMax - bounds.y) / (CHUNK_SIZE / 16);
			for (int x = xMin; x <= xMax; x++) {
				for (int y = yMin; y <= yMax; y++) {
					if(images_0.get(x) == null || images_0.get(x).get(y) == null)
						continue;
					context_0.drawImage(images_0.get(x).get(y), x*CHUNK_SIZE / 16 + bounds.x, y * CHUNK_SIZE / 16 + bounds.y, CHUNK_SIZE/16, CHUNK_SIZE / 16);
					context_2.drawImage(images_1.get(x).get(y), x*CHUNK_SIZE / 16 + bounds.x, y * CHUNK_SIZE / 16 + bounds.y, CHUNK_SIZE/16, CHUNK_SIZE / 16);
					shadow_0.drawImage(this.shadow_0.get(x).get(y), x*CHUNK_SIZE / 16 + bounds.x, y * CHUNK_SIZE / 16 + bounds.y, CHUNK_SIZE/16, CHUNK_SIZE / 16);
					shadow_1.drawImage(this.shadow_1.get(x).get(y), x*CHUNK_SIZE / 16 + bounds.x, y * CHUNK_SIZE / 16 + bounds.y, CHUNK_SIZE/16, CHUNK_SIZE / 16);
				}
			}
		}

		// Sort and draw all entities and then clouds if they intersect the screen
		Rectangle2D.Double screenBounds = new Rectangle2D.Double(-af.getTx() / af.getMxx(), -af.getTy() / af.getMyy(),
				Main.layer_0.getWidth() / af.getMxx(), Main.layer_0.getHeight() / af.getMyy());
		entities.sort((a, b) -> {
			return (int) Math.signum(a.getRenderLayer() - b.getRenderLayer());
		});
		for (Entity e : entities) {
			if (e.getRenderBounds().intersects(screenBounds))
				e.render(context_1, shadow_0, context_2);
		}
		for (Cloud c : clouds) {
			if (c.getRenderBounds().intersects(screenBounds))
				c.render(shadow_1);
		}
	}

	public void writeImage() {
		Canvas c = new Canvas(bounds.getWidth() * 16, bounds.getHeight()*16);
		GraphicsContext g = c.getGraphicsContext2D();
		for(Map.Entry<Integer, HashMap<Integer, Tile>> xT : level.entrySet())
			for(Map.Entry<Integer, Tile> yT : xT.getValue().entrySet()) {
				g.setTransform(16, 0, 0, 16, xT.getKey()*16-bounds.getMinX()*16, yT.getKey()*16-bounds.getMinY()*16);
				yT.getValue().render(g);
			}
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(c.snapshot(null, null), null), "png", new File("res/level.png"));
		} catch (IOException io) {
			System.err.println("Couldn't write level image");
			io.printStackTrace();
		}
	}

	public void put(int x, int y, Tile tile) {
		// Expand the bounds of the level to get minimums and maximums of coords
		bounds.add(x, y);
		if (level.get(x) == null)
			level.put(x, new HashMap<Integer, Tile>());
		level.get(x).put(y, tile);
	}

	public Tile get(int x, int y) {
		if (level.get(x) == null)
			return null;
		return level.get(x).get(y);
	}

	public void addEntity(Entity e) {
		addQueue.add(e);
	}

	public void removeEntity(Entity e) {
		removeQueue.add(e);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
