/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Tree
 # A basic entity that simply displays a tree

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Hitbox;

public class Tree extends Entity {
	
	private static Image texture;
	private double peekTransparency = 1;

	static {
		try {
			texture = new Image(new FileInputStream("res/entity/tree.png"));
		} catch(FileNotFoundException e) {
			texture = null;
			System.err.println("Could not find tree texture!");
		}
	};

	public Tree() {
		super();
	}

	public Tree(double x, double y) {
		super(x, y);
	}

	@Override
	public void tick(double delta) {
		// Tree does nothing
	}

	@Override
	public void render(GraphicsContext g) {
		Hitbox treebox = new Hitbox(Math.floor(x), Math.floor(y)-2, 1, 1.1);
		// if the player is behind the leaves, slowly shift "peekTransparency" to 0.75 opacity, otherwise shift it to full opacity
		if(treebox.intersects(Main.getPlayer().getRenderBounds()))
			peekTransparency = peekTransparency * 0.9 + 0.75 * 0.1;
		else
			peekTransparency = peekTransparency * 0.9 + 1.0 * 0.1;
		g.save();
		g.setLineWidth(0.1);
		g.setGlobalAlpha(peekTransparency);
		// Tree texture is 3 x 4 tiles in dimensions. offset by -1, -3
		g.drawImage(texture, Math.floor(x)-1, Math.floor(y)-3, 3, 4);
		g.strokeRect(Math.floor(x), Math.floor(y), 1.0, 1.0);
		g.strokeRect(Math.floor(x), Math.floor(y)-2, 1.0, 1.1);
		g.restore();
	}

	public Hitbox getRenderBounds() {
		return new Hitbox(Math.floor(x)-1.0, Math.floor(y)-3.0, 3.0, 4.0);
	}

	// Tree doesn't update, so empty hitbox
	public Hitbox getUpdateBounds() {
		return new Hitbox();
	}

	// Only the tile at the trunk is solid
	public Hitbox getSolidBounds() {
		return new Hitbox(Math.floor(x), Math.floor(y), 1.0, 1.0);
	}

	public double getRenderLayer() {
		return Math.floor(y) + 0.7;
	}

}
