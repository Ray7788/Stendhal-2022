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

import games.stendhal.server.maps.ados.barracks.BuyerNPC;

import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;



public class BuyerNPCTest extends ZonePlayerAndNPCTestImpl {

	private static final String ZONE_NAME = "MrothoZone";
	
	private Player player;
	private SpeakerNPC MrothoNpc;
	private Engine MrothoEngine;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();

		setupZone(ZONE_NAME, new BuyerNPC());
	}
	
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		player = createPlayer("player");
		MrothoNpc = getNPC("Mrotho");
		MrothoEngine = MrothoNpc.getEngine();
	}
	
	public BuyerNPCTest() {
		super(ZONE_NAME, "Mrotho");
		addZoneConfigurator(new BuyerNPC(), ZONE_NAME);
	}
	

}
