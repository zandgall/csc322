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
	
		move(delta);
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
