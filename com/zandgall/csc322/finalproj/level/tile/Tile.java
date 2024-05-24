/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Tile
 # Present an assortment of tiles in order to fulfill the graphics of a level

 : MADE IN NEOVIM */

public abstract class Tile {

	public static final Tile empty = new EmptyTile();

	private final int ID;
	private static ArrayList<Tile> tilemap = new ArrayList<Tile>();

	public Tile() {
		this.ID = tilemap.size();
		tilemap.add(this);
	}

	public static Tile getTile(int ID) {
		return tilemap.get(ID);
	}

	/**
	 * Draw a 1x1 size tile. The transformation is set beforehand, including positioning. 
	 * Additional transformations can be applied however, just be sure to save and restore before and after drawing!
	 */
	public abstract void render(GraphicsContext g);

	private static EmptyTile extends Tile {
		public EmptyTile() {super();}
		public void render(GraphicsContext g) {}
	}
}
