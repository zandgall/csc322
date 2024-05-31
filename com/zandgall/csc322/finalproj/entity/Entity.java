/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Entity
 # An abstract class that provides methods and fields common to entities

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Constructor;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;

public abstract class Entity {
	public static final ArrayList<EntityEntry> ENTRIES = new ArrayList<EntityEntry>();
	public static final HashMap<String, EntityEntry> NAME_MAP = new HashMap<String, EntityEntry>();

	public static <T extends Entity> void register(String name, Class<T> clazz) {
		EntityEntry<T> entry = new EntityEntry<T>(clazz);
		ENTRIES.add(entry);
		NAME_MAP.put(name, entry);
	}

	protected double x, y;
	protected double xVel, yVel;

	public Entity() {
		this.x = 0;
		this.y = 0;
		this.xVel = 0;
		this.yVel = 0;
	}

	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
		this.xVel = 0;
		this.xVel = 0;
	}

	public abstract void tick(double delta);

	public abstract void render(GraphicsContext g);

	public double getX() { return x; }
	public double getY() { return y; }
	public double getXVel() { return xVel; }
	public double getYVel() { return yVel; }

	public abstract Hitbox getRenderBounds();
	public abstract Hitbox getUpdateBounds();
	public abstract Hitbox getSolidBounds();

	protected void move(double delta) {
		double nextX = x, nextY = y;

		// Check it against all solid entities
		Hitbox box = new Hitbox(x + xVel * delta - 0.5, y + yVel * delta - 0.5, 1.0, 1.0);
		for(Entity e : Main.getLevel().getEntities()) {
			if(e == this)
				continue; // don't collide with self
			Hitbox solid = e.getSolidBounds();	
			if(solid.intersects(box)) {	
				// We increment by 1/100th of a tile until we find the exact moment we intersect with something solid
				double stepLength = Math.sqrt(xVel * xVel + yVel * yVel) * 100;
				double xStep = xVel / stepLength, yStep = yVel / stepLength;

				// If entity isn't moving then no need to update
				if(xVel == 0 && yVel == 0)
					break;
				box = new Hitbox(x - 0.5, y - 0.5, 1.0, 1.0);
				while(!box.translate(xStep, yStep).intersects(solid)) {
					nextX += xStep;
					nextY += yStep;
					box = box.translate(xStep, yStep);
				}				// We are now within 1/100th of a tile within a solid box
				// If we step in the x direction and intersect, we must stop all x velocity
				if(box.translate(xStep, 0).intersects(solid))
					xVel = 0;
				// if we step in the y direction and intersect, we must stop all y velocity
				if(box.translate(0, yStep).intersects(solid))
					yVel = 0;	
			}
		}

		// Move to the next position
		// If there was a collision, nextX, nextY, xVel, yVel will have been modified
		// and we will be pressed up against a solid wall
		x = nextX + xVel * delta;
		y = nextY + yVel * delta;

		// Handle friction
		double frictionRatio = 1 / (1 + (delta*10));
		xVel *= frictionRatio;
		yVel *= frictionRatio;

	}

	// An overridable function used to determine which entities get drawn over other entities. By default, entities that are lower on screen are drawn in front of entities that are higher
	public double getRenderLayer() { return y; }

	protected static class EntityEntry<T extends Entity> {
		public Class<T> classType;
		public EntityEntry(Class<T> classType) {
			this.classType = classType;
		}
		// public T read(ObjectInputStream)
		// public void write(ObjectOutputStream, T)
		public T construct(double x, double y) {
			try {
				Constructor<T> defaultConstructor = classType.getConstructor();
				T out = defaultConstructor.newInstance();
				out.x = x;
				out.y = y;
				return out;
			} catch (Exception ignored) {
				try {
					Constructor<T> posConstructor = classType.getConstructor(double.class, double.class);
					return posConstructor.newInstance(x, y);
				} catch(Exception ignored2) {
					System.err.println("Could not create instance of " + classType.getCanonicalName());
					return null;
				}
			}
		}
	}

}
