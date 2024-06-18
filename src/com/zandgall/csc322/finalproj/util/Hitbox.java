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
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

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
			bounds = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		else
			bounds.add(rect);
		// Sort in terms of biggest box to smallest, so that bigger boxes are checked first in terms of intersection and such
		boxes.sort((a, b) -> {
			return (int)(a.getWidth() * a.getHeight() - b.getWidth() * b.getHeight());
		});
	}

	public void add(Hitbox other) {
		for(Rectangle2D box : other.boxes)
			add(box);
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

	public ArrayList<Rectangle2D> getBoxes() {
		return boxes;
	}

	/**
	* Load hitbox from a text file. Used to load sets of different hitboxes
	* @param filepath The file to read hitboxes from
	*/
	public static Hitbox load(String filepath) {
		try {
			Scanner s = new Scanner(new File(filepath));
			int numBoxes = s.nextInt();

			Hitbox out = new Hitbox();
			for(int i = 0; i < numBoxes; i++)
				out.add(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble());
			return out;
		} catch(Exception e) {
			return unit();
		}
	}

	public static Hitbox unit() {
		return new Hitbox(0, 0, 1, 1);
	}



}
