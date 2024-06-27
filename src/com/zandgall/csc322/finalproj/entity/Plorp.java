/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

 ## Plorp
 # A basic enemy entity

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Path;

public class Plorp extends Entity {
	private static Image sheet = new Image("/entity/little_guy.png");

	// State handling
	static enum State {
		DEAD, SLEEPING, FALLING_ASLEEP, RESTING, STANDING, WALKING, WALKING_HOME, SURPRISED, CHASING
	}

	private State state = State.RESTING;

	// Timing and rendering flags
	private double timer = 0;
	private int frame = 0, horizontalFlip = 1;

	// Pathfinding elements
	private double xHome = Double.NaN, yHome = Double.NaN;
	private Path following = new Path();
	private double xTarget = Double.NaN, yTarget = Double.NaN;

	private boolean hitWall = false;

	private double health = 5;
	private long lastHit = 0;

	public Plorp() {
		super();
	}

	public Plorp(double x, double y) {
		super(x, y);
		xHome = x;
		yHome = y;
	}

	@Override
	public void tick() {
		// If home position varaibles are unset, set them
		if (Double.isNaN(xHome) || Double.isNaN(yHome)) {
			xHome = x;
			yHome = y;
		}

		Random r = new Random();
		switch (state) {
			case SLEEPING:
				if (r.nextDouble() < 0.001) { // 0.1% chance of waking up
					state = State.RESTING;
					frame = r.nextInt(4); // look in random direction
				}
				break;

			case FALLING_ASLEEP:
				timer += Main.TIMESTEP;
				frame = (int) timer;

				if (frame >= 2) {
					state = State.SLEEPING;
					frame = 0;
					timer = 0;
				}
				break;

			case RESTING:
				handleResting(r);
				break;

			case STANDING:
				handleStanding(r);
				break;

			case WALKING:
				handleWalking(r);
				break;

			case WALKING_HOME:
				if (pursueTarget(0.15)) {
					frame = 0;
					timer = 0;
					state = State.STANDING;
				}
				break;

			case SURPRISED:
				timer += Main.TIMESTEP;
				if (timer > 0.25) {
					timer = 0;
					state = State.CHASING;
				}
				break;

			case CHASING:
				handleChasing(r);
				break;

			case DEAD:
			default:
		}
		if (Math.abs(xVel) > 0.001 || Math.abs(yVel) > 0.001) {
			hitWall |= move();
		} else {
			xVel = 0;
			yVel = 0;
		}
	}

	// Plorp sits on the ground looking around and blinking
	private void handleResting(Random r) {
		timer += Main.TIMESTEP;

		// 5% chance of looking in a new direction
		if (r.nextDouble() < 0.005)
			frame = r.nextInt(4);

		// 0.1% chance of standing up
		if (r.nextDouble() < 0.001) {
			state = State.STANDING;
			frame = r.nextInt(2); // Look up or down randomly
			timer = 0;
		}

		// fall asleep after 10 seconds
		if (timer > 10) {
			state = State.FALLING_ASLEEP;
			frame = 0;
			timer = 0;
		}

		// Only check for player 0.5 seconds after switching states
		if (timer > 0.5)
			checkForPlayer();
	}

	// Plorp stands, with a chance of walking
	private void handleStanding(Random r) {
		timer += Main.TIMESTEP;

		// 0.1% chance of getting up and walking
		if (r.nextDouble() < 0.001) {
			state = State.WALKING;
			frame = 0;
			timer = 0;
			target(r.nextDouble(-2, 2) + xHome, r.nextDouble(-2, 2) + yHome);
		}

		// If standing for more than 5 seconds, sit down and rest
		if (timer > 5) {
			state = State.RESTING;
			frame = 0;
			timer = 0;
		}

		// Only check for player 0.5 seconds after switching states
		if (timer > 0.5)
			checkForPlayer();
	}

	// Plorp walks around to random points around its home coordinate
	private void handleWalking(Random r) {
		// Pursue target, and if reached, stand still
		if (pursueTarget(0.15))
			frame = 0;

		// 0.1% chance of targetting a random point
		if (r.nextDouble() < 0.001)
			target(r.nextDouble(-2, 2) + xHome, r.nextDouble(-2, 2) + yHome);

		// 0.05% chance of walking back home
		if (r.nextDouble() < 0.0005) {
			timer = 0;
			target(xHome, yHome);
			state = State.WALKING_HOME;
		}

		// Only check for player 0.5 seconds after switching states
		if (timer > 0.5)
			checkForPlayer();
	}

