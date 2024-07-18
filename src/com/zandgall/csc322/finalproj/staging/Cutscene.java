/* CSC 322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

*--------------------------------------------------------------------------------*
| THIS FILE IS UNFINISHED                                                        |
| Although this file is being submitted as a part of the assignment, the content |
| and function of this file is unfinished and unorganized. This file shall be    |
| finished and cleaned up in order to fulfill a full playable demo of this game. |
*--------------------------------------------------------------------------------*

 ## Cutscene
 # A class that is used to play out 'cutscenes', where the camera is moved/zoomed,
 # and optionally, the game is frozen, say for rendering and a select function.

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.staging;

import com.zandgall.csc322.finalproj.Camera;
import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.util.Vector;

public abstract class Cutscene {
	private double timer, duration;

	public Cutscene(double duration) {
		this.timer = 0;
		this.duration = duration;
	}

	/**
	 * Increment timer up to duration,
	 * target and tick camera,
	 */
	public boolean run() {
		timer += Main.TIMESTEP;
		tick();
		Vector target = getTarget();
		Main.getCamera().setSmoothing(getSmoothing());
		Main.getCamera().target(target.x, target.y, getTargetZoom());
		Main.getCamera().tick();
		boolean done = done();
		if (done) {
			onEnd();
			Main.getCamera().setSmoothing(Camera.DEFAULT_SMOOTHING);
		}
		return done;
	}

	/**
	 * Run every tick rather than the base game tick
	 */
	protected void tick() {
		Main.getLevel().tick();
	}

	protected boolean done() {
		return timer >= duration;
	}

	/**
	 * Ran at the end of the cutscene
	 */
	protected abstract void onEnd();

	/**
	 * Get the place that the camera should point at
	 */
	protected abstract Vector getTarget();

	/**
	 * Get the zoom amount that the camera should target
	 */
	protected double getTargetZoom() {
		return 64;
	}

	/**
	 * Get the smoothing amount for this cutscene
	 */
	protected double getSmoothing() {
		return Camera.DEFAULT_SMOOTHING;
	}
}
