/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

*--------------------------------------------------------------------------------*
| THIS FILE IS UNFINISHED                                                        |
| Although this file is being submitted as a part of the assignment, the content |
| and function of this file is unfinished and unorganized. This file shall be    |
| finished and cleaned up in order to fulfill a full playable demo of this game. |
*--------------------------------------------------------------------------------*

 ## Octoplorp
 # A boss fight for the player to reach and fight

 ## Tentacle
 # An entity that represents each of Octoplorp's tentacles

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import java.util.ArrayList;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.entity.Octoplorp.Tentacle.Segment.Type;
import com.zandgall.csc322.finalproj.staging.Cutscene;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Path;
import com.zandgall.csc322.finalproj.util.Point;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Octoplorp extends Entity {

	private static final Image body = new Image("/entity/octoplorp/body.png"),
			eye = new Image("/entity/octoplorp/eye.png");

	static enum State {
		SLEEPING, WAKING, GRABBING
	}

	private State state = State.SLEEPING;

	private double eyeX = 0, eyeY = 1;
	private int eyeFrameX = 2, eyeFrameY = 1;

	private Tentacle tutorialTentacle, firstTentacle, secondTentacle, finalTentacle;

	public Octoplorp(double x, double y) {
		super(Math.round(x), Math.round(y));
		tutorialTentacle = new Tentacle(this.x + 5.5, this.y + 6.5, this.x, this.y + 6.5);
		tutorialTentacle.tutorial = true;
		firstTentacle = new Tentacle(this.x - 5.5, this.y + 6.5, this.x, this.y + 6.5);
		secondTentacle = new Tentacle(this.x + 5.5, this.y + 12.5, this.x, this.y + 6.5);
		finalTentacle = new Tentacle(this.x - 5.5, this.y + 12.5, this.x, this.y + 6.5);
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
							Main.getLevel().addEntity(tutorialTentacle);
						}
					});
				}
				break;
			case WAKING:
				state = State.GRABBING;
				tutorialTentacle.state = Tentacle.State.CHASING;
				tutorialTentacle.speed = 5;
				Main.playCutscene(new Cutscene(5, Main.getPlayer().getX(), Main.getPlayer().getY(), 48) {
					@Override
					public void tick() {
						tutorialTentacle.tick();
					}

					public void onEnd() {

					}
				});
				break;
			case GRABBING:
				// if(tutorialTentacle.state == Tentacle.State.GRABBING)

		}
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		g.drawImage(body, Math.round(x) - 3, Math.round(y) - 3, 6, 6);
		g.drawImage(eye, eyeFrameX * 64, eyeFrameY * 32, 64, 32, Math.round(x) - 2 + eyeX, Math.round(y) - 2 + eyeY, 4,
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
		protected static final Image sheet = new Image("/entity/octoplorp/tentacles.png");

		public static enum State {
			DEAD, RESTING, GRABBING, RISING, CHASING, RETRACTING, INJURED
		};

		public State state = State.RESTING;

		public double health = 100.0, timer = 0, speed = 1, homeX, homeY, startX, startY;

		private Hitbox hitbox = new Hitbox();

		private ArrayList<Segment> segments = new ArrayList<>();
		private ArrayList<Point> traveled = new ArrayList<>();

		private int orientation = 0;

		private Path path = new Path();

		public boolean tutorial = false;

		public Tentacle(double x, double y, double homeX, double homeY) {
			super(x, y);
			hitbox.add(x - 2.5, y - 2.5, 5, 5);
			this.startX = x;
			this.startY = y;
			this.homeX = homeX;
			this.homeY = homeY;
		}

		public void tick() {
			switch (state) {
				case DEAD:	
				case RESTING:
					return;
				case RISING:
					y -= Main.TIMESTEP;
					break;
				case GRABBING:
					if (!path.empty()) {
						speed = Math.max(0.1, Math.sqrt((x - homeX) * (x - homeX) + (y - homeY) * (y - homeY)));
						followPath();
						if (path.empty()) {
							homeY -= 1;
							Segment s = segments.getLast();
							s.type = switch (s.orientation) {
								case 0 -> Segment.Type.STRAIGHT;
								case 1 -> Segment.Type.TURN_LEFT;
								case 2 -> Segment.Type.STRAIGHT;
								case 3 -> Segment.Type.TURN_RIGHT;
								default -> Segment.Type.STRAIGHT;
							};
						}
					} else {
						y = y * 0.99 + homeY * 0.01;
						orientation = 0;
					}

					Main.getPlayer().setX(x + (nextPosition().x - tileX()) * 1.5);
					Main.getPlayer().setY(y + (nextPosition().y - tileY()) * 1.5);
					break;
				case CHASING:
					timer += Main.TIMESTEP;
					if (timer >= 2) {
						timer = 0;
						pathfindTo(Main.getPlayer().tileX(), Main.getPlayer().tileY(), false);
					}
					if (!path.empty())
						followPath();
					if (getHitBounds().intersects(Main.getPlayer().getHitBounds())) {
						state = State.GRABBING;
						homeY = y - 4;
						/*
						 * Point a = new Point(tileX(), tileY()), b = nextPosition();
						 * int dX = b.x - a.x, dY = b.y - a.y;
						 * path = Path.pathfind(tileX() + dX, tileY() + dY, (int) Math.floor(homeX),
						 * (int) Math.floor(homeY), a, b);
						 */
						traveled.add(nextPosition());
						pathfindTo((int) Math.floor(homeX), (int) Math.floor(homeY), true);
					}
					break;
				case INJURED:
					timer += Main.TIMESTEP;
					if(timer >= 1)
						state = State.DEAD;
					break;
				case RETRACTING:
					timer += Main.TIMESTEP;
			}
		}

		private void pathfindTo(int x, int y, boolean watchDirection) {
			if (path.empty() || watchDirection) {
				Point a = new Point(tileX(), tileY()), b = nextPosition();
				int dX = b.x - a.x, dY = b.y - a.y;
				traveled.add(a);
				traveled.add(b);
				path = Path.pathfind(tileX() + dX, tileY() + dY, x, y, traveled.toArray(new Point[traveled.size()]));
				segments.add(new Segment(tileX(), tileY(), Type.STRAIGHT, orientation));
				for (int i = 0; i < traveled.size() - 2; i++)
					path.progress();
			} else {
				path = Path.pathfind(tileX(), tileY(), x, y, traveled.toArray(new Point[traveled.size()]));
				segments.add(new Segment(tileX(), tileY(), Type.STRAIGHT, orientation));
				for (int i = 0; i < traveled.size(); i++)
					path.progress();
			}

		}

		/**
		 * Follow links of the path
		 */
		private void followPath() {
			boolean hit = false;
			switch (orientation) {
				case 0:
					y -= Main.TIMESTEP * speed;
					hit = y - 0.5 <= path.current().y;
					break;
				case 1:
					x += Main.TIMESTEP * speed;
					hit = x - 0.5 >= path.current().x;
					break;
				case 2:
					y += Main.TIMESTEP * speed;
					hit = y - 0.5 >= path.current().y;
					break;
				case 3:
					x -= Main.TIMESTEP * speed;
					hit = x - 0.5 <= path.current().x;
					break;
			}
			if (hit) {
				x = path.current().x + 0.5;
				y = path.current().y + 0.5;
				traveled.add(new Point(tileX(), tileY()));

				int preOrientation = orientation;
				nextOrientation();

				if ((preOrientation + orientation) % 2 == 0)
					segments.add(new Segment(tileX(), tileY(), Type.STRAIGHT, preOrientation));
				else if ((orientation > preOrientation && (orientation != 3 || preOrientation != 0))
						|| (orientation == 0 && preOrientation == 3))
					segments.add(new Segment(tileX(), tileY(), Type.TURN_RIGHT, preOrientation));
				else
					segments.add(new Segment(tileX(), tileY(), Type.TURN_LEFT, preOrientation));

				path.progress();
			}

		}

		/**
		 * Quickly sets the orientation based on the path direction
		 */
		private void nextOrientation() {
			if (path.next() == null)
				return;
			if (path.next().y < path.current().y)
				orientation = 0;
			else if (path.next().x > path.current().x)
				orientation = 1;
			else if (path.next().y > path.current().y)
				orientation = 2;
			else if (path.next().x < path.current().x)
				orientation = 3;
		}

		/**
		 * Quickly grab the next point that that the tentacle would head towards with
		 * the given orientation
		 */
		private Point nextPosition() {
			return switch (orientation) {
				case 0 -> new Point((int) Math.floor(x), (int) Math.floor(y) - 1);
				case 1 -> new Point((int) Math.floor(x) + 1, (int) Math.floor(y));
				case 2 -> new Point((int) Math.floor(x), (int) Math.floor(y) + 1);
				case 3 -> new Point((int) Math.floor(x) - 1, (int) Math.floor(y));
				default -> new Point((int) Math.floor(x), (int) Math.floor(y));
			};
		}

		public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
			// Draw dirt mound
			g.drawImage(sheet, 16, 32, 48, 16, startX-1.5, startY-0.5, 3, 1);

			for (Segment s : segments)
				s.render(g);
	
			if (health < 100) {
				g.setLineWidth(0.05);
				g.setStroke(Color.BLACK);
				g.setFill(Color.RED);
				g.strokeRect(x - (orientation == 2 ? 1.75 : 0.75), y - (orientation == 0 ? 1.75 : 0.75), 1.5, 0.2);
				g.fillRect(x - (orientation == 2 ? 1.75 : 0.75), y - (orientation == 0 ? 1.75 : 0.75), 1.5, 0.2);
				g.setFill(Color.GREEN);
				g.fillRect(x - (orientation == 2 ? 1.75 : 0.75), y - (orientation == 0 ? 1.75 : 0.75),
						health * 0.015, 0.2);
			}	
			
			g.save();
			g.translate(x, y);
			g.rotate(90 * orientation);
			if (state == State.GRABBING)
				g.drawImage(sheet, 0, 0, 16, 48, -0.5, -2.5, 1, 3);
			else if (state == State.INJURED)
				g.drawImage(sheet, 32, 48, 16, 48, -0.5, -2.5, 1, 1);
			else if (state == State.DEAD)
				g.drawImage(sheet, 16, 48, 16, 16, -0.5, -0.5, 1, 1);
			else
				g.drawImage(sheet, 0, 48, 16, 48, -0.5, -2.5, 1, 3);
			g.restore();

			// Draw dirt mound cover
			g.drawImage(sheet, 32, 32, 16, 16, startX-0.5, startY-0.5, 1, 1);


			g.setLineWidth(0.02);
			g.setFill(Color.BLUEVIOLET);
			for(Point p : traveled)
				g.strokeRect(p.x, p.y, 1, 1);

			if(!path.empty())
				path.debugRender(g);
		}

		public Hitbox getRenderBounds() {
			return new Hitbox(x - 2.5, y - 2.5, 5, 5);
		}

		public Hitbox getUpdateBounds() {
			return new Hitbox(Main.getLevel().bounds);
		}

		public Hitbox getSolidBounds() {
			return hitbox;
		}

		public Hitbox getHitBounds() {
			return switch (orientation) {
				case 0 -> new Hitbox(x - 0.5, y - 1.5, 1, 2);
				case 1 -> new Hitbox(x - 0.5, y - 0.5, 2, 1);
				case 2 -> new Hitbox(x - 0.5, y - 0.5, 1, 2);
				case 3 -> new Hitbox(x - 1.5, y - 0.5, 2, 1);
				default -> new Hitbox(x - 0.5, y - 0.5, 1, 1);
			};
		}

		public void dealPlayerDamage(double damage) {
			health -= damage;
			if (health <= 0) {
				timer = 0;
				state = State.INJURED;
			}
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
				g.translate(x + 0.5, y + 0.5);
				g.rotate(90 * orientation);
				switch (type) {
					case STRAIGHT -> g.drawImage(sheet, 0, 80, 16, 16, -0.5, -0.5, 1, 1);
					case TURN_RIGHT -> g.drawImage(sheet, 16, 64, 16, 16, -0.5, -0.5, 1, 1);
					case TURN_LEFT -> g.drawImage(sheet, 16, 80, 16, 16, -0.5, -0.5, 1, 1);
				}
				g.restore();
			}
		}

	}
}
