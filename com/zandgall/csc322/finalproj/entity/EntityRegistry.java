/* CSC 322 FINAL PROJECT - PROF. FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## Entity Registry
 # Used to register every entity in lists to facilitate the level editor and i/o

 : MADE IN NEOVIM */

package com.zandgall.csc322.finalproj.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Constructor;

public class EntityRegistry { 
	public static final ArrayList<Class> classes = new ArrayList<Class>();
	public static final HashMap<String, Class<?>> nameMap = new HashMap<String, Class<?>>();
	public static final HashMap<Class<?>, String> reverseNameMap = new HashMap<Class<?>, String>();
	public static void register(String name, Class<?> clazz) {	
		classes.add(clazz);
		nameMap.put(name, clazz);
		reverseNameMap.put(clazz, name);
	}

	public static void registerClasses() {
		register("Tree", Tree.class);
		register("PlantedSword", PlantedSword.class);
	}

	public static Entity construct(Class<?> clazz, double x, double y) {
		if(!Entity.class.isAssignableFrom(clazz)) {
			System.err.println(clazz.getCanonicalName() + " is not child class of entity");
			return null;
		}
		try {
			Constructor posC = clazz.getConstructor(double.class, double.class);
			return (Entity)posC.newInstance(x, y);
		} catch(Exception ignored) {}
		try {
			Constructor defaultC = clazz.getConstructor();
			Entity out = (Entity)defaultC.newInstance();
			out.setX(x);
			out.setY(y);
			return out;
		} catch(Exception ignored) {}

		System.err.println("Could not construct instance of " + clazz.getCanonicalName());
		return null;
	}

}
