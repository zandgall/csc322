/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

*--------------------------------------------------------------------------------*
| THIS FILE IS UNFINISHED                                                        |
| Although this file is being submitted as a part of the assignment, the content |
| and function of this file is unfinished and unorganized. This file shall be    |
| finished and cleaned up in order to fulfill a full playable demo of this game. |
*--------------------------------------------------------------------------------*

 ## Octoplorp
 # A boss fight for the player to reach and fight

 ## Tentacle
 # An entity that represents each of Octoplorp's tentacles

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity.octoplorp;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.entity.Player;
import com.zandgall.csc322.finalproj.entity.Player.Special;
import com.zandgall.csc322.finalproj.staging.Cutscene;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Octoplorp extends Entity{

	private static final Image body = new Image("/entity/octoplorp/body.png"),
			eye = new Image("/entity/octoplorp/eye.png");

	static enum State {
		SLEEPING, WAKING, GRABBING, VULNERABLE
	}

	private State state = State.SLEEPING;

	private double eyeX = 0, eyeY = 1;
	private int eyeFrameX = 3, eyeFrameY = 0;

	private Tentacle tutorialTentacle, firstTentacle, secondTentacle, finalTentacle;

	private Tentacle currentTentacle;

	public Octoplorp(double x, double y) {
		super(Math.round(x), Math.round(y));
		tutorialTentacle = new Tentacle(position.getAdd(5.5, 6.5), position.getAdd(0, 6.5), position.getAdd(0, 12), position.getAdd(-12, -1));
		tutorialTentacle.tutorial = true;
		firstTentacle = new Tentacle(position.getAdd(-5.5, 6.5), position.getAdd(-14,1), position.getAdd(0, 12), position.getAdd(12, -1));
		secondTentacle = new Tentacle(position.getAdd(5.5, 12.5), position.getAdd(14, 0), position.getAdd(0, 12), position.getAdd(-12, 14));
		finalTentacle = new Tentacle(position.getAdd(-5.5, 12.5), position.getAdd(-14, 12), position.getAdd(0, 12), position.getAdd(0, 6));
		Main.getLevel().addEntity(tutorialTentacle);
		Main.getLevel().addEntity(firstTentacle);
		Main.getLevel().addEntity(secondTentacle);
		Main.getLevel().addEntity(finalTentacle);

		firstTentacle.speed = 4;
		secondTentacle.speed = 5;
		finalTentacle.speed = 6;

		currentTentacle = tutorialTentacle;
	}

	public void tick() {
		switch (state) {
			case SLEEPING:
				if (new Hitbox(getX() - 10, getY() - 10, 20, 20).intersects(Main.getPlayer().getRenderBounds())) {
					state = State.WAKING;
					Main.playCutscene(new Cutscene(5) {
						float t = 0;

						@Override
						protected void tick() {
							t += Main.TIMESTEP;
							eyeY *= 0.99;
							if (t > 4.5)
								eyeFrameY = 1;
							else if (t > 4.3)
								eyeFrameX = 0;
						}

						protected void onEnd() {
							Main.getLevel().addEntity(tutorialTentacle);
						}

						protected Vector getTarget() {
							return position;
						}

						protected double getTargetZoom() {
							return 48;
						}
					});
				}
				break;
			case WAKING:
				state = State.GRABBING;
				tutorialTentacle.state = Tentacle.State.CHASING;
				tutorialTentacle.speed = 5;
				Main.playCutscene(new Cutscene(5) {
					@Override
					protected void tick() {
						tutorialTentacle.tick();
					}

					protected void onEnd() {

					}

					protected boolean done() {
						return tutorialTentacle.state == Tentacle.State.GRABBED;
					}

					protected Vector getTarget() {
						return Main.getPlayer().getPosition();
					}


					protected double getTargetZoom() {
						return 48;
					}

				});
				break;
			case GRABBING:
				if(currentTentacle != null && currentTentacle.state == Tentacle.State.DEAD) {
					if(currentTentacle == tutorialTentacle)
						currentTentacle = firstTentacle;
					else if(currentTentacle == firstTentacle)
						currentTentacle = secondTentacle;
					else if(currentTentacle == secondTentacle)
						currentTentacle = finalTentacle;
					else if(currentTentacle == finalTentacle)
						currentTentacle = null;

					if(currentTentacle != null)
						currentTentacle.state = Tentacle.State.CHASING;
					else
						state = State.VULNERABLE;
				}
				break;
			case VULNERABLE:
				break;

		}
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		g.drawImage(body, getX() - 3, getY() - 3, 6, 6);
		if(eyeFrameX == -1)
			g.drawImage(eye, 256, 32, 64, 64, getX() - 2 + eyeX, getY() - 4 + eyeY, 4, 4);
		else
			g.drawImage(eye, eyeFrameX * 64, eyeFrameY * 32, 64, 32, getX() - 2 + eyeX, getY() - 2 + eyeY, 4, 2);
	}

	public Hitbox getRenderBounds() {	
		return new Hitbox(tileX() - 3, tileY() - 3, 6, 6);
	}

	public Hitbox getUpdateBounds() {
		return new Hitbox(Main.getLevel().bounds);
	}

	public Hitbox getSolidBounds() {
		return new Hitbox(tileX() - 3, tileY() - 3, 6, 6);
	}

	public Hitbox getHitBounds() {
		if(state == State.VULNERABLE)
			return new Hitbox(tileX() - 3, tileY() - 3, 6, 6);
		return new Hitbox();
	}

	@Override
	public double getRenderLayer() {
		if(state == State.VULNERABLE)
			return Double.NEGATIVE_INFINITY;
		return super.getRenderLayer();
	}

	@Override
	public void dealPlayerDamage(double damage) {
		Main.playCutscene(new Cutscene(Double.POSITIVE_INFINITY) {
			boolean stabbing = false;
			double t = 0, y = position.y, upY = y - 6, stabY = y - 4;

			protected void tick() {
				t += Main.TIMESTEP;
				if(t < 1) {
					if(t < 0.2) {
						eyeFrameX = 3;
						eyeFrameY = 0;
					} else if(t < 0.4) {
						eyeFrameX = 0;
						eyeFrameY = 2;
					}
					else if(t < 0.6)
						eyeFrameX = 1;
					else
						eyeFrameX = 2;
					y = y * 0.95 + upY * 0.05;
					Main.getPlayer().setX(position.x);
					Main.getPlayer().setY(y);
					Main.getPlayer().cutsceneSword(0.5 * Math.PI, 4 + t, Player.Special.NONE);
				} else if (!stabbing) {	
					if(Main.keys.get(KeyCode.Z)) {
						Main.getPlayer().cutsceneSword(0.5 * Math.PI, 0, Player.Special.STAB);
						stabbing = true;
						t = 1;
					}
				} else if(t < 3) {
					if(y < stabY) {
						y += 0.2;
					} else {
						y = stabY;
						eyeFrameX = -1;
						Main.getPlayer().takeAwaySword();
					}
					Main.getPlayer().setY(y);
				} else {	
					Main.getPlayer().getPosition().add(0, 0.04);
				}
			}

			@Override
			protected Vector getTarget() {
				return position.getAdd(0, -3);
			}

			@Override
			protected double getTargetZoom() {
				return 48;
			}

			@Override
			protected void onEnd() {}

		});
	}
}
