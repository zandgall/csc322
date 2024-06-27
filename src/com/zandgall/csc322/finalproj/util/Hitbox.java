/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

 ## Hitbox
 # A utility class to determine rectangles and groups of rectangles to recognize
 # intersections and collisions. 

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.util;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Hitbox {
	protected ArrayList<Rectangle2D> boxes = new ArrayList<>();

	protected Rectangle2D bounds;

	public Hitbox() {
		bounds = new Rectangle2D.Double(0, 0, 0, 0);
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
		if (bounds.getWidth() == 0 && bounds.getHeight() == 0)
			bounds = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		else
			bounds.add(rect);
		// Sort in terms of biggest box to smallest, so that bigger boxes are checked
		// first in terms of intersection and such
		boxes.sort((a, b) -> {
			return (int) (a.getWidth() * a.getHeight() - b.getWidth() * b.getHeight());
		});
	}

	public void add(Hitbox other) {
		for (Rectangle2D box : other.boxes)
			add(box);
	}

	public boolean intersects(Rectangle2D rect) {
		if (boxes.size() > 1 && !bounds.intersects(rect))
			return false;
		for (Rectangle2D box : boxes)
			if (box.intersects(rect))
				return true;
		return false;
	}

	public boolean intersects(double x, double y, double width, double height) {
		if (boxes.size() > 1 && !bounds.intersects(x, y, width, height))
			return false;
		for (Rectangle2D box : boxes)
			if (box.intersects(x, y, width, height))
				return true;
		return false;
	}

	public boolean intersects(Hitbox other) {
		if (boxes.size() > 1 && !other.intersects(bounds))
			return false;
		for (Rectangle2D box : boxes)
			if (other.intersects(box))
				return true;
		return false;
	}

	public Hitbox translate(double x, double y) {
		Hitbox out = new Hitbox();
		for (Rectangle2D box : boxes) {
			out.add(new Rectangle2D.Double(box.getX() + x, box.getY() + y, box.getWidth(), box.getHeight()));
		}
		return out;
	}

	public Rectangle2D getBounds() {
		return bounds;
	}

	public ArrayList<Rectangle2D> getBoxes() {
		return boxes;
	}

	/**
	 * Load hitbox from a text file. Used to load sets of different hitboxes
	 * 
	 * @param filepath The file to read hitboxes from
	 */
	public static Hitbox load(String path) {
		try {
			Scanner s = new Scanner(Hitbox.class.getResourceAsStream(path));
			int numBoxes = s.nextInt();

			Hitbox out = new Hitbox();
			for (int i = 0; i < numBoxes; i++)
				out.add(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble());
			s.close();
			return out;
		} catch (Exception e) {
			return unit();
		}
	}

	public static Hitbox unit() {
		return new Hitbox(0, 0, 1, 1);
	}

}
