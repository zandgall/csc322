/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Player
 # An entity that is controllable by the user

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import java.awt.geom.Rectangle2D;
import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.level.tile.Tile;

public class Player extends Entity {

	private double dashTimer = 0;

	public Player() {
		super();
	}

	public Player(double x, double y) {
		super(x, y);
	}

	public void tick(double delta) {
		// Arrow keys to move
		if(Main.keys.get(KeyCode.RIGHT))
			xVel += delta * 20;
		if(Main.keys.get(KeyCode.LEFT))
			xVel -= delta * 20;
		if(Main.keys.get(KeyCode.DOWN))
			yVel += delta * 20;
		if(Main.keys.get(KeyCode.UP))
			yVel -= delta * 20;

		// If player presses Z and ready to dash, dash in direction we're moving
		if(Main.keys.get(KeyCode.Z) && dashTimer <= 0) {
			double vel = Math.sqrt(xVel * xVel + yVel * yVel);

			xVel *= delta * 1000 / vel;
			yVel *= delta * 1000 / vel;

			// Next dash in 0.5 seconds
			dashTimer = 0.5;
		}
		
		// Decrease dash timer to 0 over time
		if(dashTimer > 0)
			dashTimer -= delta;
	
		//System.out.printf("%.2f, %.2f%n", xVel, yVel);
		move(delta);
	}

	@Override
	public void render(GraphicsContext g, GraphicsContext ignore_shadow, GraphicsContext ignore_2) {
		g.setFill(Color.color(1, 0, 0, 1));
		g.fillRect(x-0.5, y-0.5, 1, 1);
		g.setStroke(Color.BLACK);
		g.setLineWidth(0.01);
		g.strokeRect(x-0.5, y-0.5, 1.0, 1.0);

		Hitbox box = new Hitbox(x-0.5, y-0.5, 1.0, 1.0);
		int minX = (int)Math.floor(box.getBounds().getMinX());
		int minY = (int)Math.floor(box.getBounds().getMinY());
		int maxX = (int)Math.floor(box.getBounds().getMaxX());
		int maxY = (int)Math.floor(box.getBounds().getMaxY());
		Hitbox tilebox = new Hitbox(0, 0, 1, 1);
		for(int i = minX; i <= maxX; i++) {
			for(int j = minY; j <= maxY; j++) {
				Tile t = Main.getLevel().get(i, j);
				if(t != null && t.solidBounds(i, j) != null && t.solidBounds(i, j).intersects(box)) {
					g.setStroke(Color.RED);
					tilebox = t.solidBounds(i, j);
					for(Rectangle2D r : tilebox.getBoxes())
						g.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				} else {
					g.setStroke(Color.BLUE);
					g.strokeRect(i, j, 1, 1);
				}
			}
		}
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getSolidBounds() {
		return new Hitbox(x - 0.4, y - 0.4, 0.8, 0.8);
	}

}
