package games.stendhal.server.entity.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.common.EquipActionConsts;
import games.stendhal.server.actions.equip.EquipAction;
import games.stendhal.server.actions.equip.EquipmentAction;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.AreaUseItem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendhalRPRuleProcessor;
import marauroa.common.game.RPAction;
import marauroa.server.game.db.DatabaseFactory;
import utilities.PlayerTestHelper;
import utilities.ZoneAndPlayerTestImpl;

public class MusicalPipeTest extends ZoneAndPlayerTestImpl {
	
	private static final String ZONE_NAME = "0_semos_city";

	public MusicalPipeTest() {
	    super(ZONE_NAME);
    }
	
	/**
	 * initialize the world.
	 *
	 * @throws Exception
	 */
	@BeforeClass
	public static void buildWorld() throws Exception {
		new DatabaseFactory().initializeDatabase();
		setupZone(ZONE_NAME);
	}
	
	
	@Override
	@After
	public void tearDown() throws Exception {
		MockStendhalRPRuleProcessor.get().clearPlayers();
	}
	
	public static void tearDownAfterClass() throws Exception {
		PlayerTestHelper.removePlayer("a");
		PlayerTestHelper.removePlayer("b");
		PlayerTestHelper.removePlayer("c");
		PlayerTestHelper.removePlayer("d");
	}
	
	public AreaUseItem getPipe(String pipe) {
		return (AreaUseItem) SingletonRepository.getEntityManager().getItem(pipe + " pipe");
	}
	
	public Player equippedPlayer(String playerName, AreaUseItem item) {
		final Player player = PlayerTestHelper.createPlayer(playerName);
		player.equip("bag", item);
		
		return player;
	}
	
	public void equippedPlayer(Player player, AreaUseItem item) {
		player.equip("bag", item);
	}
	
	public void equippedPlayer(EquipmentAction eqAction, RPAction rpAction, Player player, AreaUseItem item, boolean rightHand) {
		String hand = "lhand";
		if(rightHand) {
			hand = "rhand";
		}
		rpAction.put("type", "equip");
		rpAction.put(EquipActionConsts.BASE_OBJECT, player.getID().getObjectID());
		rpAction.put(EquipActionConsts.BASE_ITEM, item.getID().getObjectID());
		rpAction.put(EquipActionConsts.BASE_SLOT, "bag");
		rpAction.put(EquipActionConsts.TARGET_OBJECT, player.getID().getObjectID());
		rpAction.put(EquipActionConsts.TARGET_SLOT, hand);

		eqAction.onAction(player, rpAction);
	}
	
	/**
	 * Test to check equipping bronze pipe
	 */
	@Test
	public void testBronzePipe() {
		String pipe_name = "bronze";
		StendhalRPZone zone = new StendhalRPZone("zone", 20, 20);
		SingletonRepository.getRPWorld().addRPZone(zone);
		AreaUseItem item = getPipe(pipe_name);
		final Player player = equippedPlayer("a", item);
		zone.add(player);
		assertTrue(player.isEquipped(pipe_name + " pipe"));
		
		assertEquals(-1, player.getCharmLevel());
		
		final EquipmentAction eqAction = new EquipAction();
		RPAction rpAction = new RPAction();
		equippedPlayer(eqAction, rpAction, player, item, false);
		
		assertEquals(40, player.getCharmLevel());
	
	}
	
	
	/**
	 * Test to check equipping silver pipe
	 */
	@Test
	public void testSilverPipe() {
		String pipe_name = "silver";
		StendhalRPZone zone = new StendhalRPZone("zone", 20, 20);
		SingletonRepository.getRPWorld().addRPZone(zone);
		AreaUseItem item = getPipe(pipe_name);
		final Player player = equippedPlayer("b", item);
		zone.add(player);
		assertTrue(player.isEquipped(pipe_name + " pipe"));
		
		assertEquals(-1, player.getCharmLevel());
		
		final EquipmentAction eqAction = new EquipAction();
		RPAction rpAction = new RPAction();
		equippedPlayer(eqAction, rpAction, player, item, false);

		assertEquals(170, player.getCharmLevel());
	}
	
	
	/**
	 * Test to check equipping gold pipe
	 */
	@Test
	public void testGoldPipe() {
		String pipe_name = "gold";
		StendhalRPZone zone = new StendhalRPZone("zone", 20, 20);
		SingletonRepository.getRPWorld().addRPZone(zone);
		AreaUseItem item = getPipe(pipe_name);
		final Player player = equippedPlayer("c", item);
		zone.add(player);
		assertTrue(player.isEquipped(pipe_name + " pipe"));
		
		assertEquals(-1, player.getCharmLevel());
		
		final EquipmentAction eqAction = new EquipAction();
		RPAction rpAction = new RPAction();
		equippedPlayer(eqAction, rpAction, player, item, false);

		assertEquals(450, player.getCharmLevel());
		
	}
	
	/**
	 * Test to check dual weilding pipes
	 */
	@Test
	public void testDualPipe() {
		
		String pipe1 = "gold";
		String pipe2 = "bronze";
		
		StendhalRPZone zone = new StendhalRPZone("zone", 20, 20);
		SingletonRepository.getRPWorld().addRPZone(zone);
		AreaUseItem item1 = getPipe(pipe1);
		AreaUseItem item2 = getPipe(pipe2);
		final Player player = equippedPlayer("d", item1);
		equippedPlayer(player, item2);
		zone.add(player);
		
		assertTrue(player.isEquipped(pipe1 + " pipe"));
		assertTrue(player.isEquipped(pipe2 + " pipe"));
		
		// Add test to check attribute value
		assertEquals(-1, player.getCharmLevel());
		
		final EquipmentAction eqAction = new EquipAction();
		RPAction rpAction = new RPAction();
		equippedPlayer(eqAction, rpAction, player, item1, true);
		equippedPlayer(eqAction, rpAction, player, item2, false);
		
		// Add test to check attribute value
		assertEquals(450, player.getCharmLevel());
			
	}
	
}
