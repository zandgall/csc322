/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

 ## Main
 # Initiates as a javafx application

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

import com.zandgall.csc322.finalproj.entity.EntityRegistry;
import com.zandgall.csc322.finalproj.entity.Player;
import com.zandgall.csc322.finalproj.staging.Cutscene;
import com.zandgall.csc322.finalproj.level.Level;

public class Main extends Application {

	// How long each timestep is that tick updates
	public static final double TIMESTEP = 0.01;

	public static Main instance = null;

	public static HashMap<KeyCode, Boolean> keys;

	// JavaFX Elements
	public static Scene scene;
	public static Stage stage;
	public static Pane root;

	// Canvas and contexts for several layers.
	public static Canvas layer_0, layer_1, shadow_0, layer_2, shadow_1, hudCanvas, throwawayCanvas;
	public static GraphicsContext c0, c1, s0, c2, s1, hudContext, throwawayContext;

	// Instances of elements included in the game
	protected Player player;
	protected Camera camera;
	protected Level level;
	protected Hud hud;
	protected Cutscene cutscene = null;

	@Override
	public void start(Stage stage) {
		// Set up static elements
		Main.instance = this;
		Main.stage = stage;

		EntityRegistry.registerClasses();

		// Set up the scene and stage elements
		setupScene();
		stage.setTitle("Final");
		stage.setScene(scene);
		stage.show();
		stage.toFront();
		stage.requestFocus();

		// Update canvas sizes when stage dimensions are changed
		stage.widthProperty().addListener((obs, oldVal, newVal) -> {
			layer_0.setWidth(newVal.doubleValue());
			layer_1.setWidth(newVal.doubleValue());
			layer_2.setWidth(newVal.doubleValue());
			shadow_0.setWidth(newVal.doubleValue());
			shadow_1.setWidth(newVal.doubleValue());
			hudCanvas.setWidth(newVal.doubleValue());
		});
		stage.heightProperty().addListener((obs, oldVal, newVal) -> {
			layer_0.setHeight(newVal.doubleValue());
			layer_1.setHeight(newVal.doubleValue());
			layer_2.setHeight(newVal.doubleValue());
			shadow_0.setHeight(newVal.doubleValue());
			shadow_1.setHeight(newVal.doubleValue());
			hudCanvas.setHeight(newVal.doubleValue());
		});

		// Key input map and events
		keys = new HashMap<KeyCode, Boolean>();
		for (KeyCode a : KeyCode.values())
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
		hud = new Hud();
		try {
			level.load("/level.bin");
		} catch (Exception e) {
			e.printStackTrace();
			return; // Can't play without level!
		}

		level.addEntity(player);

		// The main loop. As this scene animation plays, the game is updated and
		// rendered
		new AnimationTimer() {
			private long lastTime = System.nanoTime();
			double delta = 0;

			@Override
			public void handle(long currentNanoTime) {
				delta += (currentNanoTime - lastTime) * 0.000000001;
				lastTime = currentNanoTime;
				// We tick in 1/100 second increments in order to ensure game
				// physics/interaction consistency
				while (delta >= TIMESTEP) {
					tick();
					delta -= TIMESTEP;
				}
				render();
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
		s0.setImageSmoothing(true);

		layer_2 = new Canvas(1280, 720);
		c2 = layer_2.getGraphicsContext2D();
		c2.setImageSmoothing(false);

		shadow_1 = new Canvas(1280, 720);
		s1 = shadow_1.getGraphicsContext2D();
		s1.setImageSmoothing(true);

		hudCanvas = new Canvas(1280, 720);
		hudContext = hudCanvas.getGraphicsContext2D();
		hudContext.setImageSmoothing(false);

		throwawayCanvas = new Canvas(0, 0);
		throwawayContext = throwawayCanvas.getGraphicsContext2D();

		root.getChildren().add(layer_0);
		root.getChildren().add(layer_1);
		root.getChildren().add(shadow_0);
		root.getChildren().add(layer_2);
		root.getChildren().add(shadow_1);
		root.getChildren().add(hudCanvas);

		scene = new Scene(root, 1280, 720);
	}

	// Backup main function
	public static void main(String[] args) {
		Application.launch(args);
	}

	// Update scene. If there is a cutscene, run cutscene until it's over
	public void tick() {
		if (cutscene == null) {
			level.tick();
			camera.target(player.getX() + player.getXVel() * 1.5, player.getY() + player.getYVel() * 1.5);
			camera.tick();
		} else if (cutscene.run()) {
			cutscene = null;
		}
	}

	public void render() {
		// Clear all canvases
		c0.clearRect(0, 0, layer_0.getWidth(), layer_0.getHeight());
		c1.clearRect(0, 0, layer_1.getWidth(), layer_1.getHeight());
		s0.clearRect(0, 0, shadow_0.getWidth(), shadow_0.getHeight());
		c2.clearRect(0, 0, layer_2.getWidth(), layer_2.getHeight());
		s1.clearRect(0, 0, shadow_1.getWidth(), shadow_1.getHeight());
		hudContext.clearRect(0, 0, hudCanvas.getWidth(), hudCanvas.getHeight());

		// Save all context states
		c0.save();
		c1.save();
		s0.save();
		c2.save();
		s1.save();
		hudContext.save();

		// Transform all (except hud) with camera
		camera.transform(c0);
		camera.transform(c1);
		camera.transform(s0);
		camera.transform(c2);
		camera.transform(s1);
		// Don't transform hudContext

		// Draw!
		level.render(c0, c1, s0, c2, s1);
		hud.render(hudContext);

		// Restore context states
		c0.restore();
		c1.restore();
		s0.restore();
		c2.restore();
		s1.restore();
		hudContext.restore();
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

	public static void playCutscene(Cutscene cutscene) {
		instance.cutscene = cutscene;
	}

}
