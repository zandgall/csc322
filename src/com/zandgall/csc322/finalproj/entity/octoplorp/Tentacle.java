package com.zandgall.csc322.finalproj.entity.octoplorp;

import java.util.ArrayList;
import java.util.HashMap;

import com.zandgall.csc322.finalproj.Camera;
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
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Tentacle extends Entity {
	protected static final Image sheet = new Image("/entity/octoplorp/tentacles.png");

	public static final boolean TENTACLE_DEBUG = false;

	public static enum State {
		DEAD, DYING, RESTING, GRABBING, GRABBED, WINDUP, CHASING, REPOSITION, RETRACTING, INJURED, SWINGING
	};

	public static enum SegType {
		STRAIGHT, TURN_RIGHT, TURN_LEFT
	};

	public State state = State.RESTING;

	public double health = 100.0, timer = 0, speed = 1, damage = 5;

	private double corpseRotation = 1.5 * Math.PI, corpseRotationVel = 1;
	private Vector home, start, throwing, sword, corpse;

	private Hitbox hitbox = new Hitbox();

	// private ArrayList<Segment> segments = new ArrayList<>();
	private Path path = new Path(); // queue: points to travel
	private ArrayList<Point> traveled = new ArrayList<>();
	private HashMap<Point, Integer> segments = new HashMap<>();
	private HashMap<Point, SegType> segtypes = new HashMap<>();

	public ThrownSword thrownSword = null;

	/* 0 = right, 1 = down, 2 = left, 3 = up */
	private int orientation = 3;


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
			case WINDUP:
				timer+=Main.TIMESTEP;
				if(timer >= 1)
					state = State.CHASING;
				return;
			case GRABBED:
				Main.getPlayer().dealEnemyDamage(damage);
			case GRABBING:
				if (!path.empty()) {
					if(home != null)
						speed = Math.max(1, home.dist(position));
					followPath();
					if (path.empty()) {
						Point p = traveled.getLast();
						if(segments.get(p) == 1) {
							if(home != null)
								home.y += 1;
							orientation = 1;
						} else {
							if(home != null)
								home.y -= 1;
							orientation = 3;
						}
						segtypes.put(p, switch (segments.get(p)) {
							case 0 -> SegType.TURN_LEFT;
							case 1 -> SegType.STRAIGHT;
							case 2 -> SegType.TURN_RIGHT;
							case 3 -> SegType.STRAIGHT;
							default -> SegType.STRAIGHT;
						});
					}
				} else {
					if(home != null)
						position.y = position.y * 0.99 + home.y * 0.01;
					state = State.GRABBED;
				}

				Main.getPlayer().setX(position.x + (nextPosition().x - tileX()) * 1.5);
				Main.getPlayer().setY(position.y + (nextPosition().y - tileY()) * 1.5);
				break;
			case CHASING:
				timer += Main.TIMESTEP;
				if ((timer >= 2 || path.size() == 1) && (!TENTACLE_DEBUG || (Main.keys.get(KeyCode.COMMA)&&!Main.pKeys.get(KeyCode.COMMA)))) {
					timer = 0;
					pathfindTo(Main.getPlayer().tileX(), Main.getPlayer().tileY(), false);
					if(path.empty())
						state = State.REPOSITION;
				}
				if (path.size() > 1)
					followPath();
				if (getHitBounds().intersects(Main.getPlayer().getHitBounds())) {
					state = State.GRABBING;
					speed = 5;
					if(home != null)
						pathfindTo((int) Math.floor(home.x), (int) Math.floor(home.y), true);
					if(path.empty() || home == null) {
						Point next = nextPosition();
						Vector dir = new Vector(next.x-tileX(), next.y-tileY());
						home = new Vector(tileX()+0.5, tileY()+0.5);
						home.add(dir.getScale(2));
						pathfindTo((int) Math.floor(home.x), (int) Math.floor(home.y), true);
						if(path.empty())
							home = home.getSub(dir);
					}
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
					corpseRotation = orientation * 0.5 * Math.PI;
					corpse = new Vector(position.x, position.y).add(Vector.ofAngle(corpseRotation).scale(0.5));
					speed = 40;
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
				if(traveled.isEmpty()) {
					state = State.DYING;
					Main.playCutscene(new Cutscene(1) {
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

						@Override
						protected double getSmoothing() {
							return Camera.DEFAULT_SMOOTHING * 5;
						}
					});
				}
				break;
		}
	}

	private void pathfindTo(int x, int y, boolean watchDirection) {
		if (path.empty() || path.size() <= 1 || watchDirection) {
			Point a = new Point(tileX(), tileY()), b = nextPosition();
			int dX = b.x - a.x, dY = b.y - a.y;
			traveled.add(a);
			traveled.add(b);
			if(!segments.containsKey(a)) {
				// TODO: Mark unsure of
				segments.put(a, orientation);
				segtypes.put(a, SegType.STRAIGHT);
			}
			if(!segments.containsKey(b)) {
				// TODO: Mark unsure of
				segments.put(b, orientation);
				segtypes.put(b, SegType.STRAIGHT);
			}
			hitbox.add(a.x, a.y, 1, 1);
			hitbox.add(b.x, b.y, 1, 1);
			path = Path.pathfind(tileX() + dX, tileY() + dY, x, y, traveled.toArray(new Point[traveled.size()]));
			// Remove segments added before pathfinding
			traveled.removeLast();
			traveled.removeLast();
			for (int i = 0; i < traveled.size() && !path.empty(); i++)
				path.progress();
		} else {
			// Grant two tiles of previous path to lead into the next path
			traveled.add(path.progress());
			traveled.add(path.progress());
			path = Path.pathfind(traveled.getLast().x, traveled.getLast().y, x, y, traveled.toArray(new Point[traveled.size()]));
			traveled.removeLast();
			traveled.removeLast();
			for (int i = 0; i < traveled.size() && !path.empty(); i++)
				path.progress();
		}
	}

	/**
	 * Follow links of the path
	 */
	private void followPath() {
		boolean hit = false;
		if(TENTACLE_DEBUG){
			if(Main.keys.get(KeyCode.PERIOD) && !Main.pKeys.get(KeyCode.PERIOD))
				hit = true;
		} else
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

			Point p = new Point(tileX(), tileY());
			traveled.add(p);
			hitbox.add(tileX(), tileY(), 1, 1);

			int preOrientation = orientation;
			nextOrientation();

			if ((preOrientation + orientation) % 2 == 0) {
				segments.put(p, preOrientation);
				segtypes.put(p, SegType.STRAIGHT);
			} else if ((orientation > preOrientation && (orientation != 3 || preOrientation != 0))
					|| (orientation == 0 && preOrientation == 3)) {
				corpseRotationVel = 1;
				segments.put(p, preOrientation);
				segtypes.put(p, SegType.TURN_RIGHT);
			} else {
				corpseRotationVel = -1;
				segments.put(p, preOrientation);
				segtypes.put(p, SegType.TURN_LEFT);
			}

			path.progress();
		}
	}

	/**
	 * Retrace the path back to start
	 */
	private void retracePath() {
		if(traveled.isEmpty())
			return;
		boolean hit = false;
		switch(orientation) {
			case 0:
				position.x -= Main.TIMESTEP * speed;
				hit = position.x - 0.5 <= traveled.getLast().x;
				break;
			case 1:
				position.y -= Main.TIMESTEP * speed;
				hit = position.y - 0.5 <= traveled.getLast().y;
				break;
			case 2:
				position.x += Main.TIMESTEP * speed;
				hit = position.x - 0.5 >= traveled.getLast().x;
				break;
			case 3:
				position.y += Main.TIMESTEP * speed;
				hit = position.y - 0.5 >= traveled.getLast().y;
				break;

		}
		if(hit) {
			position.x = traveled.getLast().x + 0.5;
			position.y = traveled.getLast().y + 0.5;
			orientation = segments.get(traveled.getLast());
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

		for (Point p : traveled) {
			g.save();
			g.translate(p.x + 0.5, p.y + 0.5);
			g.rotate(segments.get(p) * 90);
			switch (segtypes.get(p)) {
				case STRAIGHT -> g.drawImage(sheet, 0, 0, 16, 16, -0.5, -0.5, 1, 1);
				case TURN_RIGHT -> g.drawImage(sheet, 16, 16, 16, 16, -0.5, -0.5, 1, 1);
				case TURN_LEFT -> g.drawImage(sheet, 0, 16, 16, 16, -0.5, -0.5, 1, 1);
			}
			g.restore();
		}
	
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
		if(state == State.WINDUP)
			g.translate((Math.random()-0.5)*timer*0.1, (Math.random()-0.5)*timer*0.1);
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
		} else {
			double gX = getX() - 0.5, tX = Math.floor(gX);
			double gY = getY() - 0.5, tY = Math.floor(gY);
			double clipset = -0.5 + switch(orientation) {
			case 0 -> 1 + tX - gX;
			case 1 -> 1 + tY - gY;
			case 2 -> gX - tX;
			case 3 -> gY - tY;
			default -> 0;
			};
			g.save();
			g.beginPath();
			g.rect(clipset, -0.5, 3, 1);
			g.clip();
			g.drawImage(sheet, 0, 0, 48, 16, -0.5, -0.5, 3, 1);
			g.restore();
		}
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

