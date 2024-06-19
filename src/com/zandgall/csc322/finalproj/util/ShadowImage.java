/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Shadow Image
 # Caches a shadow texture with blur and a color effect, reduces overhead later

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.util;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.FileInputStream;

public class ShadowImage {
	private Image img;
	private double blur;

	public ShadowImage(String filepath, double blur, double alpha) throws FileNotFoundException {
		Image base = new Image(new FileInputStream(filepath));
		Canvas c = new Canvas(base.getWidth() + blur * 2, base.getHeight() + blur * 2);
		GraphicsContext g = c.getGraphicsContext2D();
		g.clearRect(0, 0, base.getWidth() + blur * 2, base.getHeight() + blur * 2);
		g.setGlobalAlpha(alpha);
		g.setImageSmoothing(false);
		g.drawImage(base, blur, blur);
		g.applyEffect(new ColorAdjust(-0.8, 0.5, -0.8, 0.0));
		g.applyEffect(new GaussianBlur(blur));
		SnapshotParameters p = new SnapshotParameters();
		p.setFill(Color.TRANSPARENT);
		img = c.snapshot(p, null);
		this.blur = blur;
	}

	public void render(GraphicsContext g, double x, double y, double w, double h) {
		double wRatio = w / img.getWidth();
		double hRatio = h / img.getHeight();
		g.drawImage(img, x, y, w, h);
	}

	public void render(GraphicsContext g, double x, double y) {
		g.drawImage(img, x - blur, y - blur);
	}
}
