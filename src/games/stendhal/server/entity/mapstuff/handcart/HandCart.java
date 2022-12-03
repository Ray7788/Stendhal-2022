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

import java.awt.geom.Rectangle2D;
//import java.util.Arrays;
//import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import games.stendhal.common.Direction;
import games.stendhal.common.MathHelper;
//import games.stendhal.common.Rand;
//import games.stendhal.common.constants.SoundLayer;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.events.MovementListener;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.core.events.ZoneEnterExitListener;
import games.stendhal.server.entity.ActiveEntity;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;
//import games.stendhal.server.events.SoundEvent;
import marauroa.common.game.Definition.Type;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;
import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.core.events.UseListener;
import games.stendhal.server.entity.PassiveEntity;
//import games.stendhal.server.entity.slot.ChestSlot;
import games.stendhal.server.entity.slot.HandCartSlot;
import marauroa.common.game.RPSlot;

/**
 * A solid, movable handcart on a map. It can have different appearances,
 *
 */
public class HandCart extends Entity implements UseListener, ZoneEnterExitListener,
		MovementListener, TurnListener {

	private static final String HANDCART_RPCLASS_NAME = "handcart";
	private static final Logger logger = Logger.getLogger(HandCart.class);

	/** number of seconds until a block is reset to its original position */
	static final int RESET_TIMEOUT_IN_SECONDS = 5 * MathHelper.SECONDS_IN_ONE_MINUTE;

	/** number of seconds until another attempt to rest the block to its original position is attempted */
	static final int RESET_AGAIN_DELAY = 10;

	private static final String Z_ORDER = "z";

	private int startX;
	private int startY;
	private boolean multi;

//	private final List<String> sounds;

	private boolean resetHandCart = true;
	private boolean wasMoved = false;
	
	/**
	 * Whether the handcart is open.
	 */
	private boolean open;

	/**
	 * Creates a new handcart.
	 */
	public HandCart() {
		setRPClass(HANDCART_RPCLASS_NAME);
		put("type", HANDCART_RPCLASS_NAME);
		open = false;
		
		this.put(Z_ORDER, 8000);
		this.multi = true;
		setResistance(100);
		setDescription("You see a hand cart. Are you strong enough to push it away?");
		
		final RPSlot slot = new HandCartSlot(this);
		addSlot(slot);
	}

	/**
	 * Creates a new handcart.
	 *
	 * @param object
	 *            RPObject
	 */
	public HandCart(final RPObject object) {
		super(object);

		this.put(Z_ORDER, 8000);
		this.multi = true;
		setResistance(100);
		setDescription("You see a hand cart. Are you strong enough to push it away?");
		
		setRPClass(HANDCART_RPCLASS_NAME);
		put("type", HANDCART_RPCLASS_NAME);

		if (!hasSlot("content")) {
			final RPSlot slot = new HandCartSlot(this);
			addSlot(slot);
		}

		update();
	}


	public static void generateRPClass() {
		
		if (!RPClass.hasRPClass(HANDCART_RPCLASS_NAME)) {
			final RPClass handcart = new RPClass(HANDCART_RPCLASS_NAME);
			handcart.isA("entity");
			handcart.addAttribute("open", Type.FLAG);
			handcart.addAttribute(Z_ORDER, Type.INT);
			handcart.addRPSlot("content", 30);
		}
	}
	
	@Override
    public String getDescriptionName(final boolean definite) {
	    return Grammar.article_noun(HANDCART_RPCLASS_NAME, definite);
    }

	@Override
	public void update() {
		super.update();
		open = false;
		if (has("open")) {
			open = true;
		}
	}

	/**
	 * Open the handcart.
	 */
	public void open() {
		this.open = true;
		put("open", "");
	}

	/**
	 * Close the handcart.
	 */
	public void close() {
		this.open = false;

		if (has("open")) {
			remove("open");
		}
	}

	/**
	 * Determine if the handcart is open.
	 *
	 * @return <code>true</code> if the handcart is open.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Adds a passive entity (like an item) to the handcart.
	 *
	 * @param entity
	 *            entity to add
	 */
	public void add(final PassiveEntity entity) {
		final RPSlot content = getSlot("content");
		content.add(entity);
	}

	@Override
	public int size() {
		return getSlot("content").size();
	}

	/**
	 * Returns the content.
	 *
	 * @return iterator for the content
	 */
	public Iterator<RPObject> getContent() {
		final RPSlot content = getSlot("content");
		return content.iterator();
	}

	//
	// UseListener
	//

	@Override
	public boolean onUsed(final RPEntity user) {
		if (user.nextTo(this)) {
			if (isOpen()) {
				close();
			} else {
				open();
			}

			notifyWorldAboutChanges();
			return true;
		}
		if (user instanceof Player) {
			final Player player = (Player) user;
			player.sendPrivateText("You cannot reach the hand cart from there.");
		}
		return false;
	}

	//
	// Entity
	//

	@Override
	public String describe() {
		String text = "You see a hand cart.";

		if (hasDescription()) {
			text = getDescription();
		}

		if (isOpen()) {
			text += " It is open.";
			text += " You can right click and inspect this item to see its contents.";
		} else {
			text += " It is closed.";
		}

		return (text);
	}


	/**
	 * Resets the hand cart position to its initial state
	 */
	public void reset() {
		wasMoved = false;
		List<HandCartTarget> handcartTargetsAt = this.getZone().getEntitiesAt(getX(), getY(), HandCartTarget.class);
		for (HandCartTarget handcartTarget : handcartTargetsAt) {
			handcartTarget.untrigger();
		}
		this.setPosition(startX, startY);
		SingletonRepository.getTurnNotifier().dontNotify(this);
		this.notifyWorldAboutChanges();
	}

	/**
	 * Push this Hand Cart into a given direction
	 *
	 * @param p
	 * @param d
	 *            the direction, this hand cart is pushed into
	 */
	public void push(Player p, Direction d) {
		if (!this.mayBePushed(d)) {
			return;
		}
		// before push
		
		List<HandCartTarget> handcartTargetsAt = this.getZone().getEntitiesAt(getX(), getY(), HandCartTarget.class);
		for (HandCartTarget handcartTarget : handcartTargetsAt) {
			handcartTarget.untrigger();
		}

		// after push
		int x = getXAfterPush(d);
		int y = getYAfterPush(d);
		this.setPosition(x, y);
		handcartTargetsAt = this.getZone().getEntitiesAt(x, y, HandCartTarget.class);
		for (HandCartTarget handcartTarget : handcartTargetsAt) {
			if (handcartTarget.doesTrigger(this, p)) {
				handcartTarget.trigger(this, p);
			}
		}
		if (resetHandCart) {
			SingletonRepository.getTurnNotifier().dontNotify(this);
			SingletonRepository.getTurnNotifier().notifyInSeconds(RESET_TIMEOUT_IN_SECONDS, this);
		}
		wasMoved = true;
//		this.sendSound();
		this.notifyWorldAboutChanges();
		if (logger.isDebugEnabled()) {
			logger.debug("HandCart [" + this.getID().toString() + "] pushed to (" + this.getX() + "," + this.getY() + ").");
		}
	}

	/**
	 * should the hand cart reset to its original position after some time?
	 *
	 * @param resetHandCart true, if the hand cart should be reset; false otherwise
	 */
	public void setResetHandCart(boolean resetHandCart) {
		this.resetHandCart = resetHandCart;
	}


	public int getYAfterPush(Direction d) {
		return this.getY() + d.getdy();
	}

	public int getXAfterPush(Direction d) {
		return this.getX() + d.getdx();
	}

	private boolean wasPushed() {
		boolean xChanged = this.getInt("x") != this.startX;
		boolean yChanged = this.getInt("y") != this.startY;
		return xChanged || yChanged;
	}

	private boolean mayBePushed(Direction d) {
		boolean pushed = wasPushed();
		int newX = this.getXAfterPush(d);
		int newY = this.getYAfterPush(d);

		if (!multi && pushed) {
			return false;
		}

		// additional checks: new position must be free
		boolean collision = this.getZone().collides(this, newX, newY);

		return !collision;
	}

	/**
	 * Get the shape of this Hand Cart
	 *
	 * @return the shape or null if this Hand Cart has no shape
	 */
	public String getShape() {
		if (this.has("shape")) {
			return this.get("shape");
		}
		return null;
	}

	@Override
	public void onEntered(ActiveEntity entity, StendhalRPZone zone, int newX, int newY) {
		// do nothing
	}

	@Override
	public void onExited(ActiveEntity entity, StendhalRPZone zone, int oldX, int oldY) {
		if (logger.isDebugEnabled()) {
			logger.debug("HandCart [" + this.getID().toString() + "] notified about entity [" + entity + "] exiting [" + zone.getName() + "].");
		}
		resetInPlayerlessZone(zone, entity);
	}

	@Override
	public void onMoved(ActiveEntity entity, StendhalRPZone zone, int oldX,
			int oldY, int newX, int newY) {
		// do nothing on move
	}

	@Override
	public void onEntered(RPObject object, StendhalRPZone zone) {
		// do nothing
	}

	@Override
	public void onExited(RPObject object, StendhalRPZone zone) {
		if (logger.isDebugEnabled()) {
			logger.debug("HandCart [" + this.getID().toString() + "] notified about object [" + object + "] exiting [" + zone.getName() + "].");
		}
		resetInPlayerlessZone(zone, object);
	}

	private void resetInPlayerlessZone(StendhalRPZone zone, RPObject object) {
		if (!resetHandCart || !wasMoved) {
			return;
		}

		// reset to initial position if zone gets empty of players
		final List<Player> playersInZone = zone.getPlayers();
		int numberOfPlayersInZone = playersInZone.size();
		if (numberOfPlayersInZone == 0 || numberOfPlayersInZone == 1 && playersInZone.contains(object)) {
			resetIfInitialPositionFree();
		}
	}

	@Override
	public boolean isObstacle(Entity entity) {
		if (entity instanceof RPEntity) {
			return true;
		}

		return super.isObstacle(entity);
	}

	@Override
	public void beforeMove(ActiveEntity entity, StendhalRPZone zone, int oldX,
			int oldY, int newX, int newY) {
		if (entity instanceof Player) {
			Rectangle2D oldA = new Rectangle2D.Double(oldX, oldY, entity.getWidth(), entity.getHeight());
			Rectangle2D newA = new Rectangle2D.Double(newX, newY, entity.getWidth(), entity.getHeight());
			Direction d = Direction.getAreaDirectionTowardsArea(oldA, newA);
			this.push((Player) entity, d);
		}
	}

	@Override
	public void onTurnReached(int currentTurn) {
		resetIfInitialPositionFree();
	}

	@Override
	public void onAdded(StendhalRPZone zone) {
		super.onAdded(zone);
		this.startX = getX();
		this.startY = getY();
		zone.addMovementListener(this);
		zone.addZoneEnterExitListener(this);
	}

	@Override
	public void onRemoved(StendhalRPZone zone) {
		super.onRemoved(zone);
		zone.removeMovementListener(this);
		zone.removeZoneEnterExitListener(this);
	}

	/**
	 * Reset to initial position if no collision there, try again later if not
	 * possible
	 */
	private void resetIfInitialPositionFree() {
		if (!this.getZone().collides(this, this.startX, this.startY)) {
			this.reset();
		} else {
			// try again in a few moments
			SingletonRepository.getTurnNotifier().notifyInSeconds(RESET_AGAIN_DELAY, this);
		}
	}
}
