package com.zandgall.csc322.finalproj.entity.octoplorp;

import java.util.ArrayList;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.staging.Cutscene;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Path;
import com.zandgall.csc322.finalproj.util.Point;
import com.zandgall.csc322.finalproj.util.Util;
import com.zandgall.csc322.finalproj.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Tentacle extends Entity {
	protected static final Image sheet = new Image("/entity/octoplorp/tentacles.png");

	public static enum State {
		DEAD, DYING, RESTING, GRABBING, GRABBED, CHASING, REPOSITION, RETRACTING, INJURED, SWINGING
	};

	public State state = State.RESTING;

	public double health = 100.0, timer = 0, speed = 1;

	private double corpseRotation = 1.5 * Math.PI, corpseRotationVel = 1;
	private Vector home, start, throwing, sword, corpse;

	private Hitbox hitbox = new Hitbox();

	private ArrayList<Segment> segments = new ArrayList<>();
	private ArrayList<Point> traveled = new ArrayList<>();

	public ThrownSword thrownSword = null;

	/* 0 = right, 1 = down, 2 = left, 3 = up */
	private int orientation = 3;

	private Path path = new Path();

	public boolean tutorial = false;

	public Tentacle(Vector pos, Vector home, Vector throwing, Vector sword) {
		super(pos.x, pos.y);
		hitbox.add(pos.x - 0.5, pos.y - 0.5, 1, 1);
		start = new Vector(pos.x, pos.y);
		this.home = home;
		this.throwing = throwing;
		this.sword = sword;
	}

	public void tick() {
		switch (state) {
			case DYING:
			case DEAD:
			case RESTING:
				return;
			case GRABBED:
			case GRABBING:
				if (!path.empty()) {
					speed = Math.max(0.1, home.dist(position));
					followPath();
					if (path.empty()) {
						home.y -= 1;
						Segment s = segments.getLast();
						s.type = switch (s.orientation) {
							case 0 -> Segment.Type.TURN_LEFT;
							case 1 -> Segment.Type.STRAIGHT;
							case 2 -> Segment.Type.TURN_RIGHT;
							case 3 -> Segment.Type.STRAIGHT;
							default -> Segment.Type.STRAIGHT;
						};
					}
				} else {
					position.y = position.y * 0.99 + home.y * 0.01;
					orientation = 3;
					state = State.GRABBED;
				}

				Main.getPlayer().setX(position.x + (nextPosition().x - tileX()) * 1.5);
				Main.getPlayer().setY(position.y + (nextPosition().y - tileY()) * 1.5);
				break;
			case CHASING:
				timer += Main.TIMESTEP;
				if (timer >= 2 || path.empty()) {
					timer = 0;
					pathfindTo(Main.getPlayer().tileX(), Main.getPlayer().tileY(), false);
					if(path.empty())
						state = State.REPOSITION;
				}
				if (!path.empty())
					followPath();
				if (getHitBounds().intersects(Main.getPlayer().getHitBounds())) {
					state = State.GRABBING;
					home.y = position.y - 4;
					speed = 5;
					/*
					 * Point a = new Point(tileX(), tileY()), b = nextPosition();
					 * int dX = b.x - a.x, dY = b.y - a.y;
					 * path = Path.pathfind(tileX() + dX, tileY() + dY, (int) Math.floor(homeX),
					 * (int) Math.floor(homeY), a, b);
					 */
					pathfindTo((int) Math.floor(home.x), (int) Math.floor(home.y), true);
				}
				break;
			case REPOSITION:
				timer += Main.TIMESTEP;
				if (timer >= 2) {
					timer = 0;
					pathfindTo(Main.getPlayer().tileX(), Main.getPlayer().tileY(), false);
					if(!path.empty())
						state = State.CHASING;
				}
				retracePath();
				break;
			case INJURED:
				timer += Main.TIMESTEP;
				if (timer >= 1) {
					corpse = new Vector(position.x, position.y-0.5);
					speed = 10;
					timer = 0;
					state = State.SWINGING;
				}
				Main.getPlayer().getPosition().set(position).add(Vector.ofAngle(orientation*0.5*Math.PI).scale(1.5));
				Main.getPlayer().getVelocity().set(0, 0);
				break;
			case SWINGING:
				timer += Main.TIMESTEP * 0.5;
				corpseRotation += corpseRotationVel * timer * 0.1;
				Main.getPlayer().getPosition().set(corpse).add(Vector.ofAngle(corpseRotation));
				if(thrownSword == null && Math.abs(corpseRotation) > 4*Math.PI && Math.abs(Util.signedAngularDistance(
						corpseRotation + corpseRotationVel * 0.5 * Math.PI,
						Math.atan2(sword.y-getY(), sword.x - getX()))) < 0.2 * timer) {

					Main.getPlayer().takeAwaySword();
					thrownSword = new ThrownSword(Main.getPlayer().getX(), Main.getPlayer().getY(), sword, corpseRotationVel);
					Main.getLevel().addEntity(thrownSword);	
				} else if(Math.abs(corpseRotation) > 8*Math.PI && Math.abs(Util.signedAngularDistance(
						corpseRotation + corpseRotationVel * 0.5 * Math.PI,
						Math.atan2(throwing.y - getY(), throwing.x - getX()))) < 0.2 * timer) {
					corpse.add(Vector.ofAngle(corpseRotation));
					state = State.RETRACTING;
				}	
				break;
			case RETRACTING:
				if (corpse.sqDist(throwing) > 1) {
					Vector dir = corpse.unitDir(throwing).scale(0.1);
					corpse.add(dir);
					corpseRotation += corpseRotationVel * timer * 0.1;
					corpseRotationVel *= 0.99;
					Main.getPlayer().getPosition().set(corpse);
					Main.getPlayer().getVelocity().set(dir.getScale(100));
				}
				retracePath();
				if(segments.isEmpty()) {
					state = State.DYING;
					Main.playCutscene(new Cutscene(3) {
						@Override
						protected Vector getTarget() {
							return thrownSword.getPosition();
						}

						@Override
						protected boolean done() {
							return super.done() && thrownSword.reachedTarget;
						}

						@Override
						protected void onEnd() {
							state = State.DEAD;
						}

						@Override
						protected double getTargetZoom() {
							return 48;
						}
					});
				}
				break;
		}
	}

	private void pathfindTo(int x, int y, boolean watchDirection) {
		if (path.empty() || watchDirection) {
			Point a = new Point(tileX(), tileY()), b = nextPosition();
			int dX = b.x - a.x, dY = b.y - a.y;
			traveled.add(a);
			traveled.add(b);
			hitbox.add(a.x, a.y, 1, 1);
			hitbox.add(b.x, b.y, 1, 1);
			path = Path.pathfind(tileX() + dX, tileY() + dY, x, y, traveled.toArray(new Point[traveled.size()]));
			for (int i = 0; i < traveled.size() - 2 && !path.empty(); i++)
				path.progress();
		} else {
			path = Path.pathfind(tileX(), tileY(), x, y, traveled.toArray(new Point[traveled.size()]));
			for (int i = 0; i < traveled.size() && !path.empty(); i++)
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
				position.x += Main.TIMESTEP * speed;
				hit = position.x - 0.5 >= path.current().x;
				break;
			case 1:
				position.y += Main.TIMESTEP * speed;
				hit = position.y - 0.5 >= path.current().y;
				break;
			case 2:
				position.x -= Main.TIMESTEP * speed;
				hit = position.x - 0.5 <= path.current().x;
				break;
			case 3:
				position.y -= Main.TIMESTEP * speed;
				hit = position.y - 0.5 <= path.current().y;
				break;
		}
		if (hit) {
			position.x = path.current().x + 0.5;
			position.y = path.current().y + 0.5;
			traveled.add(new Point(tileX(), tileY()));
			hitbox.add(tileX(), tileY(), 1, 1);

			int preOrientation = orientation;
			nextOrientation();

			if ((preOrientation + orientation) % 2 == 0)
				segments.add(new Segment(tileX(), tileY(), Segment.Type.STRAIGHT, preOrientation));
			else if ((orientation > preOrientation && (orientation != 3 || preOrientation != 0))
					|| (orientation == 0 && preOrientation == 3)) {
				segments.add(new Segment(tileX(), tileY(), Segment.Type.TURN_RIGHT, preOrientation));
				corpseRotationVel = 1;
			} else {
				segments.add(new Segment(tileX(), tileY(), Segment.Type.TURN_LEFT, preOrientation));
				corpseRotationVel = -1;
			}

			path.progress();
		}
	}

	/**
	 * Retrace the path back to start
	 */
	private void retracePath() {
		if(segments.isEmpty())
			return;
		boolean hit = false;
		switch(orientation) {
			case 0:
				position.x -= Main.TIMESTEP * speed;
				hit = position.x - 0.5 <= segments.getLast().x;
				break;
			case 1:
				position.y -= Main.TIMESTEP * speed;
				hit = position.y - 0.5 <= segments.getLast().y;
				break;
			case 2:
				position.x += Main.TIMESTEP * speed;
				hit = position.x - 0.5 >= segments.getLast().x;
				break;
			case 3:
				position.y += Main.TIMESTEP * speed;
				hit = position.y - 0.5 >= segments.getLast().y;
				break;

		}
		if(hit) {
			position.x = segments.getLast().x + 0.5;
			position.y = segments.getLast().y + 0.5;
			orientation = segments.removeLast().orientation;
			while(tileX() != traveled.getLast().x || tileY() != traveled.getLast().y)
				traveled.removeLast();

			hitbox = new Hitbox();
			for(Point p : traveled)
				hitbox.add(p.x, p.y, 1, 1);
		}
	}

	/**
	 * Quickly sets the orientation based on the path direction
	 */
	private void nextOrientation() {
		if (path.next() == null)
			return;	
		if (path.next().x > path.current().x)
			orientation = 0;
		else if (path.next().y > path.current().y)
			orientation = 1;
		else if (path.next().x < path.current().x)
			orientation = 2;
		else if (path.next().y < path.current().y)
			orientation = 3;
	}

	/**
	 * Quickly grab the next point that that the tentacle would head towards with
	 * the given orientation
	 */
	private Point nextPosition() {
		return switch (orientation) {
			case 0 -> new Point(tileX() + 1, tileY());
			case 1 -> new Point(tileX(), tileY() + 1);
			case 2 -> new Point(tileX() - 1, tileY());
			case 3 -> new Point(tileX(), tileY() - 1);
			default -> new Point(tileX(), tileY());
		};
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		// Draw dirt mound
		g.drawImage(sheet, 48, 32, 48, 16, start.x - 1.5, start.y - 0.5, 3, 1);

		for (Segment s : segments)
			s.render(g, sheet);
	
		if (health < 100 && (state == State.GRABBING || state == State.GRABBED)) {
			g.setLineWidth(0.05);
			g.setStroke(Color.BLACK);
			g.setFill(Color.RED);
			g.strokeRect(getX() - (orientation == 1 ? 1.75 : 0.75), getY() - (orientation == 3 ? 1.75 : 0.75), 1.5, 0.2);
			g.fillRect(getX() - (orientation == 1 ? 1.75 : 0.75), getY() - (orientation == 3 ? 1.75 : 0.75), 1.5, 0.2);
			g.setFill(Color.GREEN);
			g.fillRect(getX() - (orientation == 1 ? 1.75 : 0.75), getY() - (orientation == 3 ? 1.75 : 0.75),
					health * 0.015, 0.2);
		}

		g.save();
		g.translate(getX(), getY());
		g.rotate(90 * orientation);
		if (state == State.GRABBING || state == State.GRABBED)
			g.drawImage(sheet, 48, 0, 48, 16, -0.5, -0.5, 3, 1);
		else if (state == State.INJURED)
			g.drawImage(sheet, 0, 32, 48, 16, -0.5, -0.5, 3, 1);
		else if (state == State.DEAD || state == State.DYING || state == State.RETRACTING) {
			g.drawImage(sheet, 32, 16, 16, 16, -0.5, -0.5, 1, 1);
			g.restore();
			g.save();
			g.translate(corpse.x, corpse.y);
			g.rotate(180 * corpseRotation / Math.PI);
			g.drawImage(sheet, 64, 16, 32, 16, -1.6, -0.5, 2, 1);
		} else if (state == State.SWINGING) {
			g.drawImage(sheet, 32, 16, 16, 16, -0.5, -0.5, 1, 1);
			g.restore();
			g.save();
			g.translate(corpse.x, corpse.y);
			g.rotate(180 * corpseRotation / Math.PI);
			g.drawImage(sheet, 64, 16, 32, 16, -0.1, -0.5, 2, 1);
		} else
			g.drawImage(sheet, 0, 0, 48, 16, -0.5, -0.5, 3, 1);
		g.restore();

		// Draw dirt mound cover
		if(state == State.DEAD || state == State.DYING)
			g.drawImage(sheet, 48, 16, 16, 16, start.x - 0.5, start.y - 0.5, 1, 1);
		else
			g.drawImage(sheet, 64, 32, 16, 16, start.x - 0.5, start.y - 0.5, 1, 1);

		g.setLineWidth(0.02);
		g.setFill(Color.BLUEVIOLET);
		for (Point p : traveled)
			g.strokeRect(p.x, p.y, 1, 1);

		if (!path.empty())
			path.debugRender(g);
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(Main.getLevel().bounds);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(Main.getLevel().bounds);
	}

	public Hitbox getSolidBounds() {
		if(state == State.DEAD || state == State.DYING || state == State.RETRACTING)
			return new Hitbox();
		return hitbox;
	}

	public Hitbox getHitBounds() {
		return switch (orientation) {
			case 0 -> new Hitbox(getX() - 0.5, getY() - 0.5, 2, 1);
			case 1 -> new Hitbox(getX() - 0.5, getY() - 0.5, 1, 2);
			case 2 -> new Hitbox(getX() - 1.5, getY() - 0.5, 2, 1);
			case 3 -> new Hitbox(getX() - 0.5, getY() - 1.5, 1, 2);
			default -> new Hitbox(getX() - 0.5, getY() - 0.5, 1, 1);
		};
	}

	public void dealPlayerDamage(double damage) {
		health -= damage;
		if (health <= 0 && (state == State.GRABBING || state == State.GRABBED || state == State.CHASING)) {
			timer = 0;
			state = State.INJURED;
		}
	}
}

