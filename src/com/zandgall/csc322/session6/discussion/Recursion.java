/**
 * This is a different type of recursion in which a class owns instances of itself,
 * and recurses through methods of their children instances
 * The base case is started in the "Generate" button onAction function
 */


import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class Recursion extends Application{

	private static class Branch {
		// This class, "Branch", has instances of "Branch" that could be null
		// This isn't recursion, but it's a base that often leads to recursion in some way
		private Branch parent = null;
		private Branch left = null, right = null;

		private int leftNumber = 0, rightNumber = 0;

		// The constructor Branch(int, int, int) conditionally calls itself, a form or recursion
		public Branch(int leftNumber, int rightNumber, Branch parent) {
			this.parent = parent;
			this.leftNumber = leftNumber;
			if(leftNumber > 0)
				left = new Branch(leftNumber - 1, rightNumber - 2, this);
			else
				this.leftNumber = 0;

			this.rightNumber = rightNumber;
			if(rightNumber > 0)
				right = new Branch(leftNumber - 2, rightNumber - 1, this);
			else
				this.rightNumber = 0;
		}

		// The private method "draw" calls itself on it's children "right" and "left" branches, (if they aren't null)
		private void draw(GraphicsContext g, double angleMultiplier) {
			// Draw this branch,
			g.strokeLine(0, 0, 100, 0);

			// Move to end of branch and scale down
			g.translate(100, 0);
			g.scale(0.8, 0.8);	

			// Save graphics state, rotate using angleMultipier, use RED color, and call draw on right branch
			if(right!=null) {
				g.save();
				g.rotate(180 * angleMultiplier);
				g.setStroke(Color.RED);
				right.draw(g, angleMultiplier*0.6); // 'dampen' angleMultiplier
				g.restore();
			}

			// Do the same for left branch, rotating in opposite direction
			if(left!=null) {
				g.save();
				g.rotate(-180 * angleMultiplier);
				g.setStroke(Color.BLUE);
				left.draw(g, angleMultiplier*0.6);
				g.restore();
			}
		}


		public void draw(GraphicsContext g) {	
			// if "right" branch exists, save state, rotate -40 degrees, and call the private draw with red, restoring state afterwards
			if(right!=null) {
				g.save();
				g.rotate(-40);
				g.setStroke(Color.RED);
				right.draw(g, 0.2);
				g.restore();
			}
			// if "left" branch exists, save state, rotate -140 degrees, and call the private draw with blue, restoring state afterwards
			if(left!=null) {
				g.save();
				g.rotate(-140);
				g.setStroke(Color.BLUE);
				left.draw(g, 0.2);
				g.restore();
			}
		}
	}

	public void start(Stage stage) {
		BorderPane p = new BorderPane();

		// Create a canvas we can draw on
		Canvas c = new Canvas(800, 800);
		GraphicsContext g = c.getGraphicsContext2D();
		// Put it in the center of the scene
		p.setCenter(c);

		// Create a box for the top, with "left" number field, "right" number field, and "generate" button
		HBox top = new HBox();
		TextField left = new TextField("Left Number");
		TextField right = new TextField("Right Number");
		Button button = new Button("Generate");
		top.getChildren().add(left);
		top.getChildren().add(right);
		top.getChildren().add(button);
		p.setTop(top);

		// When the "Generate" button is hit, (USE TEXT FIELDS TO CREATE BASE CASE)
		button.setOnAction((e) -> {
			try {
				// Create a Branch
				int l = Integer.parseInt(left.getText());
				int r = Integer.parseInt(right.getText());
				Branch n = new Branch(l, r, null);

				// Clear the screen, set line width,
				g.clearRect(0, 0, 800, 800);
				g.setLineWidth(10);
				g.save();
				g.translate(400, 600);

				// Draw the node
				n.draw(g);
				
				// Reset the graphics context for next time
				g.restore();
			} catch(Exception ex) {
				// Something went wrong (like text in fields instead of numbers)
				ex.printStackTrace();
				System.err.println("Couldn't generate node tree!");
			}
		});

		// Add to scene and show
		Scene scene = new Scene(p, 800, 900);
		stage.setScene(scene);
		stage.show();
	}
}
