package games.stendhal.server.entity.item;

import java.util.Map;
import java.util.HashMap;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPObject;
import java.util.List;
import java.util.ArrayList;


public class MusicalPipe extends AreaUseItem {
	public String name;
	public HashMap<String, Integer> values;
	private Boolean equipped = false;
	private int value;
	private List<RPEntity> attackers;
	private ArrayList<Integer> attackersDamages;


	public MusicalPipe(final String name, final String clazz, final String subclass,
		final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
		this.name = name;
		value = Integer.parseInt(attributes.get("itemset"));

	}
	/**
	 * Copy constructor.
	 *
	 * @param item Item to copy.
	 */
	public MusicalPipe(final MusicalPipe item) {
		super(item);
	}
	
	
	@Override
	protected boolean onUsedInArea(final RPEntity e, final StendhalRPZone zone, final int x, final int y) {
		Ent ent = new Ent(e);
		if (ent.isPlayer) {
			int charm_level = ent.player.getCharmLevel();
			if(charm_level > -1) {
				ent.player.sendPrivateText("Your charm level is " + charm_level);
			}
			else{
				ent.player.sendPrivateText("You can't charm any enemies. Your charm level is 0");
			}
			
			
		}

		return true;
	}

	@Override
	public boolean onEquipped(final RPEntity e, final String slot) {
		Ent ent = new Ent(e);
		if (slot.contains("hand") && ent.isPlayer) {

			ent.player.setCharmLevel(Math.max(ent.player.getCharmLevel(), value));
			ent.player.setMultipleCharms(value);
			
			equipped = true;
		}
		return super.onEquipped(e, slot);
	}

	@Override
	public boolean onUnequipped() {
		if (equipped) {
			RPObject e = this.getBaseContainer();
			Ent ent = new Ent(e);
			
			if (ent.isPlayer) {
				ent.player.setMultipleCharms(-value);

				int new_charm_level = ent.player.setCharmLevel(ent.player.getMultipleCharms());
				
				if (new_charm_level == 0) {
					ent.player.setCharmLevel(-1);
				}
				for (int i=0; i<getAttackers().size(); i++) {
					attackers.get(i).setAtk(getAttackersDamages().get(i));
				}
				getAttackersDamages().clear();
				getAttackers().clear();
					
			}
			equipped = false;
		}
		
		return super.onUnequipped();
	}

	@Override
	public boolean onUsed(RPEntity player) {
		// store original attack values of entities attacking player
		setAttackers(player.getAttackingRPEntities());
		ArrayList<Integer> attackersDamages = new ArrayList<Integer>();
		
		// set all entities attacking the player to deal 0 damage for 1 second.
		for (int i=0; i<attackers.size(); i++) {
			attackersDamages.add(attackers.get(i).getAtk());
			attackers.get(i).setAtk(0);
		}
		
		setAttackersDamages(attackersDamages);
		return true;
	}
			
	public List<RPEntity> getAttackers() {
		return this.attackers;
	}
	
	public void setAttackers(List<RPEntity> attackers) {
		this.attackers = attackers;
	}
	
	public void setAttackersDamages(ArrayList<Integer> inpList) {
		this.attackersDamages = inpList;
	}
	
	public ArrayList<Integer> getAttackersDamages() {
		return this.attackersDamages;
	}
	
	public class Ent{
		boolean isPlayer;
		Player player;
		
		public Ent(RPEntity entity) {
	       if(entity instanceof Player) {
	    	  isPlayer = true;
	    	  player = (Player) entity;
	       }
	       else {
	    	   isPlayer = false;
	       }
	    }
		
		public Ent(RPObject entity) {
	       if(entity instanceof Player) {
	    	  isPlayer = true;
	    	  player = (Player) entity;
	       }
	       else {
	    	   isPlayer = false;
	       }
	    }
	}
}