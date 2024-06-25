/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Octoplorp
 # A boss fight for the player to reach and fight

 ## Tentacle
 # An entity that represents each of Octoplorp's tentacles

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import java.util.ArrayList;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.staging.Cutscene;
import com.zandgall.csc322.finalproj.util.Hitbox;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Octoplorp extends Entity {

	private static final Image body = new Image("file:res/entity/octoplorp/body.png"),
			eye = new Image("file:res/entity/octoplorp/eye.png");

	static enum State {
		SLEEPING, WAKING, GRABBING
	}

	private State state = State.SLEEPING;

	private double eyeX = 0, eyeY = 1;
	private int eyeFrameX = 2, eyeFrameY = 1;

	private Tentacle tutorialTentacle, firstTentacle, secondTentacle, finalTentacle;

	public Octoplorp(double x, double y) {
		super(Math.round(x), Math.round(y));
		tutorialTentacle = new Tentacle(this.x + 6, this.y + 6);
		firstTentacle = new Tentacle(this.x - 6, this.y + 6);
		secondTentacle = new Tentacle(this.x + 6, this.y + 12);
		finalTentacle = new Tentacle(this.x - 6, this.y + 12);
		Main.getLevel().addEntity(tutorialTentacle);
		Main.getLevel().addEntity(firstTentacle);
		Main.getLevel().addEntity(secondTentacle);
		Main.getLevel().addEntity(finalTentacle);
	}

	public void tick() {
		switch (state) {
			case SLEEPING:
				if (new Hitbox(x - 10, y - 10, 20, 20).intersects(Main.getPlayer().getRenderBounds())) {
					state = State.WAKING;
					Main.playCutscene(new Cutscene(5, x, y, 48) {
						float t = 0;

						@Override
						public void tick() {
							t += Main.TIMESTEP;
							eyeY *= 0.9;
							if (t > 4.5)
								eyeFrameX = 0;
							else if (t > 4.3)
								eyeFrameX = 1;
						}

						public void onEnd() {
							state = State.GRABBING;
							tutorialTentacle.state = Tentacle.State.CHASING;
							Main.playCutscene(new Cutscene(2, x, y, 48) {
								@Override
								public void tick() {
									tutorialTentacle.tick();
								}

								public void onEnd() {

								}
							});
						}
					});
				}
				break;
			case WAKING:
				// ??
				break;
			case GRABBING:

		}
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		g.drawImage(body, Math.round(x) - 3, Math.round(y) - 3, 6, 6);
		g.drawImage(eye, eyeFrameX * 64, eyeFrameY * 32, 48, 32, Math.round(x) - 3 + eyeX, Math.round(y) - 2 + eyeY, 4,
				2);
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(Math.round(x) - 3, Math.round(y) - 3, 6, 6);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(Main.getLevel().bounds);
	}

	public Hitbox getSolidBounds() {
		return new Hitbox(Math.round(x) - 3, Math.round(y) - 3, 6, 6);
	}

	public Hitbox getHitBounds() {
		return new Hitbox();
	}

	// A class controlling Octoplorp tentacles
	public static class Tentacle extends Entity {
		protected static final Image sheet = new Image("file:res/entity/octoplorp/tentacles.png");

		public static enum State {
			DEAD, RESTING, GRABBING, RISING, CHASING
		};

		public State state = State.RESTING;

		private double health = 10.0, groundLevel = 0;

		private Hitbox hitbox = new Hitbox();

		private ArrayList<Segment> segments = new ArrayList<>();

		private int orientation = 0;

		public Tentacle(double x, double y) {
			super(x, y);
			groundLevel = y;
		}

		public void tick() {
			switch (state) {
				case DEAD:
					return;
				case RESTING:
					return;
				case RISING:
					y -= Main.TIMESTEP;
					break;
				case GRABBING:
					Main.getPlayer().setX(x);
					Main.getPlayer().setY(y);
					break;
				case CHASING:

			}
		}

		public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
			g.save();
			g.translate(x, y);
			g.rotate(-90 * orientation);
			g.drawImage(sheet, 0, 48, 16, 48, -0.5, -2.5, 1, 3);
			g.restore();
		}

		public Hitbox getRenderBounds() {
			return new Hitbox(hitbox.getBounds());
		}

		public Hitbox getUpdateBounds() {
			return new Hitbox(Main.getLevel().bounds);
		}

		public Hitbox getSolidBounds() {
			return hitbox;
		}

		public Hitbox getHitBounds() {
			return switch (orientation) {
				case 0 -> new Hitbox(x - 0.5, y - 2.5, 1, 3);
				case 1 -> new Hitbox(x - 0.5, y - 0.5, 3, 1);
				case 2 -> new Hitbox(x - 0.5, y - 0.5, 1, 3);
				case 3 -> new Hitbox(x - 2.5, y - 0.5, 3, 1);
				default -> new Hitbox(x - 0.5, y - 0.5, 1, 1);
			};
		}

		public void dealPlayerDamage(double damage) {
			health -= damage;
			if (health <= 0)
				state = State.DEAD;
		}

		public class Segment {
			public static enum Type {
				STRAIGHT, TURN_RIGHT, TURN_LEFT
			}

			// 0 == up, 1 == right, 2 == down, 3 == left
			private int orientation;
			private Type type;
			private int x, y;

			public Segment(int x, int y, Type type, int orientation) {
				this.x = x;
				this.y = y;
				this.type = type;
				this.orientation = orientation;
			}

			public void render(GraphicsContext g) {
				g.save();
				// Turn -90 degrees for each next orientation
				g.translate(x, y);
				g.rotate(-90 * orientation);
				switch (type) {
					case STRAIGHT -> g.drawImage(sheet, 0, 80, 16, 16, -0.5, -0.5, 1, 1);
					case TURN_RIGHT -> g.drawImage(sheet, 16, 64, 16, 16, -0.5, -0.5, 1, 1);
					case TURN_LEFT -> g.drawImage(sheet, 16, 80, 16, 16, -0.5, -0.5, 1, 1);
				}
				g.restore();
			}

			/**
			 * With type "straight", the next segment's orientation is the same as this
			 * With turn types, next orientation will be one to the left or the right
			 *
			 * @return Orientation of next potential segment
			 */
			private int nextOrientation() {
				return switch (type) {
					case STRAIGHT -> orientation;
					case TURN_RIGHT -> (orientation + 1) % 4;
					case TURN_LEFT -> (orientation + 1) % 4;
				};
			}

			/**
			 * Create a segment pointed upwards from the given point, if possible
			 */
			public Segment up() {
				return switch (nextOrientation()) {
					case 0 -> new Segment(x, y - 1, Type.STRAIGHT, 0);
					case 1 -> new Segment(x + 1, y, Type.TURN_LEFT, 0);
					case 3 -> new Segment(x - 1, y, Type.TURN_RIGHT, 0);

					case 2 -> throw new RuntimeException("Asked to turn 180 degrees!");
					default -> throw new RuntimeException("Invalid direction!");
				};
			}

			/**
			 * Create a segment pointed right from the given point, if possible
			 */
			public Segment right() {
				return switch (nextOrientation()) {
					case 0 -> new Segment(x, y - 1, Type.TURN_RIGHT, 1);
					case 1 -> new Segment(x + 1, y, Type.STRAIGHT, 1);
					case 2 -> new Segment(x, y + 1, Type.TURN_LEFT, 1);

					case 3 -> throw new RuntimeException("Asked to turn 180 degrees!");
					default -> throw new RuntimeException("Invalid direction!");
				};
			}

			/**
			 * Create a segment pointed down from the given point, if possible
			 */
			public Segment down() {
				return switch (nextOrientation()) {
					case 1 -> new Segment(x + 1, y, Type.TURN_RIGHT, 2);
					case 2 -> new Segment(x, y + 1, Type.STRAIGHT, 2);
					case 3 -> new Segment(x - 1, y, Type.TURN_LEFT, 2);

					case 0 -> throw new RuntimeException("Asked to turn 180 degrees!");
					default -> throw new RuntimeException("Invalid direction!");
				};
			}

			/**
			 * Create a segment pointed left from the given point, if possible
			 */
			public Segment left() {
				return switch (nextOrientation()) {
					case 0 -> new Segment(x, y - 1, Type.TURN_LEFT, 2);
					case 2 -> new Segment(x, y + 1, Type.TURN_RIGHT, 2);
					case 3 -> new Segment(x - 1, y, Type.STRAIGHT, 3);

					case 1 -> throw new RuntimeException("Asked to turn 180 degrees!");
					default -> throw new RuntimeException("Invalid direction!");
				};
			}

		}

	}
}
