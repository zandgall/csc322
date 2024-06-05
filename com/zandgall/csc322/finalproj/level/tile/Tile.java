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
import com.zandgall.csc322.finalproj.util.Hitbox;

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

	public static final Tile path[] = new Tile[] {
		new ImageTile("res/tiles/path.png"),
		new ImageTile("res/tiles/path_b.png"),
		new ImageTile("res/tiles/path_r.png"),
		new ImageTile("res/tiles/path_t.png"),
		new ImageTile("res/tiles/path_l.png"),
		new ImageTile("res/tiles/path_str.png"),
		new ImageTile("res/tiles/path_stl.png"),
		new ImageTile("res/tiles/path_sbr.png"),
		new ImageTile("res/tiles/path_sbl.png"),
		new ImageTile("res/tiles/path_btr.png"),
		new ImageTile("res/tiles/path_btl.png"),
		new ImageTile("res/tiles/path_bbr.png"),
		new ImageTile("res/tiles/path_bbl.png"),
		new ImageTile("res/tiles/path_s0.png"),
		new ImageTile("res/tiles/path_s1.png"),
	};

	public static final Tile flower[] = new Tile[] {
		new ImageTile("res/tiles/flower_w.png"),
		new ImageTile("res/tiles/flower_wbl.png"),
		new ImageTile("res/tiles/flower_wbr.png"),
		new ImageTile("res/tiles/flower_wtl.png"),
		new ImageTile("res/tiles/flower_wtr.png"),
		new ImageTile("res/tiles/flower_r.png"),
		new ImageTile("res/tiles/flower_rbl.png"),
		new ImageTile("res/tiles/flower_rbr.png"),
		new ImageTile("res/tiles/flower_rtl.png"),
		new ImageTile("res/tiles/flower_rtr.png"),
		new ImageTile("res/tiles/flower_b.png"),
		new ImageTile("res/tiles/flower_bbl.png"),
		new ImageTile("res/tiles/flower_bbr.png"),
		new ImageTile("res/tiles/flower_btl.png"),
		new ImageTile("res/tiles/flower_btr.png"),
	};

	public static final Tile walls[] = new Tile[] {
		new ImageTile("res/tiles/wall.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_bottom.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_bottom_left.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_bottom_left_right.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_bottom_left_right_top.png", Hitbox.unit()),	
		new ImageTile("res/tiles/wall_bottom_left_top.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_bottom_right.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_bottom_right_top.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_bottom_top.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_left.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_left_right.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_left_right_top.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_left_top.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_right.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_right_top.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_top.png", Hitbox.unit()),
		new ImageTile("res/tiles/wall_ramp_bl.png", Hitbox.load("res/tiles/wall_ramp_bl.box")),
		new ImageTile("res/tiles/wall_ramp_bl_top.png", Hitbox.load("res/tiles/wall_ramp_bl.box")),
		new ImageTile("res/tiles/wall_ramp_bl_right.png", Hitbox.load("res/tiles/wall_ramp_bl.box")),
		new ImageTile("res/tiles/wall_ramp_bl_right_top.png", Hitbox.load("res/tiles/wall_ramp_bl.box")),
		new ImageTile("res/tiles/wall_ramp_blr.png", Hitbox.load("res/tiles/wall_ramp_blr.box")),
		new ImageTile("res/tiles/wall_ramp_blr_top.png", Hitbox.load("res/tiles/wall_ramp_blr.box")),
		new ImageTile("res/tiles/wall_ramp_br.png", Hitbox.load("res/tiles/wall_ramp_br.box")),
		new ImageTile("res/tiles/wall_ramp_br_top.png", Hitbox.load("res/tiles/wall_ramp_br.box")),
		new ImageTile("res/tiles/wall_ramp_br_left.png", Hitbox.load("res/tiles/wall_ramp_br.box")),
		new ImageTile("res/tiles/wall_ramp_br_left_top.png", Hitbox.load("res/tiles/wall_ramp_br.box")),
		new ImageTile("res/tiles/wall_ramp_tl.png", Hitbox.load("res/tiles/wall_ramp_tl.box")),
		new ImageTile("res/tiles/wall_ramp_tl_bottom.png", Hitbox.load("res/tiles/wall_ramp_tl.box")),
		new ImageTile("res/tiles/wall_ramp_tl_right.png", Hitbox.load("res/tiles/wall_ramp_tl.box")),
		new ImageTile("res/tiles/wall_ramp_tl_bottom_right.png", Hitbox.load("res/tiles/wall_ramp_tl.box")),
		new ImageTile("res/tiles/wall_ramp_tlr.png", Hitbox.load("res/tiles/wall_ramp_tlr.box")),
		new ImageTile("res/tiles/wall_ramp_tlr_bottom.png", Hitbox.load("res/tiles/wall_ramp_tlr.box")),
		new ImageTile("res/tiles/wall_ramp_tr.png", Hitbox.load("res/tiles/wall_ramp_tr.box")),
		new ImageTile("res/tiles/wall_ramp_tr_bottom.png", Hitbox.load("res/tiles/wall_ramp_tr.box")),	
		new ImageTile("res/tiles/wall_ramp_tr_left.png", Hitbox.load("res/tiles/wall_ramp_tr.box")),	
		new ImageTile("res/tiles/wall_ramp_tr_bottom_left.png", Hitbox.load("res/tiles/wall_ramp_tr.box")),	
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
	 * Determine whether the tile is solid or not, returning null if not, and a Hitbox if it is
	 */
	public abstract Hitbox solidBounds(int x, int y);

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
		public Hitbox solidBounds(int x, int y) { return null; }
	}

	/**
	 * A tile type that draws a solid color
	 */
	private static class ColorTile extends Tile {
		private Color color;
		private Hitbox solid;
		public ColorTile(Color color) {
			super();
			this.color = color;
			this.solid = null;
		}
		public ColorTile(Color color, Hitbox solid) {
			this.color = color;
			this.solid = solid;
		}

		public Hitbox solidBounds(int x, int y) {
			if(solid==null)
				return null;
			return solid.translate(x, y);
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
		private Hitbox solid = null;
		public ImageTile(String path) {
			super();
			try {
				this.image = new Image(new FileInputStream(path));
			} catch (FileNotFoundException e) {
				System.err.println("Could not find tile image \""+path+"\"");
				this.image = null;
			}
		}
		public ImageTile(String path, Hitbox solid) {
			this(path);
			this.solid = solid;
		}
		public ImageTile(Image image) {
			this.image = image;
		}
		public ImageTile(Imaeg image, Hitbox solid) {
			this.image = image;
			this.solid = solid;
		}

		public Hitbox solidBounds(int x, int y) {
			if(solid==null)
				return null;
			return solid.translate(x, y);
		}

		public void render(GraphicsContext g) {
			if(image == null)
				return;
			g.drawImage(image, 0.0, 0.0, 1.0, 1.0);
		}

		public static ImageTile[] overlayCombinations(String... paths) {
			if(images.length >= 8)
				System.err.printf("Warning: Excess number of images used to create combinations of overlays! %d images will create %d tile combinations%n", images.length, (long)Math.pow(2, images.length));
			// Create a pane to hold imageviews holding every input image
			StackPane combiner = new StackPane();
			ImageView views[] = new ImageView[images.length];
			for(int i = 0; i < images.length; i++) {
				try {
					views[i] = new ImageView(new Image(new FileInputStream(path)));
				} catch (FileNotFoundException e) {
					System.err.println("Could not find tile image \""+path+"\"");
					views[i] = new ImageView();
				}
				combiner.getChildren.add(views[i]);
			}

			// Loop through a long with the binary length of images.length, starting at 1
			ImageTile out[] = new ImageTile[(long)Math.pow(2, images.length)];
			for(long i = 1; n = out.lenth; i++) {
				// Loop through every bit and use each bit to determine whether an image should be visible in the overlay
				for(int j = 0; j < images.length; j++) {
					boolean visible = (i >> j) & 0b1;
					views[j].setVisible(visible);
				}
				Image overlay = combiner.snapshot(null, null);
				out[i] = new ImageTile(overlay);
			}
		}

		public static ImageTile[] overlayCombinations(Hitbox hitbox, String... paths) {
			ImageTile[] output = overlayCombinations(paths);
			for(ImageTile a : output)
				a.solid = hitbox;
		}
	}
}
