/* $Id$ */
/***************************************************************************
 *                      (C) Copyright 2003 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.client.entity;

import marauroa.common.game.RPObject;
import marauroa.common.game.RPSlot;

/**
 * A hand cart entity.
 */
public class HandCart extends Entity {
	/**
	 * Open state property.
	 */
	public static final Property PROP_OPEN = new Property();

	/**
	 * Whether the hand cart is currently open.
	 */
	private boolean open;

	/**
	 * The current content slot.
	 */
	private RPSlot content;

	/**
	 * Create a hand cart entity.
	 */
	public HandCart() {
	}

	//
	// HandCart
	//
	
	@Override
	public boolean isObstacle(IEntity entity) {
		
		return true;
	}

	/**
	 * Get the hand cart contents.
	 *
	 * @return The contents slot.
	 */
	public RPSlot getContent() {
		return content;
	}

	/**
	 * Determine if the hand cart is open.
	 *
	 * @return <code>true</code> if the hand cart is open.
	 */
	public boolean isOpen() {
		return open;
	}

	//
	// Entity
	//

	/**
	 * Initialize this entity for an object.
	 *
	 * @param object
	 *            The object.
	 *
	 * @see #release()
	 */
	@Override
	public void initialize(final RPObject object) {
		super.initialize(object);

		if (object.hasSlot("content")) {
			content = object.getSlot("content");
		} else {
			content = null;
		}

		open = object.has("open");
	}

	//
	// RPObjectChangeListener
	//

	/**
	 * The object added/changed attribute(s).
	 *
	 * @param object
	 *            The base object.
	 * @param changes
	 *            The changes.
	 */
	@Override
	public void onChangedAdded(final RPObject object, final RPObject changes) {
		super.onChangedAdded(object, changes);

		if (changes.has("open")) {
			open = true;
			fireChange(PROP_OPEN);
		}
	}

	/**
	 * The object removed attribute(s).
	 *
	 * @param object
	 *            The base object.
	 * @param changes
	 *            The changes.
	 */
	@Override
	public void onChangedRemoved(final RPObject object, final RPObject changes) {
		super.onChangedRemoved(object, changes);

		if (changes.has("open")) {
			open = false;
			fireChange(PROP_OPEN);
		}
	}
}
