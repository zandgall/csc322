/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Cloud
 # A simple entity that just layers shadows across the ground

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.ShadowImage;

import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;

public class Cloud {
	public static ShadowImage textures[];

	private int type;
	private double speed;
	private double x, y;

	static {
		try {
			textures = new ShadowImage[] {
					new ShadowImage("res/entity/cloud_0.png", 10, 0.7),
					new ShadowImage("res/entity/cloud_1.png", 10, 0.7),
					new ShadowImage("res/entity/cloud_2.png", 10, 0.7),
					new ShadowImage("res/entity/cloud_3.png", 10, 0.7),
					new ShadowImage("res/entity/cloud_4.png", 10, 0.7),
					new ShadowImage("res/entity/cloud_5.png", 10, 0.7),
					new ShadowImage("res/entity/cloud_6.png", 10, 0.7),
					new ShadowImage("res/entity/cloud_7.png", 10, 0.7)
			};
		} catch (FileNotFoundException e) {
			textures = new ShadowImage[8];
			System.err.println("Could not find cloud textures!");
		}
	}

	public Cloud() {
		type = new Random().nextInt(8);
		speed = new Random().nextDouble(0.2, 0.5) * Main.TIMESTEP;
	}

	public Cloud(double x, double y) {
		this.x = x;
		this.y = y;
		type = new Random().nextInt(8);
		speed = new Random().nextDouble(0.2, 0.5) * Main.TIMESTEP;
	}

	public void tick() {
		// Glide across the screen;
		x -= speed;
		// check against level bounds, if too far left, respawn on right side
		if (!getRenderBounds().intersects(Main.getLevel().bounds))
			x = Main.getLevel().bounds.x + Main.getLevel().bounds.width;
	}

	public void render(GraphicsContext g) {
		textures[type].render(g, x, y, 16, 16);
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(x, y, 16, 16);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
