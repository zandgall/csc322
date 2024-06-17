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
import com.zandgall.csc322.finalproj.level.tile.Tile;

public abstract class Entity {
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

	public abstract void tick();

	/*
	* A method to define how to draw each type of entity
	* It is provided with different context layers
	* @param context_1 A context for layer 1
	* @param context_shadow A context specifically for shadows
	* @param context_2 A context for layer 2
	*/
	public abstract void render(GraphicsContext context_1, GraphicsContext context_shadow, GraphicsContext context_2);


	public double getX() { return x; }
	public double getY() { return y; }
	public double getXVel() { return xVel; }
	public double getYVel() { return yVel; }

	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }

	public abstract Hitbox getRenderBounds();
	public abstract Hitbox getUpdateBounds();
	public abstract Hitbox getSolidBounds();
	public abstract Hitbox getHitBounds();

	// Overrideable functions to handle different types of damage
	public void dealEnemyDamage(double damage) {}
	public void dealPlayerDamage(double damage) {}


	/**
	* Handles movement and collision. Also applies friction to xVel and yVel.
	* @returns True whether hit a wall/solid object
	*/
	protected boolean move() {
		if(xVel == 0 && yVel == 0)
			return false;

		boolean hitWall = false;

		double nextX = x, nextY = y;

		// Check it against all solid entities
		Hitbox box = getSolidBounds().translate(xVel * Main.TIMESTEP, yVel * Main.TIMESTEP);
		for(Entity e : Main.getLevel().getEntities()) {
			if(e == this)
				continue; // don't collide with self
			Hitbox solid = e.getSolidBounds();	
			if(solid.intersects(box)) {
				handleCollision(solid, nextX, nextY);
				hitWall = true;
			}
		}

		int minX = (int)Math.floor(box.getBounds().getMinX());
		int minY = (int)Math.floor(box.getBounds().getMinY());
		int maxX = (int)Math.floor(box.getBounds().getMaxX());
		int maxY = (int)Math.floor(box.getBounds().getMaxY());	
		for(int i = minX; i <= maxX; i++) {
			for(int j = minY; j <= maxY; j++) {
				Tile t = Main.getLevel().get(i, j);
				if(t != null && t.solidBounds(i, j) != null && t.solidBounds(i, j).intersects(box)) {
					handleCollision(t.solidBounds(i, j), nextX, nextY);
					hitWall = true;
				}
			}
		}

		// Move to the next position
		// If there was a collision, nextX, nextY, xVel, yVel will have been modified
		// and we will be pressed up against a solid wall
		x = nextX + xVel * Main.TIMESTEP;
		y = nextY + yVel * Main.TIMESTEP;

		// Handle friction
		xVel *= 0.9;
		yVel *= 0.9;

		return hitWall;
	}

	private void handleCollision(Hitbox solid, double nextX, double nextY) {
		// We increment by 1/100th of a tile until we find the exact moment we intersect with something solid
		double stepLength = Math.sqrt(xVel * xVel + yVel * yVel) * 100;
		double xStep = xVel / stepLength, yStep = yVel / stepLength;

		//System.out.printf("Intersection: going %.2f %.2f, step %.2f %.2f%n", xVel, yVel, xStep, yStep);

		Hitbox box = getSolidBounds().translate(nextX-x, nextY-y);
		
		if(box.intersects(solid)) {
			// Standing still, the player is intersecting something
			// But the player is also trying to move, we will give them 10 steps to see if they make it out of the intersection
			double preX = nextX, preY = nextY;
			for(int i = 0; i < 10 && box.intersects(solid); i++) {
				nextX += xStep;
				nextY += yStep;
				box = box.translate(xStep, yStep);
			}
			// If they didn't make it out (i.e. walking against a wall instead of away)
			// Reset their position so they don't move
			if(box.intersects(solid)) {
				nextX = preX;
				nextY = preY;
			}
		} else {
			// The player is not intersecting something right now, but is entering a hitbox
			// We will move them forward bit by bit to catch the point where they sit just at the edge of the hitbox
			// We will also cap the number of attempts to 100 (or one full tile crossed), just to avoid infinite loops
			for(int i = 0; i < 100 && !box.translate(xStep, yStep).intersects(solid); i++) {
				nextX += xStep;
				nextY += yStep;
				box = box.translate(xStep, yStep);
			}
		}

		// We are now within 1/100th of a tile within a solid box

		// If we step in the x direction and intersect, we must stop all x velocity
		if(box.translate(xStep, 0).intersects(solid))
			xVel = 0;
		// if we step in the y direction and intersect, we must stop all y velocity
		if(box.translate(0, yStep).intersects(solid))
			yVel = 0;
	}

	// An overridable function used to determine which entities get drawn over other entities. By default, entities that are lower on screen are drawn in front of entities that are higher
	public double getRenderLayer() { return y; }

}
