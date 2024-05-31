/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Tile
 # Present an assortment of tiles in order to fulfill the graphics of a level

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.level.tile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;

public abstract class Tile {

	private static ArrayList<Tile> tilemap = new ArrayList<Tile>();

	public static final Tile empty = new EmptyTile();
	public static final Tile ground = new ColorTile(Color.web("#369c6c"));
	public static final Tile grass = new ImageTile("res/tiles/grass.png");
	public static final Tile grass_tl = new ImageTile("res/tiles/grass_tl.png");
	public static final Tile grass_tr = new ImageTile("res/tiles/grass_tr.png");
	public static final Tile grass_bl = new ImageTile("res/tiles/grass_bl.png");
	public static final Tile grass_br = new ImageTile("res/tiles/grass_br.png");
	public static final Tile thickgrass = new ImageTile("res/tiles/thickgrass.png");
	
	public static final Tile tutorial[] = new Tile[] {
		new ImageTile("res/tiles/tut_up.png"),
		new ImageTile("res/tiles/tut_right.png"),
		new ImageTile("res/tiles/tut_down.png"),
		new ImageTile("res/tiles/tut_left.png"),
		new ImageTile("res/tiles/tut_z.png"),
		new ImageTile("res/tiles/tut_x.png"),
	};

	private final int ID;

	public Tile() {
		this.ID = tilemap.size();
		tilemap.add(this);
	}

	public static Tile get(int ID) {
		if(ID >= tilemap.size())
			return empty;
		if(ID < 0)
			return tilemap.get(tilemap.size()-1);
		return tilemap.get(ID);
	}

	public int getID() {
		return ID;
	}

	/**
	 * Determine whether the tile is solid or not
	 */
	public abstract boolean solid();

	/**
	 * Draw a 1x1 size tile. The transformation is set beforehand, including positioning. 
	 * Additional transformations can be applied however, just be sure to save and restore before and after drawing!
	 */
	public abstract void render(GraphicsContext g);

	/**
	 * A tile type that does not render
	 */
	private static class EmptyTile extends Tile {
		public EmptyTile() {super();}
		public void render(GraphicsContext g) {}
		public boolean solid() {return false;}
	}

	/**
	 * A tile type that draws a solid color
	 */
	private static class ColorTile extends Tile {
		private Color color;
		private boolean solid;
		public ColorTile(Color color) {
			super();
			this.color = color;
			this.solid = false;
		}
		public ColorTile(Color color, boolean solid) {
			this.color = color;
			this.solid = solid;
		}

		public boolean solid() {
			return solid;
		}

		public void render(GraphicsContext g) {
			g.setFill(color);
			g.fillRect(0.0, 0.0, 1.0, 1.0);
		}
	}

	/**
	 * A tile that displays as an image
	 */
	private static class ImageTile extends Tile {
		private Image image;
		private boolean solid;
		public ImageTile(String path) {
			super();
			try {
				this.image = new Image(new FileInputStream(path));
			} catch (FileNotFoundException e) {
				System.err.println("Could not find tile image \""+path+"\"");
				this.image = null;
			}
		}
		public ImageTile(String path, boolean solid) {
			this(path);
			this.solid = solid;
		}

		public boolean solid() {
			return solid;
		}

		public void render(GraphicsContext g) {
			if(image == null)
				return;
			g.drawImage(image, 0.0, 0.0, 1.0, 1.0);
		}
	}
}