	// Chase after target, damaging player if caught
	private void handleChasing(Random r) {
		// Chase target and if hit something, check if we hit the player and damage
		if (pursueTarget(0.3)) {
			if (new Hitbox(x - 0.5, y - 0.5, 1, 1)
					.intersects(Main.getPlayer().getHitBounds())) { // If close enough player
				Main.getPlayer().dealEnemyDamage(1.0);
			} else if (!new Hitbox(x - 4, y - 4, 8, 8).intersects(Main.getPlayer().getSolidBounds())) {
				// Ran into a wall or something else stopped it, if can't see player, stop
				timer = 0;
				frame = 0;
				target(Main.getPlayer().getX(), Main.getPlayer().getY());
				state = State.WALKING;
			}
		}

		// End of every eighth a second, recheck if the player is within chasing bounds
		// Updating the target position if so
		if ((int) (timer * 8 + Main.TIMESTEP * 8) != (int) (timer * 8)
				&& new Hitbox(x - 8, y - 8, 16, 16).intersects(Main.getPlayer().getSolidBounds())) {
			// update target position
			target(Main.getPlayer().getX(), Main.getPlayer().getY());
		}
	}

	/**
	 * Look for player in square 4 radius
	 */
	private boolean checkForPlayer() {
		if (new Hitbox(x - 4, y - 4, 8, 8).intersects(Main.getPlayer().getHitBounds())) {
			timer = 0;
			frame = 0;
			target(Main.getPlayer().getX(), Main.getPlayer().getY());
			state = State.SURPRISED;
			return true;
		}
		return false;
	}

