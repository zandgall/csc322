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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;

import java.util.HashMap;

import com.zandgall.csc322.finalproj.entity.EntityRegistry;
import com.zandgall.csc322.finalproj.entity.Player;
import com.zandgall.csc322.finalproj.entity.Tree;
import com.zandgall.csc322.finalproj.level.Level;
import com.zandgall.csc322.finalproj.level.tile.Tile;

public class Main extends Application {

	public static Main instance = null;

	public static HashMap<KeyCode, Boolean> keys;

	public static Scene scene;
	public static Stage stage;
	public static Pane root;
	public static Canvas layer_0, layer_1, shadow_0, layer_2, shadow_1;
	public static GraphicsContext c0, c1, s0, c2, s1;

	protected Player player;
	protected Camera camera;
	protected Level level;

	@Override
	public void start(Stage stage) {
		instance = this;
		Main.stage = stage;

		EntityRegistry.registerClasses();

		setupScene();
		stage.setTitle("Final");
		stage.setScene(scene);
		stage.show();
		stage.toFront();
		stage.requestFocus();

		stage.widthProperty().addListener((obs, oldVal, newVal) -> {
			layer_0.setWidth(newVal.doubleValue());
			layer_1.setWidth(newVal.doubleValue());
			layer_2.setWidth(newVal.doubleValue());
			shadow_0.setWidth(newVal.doubleValue());
			shadow_1.setWidth(newVal.doubleValue());
		});
		stage.heightProperty().addListener((obs, oldVal, newVal) -> {
			layer_0.setHeight(newVal.doubleValue());
			layer_1.setHeight(newVal.doubleValue());
			layer_2.setHeight(newVal.doubleValue());
			shadow_0.setHeight(newVal.doubleValue());
			shadow_1.setHeight(newVal.doubleValue());
		});

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
		try {
			level.load("res/level/test.bin");
		} catch(Exception e) {
			e.printStackTrace();
		}

		level.addEntity(player);

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

	// Pulled to it's own function so that it can be overridden in LevelEditor
	public void setupScene() {
		root = new Pane();
		layer_0 = new Canvas(1280, 720);
		c0 = layer_0.getGraphicsContext2D();
		c0.setImageSmoothing(false);
		layer_1 = new Canvas(1280, 720);
		c1 = layer_1.getGraphicsContext2D();
		c1.setImageSmoothing(false);
		
		shadow_0 = new Canvas(1280, 720);
		s0 = shadow_0.getGraphicsContext2D();
		s0.setImageSmoothing(false);
		s0.setGlobalAlpha(0.6);

		layer_2 = new Canvas(1280, 720);
		c2 = layer_2.getGraphicsContext2D();
		c2.setImageSmoothing(false);

		shadow_1 = new Canvas(1280, 720);
		s1 = shadow_1.getGraphicsContext2D();
		s1.setImageSmoothing(false);
		s1.setGlobalAlpha(0.7);

		root.getChildren().add(layer_0);
		root.getChildren().add(layer_1);
		root.getChildren().add(shadow_0);
		root.getChildren().add(layer_2);
		root.getChildren().add(shadow_1);

		scene = new Scene(root, 1280, 720);
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
		c0.clearRect(0, 0, layer_0.getWidth(), layer_0.getHeight());
		c1.clearRect(0, 0, layer_1.getWidth(), layer_1.getHeight());
		s0.clearRect(0, 0, shadow_0.getWidth(), shadow_0.getHeight());
		c2.clearRect(0, 0, layer_2.getWidth(), layer_2.getHeight());
		s1.clearRect(0, 0, shadow_1.getWidth(), shadow_1.getHeight());

		c0.save();
		c1.save();
		s0.save();
		c2.save();
		s1.save();
		
		camera.transform(c0);
		camera.transform(c1);
		camera.transform(s0);
		camera.transform(c2);
		camera.transform(s1);

		level.render(c0, c1, s0, c2, s1);

		s0.applyEffect(new ColorAdjust(-0.8, 0.5, -0.8, 0.0));
		s0.applyEffect(new GaussianBlur(10)); // blur radius is in pixels
		s1.applyEffect(new ColorAdjust(-0.8, 0.5, -0.8, 0.0));
		s1.applyEffect(new GaussianBlur(200)); // blur radius is in pixels

		c0.restore();
		c1.restore();
		s0.restore();
		c2.restore();
		s1.restore();
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
