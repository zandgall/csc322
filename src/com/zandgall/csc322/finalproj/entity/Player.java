/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

 ## Player
 # An entity that is controllable by the user

 ## Sword Beam
 # A minor projectile entity that spawns when doing a charge attack, and deals damage to any enemies it collides with

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import com.zandgall.csc322.finalproj.Camera;
import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.Sound;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Hitboxes;
import com.zandgall.csc322.finalproj.util.Hitnull;
import com.zandgall.csc322.finalproj.util.Hitrect;
import com.zandgall.csc322.finalproj.util.Util;
import com.zandgall.csc322.finalproj.util.Vector;

public class Player extends Entity {

	public static final boolean GOD_MODE = false;

	public static final double DASH_SPEED = 20, DASH_TIMER = 0.5, MOVE_SPEED = 0.2;

	public static Image sword = new Image("/entity/sword.png"), slash = new Image("/entity/sword_slash.png"),
			stab = new Image("/entity/sword_stab.png"), charged = new Image("/entity/sword_charged.png"),
			indicator = new Image("/entity/special_indicator.png");

	public static enum Special {
		NONE, SLASH, STAB, CHARGE
	}

	/* Sword data points */
	private boolean hasSword = false;
	private Hitboxes swordBox;
	private double swordDirection = 0, swordTargetDirection = 0, swordRotationalVelocity = 0;
	private double specialTimer = 0;
	private Special specialMove = Special.NONE, previousMove = Special.NONE;

	// Timer that controls dash
	private double dashTimer = 0;

	// Control player health, and invulnerability time
	private double health;
	private long lastHit = System.currentTimeMillis();

	public Player() {
		super();
		// position.set(0, -93);
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
				velocity.x += MOVE_SPEED;
			if (Main.keys.get(KeyCode.LEFT))
				velocity.x -= MOVE_SPEED;
			if (Main.keys.get(KeyCode.DOWN))
				velocity.y += MOVE_SPEED;
			if (Main.keys.get(KeyCode.UP))
				velocity.y -= MOVE_SPEED;
			swordTargetDirection = Math.atan2(velocity.y, velocity.x);

			// If player presses Z and ready to dash, dash in direction we're moving
			if (Main.keys.get(KeyCode.Z) && dashTimer <= 0) {
				velocity = velocity.unit();
				velocity.scale(DASH_SPEED);

				// Next dash in 0.5 seconds
				dashTimer = DASH_TIMER;
			}

			// Decrease dash timer to 0 over time
			if (dashTimer > 0)
				dashTimer -= Main.TIMESTEP;
		}
		
		if(GOD_MODE) {
			Main.getCamera().setSmoothing(Camera.DEFAULT_SMOOTHING*4);
			health = 20;
			position.add(velocity);
			velocity.set(0, 0);
		} else
			move();

