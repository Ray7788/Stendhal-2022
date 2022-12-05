package games.stendhal.server.entity.item;

public class Pipe extends Item{

	public Pipe(Item item) {
		super(item); // inherits item class
	}
	
	public void onEquip() {
		// do stuff here when equipped
		// possibly use events somehow?
		// this method should do the "charming"
		// what to return? possibly creatures charmed? <- this is preferred
		// maybe just return nothing? <- try not to do this/only do this on starting out
	}
	
	public void onDequip() {
		// revert charms on enemies here
		// return nothing?
	}
	
}
