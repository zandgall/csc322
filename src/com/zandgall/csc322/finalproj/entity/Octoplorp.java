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
import com.zandgall.csc322.finalproj.util.Hitbox;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Octoplorp extends Entity {

	private static final Image body = new Image("file:res/entity/octoplorp/body.png"),
			eye = new Image("file:res/entity/octoplorp/eye.png");

	private int eyeFrameX = 2, eyeFrameY = 1;

	public Octoplorp(double x, double y) {
		super(x, y);
	}

	public void tick() {

	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		g.drawImage(body, Math.round(x) - 3, Math.round(y) - 3, 6, 6);
		g.drawImage(eye, eyeFrameX * 64, eyeFrameY * 32, 48, 32, Math.round(x) - 2, Math.round(y) - 2, 4, 2);
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

		private Hitbox hitbox = new Hitbox();

		private ArrayList<Segment> segments = new ArrayList<>();

		public Tentacle(double x, double y) {
			super(x, y);
		}

		public void tick() {

		}

		public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {

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
			return new Hitbox();
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
				g.rotate(-90 * orientation);
				switch (type) {
					case STRAIGHT -> g.drawImage(sheet, 0, 80, 16, 16, x, y, 1, 1);
					case TURN_RIGHT -> g.drawImage(sheet, 16, 64, 16, 16, x, y, 1, 1);
					case TURN_LEFT -> g.drawImage(sheet, 16, 80, 16, 16, x, y, 1, 1);
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
