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

public class Path {
	private ArrayList<Node> path = new ArrayList<>();

	public Path() {
	}

	public Node current() {
		return path.get(0);
	}

	public void progress() {
		path.remove(0);
	}

	public boolean empty() {
		return path.isEmpty();
	}

	public void debugRender(GraphicsContext g) {
		g.setLineWidth(0.1);
		g.setStroke(Color.CYAN);
		g.strokeRect(path.get(0).x + 0.3, path.get(0).y, 0.4, 0.4);
		for (int i = 1; i < path.size(); i++) {
			g.strokeLine(path.get(i - 1).x + 0.5, path.get(i - 1).y + 0.5,
					path.get(i).x + 0.5, path.get(i).y + 0.5);
			g.strokeRect(path.get(i).x + 0.3, path.get(i).y, 0.4, 0.4);
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

	private static boolean isNodeIn(Node a, ArrayList<Node> open) {
		for (Node b : open)
			if (b.equals(a))
				return true;
		return false;
	}

	public static Path pathfind(int startX, int startY, int targetX, int targetY, GraphicsContext g) {
		// ArrayList<Node> open = new ArrayList<>();
		PriorityQueue<Node> open = new PriorityQueue<>();
		open.add(new Node(null, startX, startY, 0.0, heuristic(startX, startY, targetX, targetY)));

		HashMap<Point, Node> map = new HashMap<>();

		g.setStroke(Color.BLUE);
		g.setLineWidth(0.2);
		int iter = 0;
		while (!open.isEmpty()) {
			iter++;
			System.out.printf("Iteration %d: (%d, %d) -> (%d, %d)", iter, startX, startY, targetX, targetY);
			Node current = open.poll();
			if (current.x == targetX && current.y == targetY) {
				System.out.println("Found path!");
				return reconstruct(current);
			}

			// Define the neighbor coordinates
			int nX[] = { current.x, current.x + 1, current.x, current.x - 1 },
					nY[] = { current.y - 1, current.y, current.y + 1, current.y };

			// Loop through them, check if they have solid bounds and if they're not
			// marked yet or if the calculated gscore is less than what it had before
			for (int i = 0; i < 4; i++) {
				System.out.printf("Checking %d, %d%n", nX[i], nY[i]);
				Point p = new Point(nX[i], nY[i]);

				// Tile doesnt exist or is solid, skip
				if (Main.getLevel().get(p.x, p.y) == null
						|| Main.getLevel().get(p.x, p.y).solidBounds(p.x, p.y) != null)
					continue;

				Node n = map.getOrDefault(p, new Node(p.x, p.y));
				System.out.printf("Node passed, checking node: %.1f vs %.1f%n", n.gScore, current.gScore + 1);
				if (n.gScore > current.gScore + 1) {
					n.parent = current;
					n.gScore = current.gScore;
					n.fScore = current.gScore + heuristic(p.x, p.y, targetX, targetY);
					System.out.printf("New Open node? %b%n", !open.contains(n));
					if (!open.contains(n))
						open.add(n);
				}
			}

			System.out.println(open.size());

			Iterator<Node> i = open.iterator();
			if (!open.isEmpty())
				for (Node n = i.next(); i.hasNext(); n = i.next()) {
					g.strokeRect(n.x, n.y, 1, 1);
				}

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
		}

		System.out.println("Didn't find path.. :(");
		return new Path();
	}

	public static class Node implements Comparable<Node> {
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

		/**
		 * For all intents and purposes, two nodes are equal if they occupy the same
		 * coordinate
		 */
		public boolean isEquals(Object other) {
			if (other instanceof Node n)
				return n.x == this.x && n.y == this.y;
			return false;
		}

		/**
		 * For queue ordering
		 */
		@Override
		public int compareTo(Node other) {
			return other.fScore < fScore ? -1 : (other.fScore.equals(fScore) ? 0 : -1);
		}
	}
}
