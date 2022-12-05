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
package games.stendhal.server.entity.slot;

import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.mapstuff.handcart.HandCart;

/**
 * A slot of a hand cart which is only accessible, if the handcart is open.
 *
 */
public class HandCartSlot extends LootableSlot {
	private final HandCart handcart;

	/**
	 * Creates a HandCartSlot
	 *
	 * @param owner
	 *            HandCart owning this slot
	 */
	public HandCartSlot(final HandCart owner) {
		super(owner);
		this.handcart = owner;
	}

	@Override
	public boolean isReachableForTakingThingsOutOfBy(final Entity entity) {
		if (!handcart.isOpen()) {
			setErrorMessage("This " + ((Entity)getOwner()).getDescriptionName(true) + " is not open.");
			return false;
		}
		return super.isReachableForTakingThingsOutOfBy(entity);
	}
}
