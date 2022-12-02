package games.stendhal.server.entity.mapstuff.handcarts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.PassiveEntity;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.item.Corpse;
import games.stendhal.server.entity.mapstuff.handcart.HandCart;
import marauroa.common.game.RPClass;
import marauroa.common.game.SlotIsFullException;

/**
 *
 * checking if items can be added/removed from the hand cart 
 */

public class HandCartsTest2 {

	@Before
	public void setUp() throws Exception {
		if (!RPClass.hasRPClass("entity")) {
			Entity.generateRPClass();
		}

		if (!RPClass.hasRPClass("handcart")) {
			HandCart.generateRPClass();
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests for size.
	 */

	@Test(expected = SlotIsFullException.class)
	public final void testSize() {
		final HandCart h = new HandCart();
		assertEquals(0, h.size());
		for (int i = 0; i < 30; i++) {
			h.add(new PassiveEntity() {
			});
		}
		assertEquals(30, h.size());
		h.add(new PassiveEntity() {
		});
	}

	/**
	 * Tests for open.
	 */

	@Test
	public final void testOpen() {
		final HandCart h = new HandCart();
		assertFalse(h.isOpen());
		h.open();

		assertTrue(h.isOpen());
		h.close();
		assertFalse(h.isOpen());
	}

	/**
	 * Tests for onUsed.
	 */

	@Test
	public final void testOnUsed() {
		final HandCart h = new HandCart();
		assertFalse(h.isOpen());
		h.onUsed(new RPEntity() {

			@Override
			protected void dropItemsOn(final Corpse corpse) {
			}

			@Override
			public void logic() {

			}
		});

		assertTrue(h.isOpen());
		h.onUsed(new RPEntity() {

			@Override
			protected void dropItemsOn(final Corpse corpse) {
			}

			@Override
			public void logic() {

			}
		});
		assertFalse(h.isOpen());
	}

}