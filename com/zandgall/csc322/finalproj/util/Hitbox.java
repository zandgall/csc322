/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Hitbox
 # A utility class to determine rectangles and groups of rectangles to recognize
 # intersections and collisions. 

 TODO: Add super bounding box to determine if it's possible for a rectangle to intersect any of the boxes in the first place

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.util;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Hitbox {
	protected ArrayList<Rectangle2D> boxes = new ArrayList<>();

	protected Rectangle2D bounds;

	public Hitbox() {
		bounds = new Rectangle2D.Double(0,0,0,0);
	}

	public Hitbox(double x, double y, double width, double height) {
		boxes.add(new Rectangle2D.Double(x, y, width, height));
		bounds = new Rectangle2D.Double(x, y, width, height);
	}

	public Hitbox(Rectangle2D box) {
		boxes.add(box);
		bounds = box;
	}

	public void add(double x, double y, double width, double height) {
		add(new Rectangle2D.Double(x, y, width, height));
	}

	public void add(Rectangle2D rect) {
		boxes.add(rect);
		if(bounds.getWidth() == 0 && bounds.getHeight()==0)
			bounds = rect;
		else
			bounds.add(rect);
		// Sort in terms of biggest box to smallest, so that bigger boxes are checked first in terms of intersection and such
		boxes.sort((a, b) -> {
			return (int)(a.getWidth() * a.getHeight() - b.getWidth() * b.getHeight());
		});

	}

	public boolean intersects(Rectangle2D rect) {
		for(Rectangle2D box : boxes)
			if(box.intersects(rect))
				return true;
		return false;
	}

	public boolean intersects(double x, double y, double width, double height) {
		for(Rectangle2D box : boxes)
			if(box.intersects(x,y,width,height))
				return true;
		return false;
	}

	public boolean intersects(Hitbox other) {
		for(Rectangle2D box : boxes)
			if(other.intersects(box))
				return true;
		return false;
	}

	public Hitbox intersection(Rectangle2D rect) {
		Hitbox out = new Hitbox();
		for(Rectangle2D box : boxes)
			if(box.intersects(rect))
				out.add(box.createIntersection(rect));
		return out;
	}

	public Hitbox intersection(Hitbox other) {
		Hitbox out = new Hitbox();
		for(Rectangle2D box : boxes) {
			Hitbox inter = other.intersection(box);
			for(Rectangle2D section : inter.boxes) {
				out.add(section);
			}
		}
		return out;
	}

	public Hitbox translate(double x, double y) {
		Hitbox out = new Hitbox();
		for(Rectangle2D box : boxes) {
			out.add(new Rectangle2D.Double(box.getX() + x, box.getY() + y, box.getWidth(), box.getHeight()));
		}
		return out;
	}

	public Rectangle2D getBounds() {
		return bounds;
	}

}
