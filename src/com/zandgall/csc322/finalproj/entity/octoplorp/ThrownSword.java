package	com.zandgall.csc322.finalproj.entity.octoplorp;

import com.zandgall.csc322.finalproj.Main;
import com.zandgall.csc322.finalproj.entity.Entity;
import com.zandgall.csc322.finalproj.entity.PlantedSword;
import com.zandgall.csc322.finalproj.util.Hitbox;
import com.zandgall.csc322.finalproj.util.Util;
import com.zandgall.csc322.finalproj.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ThrownSword extends Entity {

	private static final Image image = new Image("/entity/sword.png"), plaque = new Image("/entity/sword_plaque.png");
	
	private Vector target;
	private double rotation = 0, rotationVel;

	public PlantedSword endState;
	public boolean reachedTarget = false;

	public ThrownSword(double x, double y, Vector target, double rotationVel) {
		super(x, y);
		this.target = target;
		this.rotationVel = rotationVel;
		endState = new PlantedSword(target.x, target.y);
	}

	public void tick() {
		position.add(position.unitDir(target).scale(10 * Main.TIMESTEP));
		rotation += rotationVel * 20 * Main.TIMESTEP;
		if(position.sqDist(target) < 1 && Util.signedAngularDistance(rotation, 1.5 * Math.PI) < 30 * Main.TIMESTEP) {
			position.set(target);
			Main.getLevel().removeEntity(this);
			Main.getLevel().addEntity(endState);
			reachedTarget = true;
		}
	}

	public void render(GraphicsContext g, GraphicsContext shadow, GraphicsContext g2) {
		g.save();

		g.translate(getX(), getY());
		g.rotate(180 * rotation / Math.PI);

		g.drawImage(image, -1, -0.5, 2, 1);

		g.restore();

		g.drawImage(plaque, target.x-1, target.y-1.8, 2, 2);
	}

	@Override
	public Hitbox getHitBounds() {
		return new Hitbox();
	}

	@Override
	public Hitbox getSolidBounds() {	
		return new Hitbox();
	}

	@Override
	public Hitbox getRenderBounds() {
		return new Hitbox(getX()-1, getY()-1, 2, 2);
	}

	@Override
	public Hitbox getUpdateBounds() {
		return new Hitbox(Main.getLevel().bounds);
	}
}
