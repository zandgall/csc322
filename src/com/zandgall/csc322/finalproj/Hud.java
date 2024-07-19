/* CSC322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Hud
 # A simple class that renders out an in-game Hud to draw a basic player healthbar to the screen

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Hud {
	private double healthOpacity, deathOpacity, respawnOpacity = 0, closeOpacity = 0;
	private boolean respawning = false, closing = false;

	public Hud() {
		healthOpacity = 1;
	}

	public void tick() {
		if (Main.getPlayer().getHealth() == 20)
			healthOpacity = healthOpacity * 0.99;
		else
			healthOpacity = healthOpacity * 0.9 + 0.1;

		// Player is dead
		if(!Main.getLevel().getEntities().contains(Main.getPlayer())) {
			deathOpacity = deathOpacity * 0.99 + 0.005;
		} else
			deathOpacity = deathOpacity * 0.95;

		if(respawning) {
			respawnOpacity = respawnOpacity * 0.95 + 0.05;
		} else
			respawnOpacity = respawnOpacity * 0.95;


		if(!respawning && deathOpacity > 0.05 && respawnOpacity < 0.001 && Main.keys.get(Main.lastKey)) {
			respawning = true;
		}

		if(respawning && respawnOpacity > 0.999) {
			try {
				Main.quickload();
			} catch (IOException e) {
				System.err.println("Could not quickload!");
				e.printStackTrace();
			}
			respawning = false;
		}

		if(closing)
			closeOpacity = closeOpacity * 0.995 + 0.005;
	}

	public void render(GraphicsContext g) {
		g.setGlobalAlpha(healthOpacity);
		g.setFill(Color.RED);
		g.fillRect(20, 20, 200, 20);
		g.setFill(Color.GREEN);
		g.fillRect(20, 20, Main.getPlayer().getHealth() * 10, 20);
		g.setStroke(Color.BLACK);
		g.setLineWidth(2.0);
		g.strokeRect(20, 20, 200, 20);

		g.setGlobalAlpha(deathOpacity);
		g.setFill(Color.RED);
		g.fillRect(0, 0, Main.stage.getWidth(), Main.stage.getHeight());

		g.setGlobalAlpha(respawnOpacity);
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, Main.stage.getWidth(), Main.stage.getHeight());

		g.setGlobalAlpha(closeOpacity);
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, Main.stage.getWidth(), Main.stage.getHeight());
	}

	public void closeOut() {
		closing = true;
	}
}
