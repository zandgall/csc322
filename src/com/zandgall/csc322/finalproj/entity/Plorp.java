/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Plorp
 # A basic enemy entity

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Path;
import com.zandgall.csc322.finalproj.util.Path.Node;

public class Plorp extends Entity {
	private static Image sheet;

	static {
		try {
			sheet = new Image(new FileInputStream("res/entity/little_guy.png"));
		} catch (FileNotFoundException e) {
			sheet = null;
		}
	}

	static enum State {
		DEAD, SLEEPING, FALLING_ASLEEP, RESTING, STANDING, WALKING, WALKING_HOME, SURPRISED, CHASING
	}

	private State state = State.RESTING;

	private double timer = 0, pathfindTimer = 0;
	private int frame = 0, horizontalFlip = 1;

	private double xHome = Double.NaN, yHome = Double.NaN;
	private Path following = new Path();
	private double xTarget = Double.NaN, yTarget = Double.NaN;

	private boolean hitWall = false, PATHFIND_FLAG = false;

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
		pathfindTimer += Main.TIMESTEP;

		// If home position varaibles are unset, set them
		if (Double.isNaN(xHome) || Double.isNaN(yHome)) {
			xHome = x;
			yHome = y;
		}

		Random r = new Random();
		switch (state) {
			case SLEEPING:
				if (r.nextDouble() < 0.001) { // 1% chance of waking up
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
				timer += Main.TIMESTEP;

				if (r.nextDouble() < 0.005) // 5% chance of looking in a new direction
					frame = r.nextInt(4);

				if (r.nextDouble() < 0.001) { // 0.1% chance of standing up
					state = State.STANDING;
					frame = r.nextInt(2); // Look up or down randomly
					timer = 0;
				}

				if (timer > 10) { // fall asleep after 10 seconds
					state = State.FALLING_ASLEEP;
					frame = 0;
					timer = 0;
				}

				if (timer > 0.5) // Only check for player 0.5 seconds after switching states
					checkForPlayer();
				break;

			case STANDING:
				timer += Main.TIMESTEP;

				if (r.nextDouble() < 0.001) {
					state = State.WALKING;
					frame = 0;
					timer = 0;
					target(r.nextDouble(-2, 2) + xHome, r.nextDouble(-2, 2) + yHome);
				}

				if (timer > 5) {
					state = State.RESTING;
					frame = 0;
					timer = 0;
				}

				if (timer > 0.5) // Only check for player 0.5 seconds after switching states
					checkForPlayer();
				break;

			case WALKING:
				if (pursueTarget(0.15)) {
					frame = 0;
					// Friction will apply and slow creature down
					// xVel = 0;
					// yVel = 0;
				}

				if (r.nextDouble() < 0.001) {
					target(r.nextDouble(-2, 2) + xHome, r.nextDouble(-2, 2) + yHome);
				}

				if (r.nextDouble() < 0.0005) {
					timer = 0;
					target(xHome, yHome);
					state = State.WALKING_HOME;
				}

				if (timer > 0.5) // Only check for player 0.5 seconds after switching states
					checkForPlayer();
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
					} else { // Hit a wall but sees player still
					}
				}

				// End of every eighth a second, recheck if the player is within chasing bounds
				// Updating the target position if so
				if ((int) (timer * 8 + Main.TIMESTEP * 8) != (int) (timer * 8)
						&& new Hitbox(x - 8, y - 8, 16, 16).intersects(Main.getPlayer().getSolidBounds())) {
					// update target position
					target(Main.getPlayer().getX(), Main.getPlayer().getY());
				}
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
		if (state != State.DEAD && health < 5) {
			g2.setFill(Color.RED);
			g2.fillRect(x - 0.5, y - 1.0, 1.0, 0.25);
			g2.setFill(Color.GREEN);
			g2.fillRect(x - 0.5, y - 1.0, health / 5.0, 0.25);
		}

		// if (!following.empty()) {
		following.debugRender(g1);
		// }

		g1.setFill(Color.RED);
		g1.fillRect(xTarget - 0.1, yTarget - 0.1, 0.2, 0.2);

		g1.setStroke(Color.DIMGREY);
		g1.setLineWidth(0.1);
		g1.strokeRect((int) Math.floor(x), (int) Math.floor(y), 1, 1);
		g1.setFill(Color.BLUE);
		g1.strokeRect(x - 0.05, y - 0.05, 0.1, 0.1);
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
