/* CSC322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Level Editor
 # An application built with the final project in order to edit and create levels

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.ArrayList;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.entity.Tree;
import com.zandgall.csc322.finalproj.level.Level;
import com.zandgall.csc322.finalproj.level.tile.Tile;

public class LevelEditor extends Main {

	private int tileX, tileY;
	private double entityX, entityY;
	private Entity selectedEntity = null;

	private Stage editorStage;
	private Scene editorScene;
	private VBox editorRoot;

	/* Information */
	private Text mode;

	/* Tiles */
	private HBox tileRoot;
	private Canvas currentTileView;
	private HBox tileOptionContainer;
	private double tileOptionOffset = 0;
	private ArrayList<Canvas> tileOptions;

	@Override
	public void start(Stage stage) {
		super.start(stage);

		level.getEntities().remove(player);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Main.keys.put(event.getCode(), true);
				if(event.getCode() == KeyCode.RIGHT)
					tileX++;
				if(event.getCode() == KeyCode.LEFT)
					tileX--;
				if(event.getCode() == KeyCode.DOWN)
					tileY++;
				if(event.getCode() == KeyCode.UP)
					tileY--;
				if(event.getCode() == KeyCode.T)
					mode.setText("Tile mode");
				if(event.getCode() == KeyCode.E)
					mode.setText("Entity mode");
				// If the user hits X, increment the ID of the selected tile
				if(mode.getText().equals("Tile mode")) {
					if(event.getCode() == KeyCode.X) {
						if(level.get(tileX, tileY) == null)
							level.put(tileX, tileY, Tile.get(1));
						else
							level.put(tileX, tileY, Tile.get(level.get(tileX, tileY).getID() + 1));
					}
					// Z decrements the selected tile ID
					if(event.getCode() == KeyCode.Z) {
						if(level.get(tileX, tileY) == null)
							level.put(tileX, tileY, Tile.get(-1)); // Tile.get has wrapping
						else
							level.put(tileX, tileY, Tile.get(level.get(tileX, tileY).getID() - 1));
					}
				}
				if(mode.getText().equals("Entity mode")) {
					if(event.getCode() == KeyCode.X) {
						//if(selectedEntity == null)
							
					}
				}
			}
		});

		editorStage = new Stage();
		editorRoot = new VBox();

		/* Information */

		HBox infoRoot = new HBox();
		mode = new Text("Tile mode");
		infoRoot.getChildren().add(mode);

		editorRoot.getChildren().add(infoRoot);

		/* Tiles */
		tileRoot = new HBox(58);
		currentTileView = new Canvas(70, 70);
		tileRoot.getChildren().add(currentTileView);
		tileOptionContainer = new HBox();
		tileOptions = new ArrayList<Canvas>();
		for(int i = 0; i < 18; i++) {
			tileOptions.add(new Canvas(64, 64));
			tileOptionContainer.getChildren().add(tileOptions.get(i));
			final int offset = i;
			tileOptions.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					level.put(tileX, tileY, Tile.get(offset + (int)Math.floor(tileOptionOffset)));
				}
			});
		}
		tileOptionContainer.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				int previous = (int)Math.floor(tileOptionOffset);
				tileOptionOffset+=(event.getDeltaX() + event.getDeltaY())*0.1;
				if(previous != (int)Math.floor(tileOptionOffset))
					updateTileOptions();
					
			}
		});
		updateTileOptions();
		tileRoot.getChildren().add(tileOptionContainer);


		editorRoot.getChildren().add(tileRoot);
		
		editorScene = new Scene(editorRoot, 1280, 256);
		editorStage.setTitle("Editor");
		editorStage.setScene(editorScene);
		editorStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void tick(double delta) {
		camera.target(tileX+0.5, tileY+0.5);
		camera.tick(delta*10);
	}

	private void updateTileOptions() {
		for(int i = 0; i < tileOptions.size(); i++) {
			GraphicsContext tileContext = tileOptions.get(i).getGraphicsContext2D();
			tileContext.clearRect(0, 0, 64, 64);
			tileContext.setImageSmoothing(false);
			tileContext.save();
			tileContext.scale(64, 64);
			Tile.get(i + (int)Math.floor(tileOptionOffset)).render(tileContext);
			tileContext.restore();
		}
	}

	@Override
	public void render() {
		super.render();
		gc.save();
		camera.transform(gc);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(0.1);
		gc.strokeRect(tileX, tileY, 1, 1);
		gc.restore();
		

		GraphicsContext gc2 = currentTileView.getGraphicsContext2D();
		gc2.setImageSmoothing(false);
		gc2.clearRect(0, 0, 64, 64);
		gc2.setStroke(Color.BLACK);
		gc2.setLineWidth(3);
		gc2.strokeRect(3, 3, 64, 64);
		gc2.save();
		gc2.translate(3, 3);
		gc2.scale(64, 64);
		if(level.get(tileX, tileY) != null)
			level.get(tileX, tileY).render(gc2);
		gc2.restore();
	}
}
