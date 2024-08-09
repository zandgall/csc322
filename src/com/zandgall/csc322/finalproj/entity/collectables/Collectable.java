package com.zandgall.csc322.finalproj.entity.collectables;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Hitnull;
import com.zandgall.csc322.finalproj.util.Hitrect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Collectable extends Entity {
	public Collectable(double x, double y) {
		super(x, y);
	}

	public void tick() {
		if(Main.getPlayer().getHitBounds().intersects(getUpdateBounds())) {
			Main.getHud().collect(this);
			Main.getLevel().removeEntity(this);
		}
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		g.drawImage(getTexture(), position.x - 0.5, position.y - 0.5, 1, 1);
	}

	public Hitbox getRenderBounds() {
		return new Hitrect(position.x - 0.5, position.y - 0.5, 1, 1);
	}

	public Hitbox getUpdateBounds() {
		return new Hitrect(position.x - 0.5, position.y - 0.5, 1, 1);
	}

	public Hitbox getSolidBounds() {
		return new Hitnull();
	}

	public Hitbox getHitBounds() {
		return new Hitnull();
	}

	public abstract Image getTexture();

	public abstract String getTitle();

	public abstract String getDescription();
}
