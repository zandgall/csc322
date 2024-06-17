/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Player
 # An entity that is controllable by the user

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.level.tile.Tile;

public class Player extends Entity {

	public static final double DASH_SPEED = 20, DASH_TIMER = 0.5, MOVE_SPEED = 0.2;

	public static Image sword;

	static {
		try {
			sword = new Image(new FileInputStream("res/entity/sword.png"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load player sword");
			sword = null;
		}
	}

	private boolean hasSword = false;
	private double swordTimer = 0, swordDirection = 0, swordRotationalVelocity = 0;
	private Hitbox swordBox;
	private double dashTimer = 0;

	private double health;
	private long lastHit = System.currentTimeMillis();

	public Player() {
		super();
		health = 20;
	}

	public Player(double x, double y) {
		super(x, y);
		health = 20;
	}

	public void tick() {
		// Arrow keys to move
		if(Main.keys.get(KeyCode.RIGHT))
			xVel += MOVE_SPEED;
		if(Main.keys.get(KeyCode.LEFT))
			xVel -= MOVE_SPEED;
		if(Main.keys.get(KeyCode.DOWN))
			yVel += MOVE_SPEED;
		if(Main.keys.get(KeyCode.UP))
			yVel -= MOVE_SPEED;

		// If player presses Z and ready to dash, dash in direction we're moving
		if(Main.keys.get(KeyCode.Z) && dashTimer <= 0) {
			double vel = Math.sqrt(xVel * xVel + yVel * yVel);

			xVel *= DASH_SPEED / vel;
			yVel *= DASH_SPEED / vel;

			// Next dash in 0.5 seconds
			dashTimer = DASH_TIMER;
		}
		
		// Decrease dash timer to 0 over time
		if(dashTimer > 0)
			dashTimer -= Main.TIMESTEP;
	
		//System.out.printf("%.2f, %.2f%n", xVel, yVel);
		move();

		if(hasSword) {
			if(Main.keys.get(KeyCode.X)) {
				double xTarg = 0, yTarg = 0;
				if(Main.keys.get(KeyCode.RIGHT))
					xTarg = 1;
				if(Main.keys.get(KeyCode.LEFT))
					xTarg = -1;
				if(Main.keys.get(KeyCode.UP))
					yTarg = -1;
				if(Main.keys.get(KeyCode.DOWN))
					yTarg = 1;
				if(xTarg != 0 || yTarg != 0) {
					double diff = (swordDirection - Math.atan2(yTarg, xTarg)) % (2*Math.PI);
					while(diff < 0)
						diff += 2*Math.PI;

					swordRotationalVelocity += (diff < Math.PI) ? -0.075 : 0.075;
					// If the sword is pointed close to the opposite direction, apply a little bit of friction to reduce
					// Just holding one directional key and swinging forever
					if(Math.abs(diff - Math.PI) < 0.3)
						swordRotationalVelocity *= 0.95;
				} else { // If no arrow keys held, apply friction
					swordRotationalVelocity *= 0.9;
				}
			} else { // Or no X key held, apply friction
				swordRotationalVelocity *= 0.9;
			}
			if(Math.abs(swordRotationalVelocity) > 2.0) // Sword is swinging fast enough, check if it hits any entities
				for(Entity e : Main.getLevel().getEntities())
					if(e.getHitBounds().intersects(swordBox))
						e.dealPlayerDamage(Math.abs(swordRotationalVelocity)*0.1);

			swordDirection += swordRotationalVelocity * Main.TIMESTEP;
			swordDirection %= 2*Math.PI;
			while(swordDirection < 0)
				swordDirection += 2*Math.PI;

			// Apply minimal friction
			// swordRotationalVelocity *= 0.99;

			swordBox = new Hitbox(x + Math.cos(swordDirection)*0.5-0.5, y + Math.sin(swordDirection)*0.5-0.5, 1.0, 1.0);
			swordBox.add(x+Math.cos(swordDirection)*1.5-0.5, y + Math.sin(swordDirection)*1.5-0.5, 1.0, 1.0);
		}
	}

	@Override
	public void render(GraphicsContext g, GraphicsContext ignore_shadow, GraphicsContext ignore_2) {
		g.save();
		if(System.currentTimeMillis() - lastHit < 1000)
			if((System.currentTimeMillis() / 100) % 2 == 0)
				g.setGlobalAlpha(0.5);
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

		if(hasSword) {
			// g.setFill(Color.GREEN);
			// g.fillRect(x+Math.cos(swordDirection)*0.5-0.5, y+Math.sin(swordDirection)*0.5-0.5, 1.0, 1.0);
			// g.fillRect(x+Math.cos(swordDirection)*1.5-0.5, y+Math.sin(swordDirection)*1.5-0.5, 1.0, 1.0);
			if(Math.abs(swordRotationalVelocity) > 2.0)
				g.setGlobalAlpha(1.0);
			else g.setGlobalAlpha(0.5);
			g.translate(x, y);
			g.rotate(180 * swordDirection / Math.PI);
			g.drawImage(sword, 0, -0.5, 2, 1);
		}

		g.restore();
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

	public Hitbox getHitBounds() {
		return new Hitbox(x - 0.4, y - 0.4, 0.8, 0.8);
	}

	public void dealEnemyDamage(double damage) {
		if(System.currentTimeMillis()-lastHit < 1000)
			return;
		lastHit = System.currentTimeMillis();
		health -= damage;
		if(health < 0) {
			Main.getLevel().removeEntity(this);
			System.out.println("Died!");
		}
	}

	public double getHealth() {
		return health;
	}

	public void giveSword() {
		hasSword = true;
	}

}
