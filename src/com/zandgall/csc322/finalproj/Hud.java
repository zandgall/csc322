/* CSC322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Hud
 # A simple class that renders out an in-game Hud to drive

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.zandgall.csc322.finalproj.entity.Player;

public class Hud {
	private float transparency;

	public Hud() {
		transparency = 1;
	}

	public void render(GraphicsContext g) {
		g.setGlobalAlpha(transparency);
		g.setStroke(Color.BLACK);
		g.strokeRect(10, 10, 100, 10);
		g.setFill(Color.RED);
		g.fillRect(10, 10, 100, 10);
		g.setFill(Color.GREEN);
		g.fillRect(10, 10, Main.getPlayer().getHealth() * 5, 10);
	}
}

