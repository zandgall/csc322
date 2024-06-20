/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Player
 # An entity that is controllable by the user

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.image.Image;
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

	public static Image sword, slash, stab, charged, indicator;

	static {
		try {
			sword = new Image(new FileInputStream("res/entity/sword.png"));
			slash = new Image(new FileInputStream("res/entity/sword_slash.png"));
			stab = new Image(new FileInputStream("res/entity/sword_stab.png"));
			charged = new Image(new FileInputStream("res/entity/sword_charged.png"));
			indicator = new Image(new FileInputStream("res/entity/special_indicator.png"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load player sword");
			sword = null;
			slash = null;
			stab = null;
			charged = null;
			indicator = null;
		}
	}

	static enum Special {
		NONE, SLASH, STAB, CHARGE
	}

	private boolean hasSword = false;
	private double swordDirection = 0, swordTargetDirection = 0, swordRotationalVelocity = 0;
	private Hitbox swordBox;
	private double dashTimer = 0, specialTimer = 0;
	private Special specialMove = Special.NONE, previousMove = Special.NONE;

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
		// Do sword mechanics
		if (hasSword) {
			handleSword();
		}

		// Arrow keys to move
		if (specialMove == Special.NONE) {
			if (Main.keys.get(KeyCode.RIGHT))
				xVel += MOVE_SPEED;
			if (Main.keys.get(KeyCode.LEFT))
				xVel -= MOVE_SPEED;
			if (Main.keys.get(KeyCode.DOWN))
				yVel += MOVE_SPEED;
			if (Main.keys.get(KeyCode.UP))
				yVel -= MOVE_SPEED;
			swordTargetDirection = Math.atan2(yVel, xVel);

			// If player presses Z and ready to dash, dash in direction we're moving
			if (Main.keys.get(KeyCode.Z) && dashTimer <= 0) {
				double vel = Math.sqrt(xVel * xVel + yVel * yVel);

				xVel *= DASH_SPEED / vel;
				yVel *= DASH_SPEED / vel;

				// Next dash in 0.5 seconds
				dashTimer = DASH_TIMER;
			}

			// Decrease dash timer to 0 over time
			if (dashTimer > 0)
				dashTimer -= Main.TIMESTEP;
		}

		// System.out.printf("%.2f, %.2f%n", xVel, yVel);
		move();

	}

	/**
	 * Calculate the minimum distance from angle A to B, and whether it's to the
	 * left or right
	 * 
	 * @param a Angle 1 in radians
	 * @param b Angle 2 in radians
	 * @return Signed distance from a to b
	 */
	private double signedAngularDistance(double a, double b) {
		double phi = (a - b) % Math.TAU;
		if (phi < -Math.PI)
			return phi + Math.TAU;
		else if (phi > Math.PI)
			return phi - Math.TAU;
		return phi;
	}

	private void handleSword() {
		specialTimer -= Main.TIMESTEP;
		if (specialMove == Special.CHARGE) {
			if ((int) (specialTimer * 100) % 5 == 0)
				Main.getLevel().addEntity(
						new SwordBeam(x + Math.cos(swordDirection), y + Math.sin(swordDirection), swordDirection));
			if (specialTimer <= 0) {
				previousMove = Special.CHARGE;
				specialMove = Special.NONE;
				specialTimer = 5;
			}
		} else if (specialMove == Special.SLASH) {
			// Counteract (most) friction
			xVel /= 0.91;
			yVel /= 0.91;
			for (Entity e : Main.getLevel().getEntities())
				if (e.getHitBounds().intersects(
						new Hitbox(x + Math.cos(swordDirection) - 1, y + Math.sin(swordDirection) - 1, 2, 2)))
					e.dealPlayerDamage(2);
			if (specialTimer <= 0) {
				previousMove = Special.SLASH;
				specialMove = Special.NONE;

				// Boost swinging in the proper direction
				double diff = signedAngularDistance(swordDirection, swordTargetDirection);

				swordRotationalVelocity = 20;
				if (diff > 0)
					swordRotationalVelocity *= -1;

				specialTimer = 5;
			}
		} else if (specialMove == Special.STAB) {
			// Counteract (most) friction
			xVel /= 0.91;
			yVel /= 0.91;

			for (Entity e : Main.getLevel().getEntities())
				if (e.getHitBounds().intersects(
						new Hitbox(x + Math.cos(swordDirection) - 1, y + Math.sin(swordDirection) - 1, 2, 2)))
					e.dealPlayerDamage(2);
			if (specialTimer <= 0) {
				previousMove = Special.STAB;
				specialMove = Special.NONE;
				specialTimer = 5;
			}
		} else if (Main.keys.get(KeyCode.X)) {
			// If any of the arrow keys are pressed (player is moving), swing sword in
			// direction player is moving
			if (Main.keys.get(KeyCode.LEFT) || Main.keys.get(KeyCode.RIGHT) || Main.keys.get(KeyCode.UP)
					|| Main.keys.get(KeyCode.DOWN)) {

				double diff = signedAngularDistance(swordDirection, swordTargetDirection);

				swordRotationalVelocity += (diff > 0) ? -0.075 : 0.075;
				// If the sword is pointed close to the opposite direction, apply a little bit
				// of friction to reduce
				// Just holding one directional key and swinging forever
				if (Math.PI - Math.abs(diff) < 0.3)
					swordRotationalVelocity *= 0.95;

				// If the player is dashing, perform a special move if applicable
				else if (Main.keys.get(KeyCode.Z) && dashTimer <= 0 && specialTimer <= 0) {
					if (Math.abs(swordRotationalVelocity) > 15) {
						specialMove = Special.CHARGE;
						// Boost speed
						// when in special mode, no sword friction is applied
						swordRotationalVelocity *= 2;
						specialTimer = 1;
						dashTimer = DASH_TIMER;
					} else if (Math.abs(diff) < 0.25 * Math.PI) {
						specialMove = Special.STAB;
						swordRotationalVelocity = 0;
						// swordDirection = Math.atan2(yVel, xVel);
						specialTimer = 0.2;

						// Dash with x1.5 speed and no friction
						xVel = 1.5 * DASH_SPEED * Math.cos(swordDirection);
						yVel = 1.5 * DASH_SPEED * Math.sin(swordDirection);

						// Next dash in 0.5 seconds
						dashTimer = DASH_TIMER;
					} else if (Math.abs(Math.PI * 0.5 - Math.abs(diff)) < 0.25 * Math.PI) {
						specialMove = Special.SLASH;
						swordRotationalVelocity = 0;
						specialTimer = 0.2;

						// Override dash with 1.25x speed, and no friction
						double vel = Math.sqrt(xVel * xVel + yVel * yVel);

						xVel *= 1.25 * DASH_SPEED / vel;
						yVel *= 1.25 * DASH_SPEED / vel;

						// Next dash in 0.5 seconds
						dashTimer = DASH_TIMER;
					}
				}
			} else { // If no arrow keys held, apply friction
				swordRotationalVelocity *= 0.9;
			}

		} else { // Or no X key held, apply friction
			swordRotationalVelocity *= 0.9;
		}
		if (specialMove == Special.NONE && specialTimer <= 4)
			previousMove = Special.NONE;

		if (Math.abs(swordRotationalVelocity) > 2.0) // Sword is swinging fast enough, check if it hits any entities
			for (Entity e : Main.getLevel().getEntities())
				if (e.getHitBounds().intersects(swordBox))
					e.dealPlayerDamage(Math.abs(swordRotationalVelocity) * 0.1);

		swordDirection += swordRotationalVelocity * Main.TIMESTEP;
		swordDirection %= Math.TAU;
		while (swordDirection < 0)
			swordDirection += Math.TAU;

		// Apply minimal friction
		// swordRotationalVelocity *= 0.99;

		swordBox = new Hitbox(x + Math.cos(swordDirection) * 0.5 - 0.5, y + Math.sin(swordDirection) * 0.5 - 0.5, 1.0,
				1.0);
		swordBox.add(x + Math.cos(swordDirection) * 1.5 - 0.5, y + Math.sin(swordDirection) * 1.5 - 0.5, 1.0, 1.0);
	}

	@Override
	public void render(GraphicsContext g, GraphicsContext ignore_shadow, GraphicsContext ignore_2) {
		g.save();
		if (System.currentTimeMillis() - lastHit < 1000)
			if ((System.currentTimeMillis() / 100) % 2 == 0)
				g.setGlobalAlpha(0.5);
		if (previousMove != Special.NONE)
			g.setGlobalAlpha(0.5);
		g.setFill(Color.color(1, 0, 0, 1));
		g.fillRect(x - 0.5, y - 0.5, 1, 1);
		g.setStroke(Color.BLACK);
		g.setLineWidth(0.01);
		g.strokeRect(x - 0.5, y - 0.5, 1.0, 1.0);

		Hitbox box = new Hitbox(x - 0.5, y - 0.5, 1.0, 1.0);
		int minX = (int) Math.floor(box.getBounds().getMinX());
		int minY = (int) Math.floor(box.getBounds().getMinY());
		int maxX = (int) Math.floor(box.getBounds().getMaxX());
		int maxY = (int) Math.floor(box.getBounds().getMaxY());
		Hitbox tilebox = new Hitbox(0, 0, 1, 1);
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				Tile t = Main.getLevel().get(i, j);
				if (t != null && t.solidBounds(i, j) != null && t.solidBounds(i, j).intersects(box)) {
					g.setStroke(Color.RED);
					tilebox = t.solidBounds(i, j);
					for (Rectangle2D r : tilebox.getBoxes())
						g.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
				} else {
					g.setStroke(Color.BLUE);
					g.strokeRect(i, j, 1, 1);
				}
			}
		}

		if (hasSword) {
			g.translate(x, y);
			g.save();
			g.rotate(180 * swordTargetDirection / Math.PI);
			g.setGlobalAlpha(dashTimer * 2);
			g.drawImage(indicator, -1.5, -1.5, 3, 3);
			g.restore();

			if (specialMove == Special.NONE) {
				g.setStroke(Color.LIGHTBLUE);
				g.setLineWidth(0.1);
				g.setGlobalAlpha(specialTimer);
				g.strokeArc(-1.7, -1.7, 3.4, 3.4, 90, -360 * (5 - specialTimer) / 5, ArcType.OPEN);
			}

			if (Math.abs(swordRotationalVelocity) > 2.0 || specialMove != Special.NONE)
				g.setGlobalAlpha(1.0);
			else
				g.setGlobalAlpha(0.5);
			g.rotate(180 * swordDirection / Math.PI);
			g.drawImage(sword, 0, -0.5, 2, 1);

			if (specialMove == Special.STAB) {
				// g.rotate(-180 * swordDirection / Math.PI);
				// g.setFill(Color.RED);
				// g.fillRect(Math.cos(swordDirection) - 1, Math.sin(swordDirection) - 1, 2, 2);
				// g.rotate(180 * swordDirection / Math.PI);
				g.drawImage(stab, 0, -0.5, 2.25, 1);
			} else if (specialMove == Special.SLASH) {
				// g.rotate(-180 * swordDirection / Math.PI);
				// g.setFill(Color.RED);
				// g.fillRect(Math.cos(swordDirection) - 1, Math.sin(swordDirection) - 1, 2, 2);
				// g.rotate(180 * swordDirection / Math.PI);
				if (signedAngularDistance(swordDirection, swordTargetDirection) > 0)
					g.drawImage(slash, 0, -0.5, 2, 2);
				else
					g.drawImage(slash, 0, 0.5, 2, -2);
			} else if (previousMove == Special.STAB) {
				g.setGlobalAlpha(specialTimer - 4);
				g.drawImage(stab, 0, -0.5, 2.25, 1);
			} else if (previousMove == Special.SLASH) {
				g.setGlobalAlpha(specialTimer - 4);
				if (swordRotationalVelocity < 0)
					g.drawImage(slash, 0, -0.5, 2, 2);
				else
					g.drawImage(slash, 0, 0.5, 2, -2);
			} else if (Math.abs(swordRotationalVelocity) > 15.0 && specialTimer <= 0)
				g.drawImage(charged, 0, -0.5, 2.5, 1);
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
		if (System.currentTimeMillis() - lastHit < 1000 || specialMove != Special.NONE
				|| previousMove != Special.NONE)
			return;
		lastHit = System.currentTimeMillis();
		health -= damage;
		if (health < 0) {
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

	/*
	 * Returns a number between 0 and 1 that determines how long until another
	 * special move can be done
	 */
	public double getSpecialCoolDown() {
		if (specialMove != Special.NONE)
			return 0.0;
		return Math.min(Math.max(5 - specialTimer, 0.0), 5.0) / 5.0;
	}

	private static class SwordBeam extends Entity {

		private static final Image texture = new Image("file:res/entity/sword_beam.png");

		private double direction, timer;

		public SwordBeam(double x, double y, double direction) {
			super(x, y);
			this.direction = direction;
			timer = 1;
		}

		public void tick() {
			x += Math.cos(direction) * 0.1 * timer;
			y += Math.sin(direction) * 0.1 * timer;
			for (Entity e : Main.getLevel().getEntities())
				if (e.getHitBounds().intersects(getRenderBounds()))
					e.dealPlayerDamage(1.0);
			timer -= Main.TIMESTEP;
			if (timer < 0)
				Main.getLevel().removeEntity(this);
		}

		public void render(GraphicsContext g, GraphicsContext ignore, GraphicsContext ignore2) {
			g.save();
			g.translate(x, y);
			g.rotate(180 * direction / Math.PI);
			g.drawImage(texture, -0.375, -0.75, 0.75, 1.5);
			g.restore();
		}

		public Hitbox getRenderBounds() {
			return new Hitbox(x - 0.75, y - 0.75, 1.5, 1.5);
		}

		public Hitbox getUpdateBounds() {
			return new Hitbox(Main.getLevel().bounds);
		}

		public Hitbox getSolidBounds() {
			return new Hitbox();
		}

		public Hitbox getHitBounds() {
			return new Hitbox();
		}
	}

}
