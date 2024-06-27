/* CSC322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Hud
 # A simple class that renders out an in-game Hud to draw a basic player healthbar to the screen

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Hud {
	private double transparency;

	public Hud() {
		transparency = 1;
	}

	public void render(GraphicsContext g) {
		g.setGlobalAlpha(transparency);
		g.setFill(Color.RED);
		g.fillRect(20, 20, 200, 20);
		g.setFill(Color.GREEN);
		g.fillRect(20, 20, Main.getPlayer().getHealth() * 10, 20);
		g.setStroke(Color.BLACK);
		g.setLineWidth(2.0);
		g.strokeRect(20, 20, 200, 20);

		if (Main.getPlayer().getHealth() == 20)
			transparency = transparency * 0.99;
		else
			transparency = transparency * 0.9 + 0.1;
	}
}
