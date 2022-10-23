package games.stendhal.server.maps.ados.city;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;

import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;


public class MakeupArtistNPCTest extends ZonePlayerAndNPCTestImpl {
	
	private static final String ZONE_NAME = "testzone";
	
	private Player player;
	private SpeakerNPC fidoreaNpc;
	private Engine fidoreaEngine;
	
	private static final String HELP_REPLY = "I sell masks. If you don't like your mask, you can #return and I will remove it, or you can just wait %s, until it wears off.";
	private static final String BUY_REPLY = "To buy a mask will cost 20. Do you want to buy it?";
	
	
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
		fidoreaNpc = SingletonRepository.getNPCList().get("Fidorea");
		fidoreaEngine = fidoreaNpc.getEngine();
	}
	
	public MakeupArtistNPCTest() {
		super(ZONE_NAME, "Fidorea");
	}
	
	@Test
	public void testDialogue() {
		startConversation();

		checkReply("job", "I am a makeup artist.");
		checkReply("help", HELP_REPLY);

		endConversation();
	}

	private void startConversation() {
		fidoreaEngine.step(player, ConversationPhrases.GREETING_MESSAGES.get(0));
		assertTrue(fidoreaNpc.isTalking());
		assertEquals("Hi, there. Do you need #help with anything?", getReply(fidoreaNpc));
	}
	
	private void endConversation() {
		fidoreaEngine.step(player, ConversationPhrases.GOODBYE_MESSAGES.get(0));
		assertFalse(fidoreaNpc.isTalking());
		assertEquals("Bye, come back soon.", getReply(fidoreaNpc));
	}

	private void checkReply(String question, String expectedReply) {
		fidoreaEngine.step(player, question);
		assertTrue(fidoreaNpc.isTalking());
		assertEquals(expectedReply, getReply(fidoreaNpc));
	}
	
	@Test
	public void checkBuyMask() {
		assertTrue(fidoreaEngine.step(player, "hi"));
		assertEquals("Hi, there. Do you need #help with anything?", getReply(fidoreaNpc));
		
		assertTrue(equipWithMoney(player, 120));
		
		assertTrue(fidoreaEngine.step(player, "buy dog mask"));
		assertEquals(BUY_REPLY, getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 998, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy fox mask"));
		assertEquals(BUY_REPLY, getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 999, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy frog mask"));
		assertEquals(BUY_REPLY, getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 995, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy monkey mask"));
		assertEquals(BUY_REPLY, getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 997, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy penguin mask"));
		assertEquals(BUY_REPLY, getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 996, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "buy bear mask"));
		assertEquals(BUY_REPLY, getReply(fidoreaNpc));
		assertTrue(fidoreaEngine.step(player, "yes"));
		assertEquals((Integer) 994, player.getOutfit().getLayer(("mask")));
		
		assertTrue(fidoreaEngine.step(player, "bye"));
		assertEquals("Bye, come back soon.", getReply(fidoreaNpc));
	}
	
	
}