/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Entity
 # An abstract class that provides methods and fields common to entities

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import javafx.scene.canvas.GraphicsContext;
import com.zandgall.csc322.finalproj.util.Hitbox;

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

	public abstract void tick(double delta);

	public abstract void render(GraphicsContext g);

	public double getX() { return x; }
	public double getY() { return y; }
	public double getXVel() { return xVel; }
	public double getYVel() { return yVel; }

	public abstract Hitbox getRenderBounds();
	public abstract Hitbox getUpdateBounds();
	public abstract Hitbox getSolidBounds();

	// An overridable function used to determine which entities get drawn over other entities. By default, entities that are lower on screen are drawn in front of entities that are higher
	public double getRenderLayer() { return y; }

}
