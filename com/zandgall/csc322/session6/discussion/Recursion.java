
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
	private static class Node {
		private Node parent = null;
		private Node left = null, right = null;
		private int leftNumber = 0, rightNumber = 0;

		public Node(int leftNumber, int rightNumber, Node parent) {
			this.parent = parent;
			this.leftNumber = leftNumber;
			if(leftNumber > 0)
				left = new Node(leftNumber - 1, rightNumber - 2, this);
			else
				this.leftNumber = 0;

			this.rightNumber = rightNumber;
			if(rightNumber > 0)
				right = new Node(leftNumber - 2, rightNumber - 1, this);
			else
				this.rightNumber = 0;
		}

		private void draw(GraphicsContext g, double angleMultiplier) {
			g.scale(0.9, 0.9);
			g.translate(100, 0);
			g.save();
			g.rotate(-180 * angleMultiplier);
			g.setStroke(Color.RED);
			if(right!=null)
				right.draw(g, 0.5);
			g.restore();
			g.save();
			g.rotate(180 * angleMultiplier);
			g.setStroke(Color.BLUE);
			if(left!=null)
				left.draw(g, 0.5);
			g.restore();
		}

		public void draw(GraphicsContext g) {
			g.setLineWidth(10);
			g.scale(100, 100);
			g.translate(250, 200);
			g.setStroke(Color.RED);
			g.save();
			if(right!=null)
				right.draw(g, 0.5);
			g.restore();
			g.save();
			g.rotate(180);
			g.setStroke(Color.BLUE);
			if(left!=null)
				left.draw(g, 0.5);
			g.restore();
		}
	}

	public void start(Stage stage) {
		BorderPane p = new BorderPane();

		Canvas c = new Canvas(500, 400);
		GraphicsContext g = c.getGraphicsContext2D();
		p.setCenter(c);

		HBox top = new HBox();
		TextField left = new TextField("Left Number");
		TextField right = new TextField("Right Number");
		Button button = new Button("Generate");
		top.getChildren().add(left);
		top.getChildren().add(right);
		top.getChildren().add(button);
		p.setTop(top);
		button.setOnAction((e) -> {
			try {
				int l = Integer.parseInt(left.getText());
				int r = Integer.parseInt(right.getText());
				Node n = new Node(l, r, null);
				g.clearRect(0, 0, 500, 400);
				n.draw(g);
			} catch(Exception ex) {
				ex.printStackTrace();
				System.err.println("Couldn't generate node tree!");
			}
		});
		Scene scene = new Scene(p, 500, 500);
		stage.setScene(scene);
		stage.show();
	}
}
