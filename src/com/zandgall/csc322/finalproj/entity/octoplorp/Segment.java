package com.zandgall.csc322.finalproj.entity.octoplorp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Segment {
	public static enum Type {
		STRAIGHT, TURN_RIGHT, TURN_LEFT
	}

	// 0 == right, 1 == down, 2 == left, 3 == up
	public int orientation;
	public Type type;
	public int x, y;

	public Segment(int x, int y, Type type, int orientation) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.orientation = orientation;
	}

	public void render(GraphicsContext g, Image sheet) {
		g.save();
		// Turn -90 degrees for each next orientation
		g.translate(x + 0.5, y + 0.5);
		g.rotate(90 * orientation);
		switch (type) {
			case STRAIGHT -> g.drawImage(sheet, 0, 0, 16, 16, -0.5, -0.5, 1, 1);
			case TURN_RIGHT -> g.drawImage(sheet, 16, 16, 16, 16, -0.5, -0.5, 1, 1);
			case TURN_LEFT -> g.drawImage(sheet, 0, 16, 16, 16, -0.5, -0.5, 1, 1);
		}
		g.restore();
	}
}
