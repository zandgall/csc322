/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Octoplorp
 # A boss fight for the player to reach and fight

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Octoplorp extends Entity {

	private static final Image body = new Image("file:res/entity/octoplorp/body.png"),
			eye = new Image("file:res/entity/octoplorp/eye.png");

	public Octoplorp(double x, double y) {
		super(x, y);
	}

	public void tick() {

	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {

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
}
