/***************************************************************************
 *                   (C) Copyright 2003-2011 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import games.stendhal.common.MathHelper;
import games.stendhal.server.core.scripting.ScriptImpl;
import games.stendhal.server.entity.mapstuff.handcart.HandCart;
import games.stendhal.server.entity.player.Player;

/**
 * Enables admins to create (list and remove) handcart.
 *
 */
public class SummonHandCart extends ScriptImpl {

	private final Map<Integer, HandCart> storage = new HashMap<Integer, HandCart>();

	private int handcartcounter;

	/**
	 * Adds a hand cart.
	 *
	 * @param player
	 *            admin who put the hand cart
	 * @param args
	 *            zone x y
	 */
	public void add(final Player player, final List<String> args) {
		if (args.size() == 3) {

			// read zone and x,y. Use player's data as default on "-".
			final String myZone = args.get(0);
			if ("-".equals(myZone)) {
				sandbox.setZone(sandbox.getZone(player));
			} else {
				if (!sandbox.setZone(myZone)) {
					sandbox.privateText(player, "Zone not found.");
					return;
				}
			}
			int x = 0;
			if ("-".equals(args.get(1))) {
				x = player.getX();
			} else {
				x = MathHelper.parseInt(args.get(1));
			}
			int y = 0;
			if ("-".equals(args.get(2))) {
				y = player.getY();
			} else {
				y = MathHelper.parseInt(args.get(2));
			}

			final HandCart handcart = new HandCart();
			handcart.setPosition(x, y);


			// add hand cart to game
			sandbox.add(handcart);

			// put it into our storage for later "list" or "del" commands
			handcartcounter++;
			storage.put(Integer.valueOf(handcartcounter), handcart);
		} else {
			// syntax error, print help text
			sandbox.privateText(
					player,
					"This script creates, lists or removes hand cart. Syntax: \r\nSummonHandCart.class <zone> <x> <y>. The first 3 parameters can be \"-\".\r\nSummonHandCart.class list\r\nSummonHandCart.class del <n>");
		}
	}

	/**
	 * Removes the specified hand cart.
	 *
	 * @param player
	 *            admin
	 * @param args
	 *            hand cart number at index 1
	 */
	public void delete(final Player player, final List<String> args) {
		int i;
		try {
			i = Integer.parseInt(args.get(1));
		} catch (final NumberFormatException e) {
			sandbox.privateText(player, "Please specify a number");
			return;
		}

		final HandCart handcart = storage.get(Integer.valueOf(i));
		if (handcart != null) {
			storage.remove(Integer.valueOf(i));
			sandbox.remove(handcart);
			final StringBuilder sb = new StringBuilder();
			sb.append("Removed handcart ");
			handcartToString(sb, handcart);
			sandbox.privateText(player, sb.toString());
		} else {
			sandbox.privateText(player, "HandCart " + i + " does not exist");
		}
	}

	private void handcartToString(final StringBuilder sb, final HandCart handcart) {
		sb.append(handcart.getZone().getName());
		sb.append(" ");
		sb.append(handcart.getX());
		sb.append(" ");
		sb.append(handcart.getY());
		sb.append(" ");
		sb.append("\"" + handcart.toString() + "\"");
	}

	/**
	 * Lists all handcarts.
	 *
	 * @param player
	 *            admin invoking this script
	 */
	public void list(final Player player) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Listing handcarts:");

		int i = 1;
		while (i <= handcartcounter) {
			final HandCart handcart = storage.get(Integer.valueOf(i));
			if (handcart != null) {
				sb.append("\r\n");
				sb.append(i);
				sb.append(". ");
				handcartToString(sb, handcart);
			}
			i++;
		}
		sandbox.privateText(player, sb.toString());
	}

	@Override
	public void execute(final Player admin, final List<String> args) {
		if (args.size() == 0) {
			admin.sendPrivateText("/script SummonHandCart.class zone x y (the first three parameters may be \"-\"\n/script SummonHandCart.class list\n/script SummonHandCart.class del <n>");
			return;
		}

		final String temp = args.get(0);
		if ("list".equals(temp)) {
			list(admin);
		} else if ("del".equals(temp) || "delete".equals(temp) || "remove".equals(temp)) {
			delete(admin, args);
		} else {
			add(admin, args);
		}
	}

}
