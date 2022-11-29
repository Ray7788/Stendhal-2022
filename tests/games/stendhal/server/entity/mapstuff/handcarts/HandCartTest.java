package games.stendhal.server.entity.mapstuff.handcarts;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.common.Direction;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.mapstuff.handcart.HandCart;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.HandCartHelper;

/**
 * 
 * checking if the handcart can be moved.
 * 
 *
 */

public class HandCartTest {
	@BeforeClass
	public static void beforeClass() {
		HandCartHelper.generateRPClasses();
        MockStendlRPWorld.get();
	}

	@Test
	public final void testReset() {
		HandCart hc = new HandCart();
		hc.setPosition(0, 0);
		StendhalRPZone z = new StendhalRPZone("test", 10, 10);
		z.add(hc);
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

		hc.reset();
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

		hc.put("x", 2);
		hc.reset();
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

		hc.put("y", 2);
		hc.reset();
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

		hc.put("x", 2);
		hc.put("y", 2);
		hc.reset();
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));
	}

	@Test
	public void testPush() {
		HandCart hc = new HandCart();
		hc.setPosition(0, 0);
		StendhalRPZone z = new StendhalRPZone("test", 10, 10);
		Player p = PlayerTestHelper.createPlayer("pusher");
		z.add(hc);
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

		hc.push(p, Direction.RIGHT);
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(1)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

		hc.push(p, Direction.LEFT);
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));

		hc.push(p, Direction.DOWN);
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(1)));

		hc.push(p, Direction.UP);
		assertThat(Integer.valueOf(hc.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getY()), is(Integer.valueOf(0)));
	}

	@Test
	public void testCoordinatesAfterPush() {
		HandCart hc = new HandCart();
		hc.setPosition(0, 0);
		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.UP)), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.UP)), is(Integer.valueOf(-1)));

		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.DOWN)), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.DOWN)), is(Integer.valueOf(1)));

		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.LEFT)), is(Integer.valueOf(-1)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.LEFT)), is(Integer.valueOf(0)));

		assertThat(Integer.valueOf(hc.getXAfterPush(Direction.RIGHT)), is(Integer.valueOf(1)));
		assertThat(Integer.valueOf(hc.getYAfterPush(Direction.RIGHT)), is(Integer.valueOf(0)));
	}

	@Test
	public void testCollisionOnPush() throws Exception {
		HandCart hc1 = new HandCart();
		hc1.setPosition(0, 0);
		StendhalRPZone z = new StendhalRPZone("test", 10, 10);
		Player p = PlayerTestHelper.createPlayer("pusher");
		z.add(hc1, false);

		// one successful push
		hc1.push(p, Direction.RIGHT);
		assertThat(Integer.valueOf(hc1.getX()), is(Integer.valueOf(1)));

		// now we add an obstacle right of hc1
		HandCart hc2 = new HandCart();
		hc2.setPosition(02, 0);
		z.add(hc2, false);

		// push should not be executed now and stay at the former place
		hc1.push(p, Direction.RIGHT);
		assertThat(Integer.valueOf(hc1.getX()), is(Integer.valueOf(1)));
	}
}