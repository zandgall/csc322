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
	
	private static Image trunk, leaves, shadow;
	private double peekTransparency = 1;

	static {
		try {
			trunk = new Image(new FileInputStream("res/entity/tree_trunk.png"));
			leaves = new Image(new FileInputStream("res/entity/tree_leaves.png"));
			shadow = new Image(new FileInputStream("res/entity/tree_shadow.png"));
		} catch(FileNotFoundException e) {
			trunk = null;
			leaves = null;
			shadow = null;
			System.err.println("Could not find tree textures!");
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
	public void render(GraphicsContext g1, GraphicsContext gs, GraphicsContext g2) {
		Hitbox treebox = new Hitbox(x-1.0, y-2.5, 2, 1.6);
		// if the player is behind the leaves, slowly shift "peekTransparency" to 0.75 opacity, otherwise shift it to full opacity
		if(treebox.intersects(Main.getPlayer().getRenderBounds()))
			peekTransparency = peekTransparency * 0.9 + 0.75 * 0.1;
		else
			peekTransparency = peekTransparency * 0.9 + 1.0 * 0.1;

		// Tree texture is 3 x 4 tiles in dimensions. offset by -1.5, -3.5
		g1.drawImage(trunk, x-1.5, y-3.5, 3, 4);
		// Shadow is 1 tile lower
		gs.drawImage(shadow, x-1.5, y-2.5, 3, 4);

		// We *might* draw leaves with transparency, so we backup here
		if(peekTransparency!=1.0) {
			g2.save();	
			g2.setGlobalAlpha(peekTransparency);	
			g2.drawImage(leaves, x-1.5, y-3.5, 3, 4);
			g2.restore();
		} else
			g2.drawImage(leaves, x-1.5, y-3.5, 3, 4);

		// Debug hitboxes
		g2.setLineWidth(0.01);
		g2.strokeRect(x-0.3, y-0.3, 0.6, 0.6);
		g2.strokeRect(x-1.0, y-2.5, 2.0, 1.6);

	}

	public Hitbox getRenderBounds() {
		return new Hitbox(x-1.5, y-3.5, 3.0, 5.0);
	}

	// Tree doesn't update, so empty hitbox
	public Hitbox getUpdateBounds() {
		return new Hitbox();
	}

	// Only the tile at the trunk is solid
	public Hitbox getSolidBounds() {
		return new Hitbox(x-0.3, y-0.3, 0.6, 0.6);
	}

	public double getRenderLayer() {
		return Math.floor(y) + 0.7;
	}

}
