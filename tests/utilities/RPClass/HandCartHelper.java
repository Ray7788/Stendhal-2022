package utilities.RPClass;

import games.stendhal.server.entity.mapstuff.area.AreaEntity;
import games.stendhal.server.entity.mapstuff.handcart.HandCart;
import games.stendhal.server.entity.mapstuff.handcart.HandCartTarget;
import marauroa.common.game.RPClass;

public class HandCartHelper {
	public static void generateRPClasses() {
		EntityTestHelper.generateRPClasses();
		if(!RPClass.hasRPClass("area")) {
			AreaEntity.generateRPClass();
		}
		if(!RPClass.hasRPClass("handcart")) {
			HandCart.generateRPClass();
		}
		if(!RPClass.hasRPClass("handcarttarget")) {
			HandCartTarget.generateRPClass();
		}
	}
}