		if(getY() < -150) {
			Sound.BossDrums.fadeTo(1.f);
			if(hasSword)
				Sound.BossEPiano.fadeTo(1.f);
		} else {
			Sound.BossDrums.fadeTo(0.f);
			Sound.BossEPiano.fadeTo(0.f);
		}
	}

	private void handleSword() {
		specialTimer -= Main.TIMESTEP;
		if (specialMove == Special.CHARGE)
			doCharge();
		else if (specialMove == Special.SLASH)
			doSlash();
		else if (specialMove == Special.STAB)
			doStab();
		// If X and any of the arrow keys are pressed (player is moving), swing sword in
		// direction player is moving
		else if (Main.keys.get(KeyCode.X) && (Main.keys.get(KeyCode.LEFT) || Main.keys.get(KeyCode.RIGHT)
				|| Main.keys.get(KeyCode.UP) || Main.keys.get(KeyCode.DOWN))) {

			// Find out how far (and in which direction) the swordDirection is from the
			// swordTargetDirection, and swing towards the target
			double diff = Util.signedAngularDistance(swordDirection, swordTargetDirection);

			swordRotationalVelocity += (diff > 0) ? -0.1 : 0.1;
			// If the sword is pointed close to the opposite direction, apply a little bit
			// of friction to reduce
			// Just holding one directional key and swinging forever
			if (Math.PI - Math.abs(diff) < 0.3)
				swordRotationalVelocity *= 0.95;

			// If the player is dashing, perform a special move if applicable
			else if (Main.keys.get(KeyCode.Z) && dashTimer <= 0 && specialTimer <= 0) {

				// If swinging sword fast enough, do charged special
				if (Math.abs(swordRotationalVelocity) > 15) {
					specialMove = Special.CHARGE;

					// Boost swing speed
					// when in special mode, no sword friction is applied
					swordRotationalVelocity *= 2;
					specialTimer = 1;
					dashTimer = DASH_TIMER;

					// If sword is within 45 degrees of target direction, perform stab
				} else if (Math.abs(diff) < 0.25 * Math.PI) {
					specialMove = Special.STAB;

					Main.getLevel().addEntity(new StabBeam(getX(), getY(), swordDirection));

					swordRotationalVelocity = 0;
					specialTimer = 0.2;

					// Dash with x1.5 speed and no friction in direction of sword
					velocity = Vector.ofAngle(swordDirection);
					velocity.scale(1.5 * DASH_SPEED);

					// Next dash in 0.5 seconds
					dashTimer = DASH_TIMER;

					// If sword is within 45 degrees of perpendicular to moving direction, slash
				} else if (Math.abs(Math.PI * 0.5 - Math.abs(diff)) < 0.25 * Math.PI) {
					specialMove = Special.SLASH;
					swordRotationalVelocity = 0;
					specialTimer = 0.2;

					// Override dash with 1.25x speed, and no friction
					velocity = velocity.unit();
					velocity.scale(1.25 * DASH_SPEED);

					// Next dash in 0.5 seconds
					dashTimer = DASH_TIMER;
				}
			}
		} else { // If no x or arrow keys held, apply friction
			swordRotationalVelocity *= 0.9;
		}

		// Update previous move for invulnerability and graphics
		if (specialMove == Special.NONE && specialTimer <= 4)
			previousMove = Special.NONE;

		// Sword is swinging fast enough, check if it hits any entities
		if (Math.abs(swordRotationalVelocity) > 2.0)
			for (Entity e : Main.getLevel().getEntities())
				if (e.getHitBounds().intersects(swordBox))
					e.dealPlayerDamage(Math.abs(swordRotationalVelocity) * 0.1);

		// Swing sword
		swordDirection += swordRotationalVelocity * Main.TIMESTEP;
		swordDirection %= Math.TAU;
		while (swordDirection < 0)
			swordDirection += Math.TAU;

		// Update sword hitbox
		swordBox = (Hitboxes)new Hitboxes(-0.5, -0.5, 1.0, 1.0).translate(position).translate(Vector.ofAngle(swordDirection).scale(0.5));
		swordBox.add(new Hitboxes(-0.5, -0.5, 1.0, 1.0).translate(position).translate(Vector.ofAngle(swordDirection).scale(1.5)));
	}

	private void doCharge() {
		// Every 5 frames, summon a SwordBeam
		if ((int) (specialTimer * 100) % 5 == 0)
			Main.getLevel().addEntity(
					new SwordBeam(getX() + Math.cos(swordDirection), getY() + Math.sin(swordDirection), swordDirection));

		// when the special runs out, update variables
		if (specialTimer <= 0) {
			previousMove = Special.CHARGE;
			specialMove = Special.NONE;
			specialTimer = 5;
		}
	}

	private void doSlash() {
		// Counteract (most) friction
		velocity.scale(1.0/0.91);

		// Check for and damage intersecting entities
		for (Entity e : Main.getLevel().getEntities())
			if (e.getHitBounds().intersects(new Hitrect(-1, -1, 2, 2).translate(position).translate(Vector.ofAngle(swordDirection))))
				e.dealPlayerDamage(2);

		// When special runs out, update variables and boost swing speed
		if (specialTimer <= 0) {
			previousMove = Special.SLASH;
			specialMove = Special.NONE;

			// Boost swinging in the proper direction
			double diff = Util.signedAngularDistance(swordDirection, swordTargetDirection);

			swordRotationalVelocity = 20;
			if (diff > 0)
				swordRotationalVelocity *= -1;

			specialTimer = 5;
		}
	}

	private void doStab() {
		// Counteract (most) friction
		velocity.scale(1.0 / 0.91);

		// Check for and damage intersecting entities
		for (Entity e : Main.getLevel().getEntities())
			if (e.getHitBounds().intersects(new Hitrect(-1, -1, 2, 2).translate(position).translate(Vector.ofAngle(swordDirection))))
				e.dealPlayerDamage(2);

		// When special runs out, update variables
		if (specialTimer <= 0) {
			previousMove = Special.STAB;
			specialMove = Special.NONE;
			specialTimer = 5;
		}
	}

	@Override
	public void render(GraphicsContext g, GraphicsContext ignore_shadow, GraphicsContext ignore_2) {
		g.save();
		g.setFill(Color.RED);
		g.fillRect(getX() - 0.5, getY() - 0.5, 1.0, 1.0);

		// If player was hit recently, or a special move was used recently, draw
		// transparent
		if (System.currentTimeMillis() - lastHit < 1000)
			if ((System.currentTimeMillis() / 100) % 2 == 0) // Every other frame on damage
				g.setGlobalAlpha(0.5);
		if (previousMove != Special.NONE)
			g.setGlobalAlpha(0.5);

		// Sword drawing
		if (hasSword) {
			g.translate(getX(), getY());

			// Draw sword special indicator
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

			// Draw fully opaque only when sword is swinging fast enough to do damage
			if (Math.abs(swordRotationalVelocity) > 2.0 || specialMove != Special.NONE)
				g.setGlobalAlpha(1.0);
			else
				g.setGlobalAlpha(0.5);
			g.rotate(180 * swordDirection / Math.PI);
			g.drawImage(sword, 0, -0.5, 2, 1);

			// Draw special move related sprites, or fade outs
			if (specialMove == Special.STAB)
				g.drawImage(stab, 0, -0.5, 2.25, 1);
			else if (specialMove == Special.SLASH) {
				if (Util.signedAngularDistance(swordDirection, swordTargetDirection) > 0)
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
		return new Hitrect(getX() - 0.5, getY() - 0.5, 1, 1);
	}

	public Hitbox getUpdateBounds() {
		return new Hitrect(Main.getLevel().bounds);
	}

	public Hitbox getSolidBounds() {
		return new Hitrect(getX() - 0.4, getY() - 0.4, 0.8, 0.8);
	}

	public Hitbox getHitBounds() {
		return new Hitrect(getX() - 0.4, getY() - 0.4, 0.8, 0.8);
	}

	// When recieving damage, only take damage if player wasn't hit in last second,
	// and is not doing a special move, and is not cooling down from a special move
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

	public void addHealth(double health) {
		this.health += health;
		if (this.health > 20)
			this.health = 20;
	}

	public void giveSword() {
		hasSword = true;
		Sound.EPiano.fadeTo(1.f);
		Sound.Drums.fadeTo(1.f);
	}

	public void takeAwaySword() {
		hasSword = false;
		Sound.EPiano.fadeTo(0.f);
		Sound.BossEPiano.fadeTo(0.f);
	}

	public void cutsceneSword(double rotation, double specialTimer, Special special) {
		this.swordDirection = rotation;
		this.specialTimer = specialTimer;
		this.specialMove = special;
	}

	private static class StabBeam extends Entity {
		private static final Image texture = new Image("/entity/stabbeam.png");

		private double timer, direction;
		
		public StabBeam(double x, double y, double direction) {
			super(x, y);
			position.add(Vector.ofAngle(direction).scale(2.5));
			this.direction = direction;
			this.timer = 1;
		}

		public void tick() {
			position.add(Vector.ofAngle(direction).scale(1.5*DASH_SPEED*Main.TIMESTEP));

			for(Entity e : Main.getLevel().getEntities())
				if(e.getHitBounds().intersects(getRenderBounds()))
					e.dealPlayerDamage(1.0);

			timer -= Main.TIMESTEP;
			if(timer < 0)
				Main.getLevel().removeEntity(this);
		}

		public void render(GraphicsContext g, GraphicsContext ignore, GraphicsContext ignore2) {
			g.save();
			if(timer < 0.5)
				g.setGlobalAlpha(timer*2);
			g.translate(getX(), getY());
			g.rotate(180 * direction / Math.PI);
			g.drawImage(texture, -1, -1.5, 1.625, 3);
			g.restore();
		}

		public Hitbox getRenderBounds() {
			return new Hitrect(getX() - 1, getY() - 1.5, 1.625, 3);
		}

		public Hitbox getUpdateBounds() {
			return new Hitrect(Main.getLevel().bounds);
		}

		public Hitbox getSolidBounds() {
			return new Hitnull();
		}

		public Hitbox getHitBounds() {
			return new Hitnull();
		}

	}

	private static class SwordBeam extends Entity {

		private static final Image texture = new Image("/entity/sword_beam.png");

		private double timer, direction;

		public SwordBeam(double x, double y, double direction) {
			super(x, y);
			this.direction = direction;
			this.timer = 1;
		}

		public void tick() {
			// Move in given direction, slower over time
			position.add(Vector.ofAngle(direction).scale(0.1*timer));

			// If intersecting enemies, deal 1 damage
			for (Entity e : Main.getLevel().getEntities())
				if (e.getHitBounds().intersects(getRenderBounds()))
					e.dealPlayerDamage(1.0);

			// Count down and despawn when time is up
			timer -= Main.TIMESTEP;
			if (timer < 0)
				Main.getLevel().removeEntity(this);
		}

		public void render(GraphicsContext g, GraphicsContext ignore, GraphicsContext ignore2) {
			g.save();
			g.translate(getX(), getY());
			g.rotate(180 * direction / Math.PI);
			g.drawImage(texture, -0.375, -0.75, 0.75, 1.5);
			g.restore();
		}

		public Hitbox getRenderBounds() {
			return new Hitrect(getX() - 0.75, getY() - 0.75, 1.5, 1.5);
		}

		public Hitbox getUpdateBounds() {
			return new Hitrect(Main.getLevel().bounds);
		}

		public Hitbox getSolidBounds() {
			return new Hitnull();
		}

		public Hitbox getHitBounds() {
			return new Hitnull();
		}
	}

}
