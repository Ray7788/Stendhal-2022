package games.stendhal.server.entity.item;

//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.not;
//import static org.hamcrest.number.IsCloseTo.closeTo;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;

import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;

public class PipeTest {
	@BeforeClass
	public static void setUpBeforeClass() {
		ItemTestHelper.generateRPClasses();
		MockStendlRPWorld.get();
	}
	
	@Test
	public void testOnEquip() {
		Pipe pipe = new Pipe(ItemTestHelper.createItem("pipe"));
		Player mockPlayer = PlayerTestHelper.createPlayer("player");
		
		mockPlayer.equip("lhand", pipe);
		assertTrue(mockPlayer.isEquipped("pipe"));
		
	}
	
	@Test
	public void testOnDequip() {
		
	}
}
