package com.zandgall.csc322.finalproj.util;

public class Hitnull extends Hitbox {
	public Hitnull() {}

	public boolean intersects(double x, double y, double w, double h) {
		return false;
	}

	public boolean intersects(Hitbox box) {
		return false;
	}

	public Hitbox translate(double x, double y) {
		return this;
	}

	public Rect getBounds() {
		return new Rect();
	}
}
