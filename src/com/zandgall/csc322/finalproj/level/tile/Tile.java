/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Tile
 # Present an assortment of tiles in order to fulfill the graphics of a level

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.level.tile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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

	public static final Tile walls[] = ImageTile.loadCombinations("res/tiles/walls/wall.combinations");
	public static final Tile edges[] = ImageTile.loadCombinations("res/tiles/walls/edges.combinations");

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

	public static int maxID() {return tilemap.size() - 1;}

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
		public ImageTile(Image image, Hitbox solid) {
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

		public static ImageTile[] overlayCombinations(String background, String... paths) {
			if(paths.length >= 8)
				System.err.printf("Warning: Excess number of images used to create combinations of overlays! %d images will create %d tile combinations%n", paths.length, (long)Math.pow(2, paths.length));
			// Create a pane to hold imageviews holding every input image
			StackPane combiner = new StackPane();

			// Add background, always visible
			ImageView bgView = new ImageView();
			try {
				bgView.setImage(new Image(new FileInputStream(background)));
			} catch(FileNotFoundException e) {
				System.err.println("Could not find tile image \""+background+"\"");
			}
			combiner.getChildren().add(bgView);

			// Add variable images which are toggled on and off
			ImageView views[] = new ImageView[paths.length];
			for(int i = 0; i < paths.length; i++) {
				try {
					views[i] = new ImageView(new Image(new FileInputStream(paths[i])));
				} catch (FileNotFoundException e) {
					System.err.println("Could not find tile image \""+paths[i]+"\"");
					views[i] = new ImageView();
				}
				combiner.getChildren().add(views[i]);
			}

			// Loop through a long with the binary length of images.length, starting at 1
			ImageTile out[] = new ImageTile[(int)Math.pow(2, paths.length) - 1];
			for(int i = 1; i <= out.length; i++) {
				// Loop through every bit of the index and use each bit to determine whether an image should be visible in the overlay
				for(int j = 0; j < paths.length; j++) {
					boolean visible = ((i >> j) & 0b1)==1;
					views[j].setVisible(visible);
				}
				Image overlay = combiner.snapshot(null, null);
				out[i - 1] = new ImageTile(overlay);
			}
			return out;
		}

		public static ImageTile[] overlayCombinations(Hitbox hitbox, String background, String... paths) {
			ImageTile[] output = overlayCombinations(background, paths);
			for(ImageTile a : output) {
				if(a != null && hitbox != null)
					a.solid = hitbox;
				else System.err.println((a==null) + " " + (hitbox==null));
			}
			return output;
		}

		public static Image overlay(String... paths) {
			// Create a pane to hold imageviews holding every input image (given by paths)
			StackPane combiner = new StackPane();
			ImageView views[] = new ImageView[paths.length];
			for(int i = 0; i < paths.length; i++) {
				try {
					views[i] = new ImageView(new Image(new FileInputStream(paths[i])));
				} catch (FileNotFoundException e) {
					System.err.println("Could not find tile image \""+paths[i]+"\"");
					views[i] = new ImageView();
				}
				combiner.getChildren().add(views[i]);
			}
			// Take a 'snapshot' which returns an image of everything overlayed
			return combiner.snapshot(null, null);
		}

		public static ImageTile[] loadCombinations(String filepath) {
			try {
				Scanner s = new Scanner(new File(filepath));
				ArrayList<ImageTile> output = new ArrayList<>();
				while(s.hasNext()) {
					String tags[] = s.nextLine().split("\\s+");
					ArrayList<String> paths = new ArrayList<>();
					Hitbox box = new Hitbox();
					for(int i = 0; i < tags.length; i++) {
						if(new File(tags[i] + ".box").exists())
							box.add(Hitbox.load(tags[i] + ".box"));
						if(new File(tags[i] + ".png").exists())
							paths.add(tags[i] + ".png");
					}
					output.add(new ImageTile(overlay(paths.toArray(new String[paths.size()])), box));
				}
				return output.toArray(new ImageTile[output.size()]);
			} catch (Exception e) {
				System.err.println("Could not load combinations from " + filepath);
				e.printStackTrace();
			}
			return new ImageTile[0];
		}
	}
}
