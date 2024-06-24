/* CSC 322 FINAL PROJECT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Path
 # A class that stores details on how to get from one place to another
 # Used in pathfinding

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.awt.Point;

import com.zandgall.csc322.finalproj.Main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Path {
	private ArrayList<Node> path = new ArrayList<>();

	public Path() {
	}

	public Node current() {
		return path.get(0);
	}

	public Node next() {
		if (path.size() > 1)
			return path.get(1);
		return null;
	}

	public void progress() {
		path.remove(0);
	}

	public boolean empty() {
		return path.isEmpty();
	}

	public void debugRender(GraphicsContext g) {
		if (path.isEmpty())
			return;
		g.setLineWidth(0.2);
		g.setStroke(Color.BLACK);
		for (int i = 1; i < path.size(); i++) {
			g.strokeLine(path.get(i - 1).x + 0.5, path.get(i - 1).y + 0.5,
					path.get(i).x + 0.5, path.get(i).y + 0.5);
		}
	}

	private static Path reconstruct(Node endPoint) {
		Path p = new Path();
		for (Node n = endPoint; n != null; n = n.getParent())
			p.path.addFirst(n);
		return p;
	}

	/**
	 * Find taxicab distance between a point and a target for pathfind algorithm
	 */
	private static double heuristic(int x, int y, int targetX, int targetY) {
		return Math.abs(targetX - x) + Math.abs(targetY - y);
	}

	/**
	 * An implementation of the A* search algorithm, for finding paths around solid
	 * tiles
	 *
	 * @param startX  Starting X position
	 * @param startY  Starting Y position
	 * @param targetX Target X position
	 * @param targetY Target Y position
	 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A*
	 *      Wikipedia</a>
	 */
	public static Path pathfind(int startX, int startY, int targetX, int targetY) {
		ArrayList<Point> open = new ArrayList<>();

		HashMap<Point, Node> map = new HashMap<>();
		map.put(new Point(startX, startY),
				new Node(null, startX, startY, 0.0, heuristic(startX, startY, targetX, targetY)));
		open.add(new Point(startX, startY));

		// Have a limit to how many pathfind iterations we take
		int iter = 0;
		while (!open.isEmpty() && iter < 100) {
			iter++;

			open.sort((a, b) -> {
				double aV = (map.containsKey(a) ? map.get(a).fScore : Double.POSITIVE_INFINITY);
				double bV = (map.containsKey(b) ? map.get(b).fScore : Double.POSITIVE_INFINITY);
				return (int) Math.signum(aV - bV);
			});
			Node current = map.get(open.get(0));
			open.removeFirst();
			if (current.x == targetX && current.y == targetY)
				return reconstruct(current);

			// Define the neighbor coordinates
			int nX[] = { current.x, current.x + 1, current.x, current.x - 1 },
					nY[] = { current.y - 1, current.y, current.y + 1, current.y };

			// Loop through them, check if they have solid bounds and if they're not
			// marked yet or if the calculated gscore is less than what it had before
			for (int i = 0; i < 4; i++) {
				Point p = new Point(nX[i], nY[i]);

				// Tile doesnt exist or is solid, skip
				if (Main.getLevel().get(p.x, p.y) == null
						|| Main.getLevel().get(p.x, p.y).solidBounds(p.x, p.y) != null)
					continue;

				Node n = map.getOrDefault(p, new Node(p.x, p.y));

				if (n.gScore > current.gScore + 1) {
					n.parent = current;
					n.gScore = current.gScore + 1;
					n.fScore = current.gScore + 1 + heuristic(p.x, p.y, targetX, targetY);

					if (!map.containsKey(p))
						map.put(p, n);
					if (!open.contains(p))
						open.add(p);
				}
			}
		}

		return new Path();
	}

	public static class Node {
		public Node parent = null;
		public Double gScore = Double.POSITIVE_INFINITY, fScore = Double.POSITIVE_INFINITY;

		public int x, y;

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Node(Node parent, int x, int y, Double gScore, Double fScore) {
			this.parent = parent;
			this.x = x;
			this.y = y;
			this.gScore = gScore;
			this.fScore = fScore;
		}

		public Node getParent() {
			return parent;
		}
	}
}
