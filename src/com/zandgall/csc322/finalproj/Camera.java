/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

 ## Camera
 # A class that stores and applies graphics transformation data to replicate a moving camera

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import javafx.scene.canvas.GraphicsContext;

public class Camera {
	public static final double SMOOTHING = 0.01, DEFAULT_ZOOM = 64;
	private double x, y;
	private double zoom; // The size a single pixel will be after transform

	private double targetX, targetY, targetZoom;

	public Camera() {
		this.x = 0;
		this.y = 0;
		this.zoom = DEFAULT_ZOOM;
	}

	public void tick() {
		// Smooth x, y, and zoom to their target values
		this.x = x * (1 - SMOOTHING) + targetX * SMOOTHING;
		this.y = y * (1 - SMOOTHING) + targetY * SMOOTHING;
		this.zoom = zoom * (1 - SMOOTHING) + targetZoom * SMOOTHING;
	}

	// Update targetX Y and zoom
	public void target(double x, double y, double zoom) {
		targetX = x;
		targetY = y;
		targetZoom = zoom;
	}

	// Update just target x and y
	public void target(double x, double y) {
		target(x, y, DEFAULT_ZOOM);
	}

	// Create the transformation with the given x, y and zoom
	public void transform(GraphicsContext gc) {
		// Clamp translation to nearest pixel to avoid gaps between tiles
		gc.transform(zoom, 0, 0, zoom, (int) (-x * zoom + Main.layer_0.getWidth() * 0.5),
				(int) (-y * zoom + Main.layer_0.getHeight() * 0.5));
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
