/* CSC 322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Cutscene
 # A class that is used to play out 'cutscenes', where the camera is moved/zoomed,
 # and optionally, the game is frozen, say for rendering and a select function.

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.staging;

import com.zandgall.csc322.finalproj.Main;

public abstract class Cutscene {
	private double timer, duration, targetX, targetY, targetZoom;

	public Cutscene(double duration, double targetX, double targetY, double targetZoom) {
		this.timer = 0;
		this.duration = duration;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZoom = targetZoom;
	}

	public Cutscene(double duration, double targetX, double targetY) {
		this(duration, targetX, targetY, 64);
	}

	/**
	 * Increment timer up to duration,
	 * target and tick camera,
	 */
	public boolean run() {
		timer += Main.TIMESTEP;
		System.out.printf("%.1f%n", timer);
		tick();
		Main.getCamera().target(targetX, targetY, targetZoom);
		Main.getCamera().tick();
		if (timer >= duration)
			onEnd();
		return timer >= duration;
	}

	/**
	 * Run every tick rather than the base game tick
	 */
	public void tick() {
		Main.getLevel().tick();
	}

	/**
	 * Ran at the end of the cutscene
	 */
	public abstract void onEnd();

}
