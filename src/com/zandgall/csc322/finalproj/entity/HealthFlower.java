/* CSC322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.


 ## Health Flower
 # A simple animated pickup entity that grants the player more health

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.Sound;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Hitnull;
import com.zandgall.csc322.finalproj.util.Hitrect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class HealthFlower extends Entity {

	private static final Image texture = new Image("/entity/health_flower.png");

	private double timer;

	private Hitbox bounds;

	public HealthFlower(double x, double y) {
		super(x, y);
		bounds = new Hitrect(x - 0.5, y - 0.5, 1, 1);
	}

	/**
	 * Just checks for player intersection and adds health and dies when there is
	 */
	public void tick() {
		if (Main.getPlayer().getHitBounds().intersects(getUpdateBounds())) {
			Main.getPlayer().addHealth(5.0);
			Main.getLevel().removeEntity(this);
			Sound.EffectPluck.charge();
		}
		timer += Main.TIMESTEP;
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		int frame = (int) Math.max((timer * 8) % 8 - 4, 0);
		g.drawImage(texture, 0, frame * 16, 16, 16, position.x - 0.5, position.y - 0.5, 1, 1);
	}

	public Hitbox getRenderBounds() {
		return bounds;
	}

	public Hitbox getUpdateBounds() {
		return bounds;
	}

	public Hitbox getSolidBounds() {
		return Hitnull.instance;
	}

	public Hitbox getHitBounds() {
		return Hitnull.instance;
	}

}
