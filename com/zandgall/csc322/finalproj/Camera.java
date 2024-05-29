/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Camera
 # A class that stores and applies graphics transformation data to replicate a moving camera

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import javafx.scene.canvas.GraphicsContext;

public class Camera {
	private double x, y;
	private double zoom; // The size a single pixel will be after transform

	private double targetX, targetY;

	public Camera() {
		this.x = 0;
		this.y = 0;
		this.zoom = 64;
	}

	public void tick(double delta) {
		this.x = x * (1-delta) + targetX * delta;
		this.y = y * (1-delta) + targetY * delta;
	}

	public void target(double x, double y) {
		targetX = x;
		targetY = y;
	}

	public void transform(GraphicsContext gc) {
		// Clamp translation to nearest pixel to avoid gaps between tiles
		gc.transform(zoom, 0, 0, zoom, (int)(-x * zoom + Main.canvas.getWidth() * 0.5), (int)(-y * zoom + Main.canvas.getHeight() * 0.5));
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZoom() {
		return zoom;
	}
}
