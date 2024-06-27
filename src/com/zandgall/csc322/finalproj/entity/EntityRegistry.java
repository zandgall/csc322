/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU
 -- I certify, that this computer program submitted by me is all of my own work.

 ## Entity Registry
 # Used to register every entity in lists to facilitate the level editor and i/o

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Constructor;

public class EntityRegistry {
	public static final ArrayList<Class<?>> classes = new ArrayList<>();
	public static final HashMap<String, Class<?>> nameMap = new HashMap<String, Class<?>>();
	public static final HashMap<Class<?>, String> reverseNameMap = new HashMap<Class<?>, String>();

	public static void register(String name, Class<?> clazz) {
		classes.add(clazz);
		nameMap.put(name, clazz);
		reverseNameMap.put(clazz, name);
	}

	/**
	 * Register all entities used in the game
	 */
	public static void registerClasses() {
		register("Tree", Tree.class);
		register("PlantedSword", PlantedSword.class);
		register("Plorp", Plorp.class);
		register("HealthFlower", HealthFlower.class);
		register("Octoplorp", Octoplorp.class);
	}

	/**
	 * Create an instance of an Entity given an entity subclass
	 */
	public static Entity construct(Class<?> clazz, double x, double y) {
		// If the class isn't an entity class, quit
		if (!Entity.class.isAssignableFrom(clazz)) {
			System.err.println(clazz.getCanonicalName() + " is not child class of entity");
			return null;
		}

		// Attempt to construct the entity with the Class(x, y) constructor
		try {
			Constructor<?> posC = clazz.getConstructor(double.class, double.class);
			return (Entity) posC.newInstance(x, y);
		} catch (Exception ignored) {
		}

		System.err.println("Could not construct instance of " + clazz.getCanonicalName());
		return null;
	}

}
