/* CSC 322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Level
 # Stores information about the world, and serves as a wrapper to update and render all things in a level

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.level;

import com.zandgall.csc322.finalproj.level.tile.Tile;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.Camera;
import com.zandgall.csc322.finalproj.Main;

import javafx.scene.transform.Affine;
import javafx.scene.canvas.GraphicsContext;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;

public class Level {

	private HashMap<Integer, HashMap<Integer, Tile>> level = new HashMap<>();

	private ArrayList<Entity> entities = new ArrayList<>();

	public Level() {}

	public void tick(Double delta) {
		Camera c = Main.getCamera();
		Hitbox screen = new Hitbox(c.getX()-0.5 * Main.canvas.getWidth() / c.getZoom(), c.getY() - 0.5 * Main.canvas.getHeight() / c.getZoom(), Main.canvas.getWidth() / c.getZoom(), Main.canvas.getHeight() / c.getZoom());
		for(Entity e : entities)
			if(e.getUpdateBounds().intersects(screen))
				e.tick(delta);
	}

	public void render(GraphicsContext g) {
		Affine af = g.getTransform();
		int xMin = (int)Math.floor(-af.getTx()/af.getMxx());
		int xMax = (int)(-af.getTx()/af.getMxx() + (1 / af.getMxx()) * Main.canvas.getWidth());
		int yMin = (int)Math.floor(-af.getTy()/af.getMyy());
		int yMax = (int)(-af.getTy()/af.getMyy() + (1 / af.getMyy()) * Main.canvas.getHeight());
		//System.out.printf("(%d, %d) to (%d, %d) - (%.2f, %.2f, %.2f, %.2f)%n", xMin, yMin, xMax, yMax, af.getMxx(), af.getMyy(), af.getTx(), af.getTy());
		for(int x = xMin; x <= xMax; x++)
			for(int y = yMin; y <= yMax; y++) {
				if(level.get(x) == null || level.get(x).get(y) == null)
					continue;
				g.save();
				g.translate(x, y);
				level.get(x).get(y).render(g);
				g.restore();
			}

		Rectangle2D.Double screenBounds = new Rectangle2D.Double(-af.getTx()/af.getMxx(), -af.getTy()/af.getMyy(), Main.canvas.getWidth() / af.getMxx(), Main.canvas.getHeight() / af.getMyy());
		entities.sort((a,b) -> {return (int)Math.signum(a.getRenderLayer() - b.getRenderLayer()); });
		for(Entity e : entities) {
			if(e.getRenderBounds().intersects(screenBounds))
				e.render(g);
		}
	}

	public void put(int x, int y, Tile tile) {
		if(level.get(x) == null)
			level.put(x, new HashMap<Integer, Tile>());
		level.get(x).put(y, tile);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