	// Pathfind (if necessary) towards target x and y
	private void target(double tx, double ty) {
		xTarget = tx;
		yTarget = ty;

		// Check if there is a wall between here and the target. If there is,
		// pathfinding is necessary
		boolean wall = false;
		// As long as we're within 'checkbox', we're between (x, y) and (tx, ty)
		Hitbox checkbox = new Hitbox(Math.min(tx, x), Math.min(ty, y), Math.abs(tx - x), Math.abs(ty - y));
		// Unit direction from (x, y) -> (tx, ty)
		double dir = Math.sqrt((x - tx) * (x - tx) + (y - ty) * (y - ty));
		double dirX = (tx - x) / dir, dirY = (ty - y) / dir;

		// Create a box from solid bounds. While it intersects 'checkbox', and a wall
		// hasn't been found, move it in the unit direction and check for a solid tile
		for (Hitbox box = getSolidBounds(); checkbox.intersects(box) && !wall; box = box.translate(dirX, dirY)) {
			int tileX = (int) Math.floor(box.getBounds().getCenterX());
			int tileY = (int) Math.floor(box.getBounds().getCenterY());
			// if the tile at the center of the box has a hitbox, we'll need to pathfind
			// around it
			wall |= Main.getLevel().get(tileX, tileY).solidBounds(tileX, tileY) != null;
		}

		// Pathfind if there are obstacles,
		if (wall) {
			following = Path.pathfind((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(tx),
					(int) Math.floor(ty));
			if (!following.empty())
				following.progress();
		} else
			following = new Path(); // Empty path to just follow x/yTarget
	}

	/**
	 * Used by the "WALKING", "WALKING_HOME", and "CHASING" states. And as such,
	 * updates 'frame' and 'timer' as they would otherwise
	 * 
	 * @param speed The speed to move at
	 */
	private boolean pursueTarget(double speed) {
		timer += Main.TIMESTEP;

		// Create a target, if there's a following path, follow that
		// otherwise, follow x/yTarget
		double tX, tY;
		if (!following.empty()) {
			tX = following.current().x + 0.5;
			tY = following.current().y + 0.5;
		} else {
			tX = xTarget;
			tY = yTarget;
		}

		// If we're close enough to the target, progress path or update x/yTarget
		double distance = Math.sqrt((tX - x) * (tX - x) + (tY - y) * (tY - y));
		if (distance < 0.1 || hitWall) {
			// Hitwall is flagged when the Plorp hits something, which might be the player
			if (following.empty() || hitWall || Math.pow(xTarget - x, 2) + Math.pow(yTarget - y, 2) < 0.2) {
				// xTarget = x;
				// yTarget = y;
				hitWall = false;
				// Hit target
				return true;
			} else {
				// System.out.println("Progressing path");
				following.progress();
			}
		}

		// Update graphics flags
		horizontalFlip = (tX < x) ? 1 : -1;
		frame = (int) (timer * 3) % 4;

		// Accelerate 0.3 units/sec^2 towards (tX, tY)
		xVel += 0.3 * (tX - x) / distance;
		yVel += 0.3 * (tY - y) / distance;

		// Did not hit target
		return false;
	}

	@Override
	public void render(GraphicsContext g1, GraphicsContext gs, GraphicsContext g2) {
		g1.save();

		// Half transparent if hit recently
		if (state != State.DEAD && System.currentTimeMillis() - lastHit < 100
				&& (System.currentTimeMillis() / 50) % 2 == 0)
			g1.setGlobalAlpha(0.5);

		g1.translate(x, y);
		g1.scale(horizontalFlip, 1);
		switch (state) {
			case SLEEPING:
				g1.drawImage(sheet, 0, 64, 16, 16, -0.5, -0.5, 1, 1);
				// TODO: "zzz" particles
				break;
			case FALLING_ASLEEP:
				g1.drawImage(sheet, frame * 16, 32, 16, 16, -0.5, -0.5, 1, 1);
				break;
			case RESTING:
				if ((int) (timer * 10) % 20 == 0) { // 1 out of 20 frames are a blink
					if (frame >= 2) // Looking back
						g1.drawImage(sheet, 16, 48, 16, 16, -0.5, -0.5, 1, 1);
					else
						g1.drawImage(sheet, 0, 48, 16, 16, -0.5, -0.5, 1, 1);
				} else {
					int xoff[] = { 0, 16, 0, 16 }, yoff[] = { 0, 0, 16, 16 };
					g1.drawImage(sheet, xoff[frame], yoff[frame], 16, 16, -0.5, -0.5, 1, 1);
				}
				break;
			case STANDING:
				g1.drawImage(sheet, 32 + 16 * frame, 0, 16, 16, -0.5, -0.5, 1, 1);
				break;
			case WALKING:
			case WALKING_HOME:
			case CHASING:
				int up = (yTarget < y) ? 16 : 0;
				g1.drawImage(sheet, 32 + up, frame * 16, 16, 16, -0.5, -0.5, 1, 1);
				break;
			case SURPRISED:
				g1.drawImage(sheet, 16, 64, 16, 16, -0.5, -0.5, 1, 1);
				break;
			case DEAD:
				g1.drawImage(sheet, 32 + frame * 16, 64, 16, 16, -0.5, -0.5, 1, 1);
				break;
			default:
				break;
		}
		g1.restore();

		// Draw a health bar if applicable
		if (state != State.DEAD && health < 5) {
			g2.setFill(Color.RED);
			g2.fillRect(x - 0.5, y - 1.0, 1.0, 0.25);
			g2.setFill(Color.GREEN);
			g2.fillRect(x - 0.5, y - 1.0, health / 5.0, 0.25);
		}
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(x - 5, y - 5, 10, 10);
	}

	public Hitbox getSolidBounds() {
		if (state == State.DEAD)
			return new Hitbox();
		return new Hitbox(x - 0.05, y - 0.05, 0.1, 0.1);
	}

	public Hitbox getHitBounds() {
		return new Hitbox(x - 0.4, y - 0.2, 0.8, 0.5);
	}

	public void dealPlayerDamage(double damage) {
		// Dont receive damage if dead or surprised
		if (state == State.DEAD || state == State.SURPRISED)
			return;
		lastHit = System.currentTimeMillis();
		health -= damage;
		state = State.SURPRISED;

		if (health <= 0 && state != State.DEAD) {
			timer = 0;
			frame = new Random().nextInt(2); // pick between 2 random dead sprites
			state = State.DEAD;
		}
	}
}
