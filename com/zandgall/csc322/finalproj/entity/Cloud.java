/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Cloud
 # A simple entity that just layers shadows across the ground

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

import com.zandgall.csc322.finalproj.Main;

public class Cloud {
	public static Image textures[];
	
	private int type;
	private double speed;
	private double x, y;

	static {
		try {
			textures = new Image[] {
				new Image(new FileInputStream("res/entity/cloud_0.png")),
				new Image(new FileInputStream("res/entity/cloud_1.png")),
				new Image(new FileInputStream("res/entity/cloud_2.png")),
				new Image(new FileInputStream("res/entity/cloud_3.png")),
				new Image(new FileInputStream("res/entity/cloud_4.png")),
				new Image(new FileInputStream("res/entity/cloud_5.png")),
				new Image(new FileInputStream("res/entity/cloud_6.png")),
				new Image(new FileInputStream("res/entity/cloud_7.png"))
			};
		} catch(FileNotFoundException e) {
			textures = new Image[8];
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
		if(!getRenderBounds().intersects(Main.getLevel().bounds))
			x = Main.getLevel().bounds.x + Main.getLevel().bounds.width;
	}

	public void render(GraphicsContext g) {	
		g.drawImage(textures[type], x, y, 16, 16);
	}	

	public Hitbox getRenderBounds() { return new Hitbox(x, y, 16, 16); }

	public double getX() {return x;}
	public double getY() {return y;}
}
