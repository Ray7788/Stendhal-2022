package games.stendhal.server.entity.item;

import java.util.Map;

/**
 * A special outfit that lets you walk near enemy
 * creatures undetected.
 */
public class Equipment extends Item{
	private static String hidden_from = "human";
 
	public Equipment(String name, String clazz, String subclass, Map<String, String> attributes) {
	    super(name, clazz, subclass, attributes);
	}
 
	public static void setHiddenFrom() {
		// The logic for knowing which costume hides you from which creature should
		hidden_from = "";
	}
 
 /**
  * Returns creature class which players should be invisible to.
  */
	public static String getHiddenFrom() {
		return hidden_from;
	}
 
}