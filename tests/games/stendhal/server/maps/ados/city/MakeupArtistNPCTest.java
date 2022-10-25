package games.stendhal.server.maps.ados.city;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;

import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;


public class MakeupArtistNPCTest extends ZonePlayerAndNPCTestImpl {
	
	private static final String ZONE_NAME = "testZone";
	
	private Player player;
	private SpeakerNPC fidoreaNpc;
	private Engine fidoreaEngine;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();

		setupZone(ZONE_NAME, new MakeupArtistNPC());
	}
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		player = createPlayer("player");
		fidoreaNpc = getNPC("Fidorea");
		fidoreaEngine = fidoreaNpc.getEngine();
	}
	
	public MakeupArtistNPCTest() {
		super(ZONE_NAME, "Fidorea");
		addZoneConfigurator(new MakeupArtistNPC(), ZONE_NAME);
	}
	
	@Test
	public void testHiandBye() {
		startConversation();

		endConversation();
	}

	private void startConversation() {
		assertTrue(fidoreaEngine.step(player, "hi"));
		assertEquals("Hi, there. Do you need #help with anything?", getReply(fidoreaNpc));
	}
	
	private void endConversation() {
		assertTrue((fidoreaEngine.step(player, "bye")));
		assertEquals("Bye, come back soon.", getReply(fidoreaNpc));
	}

	
	@Test
	public void checkBuyMask() {
		assertTrue(fidoreaEngine.step(player, "hi"));
		assertEquals("Hi, there. Do you need #help with anything?", getReply(fidoreaNpc));
		
		assertTrue(equipWithMoney(player, 120));
		
		assertTrue(fidoreaEngine.step(player, "buy dog mask"));
		assertEquals("To buy a dog mask will cost 20. Do you want to buy it?", getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 998, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy fox mask"));
		assertEquals("To buy a fox mask will cost 20. Do you want to buy it?", getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 999, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy frog mask"));
		assertEquals("To buy a frog mask will cost 20. Do you want to buy it?", getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 995, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy monkey mask"));
		assertEquals("To buy a monkey mask will cost 20. Do you want to buy it?", getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 997, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy penguin mask"));
		assertEquals("To buy a penguin mask will cost 20. Do you want to buy it?", getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 996, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy bear mask"));
		assertEquals("To buy a bear mask will cost 20. Do you want to buy it?", getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 994, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "bye"));
		assertEquals("Bye, come back soon.", getReply(fidoreaNpc));
	}
	
	
}

