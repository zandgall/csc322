package com.zandgall.csc322.finalproj.level;

import com.zandgall.csc322.finalproj.Main;

import java.awt.geom.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpecialImage {
	private Image image;
	private String path;
	private double x, y, damping;
	public SpecialImage(String path, double x, double y, double damping) {
		image = new Image(path);
		this.path = path;
		this.x = x;
		this.y = y;
		this.damping = damping;
		System.out.printf("New SpecialImage %s at (%.1f, %.1f) with %.1f damping%n", path, x, y, damping);
	}

	public void render(GraphicsContext g) {
		Rectangle2D rb = getRenderBox();
		System.out.printf("Drawing image %s at (%.1f, %.1f)%n", path, rb.getMinX(), rb.getMinY());
		// Reminder that each unit is a 16x16 tile
		// So scale image down by 1/16 to match
		g.drawImage(image, rb.getMinX(), rb.getMinY(), rb.getWidth(), rb.getHeight());
	}

	public Rectangle2D getRenderBox() {
		double w = image.getWidth() / 16;
		double h = image.getHeight() / 16;
		double x = (this.x - w / 2) * (1 - damping) + Main.getCamera().getX() * damping;
		double y = (this.y - h / 2) * (1 - damping) + Main.getCamera().getY() * damping;
		return new Rectangle2D.Double(x, y, w, h);
	}
}
