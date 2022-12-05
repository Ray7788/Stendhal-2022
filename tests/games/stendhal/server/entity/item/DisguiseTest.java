package games.stendhal.server.entity.item;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;

public class DisguiseTest {
 
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        MockStendlRPWorld.get();
        ItemTestHelper.generateRPClasses();
    }
    
    /** 
     * Player doesn't equip any costumes
    */
    @Test
    public void commanderShouldAttckThePlayerIfNoCostumeIsEquiped() {
	    // initialise a zone
	    StendhalRPZone testZone = new StendhalRPZone("testZone", 10, 10);
	
	    // add a player
	    Player player = PlayerTestHelper.createPlayer("player1");
	
	    // add an imperial commander
	    final Creature iCommander = SingletonRepository.getEntityManager().getCreature("imperial commander");

	    // add the player and the imperial commander to the zone
	    testZone.add(player);
	    testZone.add(iCommander);
	
	    // check that the imperial commander has a target
	    assertNotNull(iCommander.getNearestEnemy(10));
    }


    /** 
     * Player wears certain kind of costumes
    */
    @Test
    public void commanderShouldNotAttckThePlayerIfCostumeIsEquiped() {
    	StendhalRPZone testZone = new StendhalRPZone("testZone", 10, 10);

	    Player player = PlayerTestHelper.createPlayer("player1");
	    Equipment reviCostume = new Equipment("disguise armor", "", "", new HashMap<String, String>());
	    player.equip("armor", reviCostume);
	
	    final Creature iCommander = SingletonRepository.getEntityManager().getCreature("imperial commander");
	
	    testZone.add(player);
	    testZone.add(iCommander);
	
	    assertNull(iCommander.getNearestEnemy(10));
    }

    /** 
     * Another kind of creature will attack the player even if it is equipped with costume
    */
    @Test
    public void wolfShouldAttckThePlayerIfCostumeIsEquiped() {
    StendhalRPZone testZone = new StendhalRPZone("testZone", 10, 10);

    Player player = PlayerTestHelper.createPlayer("player1");
    Equipment reviCostume = new Equipment("disguise armor", "", "", new HashMap<String, String>());
    player.equip("armor", reviCostume);

    final Creature iCommander = SingletonRepository.getEntityManager().getCreature("imperial commander");
    // the class of wolf is different from soldiers(human) 
    final Creature wolf = SingletonRepository.getEntityManager().getCreature("wolf");

    testZone.add(player);

    testZone.add(iCommander);
    testZone.add(wolf);

    assertNull(iCommander.getNearestEnemy(10));
    assertNotNull(wolf.getNearestEnemy(10));
    }

}
