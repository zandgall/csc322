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
import com.zandgall.csc322.finalproj.Main;

public class Player {

	private double x, y;
	private double xVel, yVel;

	private double dashTimer = 0;

	public Player() {
		this.x = 0;
		this.y = 0;
		this.xVel = 0;
		this.yVel = 0;
	}

	public void tick(double delta) {
		if(Main.keys.get(KeyCode.RIGHT))
			xVel += delta * 20;
		if(Main.keys.get(KeyCode.LEFT))
			xVel -= delta * 20;
		if(Main.keys.get(KeyCode.DOWN))
			yVel += delta * 20;
		if(Main.keys.get(KeyCode.UP))
			yVel -= delta * 20;

		if(Main.keys.get(KeyCode.Z) && dashTimer <= 0) {
			double vel = Math.sqrt(xVel * xVel + yVel * yVel);

			xVel *= delta * 1000 / vel;
			yVel *= delta * 1000 / vel;

			dashTimer = 0.5;
		}
		
		if(dashTimer > 0)
			dashTimer -= delta;

		x += xVel * delta;
		y += yVel * delta;

		xVel -= xVel * delta * 10;
		yVel -= yVel * delta * 10;

		System.out.printf("%.2f %.2f (%.2f %.2f) %f%n", x, y, xVel, yVel, delta);
	}

	public void render(GraphicsContext g) {
		g.setFill(Color.color(1, 0, 0, 1));
		g.fillRect(x-0.5, y-0.5, 1, 1);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getXVel() {
		return xVel;
	}

	public double getYVel() {
		return yVel;
	}


}
