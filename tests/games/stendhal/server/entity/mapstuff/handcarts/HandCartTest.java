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
		HandCart h = new HandCart();
		h.setPosition(0, 0);
		StendhalRPZone z = new StendhalRPZone("test", 10, 10);
		z.add(h);
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));

		h.reset();
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));

		h.put("x", 2);
		h.reset();
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));

		h.put("y", 2);
		h.reset();
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));

		h.put("x", 2);
		h.put("y", 2);
		h.reset();
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));
	}

	@Test
	public void testPush() {
		HandCart h = new HandCart();
		h.setPosition(0, 0);
		StendhalRPZone z = new StendhalRPZone("test", 10, 10);
		Player p = PlayerTestHelper.createPlayer("pusher");
		z.add(h);
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));

		h.push(p, Direction.RIGHT);
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(1)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));

		h.push(p, Direction.LEFT);
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));

		h.push(p, Direction.DOWN);
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(1)));

		h.push(p, Direction.UP);
		assertThat(Integer.valueOf(h.getX()), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getY()), is(Integer.valueOf(0)));
	}

	@Test
	public void testCoordinatesAfterPush() {
		HandCart h = new HandCart();
		h.setPosition(0, 0);
		assertThat(Integer.valueOf(h.getXAfterPush(Direction.UP)), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getYAfterPush(Direction.UP)), is(Integer.valueOf(-1)));

		assertThat(Integer.valueOf(h.getXAfterPush(Direction.DOWN)), is(Integer.valueOf(0)));
		assertThat(Integer.valueOf(h.getYAfterPush(Direction.DOWN)), is(Integer.valueOf(1)));

		assertThat(Integer.valueOf(h.getXAfterPush(Direction.LEFT)), is(Integer.valueOf(-1)));
		assertThat(Integer.valueOf(h.getYAfterPush(Direction.LEFT)), is(Integer.valueOf(0)));

		assertThat(Integer.valueOf(h.getXAfterPush(Direction.RIGHT)), is(Integer.valueOf(1)));
		assertThat(Integer.valueOf(h.getYAfterPush(Direction.RIGHT)), is(Integer.valueOf(0)));
	}

	@Test
	public void testCollisionOnPush() throws Exception {
		HandCart h1 = new HandCart();
		h1.setPosition(0, 0);
		StendhalRPZone z = new StendhalRPZone("test", 10, 10);
		Player p = PlayerTestHelper.createPlayer("pusher");
		z.add(h1, false);

		// one successful push
		h1.push(p, Direction.RIGHT);
		assertThat(Integer.valueOf(h1.getX()), is(Integer.valueOf(1)));

		// now we add an obstacle right of h1
		HandCart h2 = new HandCart();
		h2.setPosition(02, 0);
		z.add(h2, false);

		// push should not be executed now and stay at the former place
		h1.push(p, Direction.RIGHT);
		assertThat(Integer.valueOf(h1.getX()), is(Integer.valueOf(1)));
	}
}