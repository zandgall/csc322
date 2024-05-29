/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Main
 # Initiates as a javafx application

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

import com.zandgall.csc322.finalproj.entity.Player;
import com.zandgall.csc322.finalproj.entity.Tree;
import com.zandgall.csc322.finalproj.level.Level;
import com.zandgall.csc322.finalproj.level.tile.Tile;

public class Main extends Application {

	public static Main instance = null;

	public static HashMap<KeyCode, Boolean> keys;

	public static Scene scene;
	public static Stage stage;
	public static Canvas canvas;
	public static GraphicsContext gc;

	private Player player;
	private Camera camera;
	private Level level;

	@Override
	public void start(Stage stage) {
		instance = this;
		Main.stage = stage;

		Pane root = new Pane();
		canvas = new Canvas(1280, 720);
		gc = canvas.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		root.getChildren().add(canvas);

		scene = new Scene(root, 1280, 720);
		stage.setTitle("Final");
		stage.setScene(scene);
		stage.show();

		keys = new HashMap<KeyCode, Boolean>();
		for(KeyCode a : KeyCode.values())
			keys.put(a, false); // Initialize all keys to false

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Main.keys.put(event.getCode(), true);
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Main.keys.put(event.getCode(), false);
			}
		});

		player = new Player();
		camera = new Camera();
		level = new Level();

		for(int i = -10; i < 10; i++)
			for(int j = -10; j < 10; j++)
				level.put(i, j, Tile.ground);
		
		for(int i = -2; i <= 2; i++)
			for(int j = -2; j <= 2; j++)
				if(i*i + j*j < 6)
					level.put(i-4, j-5, Tile.grass);
		level.put(-6, -7, Tile.grass_tl);
		level.put(-2, -7, Tile.grass_tr);
		level.put(-6, -3, Tile.grass_bl);
		level.put(-2, -3, Tile.grass_br);

		level.put(-5, -6, Tile.thickgrass);
		level.put(-4, -6, Tile.thickgrass);
		level.put(-3, -6, Tile.thickgrass);
		level.put(-5, -5, Tile.thickgrass);
		level.put(-3, -5, Tile.thickgrass);
		level.put(-5, -4, Tile.thickgrass);
		level.put(-4, -4, Tile.thickgrass);
		level.put(-3, -4, Tile.thickgrass);

		level.addEntity(player);
		level.addEntity(new Tree(-3.5, -4.5));

		new AnimationTimer() {
			private long lastTime = System.nanoTime(); 

			@Override 
			public void handle(long currentNanoTime) {
				double delta = (currentNanoTime - lastTime)*0.000000001;
				lastTime = currentNanoTime;
				tick(delta);
				render();

				try {
                    Thread.sleep(13);
                } catch (InterruptedException e) {
                    // Do nothing
                }
			}
		}.start();
	}


	public static void main(String[] args) {	
		Application.launch(args);
	}

	public void tick(double delta) {
		//player.tick(delta);
		level.tick(delta);
		camera.target(player.getX() + player.getXVel()*1.5, player.getY() + player.getYVel()*1.5);
		camera.tick(delta*0.9);
	}

	public void render() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.save();
		camera.transform(gc);	
		level.render(gc);
		// player.render(gc);
		gc.restore();
	}

	// Global accessers
	public static Level getLevel() {
		return instance.level;
	}

	public static Camera getCamera() {
		return instance.camera;
	}

	public static Player getPlayer() {
		return instance.player;
	}

}
