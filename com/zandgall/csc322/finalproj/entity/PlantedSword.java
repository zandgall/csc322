/* CSC322 FINAL PROJECT - PROF FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Planted Sword
 # A simple pickup item

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;

public class PlantedSword extends Entity {

	private static Image texture;

	static {
		try {
			texture = new Image(new FileInputStream("res/entity/planted_sword.png"));
		} catch(FileNotFoundException e) {
			texture = null;
		}
	}

	private Hitbox swordbox = null;

	public PlantedSword() {
		super();
	}
	public PlantedSword(double x, double y) {
		super(x, y);
		swordbox = new Hitbox(x - 0.5, y - 0.2, 1.0, 0.4);
	}

	@Override
	public void tick(double delta) {
		if(swordbox == null)
			swordbox = new Hitbox(x - 0.5, y - 0.2, 1.0, 0.4);
		// Check if intersects with player, if so, give the player a sword
		if(Main.getPlayer().getSolidBounds().intersects(swordbox)) {
			Main.getPlayer().giveSword();
			Main.getLevel().removeEntity(this);
		}
	}

	@Override
	public void render(GraphicsContext g1, GraphicsContext gs, GraphicsContext g2) {
		g1.drawImage(texture, x-1, y-1.8, 2, 2);
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(x-1, y-1.8, 2, 2);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(x-1, y-1.8, 2, 2);
	}

	public Hitbox getSolidBounds() {
		return new Hitbox();
	}

}
