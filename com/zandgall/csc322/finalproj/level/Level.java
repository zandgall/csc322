/* CSC 322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Level
 # Stores information about the world, and serves as a wrapper to update and render all things in a level

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.level;

import com.zandgall.csc322.finalproj.level.tile.Tile;
import com.zandgall.csc322.finalproj.entity.EntityRegistry;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.entity.Cloud;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.Camera;
import com.zandgall.csc322.finalproj.Main;

import javafx.scene.transform.Affine;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Level {
	public Rectangle bounds = new Rectangle(0,0,0,0);
	private HashMap<Integer, HashMap<Integer, Tile>> level = new HashMap<>();

	private ArrayList<Entity> entities = new ArrayList<>(), removeQueue = new ArrayList<>();
	private ArrayList<Cloud> clouds = new ArrayList<>();

	public Level() {}

	public void load(String filepath) throws IOException {

		// Clear level data
		level.clear();
		entities.clear();

		// Load file
		FileInputStream fis;
		try {
			fis = new FileInputStream(filepath);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
	
		ObjectInputStream s = new ObjectInputStream(fis);
		
		// Check version number
		byte major = s.readByte();
		byte minor = s.readByte();

		if(major != 1 || minor != 1) {
			System.err.println("Unknown level version!!");
			return;
		}

		// Read Y range
		for(int y = s.readInt(), end = y + s.readInt(); y <= end; y++) {
			// The left x position of this line
			int x = s.readInt();
			boolean reading = true;
			while(reading) {
				// Read tile for this position
				int tile = s.readInt();

				// If the tile is empty,
				if(tile == 0) {
					// Read next int
					int next = s.readInt();
					// If it is 0, it signifies the end of this line
					if(next == 0)
						reading = false;
					// If it's not 0, jump forward 'next' number of tiles
					// Functionally the same as adding 'next' number of empty tiles
					else
						x += next;
				} else {
					// Place the tile
					put(x, y, Tile.get(tile));
					x++;
				}
			}
		}

		int numEntities = s.readInt();
		for(int i = 0; i < numEntities; i++) {
			// Read data and construct entity
			String entityName = s.readUTF();
			double x = s.readDouble(), y = s.readDouble();

			Class entityClass = EntityRegistry.nameMap.get(entityName);
			addEntity(EntityRegistry.construct(entityClass, x, y));
		}

		// Populate clouds
		Random r = new Random();
		System.out.printf("Using bounds to create clouds: %d %d %d %d%n", bounds.x, bounds.x + bounds.width, bounds.y, bounds.y+bounds.height);
		for(int i = 0; i < bounds.width * bounds.height / 200; i++) {
			clouds.add(new Cloud(r.nextDouble(bounds.x, bounds.x+bounds.width), r.nextDouble(bounds.y, bounds.y+bounds.height)));
			System.out.printf("Adding cloud at %.2f %.2f%n", clouds.get(i).getX(), clouds.get(i).getY());
		}

		s.close();
	}

	public void tick(Double delta) {
		Camera c = Main.getCamera();
		// TODO: Pull out to Camera.getScreenBounds()
		Hitbox screen = new Hitbox(c.getX()-0.5 * Main.layer_0.getWidth() / c.getZoom(), c.getY() - 0.5 * Main.layer_0.getHeight() / c.getZoom(), Main.layer_0.getWidth() / c.getZoom(), Main.layer_0.getHeight() / c.getZoom());
		for(Entity e : entities)
			if(e.getUpdateBounds().intersects(screen))
				e.tick(delta);

		for(Entity e : removeQueue)
			if(!entities.remove(e))
				System.err.println("Asked to remove entity that does not exist...");
		removeQueue.clear();

		for(Cloud cloud : clouds)
			cloud.tick(delta);
	}

	/*
	* A level render method provided with several graphical layers
	* @param context_0 A context for layer 0 - usually reserved for tiles
	* @param context_1 A context for layer 1
	* @param context_shadow A context specifically for shadows (applies to layers 1 and 0)
	* @param context_2 A context for layer 2
	*/
	public void render(GraphicsContext context_0, GraphicsContext context_1, GraphicsContext shadow_0, GraphicsContext context_2, GraphicsContext shadow_1) {
		// All contexts should use the same transform at this time
		Affine af = context_0.getTransform();
		int xMin = (int)Math.floor(-af.getTx()/af.getMxx());
		int xMax = (int)(-af.getTx()/af.getMxx() + (1 / af.getMxx()) * Main.layer_0.getWidth());
		int yMin = (int)Math.floor(-af.getTy()/af.getMyy());
		int yMax = (int)(-af.getTy()/af.getMyy() + (1 / af.getMyy()) * Main.layer_0.getHeight());
		//System.out.printf("(%d, %d) to (%d, %d) - (%.2f, %.2f, %.2f, %.2f)%n", xMin, yMin, xMax, yMax, af.getMxx(), af.getMyy(), af.getTx(), af.getTy());
		for(int x = xMin; x <= xMax; x++)
			for(int y = yMin; y <= yMax; y++) {
				if(level.get(x) == null || level.get(x).get(y) == null)
					continue;
				context_0.save();
				context_0.translate(x, y);
				level.get(x).get(y).render(context_0);
				context_0.restore();
			}

		Rectangle2D.Double screenBounds = new Rectangle2D.Double(-af.getTx()/af.getMxx(), -af.getTy()/af.getMyy(), Main.layer_0.getWidth() / af.getMxx(), Main.layer_0.getHeight() / af.getMyy());
		entities.sort((a,b) -> {return (int)Math.signum(a.getRenderLayer() - b.getRenderLayer()); });
		for(Entity e : entities) {
			if(e.getRenderBounds().intersects(screenBounds))
				e.render(context_1, shadow_0, context_2);
		}
		for(Cloud c : clouds) {
			if(c.getRenderBounds().intersects(screenBounds))
				c.render(shadow_1);
		}
	}

	public void put(int x, int y, Tile tile) {
		// Expand the bounds of the level to get minimums and maximums of coords
		bounds.add(x, y);
		if(level.get(x) == null)
			level.put(x, new HashMap<Integer, Tile>());
		level.get(x).put(y, tile);
	}

	public Tile get(int x, int y) {
		if(level.get(x) == null)
			return null;
		return level.get(x).get(y);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void removeEntity(Entity e) {
		removeQueue.add(e);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
