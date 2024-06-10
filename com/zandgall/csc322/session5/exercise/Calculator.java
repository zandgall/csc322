/* CSC322 SESSION 5: EXERCISE - PROF. SUSAN FURTNEY
 > I certify, that this computer program submitted by me is all of my own work.
 > ZANDER GALL - GALLA@CSP.EDU

 ## Calculator
 # A simple JavaFX program that lets a user enter in two numbers and add, subtract, multiply, or divide them

 : MADE IN NEOVIM */

package com.zandgall.csc322.session5.exercise;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Calculator extends Application {
	@Override
	public void start(Stage stage) {
		// Set up grid pane
		GridPane grid = new GridPane(10, 10);
		grid.setAlignment(Pos.CENTER);

		// Add labels
		TextField numberA = new TextField();
		grid.add(numberA, 0, 0);

		TextField numberB = new TextField();
		grid.add(numberB, 1, 0);

		TextField equals = new TextField("=");
		equals.setEditable(false);
		equals.setAlignment(Pos.CENTER);
		grid.add(equals, 2, 0);

		TextField output = new TextField("0");
		output.setEditable(false);
		output.setAlignment(Pos.CENTER);
		grid.add(output, 3, 0);

		// Add calculation buttons
		Button add = new Button("Add");
		grid.add(add, 0, 1);
		GridPane.setHalignment(add, HPos.CENTER);

		Button subtract = new Button("Subtract");
		grid.add(subtract, 1, 1);
		GridPane.setHalignment(subtract, HPos.CENTER);

		Button multiply = new Button("Multiply");
		grid.add(multiply, 2, 1);
		GridPane.setHalignment(multiply, HPos.CENTER);

		Button divide = new Button("Divide");
		grid.add(divide, 3, 1);
		GridPane.setHalignment(divide, HPos.CENTER);

		// Set operations functions, parsing doubles of numberA and numberB, performing the operation, and converting to text with String.format
		add.setOnAction(e -> {
			output.setText(String.format("%f", Double.parseDouble(numberA.getText()) + Double.parseDouble(numberB.getText())));
		});
		subtract.setOnAction(e -> {
			output.setText(String.format("%f", Double.parseDouble(numberA.getText()) - Double.parseDouble(numberB.getText())));
		});
		multiply.setOnAction(e -> {
			output.setText(String.format("%f", Double.parseDouble(numberA.getText()) * Double.parseDouble(numberB.getText())));
		});
		divide.setOnAction(e -> {
			output.setText(String.format("%f", Double.parseDouble(numberA.getText()) / Double.parseDouble(numberB.getText())));
		});

		// Set up scene and show
		Scene scene = new Scene(grid, 400, 60);
		stage.setScene(scene);
		stage.setTitle("Calculator");
		stage.show();
	}
}
