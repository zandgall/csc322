/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Player
 # An entity that is controllable by the user

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import java.awt.geom.Rectangle2D;
import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;

public class Player extends Entity {

	private double dashTimer = 0;

	public Player() {
		super();
	}

	public Player(double x, double y) {
		super(x, y);
	}

	public void tick(double delta) {
		// Arrow keys to move
		if(Main.keys.get(KeyCode.RIGHT))
			xVel += delta * 20;
		if(Main.keys.get(KeyCode.LEFT))
			xVel -= delta * 20;
		if(Main.keys.get(KeyCode.DOWN))
			yVel += delta * 20;
		if(Main.keys.get(KeyCode.UP))
			yVel -= delta * 20;

		// If player presses Z and ready to dash, dash in direction we're moving
		if(Main.keys.get(KeyCode.Z) && dashTimer <= 0) {
			double vel = Math.sqrt(xVel * xVel + yVel * yVel);

			xVel *= delta * 1000 / vel;
			yVel *= delta * 1000 / vel;

			// Next dash in 0.5 seconds
			dashTimer = 0.5;
		}
		
		// Decrease dash timer to 0 over time
		if(dashTimer > 0)
			dashTimer -= delta;

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
				System.out.printf("Intersecting: Moving %.2f, %.2f, vel: %.2f, %.2f %n", xStep, yStep, xVel, yVel);
				if(xVel == 0 && yVel == 0) {
					break;
				}
				box = new Hitbox(x - 0.5, y - 0.5, 1.0, 1.0);
				while(!box.translate(xStep, yStep).intersects(solid)) {
					nextX += xStep;
					nextY += yStep;
					box = box.translate(xStep, yStep);
				}
				if(box.intersects(solid)) {
					System.out.println("We fucked up!");
				}
				// We are now within 1/100th of a tile within a solid box
				// If we step in the x direction and intersect, we must stop all x velocity
				if(box.translate(xStep, 0).intersects(solid)) {
					xVel = 0;
					System.out.println("Reset xVel");
				}
				// if we step in the y direction and intersect, we must stop all y velocity
				if(box.translate(yStep, 0).intersects(solid)) {
					yVel = 0;
					System.out.println("Reset yVel");
				}
			}
		}

		x = nextX + xVel * delta;
		y = nextY + yVel * delta;

		double frictionRatio = 1 / (1 + (delta*10));
		xVel *= frictionRatio;
		yVel *= frictionRatio;

		//System.out.printf("%.2f %.2f (%.2f %.2f) %f%n", x, y, xVel, yVel, delta);
	}

	public void render(GraphicsContext g) {
		g.setFill(Color.color(1, 0, 0, 1));
		g.fillRect(x-0.5, y-0.5, 1, 1);
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getSolidBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

}
