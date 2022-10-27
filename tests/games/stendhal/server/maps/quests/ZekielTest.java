/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.quests;

//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
//import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
//import games.stendhal.server.core.engine.StendhalRPZone;
//import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
//import games.stendhal.server.maps.kalavan.castle.SadScientistNPC;
import games.stendhal.server.maps.semos.wizardstower.WizardsGuardStatueNPC;
import games.stendhal.server.maps.semos.wizardstower.WizardsGuardStatueSpireNPC;
//import games.stendhal.server.maps.semos.townhall.MayorNPC;
import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

public class ZekielTest extends ZonePlayerAndNPCTestImpl{
	private static final String QUEST_SLOT = "zekiels_practical_test";
	// better: use the one from quest and make it visible
	//private static final String NEEDED_ITEMS = "emerald=1;obsidian=1;sapphire=1;carbuncle=2;gold bar=20;mithril bar=1;shadow legs=1";
	//private static final int REQUIRED_BEESWAX = 6;
	//private static final int REQUIRED_IRON = 2;
	private Player player = null;
	private SpeakerNPC npc = null;
	private Engine en = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		setupZone("admin_test");
	}

	public ZekielTest() {
		setNpcNames("Zekiel the guardian");
		setZoneForPlayer("admin_test");
		addZoneConfigurator(new WizardsGuardStatueNPC(), "admin_test");
		addZoneConfigurator(new WizardsGuardStatueSpireNPC(), "admin_test");
	}
	@Before
	@Override
	public void setUp() throws Exception{

		//final StendhalRPZone zone = new StendhalRPZone("admin_test");
		super.setUp();

		//new SadScientistNPC().configureZone(zone, null);
		//new MayorNPC().configureZone(zone, null);
		
		//new WizardsGuardStatueNPC().configureZone(zone, null);

		AbstractQuest quest = new ZekielsPracticalTestQuest();
		quest.addToWorld();

		player = PlayerTestHelper.createPlayer("bob");
	}

	@Test
	public void testQuest() {

		npc = SingletonRepository.getNPCList().get("Zekiel the guardian");
		en = npc.getEngine();

		// -----------------------------------------------


		// -----------------------------------------------

		// [23:00] Admin kymara changed your state of the quest 'sad_scientist' from 'done' to 'null'
		// [23:00] Changed the state of quest 'sad_scientist' from 'done' to 'null'
		// [23:00] Script "AlterQuest.class" was successfully executed.
		en.step(player, "hi");
		assertEquals("Greetings Stranger! I am Zekiel the #guardian.", getReply(npc));
		en.step(player, "test");
		assertEquals("The practical test will be your #quest from me.", getReply(npc));
		en.step(player, "quest");
		assertEquals("First you need six magic candles. Bring me 6 pieces of #beeswax and 2 pieces of #iron, then I will summon the candles for you. After this you can start the practical test.", getReply(npc));
		en.step(player, "bye");
		assertEquals("SO long!", getReply(npc));

		// -----------------------------------------------
		//getting item for test
		PlayerTestHelper.equipWithStackableItem(player, "beeswax", 6);
		PlayerTestHelper.equipWithStackableItem(player, "iron", 2);
		
		en.step(player, "hi");
		assertEquals("Finally !! YOu have gotten all the items required for me to summon the candles. YOu may start the Practical test now", getReply(npc));
		en.step(player, "send");
		assertEquals("YOu cant drop candles after this stage.", getReply(npc));
		assertEquals("first_step",player.getQuest(QUEST_SLOT));
		//en.step(player, "yes");
		//assertEquals("My wife is living in Semos City. She loves gems. Can you bring me some #gems that I need to make a pair of precious #legs?", getReply(npc));
		//en.step(player, "no");
		//assertEquals("Go away before I kill you!", getReply(npc));
		//en.step(player, "bye");
		//assertEquals("Go away!", getReply(npc));

		// -----------------------------------------------

		//player.setPosition(26, 15);
		//en.setPlayer
//		en.step(player, "hi");
//		assertEquals("Go away!", getReply(npc));
//		en.step(player, "task");
//		assertEquals("So...looks like you want to help me?", getReply(npc));
//		en.step(player, "yes");
//		assertEquals("My wife is living in Semos City. She loves gems. Can you bring me some #gems that I need to make a pair of precious #legs?", getReply(npc));
//		en.step(player, "gems");
//		assertEquals("I need an emerald, an obsidian, a sapphire, 2 carbuncles, 20 gold bars and one mithril bar. Can you do that for my wife?", getReply(npc));
//		en.step(player, "legs");
//		assertEquals("Jewelled legs. I need an emerald, an obsidian, a sapphire, 2 carbuncles, 20 gold bars and one mithril bar. Can you do that for my wife? Can you bring what I need?", getReply(npc));
//		en.step(player, "yes");
//		assertEquals("I am waiting, Semos man.", getReply(npc));

		// -----------------------------------------------

		//en.step(player, "hi");
		//assertEquals("Hello. Do you have any #items I need for the jewelled legs?", getReply(npc));

		// summon all the items needed:
		// but not all the gold bar
//		PlayerTestHelper.equipWithStackableItem(player, "gold bar", 10);
//		PlayerTestHelper.equipWithItem(player, "mithril bar");
//		PlayerTestHelper.equipWithItem(player, "emerald");
//		PlayerTestHelper.equipWithItem(player, "obsidian");
//		PlayerTestHelper.equipWithItem(player, "sapphire");
//		PlayerTestHelper.equipWithStackableItem(player, "carbuncle", 2);
	}
}
