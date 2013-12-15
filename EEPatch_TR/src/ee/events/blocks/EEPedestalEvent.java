package ee.events.blocks;

import org.bukkit.entity.Player;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import ee.TilePedestal;
import ee.events.EEEnums.EEPedestalAction;
import ee.events.EEEvent;

public class EEPedestalEvent extends EEEvent {
	protected TilePedestal block;
	protected EntityHuman human;
	protected EEPedestalAction action;
	protected ItemStack item;
	
	public EEPedestalEvent(TilePedestal pedestal, ItemStack item, EntityHuman human, EEPedestalAction action) {
		this.block = pedestal;
		this.item = item;
		this.human = human;
		this.action = action;
	}

	public EEPedestalAction getAction() {
		return action;
	}

	public TilePedestal getPedestal() {
		return block;
	}

	public ItemStack getItem() {
		return item;
	}
	
	public EntityHuman getHuman(){
		return human;
	}
	
	public Player getActivationPlayer(){
		if (human == null) return null;
		return (Player) human.getBukkitEntity();
	}
	
	public void setAction(EEPedestalAction action){
		this.action = action;
	}
	
}
