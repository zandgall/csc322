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

public class Main extends Application {

	public static HashMap<KeyCode, Boolean> keys;
	
	public static Scene scene;
	public static Stage stage;
	public static Canvas canvas;
	public static GraphicsContext gc;

	private Player player;
	private Camera camera;

	@Override
	public void start(Stage stage) {
		Main.stage = stage;

		Pane root = new Pane();
		canvas = new Canvas(1280, 720);
		gc = canvas.getGraphicsContext2D();
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
		keys = new HashMap<KeyCode, Boolean>();
		Application.launch(args);
	}

	public void tick(double delta) {
		player.tick(delta);
		camera.target(player.getX() + player.getXVel() * 2, player.getY() + player.getYVel()* 2);
		camera.tick(delta);
	}

	public void render() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.save();
		camera.transform(gc);	
		player.render(gc);
		gc.restore();
	}

}
