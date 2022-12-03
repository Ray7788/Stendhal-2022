/***************************************************************************
 *                   (C) Copyright 2012-2016 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.mapstuff.handcart;

import games.stendhal.server.entity.mapstuff.PuzzleEntity;
import games.stendhal.server.entity.mapstuff.area.AreaEntity;
import games.stendhal.server.entity.mapstuff.puzzle.PuzzleBuildingBlock;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPClass;

/**
 * An entity representing a target for a pushable hand cart
 *
 */
public class HandCartTarget extends AreaEntity implements PuzzleEntity {
	private String shape = null;

	private ChatAction action;
	private ChatCondition condition;
	private PuzzleBuildingBlock puzzleBuildingBlock;

	/**
	 * Generate the RPClass
	 */
	public static void generateRPClass() {
		RPClass clazz = new RPClass("handcarttarget");
		clazz.isA("area");
	}

	/**
	 * Create a Hand CartTarget accepting any HAnd CArt
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public HandCartTarget() {
		setRPClass("handcarttarget");
		put("type", "handcarttarget");
		hide();
	}

	/**
	 * Create a shaped HandCart Target, that only accepts HAnd Carts of a certain shape
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param shape accepted shape
	 */
	public HandCartTarget(String shape) {
		this();
		this.shape = shape;
	}

	/**
	 * Check if a Hand Cart would trigger this HandCartTarget
	 *
	 * @param h the HAnd CArt to check
	 * @param p
	 * @return true iff the given Hand Cart would trigger this target
	 */
	public boolean doesTrigger(HandCart h, Player p) {
		String handcartShape = h.getShape();
		String targetShape = this.getShape();
		boolean shapeFits = true;
		boolean conditionMet = true;

		if(targetShape != null) {
			shapeFits = targetShape.equals(handcartShape);
		}

		if(this.condition != null) {
			conditionMet = this.condition.fire(p, null, null);
		}

		return conditionMet && shapeFits;
	}

	/**
	 * Trigger this HandCartTarget
	 *
	 * @param h The HandCart that was pushed on this target
	 * @param p The Player who has pushed the triggering HandCart on this target
	 */
	public void trigger(HandCart h, Player p) {
		if(this.action != null) {
			this.action.fire(p, null, null);
		}
		if (puzzleBuildingBlock != null) {
			puzzleBuildingBlock.put("active", true);
		}
	}

	/**
	 * Untriggeres this HandCart Target
	 */
	public void untrigger() {
		if (puzzleBuildingBlock != null) {
			puzzleBuildingBlock.put("active", false);
		}
	}

	/**
	 * Get the shape of this HandCartTarget
	 *
	 * @return the shape or null if this HandCartTarget has no shape
	 */
	public String getShape() {
		return shape;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(ChatAction action) {
		this.action = action;
	}

	/**
	 * Set the ChatCondition to check
	 *
	 * @param condition the condition to set
	 */
	public void setCondition(ChatCondition condition) {
		this.condition = condition;
	}

	@Override
	public void puzzleExpressionsUpdated() {
		// do nothing
	}

	@Override
	public void setPuzzleBuildingBlock(PuzzleBuildingBlock buildingBlock) {
		this.puzzleBuildingBlock = buildingBlock;
	}

}
