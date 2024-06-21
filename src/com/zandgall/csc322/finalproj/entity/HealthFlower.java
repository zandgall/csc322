/* CSC322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Health Flower
 # A simple animated pickup entity that grants the player more health

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class HealthFlower extends Entity {

	private static final Image texture = new Image("file:res/entity/health_flower.png");

	private double timer;

	public HealthFlower(double x, double y) {
		super(x, y);
	}

	public void tick() {
		if (Main.getPlayer().getHitBounds().intersects(getUpdateBounds())) {
			Main.getPlayer().addHealth(5.0);
			Main.getLevel().removeEntity(this);
		}
		timer += Main.TIMESTEP;
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		int frame = (int) Math.max((timer * 8) % 8 - 4, 0);
		g.drawImage(texture, 0, frame * 16, 16, 16, x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(x - 0.5, y - 0.5, 1, 1);
	}

	public Hitbox getSolidBounds() {
		return new Hitbox();
	}

	public Hitbox getHitBounds() {
		return new Hitbox();
	}

}
